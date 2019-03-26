/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.LEDs;

import edu.wpi.first.wpilibj.Timer;
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


    /*

    STATES
        //Normal
        OFF,                    //0
        STD_BLUE_ORANGE_CHASE,  //1
        //Vision/Line Ready
        LINE_FOLLOW_READY,      //10
        VISION_READY,           //11
        //Game Piece
        PIECE_COLLECTED,        //100
        //Climb
        TIME_2_CLIMB_KIDS,      //101
        CLIMBING,               //110
        ALL_DONE_NAP_TIME       //111


    */
    // Called repeatedly when this Command is scheduled to run
    LEDs.State state;
    @Override
    protected void execute() {

        if(Robot.core.hasCargo()||Robot.core.hasHatch()) state = LEDs.State.PIECE_COLLECTED;

        else if(Timer.getMatchTime() < 30) state = LEDs.State.TIME_2_CLIMB_KIDS;

        else if(Robot.core.isVisionReady()) state = LEDs.State.VISION_READY;
        else if(Robot.core.timeOnLine() > 0.25) state = LEDs.State.LINE_FOLLOW_READY;
        else state = LEDs.State.STD_BLUE_ORANGE_CHASE;

        if(Robot.climber.getTicks() > 5) state = LEDs.State.CLIMBING;
        if(Robot.climber.isDoneClimbing()) state = LEDs.State.ALL_DONE_NAP_TIME;

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
