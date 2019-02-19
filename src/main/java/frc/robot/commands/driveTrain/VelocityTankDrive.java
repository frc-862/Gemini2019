/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.util.LightningMath;
import frc.robot.Constants;
import frc.robot.Robot;

public class VelocityTankDrive extends Command {
    public VelocityTankDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.drivetrain.configurePID(Constants.drivePIDs);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

        SmartDashboard.putNumber("Right_Joy_Raw", Robot.oi.getRightPower());
        SmartDashboard.putNumber("Left_Joy_Raw", Robot.oi.getLeftPower());

        Robot.drivetrain.setVelocity(Math.pow((Robot.oi.getLeftPower()),  Constants.drivePowerCurve) * Constants.velocityMultiplier,
                                     Math.pow((Robot.oi.getRightPower()), Constants.drivePowerCurve) * Constants.velocityMultiplier);

        SmartDashboard.putNumber("RAW_LeftVelocity", Robot.drivetrain.getLeftVelocity());
        SmartDashboard.putNumber("RAW_RightVelocity", Robot.drivetrain.getRightVelocity());

        SmartDashboard.putNumber("FPS_LeftVelocity", LightningMath.talon2fps(Robot.drivetrain.getLeftVelocity()));
        SmartDashboard.putNumber("FPS_RightVelocity", LightningMath.talon2fps(Robot.drivetrain.getRightVelocity()));
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        // Robot.drivetrain.setVelocity(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
