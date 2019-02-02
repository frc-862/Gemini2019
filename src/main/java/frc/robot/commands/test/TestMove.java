/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.robot.Robot;

public class TestMove extends Command {

    private double run_duration = 1.0;
    private double run_power    = 0.2;

    public TestMove() {
        SmartDashboard.putNumber("runDuration", 1.0);
        SmartDashboard.putNumber("runPower", 0.2);
        DataLogger.addDataElement("runDuration", () -> run_duration);
        DataLogger.addDataElement("runPower", () -> run_power);
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        run_duration = SmartDashboard.getNumber("runDuration", 1.0);
        run_power = SmartDashboard.getNumber("runPower", 0.2);
        Robot.drivetrain.resetDistance();
    }
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.drivetrain.setPower(run_power, run_power);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (this.timeSinceInitialized() > run_duration);
    }

    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }
}
