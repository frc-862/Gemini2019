/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.calibration;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.CommandLogger;
import frc.robot.Robot;

public class TestMove extends Command {
    private CommandLogger logger = new CommandLogger(getClass().getSimpleName());
    private double run_duration = 1.0;
    private double run_power    = 0.2;

    public TestMove() {
        SmartDashboard.putNumber("runDuration", 1.0);
        SmartDashboard.putNumber("runPower", 0.2);

        logger.addDataElement("runPower");
        logger.addDataElement("leftVelocity");
        logger.addDataElement("rightVelocity");
        logger.addDataElement("leftDistance");
        logger.addDataElement("rightDistance");

        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        logger.reset();
        run_duration = SmartDashboard.getNumber("runDuration", 1.0);
        run_power = SmartDashboard.getNumber("runPower", 0.2);
        Robot.drivetrain.resetDistance();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.drivetrain.setVelocity(run_power, run_power);
        logger.set("runPower", run_power);
        logger.set("leftVelocity", Robot.drivetrain.getLeftVelocity());
        logger.set("rightVelocity", Robot.drivetrain.getRightVelocity());
        logger.set("leftDistance", Robot.drivetrain.getLeftDistance());
        logger.set("rightDistance", Robot.drivetrain.getRightDistance());
        logger.write();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (timeSinceInitialized() > run_duration);
    }

    @Override
    protected void end() {
        Robot.drivetrain.stop();
        logger.flush();
    }
}
