/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.test;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.logging.CommandLogger;
import frc.robot.Robot;

public class RightDriveZero extends Command {

    CommandLogger logger = new CommandLogger(getClass().getSimpleName());

    public RightDriveZero() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);

        logger.addDataElement("actualLeftVelocity");
        logger.addDataElement("actualRightVelocity");

        logger.addDataElement("actualTheta");

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        (new ResetDriveSensors()).start();
        Robot.drivetrain.getRightMaster().set(ControlMode.MotionMagic, 0);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

        logger.set("actualLeftVelocity", Robot.drivetrain.getLeftVelocity());
        logger.set("actualRightVelocity", Robot.drivetrain.getRightVelocity());

        logger.set("actualTheta", Robot.core.getYaw());

        Robot.drivetrain.getLeftMaster().set(ControlMode.PercentOutput, 0.35);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (Math.abs(Robot.core.getYaw() - 180) < 2.5);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

}
