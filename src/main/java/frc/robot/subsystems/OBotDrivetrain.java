/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.lightning.subsystems.CANDrivetrain;
import frc.lightning.util.MotorConfig;
import frc.robot.commands.TankDrive;

/**
 * Add your docs here.
 */
public class OBotDrivetrain extends CANDrivetrain {
  WPI_TalonSRX leftFollow1;
  WPI_TalonSRX leftFollow2;

  WPI_TalonSRX rightFollow1;
  WPI_TalonSRX rightFollow2;

  public static OBotDrivetrain create() {
    return new OBotDrivetrain(
      new WPI_TalonSRX(1),
      new WPI_TalonSRX(2),
      new WPI_TalonSRX(3),
      new WPI_TalonSRX(4),
      new WPI_TalonSRX(5),
      new WPI_TalonSRX(6)
    );    
  }

  public OBotDrivetrain(WPI_TalonSRX left, WPI_TalonSRX left2, WPI_TalonSRX left3, WPI_TalonSRX right, WPI_TalonSRX right2, WPI_TalonSRX right3) {
    super(left, right);
  
    leftFollow1 = left2;
    leftFollow2 = left3;
    rightFollow1 = right2;
    rightFollow2 = right3;

    MotorConfig drive = MotorConfig.get("drive.json");
    withEachMotor((m) -> drive.registerMotor(m));
  }

    
  public void configAllSettings(TalonSRXConfiguration config){

    withEachMaster((m) -> m.configAllSettings(config));

  }

  public void withEachMotor(Consumer<TalonSRX> fn)  {
    fn.accept(getLeftMaster());
    fn.accept(getRightMaster());
    fn.accept(leftFollow1);
    fn.accept(leftFollow2);
    fn.accept(rightFollow1);
    fn.accept(rightFollow2);
  }

  public void configureMotors() {
    getLeftMaster().setInverted(true);
    leftFollow1.follow(getLeftMaster());
    leftFollow2.follow(getLeftMaster());

    rightFollow1.follow(getRightMaster());
    rightFollow1.setInverted(true);
    rightFollow2.follow(getRightMaster());
    rightFollow2.setInverted(true);

    withEachMaster((m) -> {
      m.configOpenloopRamp(0.2);
      m.configClosedloopRamp(0.2);
    });

    enableLogging();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new TankDrive());
  }
}
