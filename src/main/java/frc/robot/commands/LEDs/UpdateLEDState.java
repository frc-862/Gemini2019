/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.LEDs;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.LEDs;

public class UpdateLEDState extends Command {
    public UpdateLEDState() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        //requires(Robot.drivetrain);
        requires(Robot.leds);
        //requires(Robot.core);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        LEDs.State state = LEDs.State.NORMAL;
        if(Robot.core.hasCargo()) state = LEDs.State.CARGO_COLLECTED;
        else if(Robot.core.hasHatch()) state = LEDs.State.HATCH_COLLECTED;
        else if(Robot.core.timeOnLine() > 0.25) state = LEDs.State.LINE_FOLLOWING;
        else if(Robot.oi.getRainbowButtonPressed()) state = LEDs.State.RAINBOW;
        else state = LEDs.State.NORMAL;
        Robot.leds.setState(state);
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
