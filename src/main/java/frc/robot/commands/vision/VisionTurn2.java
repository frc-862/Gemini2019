/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.NoTargetException;
import frc.robot.util.Target;

public class VisionTurn2 extends Command {

//Variables and constants that will be moved to another class later

    private final double SQUINT_BOUND = 7;
    boolean finishing = false;
    private Target target;
    private Target target2;
    private double startRotation;
    private double startDist;
    private double finalHeading = 0;
    private int counter = 0;
    private double finalApproachDist;

//End of things that will be moved later

    public VisionTurn2() {
      finalApproachDist = 0;
        // Use requires() here to declare subsystem dependencies
        requires(Robot.vision);
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      finishing = false;
        Robot.drivetrain.setPower(0,0);
        finalHeading = 0;
        target = null;
        Timer.delay(0.2);
        try {
            target = Robot.vision.getBestTarget();
        } catch(NoTargetException e) {
            end();
        }
        startRotation = Robot.core.getContinuousHeading();
        startDist = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

        counter++;
        try {
            target = Robot.vision.getBestTarget();
            startRotation = Robot.core.getContinuousHeading();
        } catch(NoTargetException e) {
        }
        //Robot.drivetrain.setPower(0.3,0.3);
        //SmartDashboard.putString("vision turn status", "VisionTurn start");
        if (target == null){
            SmartDashboard.putString("vision turn status", "target1 null");
            end();
        }
        else if(!finishing && target.standoff() < 140 && target.standoff() > 25 && Math.abs(target.squint() - (Robot.core.getContinuousHeading() - startRotation)) > SQUINT_BOUND) {
            double basePower = 0.15 * Math.signum(target.squint());
            SmartDashboard.putNumber("visionturn sign", basePower);
            Robot.drivetrain.setPower(0.2 + basePower, 0.2 -basePower);
            System.out.println("Moving . . . ");
            SmartDashboard.putNumber("vision target turn", target.squint());
            SmartDashboard.putNumber("counter",counter);
            SmartDashboard.putString("vision turn status", "VisionTurn turning");
        }
        else {
            startRotation = 0;
            SmartDashboard.putString("vision turn status", "turn done");
            if(finalHeading == 0) {
                finalHeading = Robot.core.getContinuousHeading();
            }
            try {
                target2 = Robot.vision.getBestTarget();
                startDist = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
                SmartDashboard.putNumber("startDist", startDist);
                SmartDashboard.putNumber("standoff", target2.standoff());
                SmartDashboard.putString("vision turn status", "got target2");
                
            } catch(NoTargetException e) {
            }
            if(target2 == null) {
                end();
            }
            else if(target2.standoff() < 140 && target2.standoff() - ((Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2 - startDist) > 25) {
                SmartDashboard.putNumber("final heading", finalHeading);
                SmartDashboard.putNumber("DistAway",  ((Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2 - startDist));
                Robot.drivetrain.setPower(0.2,0.2);
                SmartDashboard.putString("vision turn status", "VisionTurn Straight");
                finalApproachDist = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
            } 
            else if((Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2 - finalApproachDist < 50) {
              SmartDashboard.putNumber("final approach dist", finalApproachDist);
              SmartDashboard.putString("vision turn status", "VisionTurn final approach");
              SmartDashboard.putNumber("final approach progress", (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2 - finalApproachDist);
              SmartDashboard.putNumber("encoder dist", ((Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2));
              Robot.drivetrain.setPower(0.2, 0.2);
              finishing = true;
            } 
            else if(finishing) {
              SmartDashboard.putString("vision turn status", "VisionTurn finished");
              Robot.drivetrain.setPower(0, 0);
            }
            else {
                end();
            }
        }

    }



    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
        //return ((Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2) >= (target.standoff() - 12);
        
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.setPower(0,0);
        //SmartDashboard.putString("vision turn status", "VisionTurn Done");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
