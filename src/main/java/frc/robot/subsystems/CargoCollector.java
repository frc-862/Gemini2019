/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.HABClimb;
import frc.lightning.logging.DataLogger;
import frc.robot.Constants;
import frc.robot.commands.CollectCargo;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * CargoCollector includes both ground collect
 * and elevator collect wheels
 * 
 */
public class CargoCollector extends Subsystem {
  final double collectPower = -0.6;
  final double holdPower = -0.2;
  final double hasCargoDistance = 0.7;
  
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private final WPI_VictorSPX leftGripper;
  private final WPI_VictorSPX rightGripper;
  private final WPI_TalonSRX groundCollect;
  DoubleSolenoid deployer;

  public CargoCollector(){
    leftGripper = new WPI_VictorSPX(21);
    leftGripper.setInverted(true);
    rightGripper = new WPI_VictorSPX(20);//?????
    groundCollect = new WPI_TalonSRX(29);

    DataLogger.addDataElement("CargoDistanceSensor", this::cargoDistanceSensor);
    
    stop();
    stopGroundCollect();
  }

  /** 
   * Will be linked to TOF (Time of Flight) sensor when 
   * electrical/controls get it setup for us
   * 
   * TODO link this up
   */
  public double cargoDistanceSensor() {
    return -1;
  }

  public boolean hasCargo() {
    return cargoDistanceSensor() >= 0 && cargoDistanceSensor() <= hasCargoDistance;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    //setDefaultCommand(new HABClimb());
  }
    
  public void collectBall(){
    System.out.println("collect it");
    groundCollect.set(ControlMode.PercentOutput, 1.0);
  }

  public void ejectBall(){
    System.out.println("eject it");
    groundCollect.set(ControlMode.PercentOutput, -1.0);
  }

  public void stopGroundCollect(){
    System.out.println("stop it");
    groundCollect.set(ControlMode.PercentOutput, 0.0);  
  }

  public void stop(){
    setPower(0.0);
  }

  public void hold() {
    setPower(holdPower);
  }

  private void setPower(double pwr){
    leftGripper.set(ControlMode.PercentOutput, pwr);
    rightGripper.set(ControlMode.PercentOutput, pwr);
  }

  public void deployGroundCollect() {
    deployer.set(DoubleSolenoid.Value.kForward);
  }

  public void retractGroundCollect() {
    deployer.set(DoubleSolenoid.Value.kReverse);
  }

  public void collect() {
    setPower(collectPower);
  }

  public void eject(){
    setPower(-collectPower);
  }

}
