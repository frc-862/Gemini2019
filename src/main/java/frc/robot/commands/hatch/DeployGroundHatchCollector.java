/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DeployGroundHatchCollector extends Command {
    public DeployGroundHatchCollector() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.hatchGroundCollector);

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.hatchGroundCollector.toggleDeployer();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return this.timeSinceInitialized() >= 0.2;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.hatchGroundCollector.toggleDeployer();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
