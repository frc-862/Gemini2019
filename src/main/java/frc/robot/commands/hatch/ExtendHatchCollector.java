/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class ExtendHatchCollector extends InstantCommand {
  /**
   * Add your docs here.
   */
  public ExtendHatchCollector() {
    super();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.hatchPanelCollector);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.hatchPanelCollector.extend();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return this.timeSinceInitialized() >= 0.1; 
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
   
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

}
