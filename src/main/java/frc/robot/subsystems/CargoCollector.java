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
import frc.robot.Constants;
import frc.robot.commands.CollectCargo;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Add your docs here.
 */
public class CargoCollector extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private final WPI_VictorSPX  leftGripper;
  private final WPI_VictorSPX rightGripper;
  private final WPI_TalonSRX fourbar;
  DoubleSolenoid deployer;

  public CargoCollector(){
    leftGripper = new WPI_VictorSPX(21);
    rightGripper = new WPI_VictorSPX(20);//?????
    fourbar = new WPI_TalonSRX(29);
    stop();
    stopFourbar();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    //setDefaultCommand(new HABClimb());
  }

  public void collectBall(){
    fourbar.set(ControlMode.PercentOutput, 1.0);
  }

  public void ejectBall(){
    fourbar.set(ControlMode.PercentOutput, -1.0);
  }

  public void stopFourbar(){
    fourbar.set(ControlMode.PercentOutput, 0.0);  
  }
  public void stop(){
    System.out.println("STOP");
    leftGripper.set(0.0);
    rightGripper.set(0.0);
  }

  public void hold(){
    
  }

  public void setPower(double pwr){
    System.out.println("power " + pwr);
    leftGripper.set(pwr);
    rightGripper.set(pwr);
  }
  public void toggleDeployer() {
    if (deployer.get() == DoubleSolenoid.Value.kForward) {
      deployer.set(DoubleSolenoid.Value.kReverse);
    } else {
      deployer.set(DoubleSolenoid.Value.kForward);
    }
  }

  public void collect(){
    System.out.println("COLLECT");
    rightGripper.set(-0.6);
    leftGripper.set(0.6);
  }

  public void eject(){
    System.out.println("EJECT");
    rightGripper.set(0.6);
    leftGripper.set(-0.6);
  }

}
