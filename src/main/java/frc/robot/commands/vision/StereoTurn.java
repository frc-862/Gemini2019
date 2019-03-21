/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/


package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.util.NoTargetException;
import frc.robot.util.Target;

public class StereoTurn extends Command {

    private final double SQUINT_BOUND = 5;
    private final double BIG_SQUINT_BOUND = 5;
    private final double CAMERA_TO_CENTER = 10;
    double centerStandoffFromLeft, centerSquintFromLeft;
    double currentDistance = 0;

    public StereoTurn() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.stereoVision);
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {



        //centerStandoffFromLeft = Math.pow(Math.pow( (leftStandoff * Math.cos(leftSquint)), 2) + Math.pow(leftStandoff * Math.sin(leftSquint + CAMERA_TO_CENTER), 2), .5);

        try {
            // double leftSquint = Robot.stereoVision.getBestTarget().squint();
            // double leftStandoff = Robot.stereoVision.getBestTarget().standoff();

            // centerStandoffFromLeft = Math.pow(Math.pow( (leftStandoff * Math.cos(leftSquint)), 2) + Math.pow(leftStandoff * Math.sin(leftSquint + CAMERA_TO_CENTER), 2), .5);
            // centerSquintFromLeft = Math.atan((leftStandoff * Math.cos(leftSquint)) / (leftStandoff * Math.sin(leftSquint) + CAMERA_TO_CENTER));

            // if (Math.abs(centerSquintFromLeft) > SQUINT_BOUND) {

            //     Robot.drivetrain.setPower(0.3 * Math.signum(centerSquintFromLeft), 0.3 * -Math.signum(centerSquintFromLeft));
            //     //Robot.drivetrain.setPower(0.4,0.4);
            //     SmartDashboard.putString("stereoVision turn status", "turning");
            //     SmartDashboard.putString("Left Stereo Standoff", Double.toString(centerStandoffFromLeft));

            // }
            Target t = Robot.stereoVision.getBestTarget();
            final double squint = t.squint();
            final double standoff = t.standoff();
            if ( Math.abs(squint) > BIG_SQUINT_BOUND) {
                Robot.drivetrain.setPower(1 * Math.signum(squint),-1 * Math.signum(squint));
                SmartDashboard.putString("vision turn status", "big adjust");
                
            }
            else if (Math.abs(squint) > SQUINT_BOUND && standoff >= 20){

                double adjustPower = 0.3 * Math.signum(squint);
                Robot.drivetrain.setPower(.35 + adjustPower, .35 - adjustPower);
                SmartDashboard.putString("vision turn status", "small adjust");
                currentDistance = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
            }
            
             else {
                //double scaledStandoff = t.standoff() / 72; //start slowing down at 50 in from the target
                //final double maxPower = .5;
                //double robotPower = maxPower * scaledStandoff;
                //robotPower = Math.min(maxPower, robotPower);
                //robotPower = Math.max(.2, robotPower);
              
                    Robot.drivetrain.setPower(.35, .35); 
                    SmartDashboard.putString("vision turn status", "straight");
            } 

        } catch(NoTargetException e) {


            Robot.drivetrain.setPower(0, 0);
            SmartDashboard.putString("stereoVision turn status", "no target");


        }

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
        //return Robot.core.timeOnLine() > 0.2;
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
