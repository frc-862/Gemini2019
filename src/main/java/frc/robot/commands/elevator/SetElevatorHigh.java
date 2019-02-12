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

public class SetElevatorHigh extends Command {
    public SetElevatorHigh() {
        requires(Robot.elevator);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.elevator.goToHigh();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return epsilon(Robot.elevator.elevatorMotor.getSelectedSensorPosition(), Constants.elevatorTopHeight, 10);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() { }

    private boolean epsilon(double value, double target, double tolerance) {
        return Math.abs(value - target) <= tolerance;
    }
}
