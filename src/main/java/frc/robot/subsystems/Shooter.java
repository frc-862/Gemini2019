/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.MotorConfig;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Shooter extends Subsystem {
  private DoubleSolenoid kicker = new DoubleSolenoid(RobotMap.compressorCANId, 
      RobotMap.kickerCANId1, RobotMap.kickerCANId2);

  private WPI_TalonSRX leftFlywheel = new WPI_TalonSRX(RobotMap.leftFlywheelCANId);
  private WPI_TalonSRX rightFlywheel = new WPI_TalonSRX(RobotMap.rightFlywheelCANId);

  private MotorConfig flywheelConfig = MotorConfig.get("flywheels.json");

  public Shooter() {
    leftFlywheel.setInverted(true);
    withFlywheels((m) -> flywheelConfig.registerMotor(m));

    DataLogger.addDelayedDataElement("leftFlywheelCommand", () -> leftFlywheel.getMotorOutputPercent());
    DataLogger.addDelayedDataElement("rightFlywheelCommand", () -> rightFlywheel.getMotorOutputPercent());

    DataLogger.addDelayedDataElement("leftFlywheelCurrent", () -> leftFlywheel.getOutputCurrent());
    DataLogger.addDelayedDataElement("rightFlywheelCurrent", () -> rightFlywheel.getOutputCurrent());

    DataLogger.addDelayedDataElement("leftFlywheelSpeed", () -> leftFlywheel.getSelectedSensorVelocity());
    DataLogger.addDelayedDataElement("rightFlywheelSpeed", () -> rightFlywheel.getSelectedSensorVelocity());

    addChild("Left Flywheel", leftFlywheel);
    addChild("Right Flywheel", rightFlywheel);
    addChild("Kicker", kicker);
  }

  protected void withFlywheels(Consumer<WPI_TalonSRX> fn) {
    fn.accept(leftFlywheel);
    fn.accept(rightFlywheel);
  }

  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public void setFlywheelPower(double pwr) {
    withFlywheels((fw) -> fw.set(ControlMode.PercentOutput, pwr));
  }

  public void kick() {
    kicker.set(Value.kForward);
  }

  public void retract() {
    kicker.set(Value.kReverse);
  }
  
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
