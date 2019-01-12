/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.lightning.subsystems.CANDrivetrain;
import frc.lightning.util.MotorConfig;
import frc.robot.commands.TankDrive;

/**
 * Add your docs here.
 */
public class GlitchDrivetrain extends CANDrivetrain {
  VictorSPX leftFollow1;
  VictorSPX leftFollow2;

  VictorSPX rightFollow1;
  VictorSPX rightFollow2;

  public static GlitchDrivetrain create() {
    return new GlitchDrivetrain(
      new TalonSRX(1), 
      new VictorSPX(2), 
      new VictorSPX(3),
      new TalonSRX(4),
      new VictorSPX(5),
      new VictorSPX(6));
  }

  public GlitchDrivetrain(TalonSRX left, VictorSPX left2, VictorSPX left3, TalonSRX right, VictorSPX right2, VictorSPX right3) {
    super(left, right);
  
    leftFollow1 = left2;
    leftFollow2 = left3;
    rightFollow1 = right2;
    rightFollow2 = right3;

    configureMotors();

    MotorConfig drive = MotorConfig.get("drive.json");
    withEachMotor((m) -> drive.registerMotor(m));
  }

  public void withEachMotor(Consumer<BaseMotorController> fn)  {
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
