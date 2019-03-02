//Button A on controller
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.util.NoTargetException;
import frc.lightning.util.Target;
import frc.robot.Robot;

public class VisionDriveAndAdjust extends Command {


  private final double SQUINT_BOUND = 3;
  private final double ROTATION_BOUND = 7;
  private final double SQUINT_POSITIVE_EDGE = 10;
  private final double PIXEL_POSITIVE_EDGE = 100;
  private final double DISTANCE_ADJUST_RATIO = 10;
  private final double ROTATION_ADJUST_RATIO = 20;
  private final double ADJUST_RATIO = 15;
  

  public VisionDriveAndAdjust() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.vision);
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    try {
      Target target = Robot.vision.getBestTarget();
      double squint = target.squint(), rotation = target.rotation(), standoff = target.standoff();
      int x = target.x();
      double squintGoal;
      double driveAdjustment;
      double centerAngle = 0;
      int boundsAdjustment = 1;

      if (target.standoff() > 100){

        double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.05; //power constant taken from the else if block directly below
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("adjust status", "only squint");
      }
      //Archaic solution
      /*
      else if (target.standoff() > 50 && Math.abs(target.rotation()) > 30 && target.rotation() > 0){

        squint = squint - 20 * Math.signum(target.rotation());
        double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.05;
        double rotateAdjustment = Math.signum(rotation) * .1;
        Robot.drivetrain.setPower(.3 + squintAdjustment - rotateAdjustment, .3 - squintAdjustment + rotateAdjustment);
        SmartDashboard.putString("squint status", "right");
      }

      else if (target.standoff() > 50 && Math.abs(target.rotation()) > 30 && target.rotation() < 0){
        squint = squint + 20 * Math.signum(target.rotation());
        double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.05;
        double rotateAdjustment = Math.signum(rotation) * .1;
        Robot.drivetrain.setPower(.3 + squintAdjustment - rotateAdjustment, .3 - squintAdjustment + rotateAdjustment);
        SmartDashboard.putString("squint status", "left");
      }
      */

      /*
      //----Start of John's solution--------------------------------------------------------------------------------
      //When target rotation is positive
      else if (Math.abs(target.rotation()) > ROTATION_BOUND && target.standoff() > 50){
        squint = -SQUINT_POSITIVE_EDGE - squint;
        double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.08;
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("squint status", "right");
      }
      //When target rotation is negative
      else if (Math.abs(target.rotation()) < ROTATION_BOUND && target.standoff() > 50){
        squint = SQUINT_POSITIVE_EDGE - squint;
        double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.08;
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("squint status", "left");
      }
       //-----End of John's solution-----------------------------------------------------------------------------
      */

      //New squint correct

      else if (Math.abs(squint) > SQUINT_BOUND || Math.abs(target.rotation()) > ROTATION_BOUND) {

        if (squint > SQUINT_POSITIVE_EDGE){
          boundsAdjustment = -1;
        }
        else if (squint < -SQUINT_POSITIVE_EDGE){
          boundsAdjustment = -1;
        }
        else {
          boundsAdjustment = 1;
        }

        squintGoal = centerAngle + (standoff / DISTANCE_ADJUST_RATIO) * (target.rotation() / ROTATION_ADJUST_RATIO);
        driveAdjustment = squint - squintGoal;
        Robot.drivetrain.setPower(.3 + driveAdjustment / ADJUST_RATIO * boundsAdjustment,.3 - driveAdjustment / ADJUST_RATIO * boundsAdjustment);
        SmartDashboard.putString("adjust status", "everything");
        }

      /*
      Old correct adjustment
      else if (Math.abs(squint) > SQUINT_BOUND || Math.abs(target.rotation()) > ROTATION_BOUND) {

        double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.05; //power constant taken from the else if block directly below
       // double rotateAdjustment = Math.signum(rotation) * Math.pow(Math.abs(rotation), 0.5) * 0.05 / (Math.abs(squint) + 1);// / Math.abs(squint);
        double rotateAdjustment = Math.signum(rotation) * .1;
        Robot.drivetrain.setPower(.3 + squintAdjustment - rotateAdjustment, .3 - squintAdjustment + rotateAdjustment);
        SmartDashboard.putString("squint status", "squint correct");
        
      }
      */


      else if (target.standoff() > 20) {

          //double power = 0.035 * target.standoff() + 0.075;
          Robot.drivetrain.setPower(0.25, 0.25);
          SmartDashboard.putString("adjust status", "not turning");
          
      }

      else {
        Robot.drivetrain.setPower(0,0);
        SmartDashboard.putString("adjust status", "not moving");
      }

  } catch(NoTargetException e) {

      
      Robot.drivetrain.setPower(0, 0);
      SmartDashboard.putString("adjust status", "no target");
      

  }
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
