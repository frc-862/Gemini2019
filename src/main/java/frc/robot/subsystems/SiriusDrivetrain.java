/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import frc.lightning.logging.DataLogger;
import frc.lightning.subsystems.SpeedControllerDrivetrain;
import frc.robot.RobotMap;
import frc.robot.commands.TankDrive;

/**
 * Add your docs here.
 */
public class SiriusDrivetrain extends SpeedControllerDrivetrain {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public static SiriusDrivetrain create() {
    SpeedControllerGroup left = new SpeedControllerGroup(
      new Victor(RobotMap.leftMotor1), new Victor(RobotMap.leftMotor2));

    SpeedControllerGroup right = new SpeedControllerGroup(
        new Victor(RobotMap.rightMotor1), new Victor(RobotMap.rightMotor2));

    right.setInverted(true);

    Encoder leftEncoder = new Encoder(RobotMap.leftEncoder1, RobotMap.leftEncoder2);
    Encoder rightEncoder = new Encoder(RobotMap.rightEncoder1, RobotMap.rightEncoder2);

    return new SiriusDrivetrain(left, leftEncoder, right, rightEncoder);
  }

  public SiriusDrivetrain(SpeedController left, Encoder leftEnc, SpeedController right, Encoder rightEnc) {
    super(left, leftEnc, right, rightEnc);

    DataLogger.addDataElement("leftPower", () -> left.get());
    DataLogger.addDataElement("rightPower", () -> right.get());

    DataLogger.addDataElement("leftPosition", () -> leftEncoder.getDistance());
    DataLogger.addDataElement("rightPosition", () -> rightEncoder.getDistance());

    DataLogger.addDataElement("leftVelocity", () -> leftEncoder.getRate());
    DataLogger.addDataElement("rightVelocity", () -> rightEncoder.getRate());
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new TankDrive());
  }
}
