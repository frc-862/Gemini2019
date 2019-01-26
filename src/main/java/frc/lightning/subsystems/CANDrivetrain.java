/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.subsystems;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.lightning.logging.DataLogger;

/**
 * Add your docs here.
 */
public abstract class CANDrivetrain extends LightningDrivetrain {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  protected WPI_TalonSRX leftMaster;
  protected WPI_TalonSRX rightMaster;

  protected CANDrivetrain(WPI_TalonSRX left, WPI_TalonSRX right) {
    leftMaster = left;
    rightMaster = right;
  }

  protected void enableLogging() {
    withEachMaster((label, talon) -> {
      //DataLogger.addDelayedDataElement(label + "Position", () -> talon.getSelectedSensorPosition());
      //DataLogger.addDelayedDataElement(label + "Velocity", () -> talon.getSelectedSensorVelocity());
      //DataLogger.addDelayedDataElement(label + "MasterCurrent", () -> talon.getOutputCurrent());
      //DataLogger.addDelayedDataElement(label + "MasterOutputPercent", () -> talon.getMotorOutputPercent());
    });
  }

  protected void withEachMaster(Consumer<WPI_TalonSRX> fn) {
    fn.accept(leftMaster);
    fn.accept(rightMaster);
  }

  protected void withEachMaster(BiConsumer<String,WPI_TalonSRX> fn) {
    fn.accept("left", leftMaster);
    fn.accept("right", rightMaster);
  }

  public WPI_TalonSRX getLeftMaster() {
    return leftMaster;
  }

  public WPI_TalonSRX getRightMaster() {
    return rightMaster;
  }

  @Override
  public void setPower(double left, double right) {
    leftMaster.set(ControlMode.PercentOutput, left);
    rightMaster.set(ControlMode.PercentOutput, right);
  }

  @Override
  public void setVelocity(double left, double right) {
    leftMaster.set(ControlMode.Velocity, left);
    rightMaster.set(ControlMode.Velocity, right);
  }

  @Override
  public void brake() {
    withEachMaster((m) -> m.setNeutralMode(NeutralMode.Brake));
  }

  @Override
  public void coast() {
    withEachMaster((m) -> m.setNeutralMode(NeutralMode.Coast));
  }

  @Override
  public double getLeftDistance() {
    return leftMaster.getSelectedSensorPosition();
  }

  @Override
  public double getRightDistance() {
    return rightMaster.getSelectedSensorPosition();
  }

  @Override
  public double getLeftVelocity() {
    return leftMaster.getSelectedSensorVelocity();
  }

  @Override
  public double getRightVelocity() {
    return rightMaster.getSelectedSensorVelocity();
  }

  @Override
  public void resetDistance() {
    withEachMaster((m) -> m.setSelectedSensorPosition(0));
  }
}
