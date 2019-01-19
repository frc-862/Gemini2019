/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.CollectCargo;
import frc.robot.commands.HABClimb;

/**
 * Add your docs here.
 */
public class CargoCollector extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private final VictorSP winchMotor1;
  private final VictorSP winchMotor2;

  public CargoCollector(){
    winchMotor1 = new VictorSP(1);
    winchMotor2 = new VictorSP(2);//?????
    stop();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new HABClimb());
  }

  public void stop(){
    System.out.println("STOP");
    winchMotor1.set(0.0);
    winchMotor2.set(0.0);
    //winchMotor2.set(ControlMode.PercentOutput, 0.0);
  }

  public void setPower(double pwr){
    System.out.println("power " + pwr);
    winchMotor1.set(pwr);
    winchMotor2.set(pwr);
  }

  public void collect(){
    System.out.println("COLLECT");
    winchMotor1.set(1.0);
    //winchMotor2.set(ControlMode.PercentOutput, 0.5);
  }

  public void eject(){
    System.out.println("EJECT");
    winchMotor1.set(-1.0);
    //winchMotor2.set(ControlMode.PercentOutput, -0.5);
  }

}
