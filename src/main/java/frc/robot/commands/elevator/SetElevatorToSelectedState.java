/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Elevator;

public class SetElevatorToSelectedState extends Command {
  public SetElevatorToSelectedState() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.elevator);
  }

  private Elevator.HeightState heightState;

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    heightState = Robot.elevator.getHeightState();

    switch (heightState){
      case HIGH_ROCKET:
        Robot.elevator.goToHigh();
        break;
      case LOW_ROCKET:
        Robot.elevator.goToLow();
        break;
      case MID_ROCKET:
        Robot.elevator.goToMid();
        break;
      case CARGO_COLLECT:
        Robot.elevator.goToCollect();
        break;
    }

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
