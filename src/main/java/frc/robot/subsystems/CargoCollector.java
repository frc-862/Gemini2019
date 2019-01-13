/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.CollectCargo;

/**
 * Add your docs here.
 */
public class CargoCollector extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private final TalonSRX winchMotor1 = new TalonSRX(7);
  //private final TalonSRX winchMotor2 = new TalonSRX(6);

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new CollectCargo());
  }

  public void stop(){
    winchMotor1.set(ControlMode.PercentOutput, 0.0);
    //winchMotor2.set(ControlMode.PercentOutput, 0.0);
  }

  public void collect(){
    winchMotor1.set(ControlMode.PercentOutput, 0.5);
    //winchMotor2.set(ControlMode.PercentOutput, 0.5);
  }

  public void eject(){
    winchMotor1.set(ControlMode.PercentOutput, -0.5);
    //winchMotor2.set(ControlMode.PercentOutput, -0.5);
  }

}
