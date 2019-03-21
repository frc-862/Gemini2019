/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class GroundCollector extends Subsystem {
 
  public WPI_VictorSPX groundollect;
  public GroundCollector() {
    groundollect = new WPI_VictorSPX(RobotMap.groundCollect);
        addChild("Ground Collect", groundollect);
    
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }
  public void setGroundCollet(double pwr){
    groundollect.set(ControlMode.PercentOutput, pwr);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
