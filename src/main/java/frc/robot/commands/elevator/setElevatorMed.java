/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;

public class setElevatorMed extends Command {
  public setElevatorMed() {
    requires(Robot.elevator); 
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  Robot.elevator.motor1.getSelectedSensorPosition();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.elevator.motor1.getSelectedSensorPosition()>Constants.elevatorMiddleHeight) {
    Robot.elevator.setpower(Constants.elevatorDownPower);  
    }else{
      Robot.elevator.setpower(Constants.elevatorUpPower);   
    } 
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return epsilon(Robot.elevator.motor1.getSelectedSensorPosition(), Constants.elevatorMiddleHeight, 10);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.elevator.setpower(Constants.elevatorHoldPower);  
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

  private boolean epsilon(double value, double target, double tolerance) {
    return Math.abs(value - target) <= tolerance;
  }
}
