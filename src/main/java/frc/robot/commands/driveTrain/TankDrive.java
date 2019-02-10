/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.LightningMath;
import frc.robot.Robot;

public class TankDrive extends Command {
    public TankDrive() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        //requires(Robot.glitchDriveTrain);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double left = Robot.oi.getLeftPower();
        double right = Robot.oi.getRightPower();
        Robot.drivetrain.setPower(left, right);

        SmartDashboard.putNumber("RAW_LeftVelocity", Robot.drivetrain.getLeftVelocity());
        SmartDashboard.putNumber("RAW_RightVelocity", Robot.drivetrain.getRightVelocity());

        SmartDashboard.putNumber("FPS_LeftVelocity", LightningMath.talon2fps(Robot.drivetrain.getLeftVelocity()));
        SmartDashboard.putNumber("FPS_RightVelocity", LightningMath.talon2fps(Robot.drivetrain.getRightVelocity()));

        DataLogger.addDataElement("FPS_LeftVelocity", () -> LightningMath.talon2fps(Robot.drivetrain.getLeftVelocity()));
        DataLogger.addDataElement("FPS_RightVelocity", () -> LightningMath.talon2fps(Robot.drivetrain.getRightVelocity()));

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
