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
  private final double ROTATION_BOUND = 3;
  private final double SQUINT_POSITIVE_EDGE = 10;

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
      double squint = target.squint(), rotation = target.rotation();

      //Invert squint adjustment if we start to lose the target (to bring it back into view)
      int squintInverter = target.type() != Target.type.COMPLETE ? -1 : 1;
      if (target.standoff() > 130){

        double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.08; //power constant taken from the else if block directly below
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("squint status", "only squint");
      }
      /*
      else if (target.standoff() > 50 && Math.abs(target.rotation()) > 30 && target.rotation() > 0){

        squint = squint - 20 * Math.signum(target.rotation());
        double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.05;
        double rotateAdjustment = Math.signum(rotation) * .1;
        Robot.drivetrain.setPower(.3 + squintAdjustment - rotateAdjustment, .3 - squintAdjustment + rotateAdjustment);
        SmartDashboard.putString("squint status", "right");
      }

      else if (target.standoff() > 50 && Math.abs(target.rotation()) > 30 && target.rotation() < 0){
        squint = squint + 20 * Math.signum(target.rotation());
        double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.05;
        double rotateAdjustment = Math.signum(rotation) * .1;
        Robot.drivetrain.setPower(.3 + squintAdjustment - rotateAdjustment, .3 - squintAdjustment + rotateAdjustment);
        SmartDashboard.putString("squint status", "left");
      }
      */
      //----Start of John's simple solution    ----------------------------------------------------------------------
      //When target rotation is positive
      /*
      else if (Math.abs(target.rotation()) > SQUINT_BOUND && target.standoff() > 50){
        squint = -SQUINT_POSITIVE_EDGE - squint;
        double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.05;
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("squint status", "right");
      }
      //When target rotation is negative
      else if (Math.abs(target.rotation()) < SQUINT_BOUND && target.standoff() > 50){
        squint = SQUINT_POSITIVE_EDGE - squint;
        double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.05;
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("squint status", "left");
      }
      */
    
      //Redo
       //If target rotation is positive
      else if (Math.abs(target.rotation()) > ROTATION_BOUND && target.standoff() > 50){
        squint = SQUINT_POSITIVE_EDGE + squint;
        double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.05;
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("squint status", "right");
      }
      //If target rotation is negative
      else if (Math.abs(target.rotation()) < -ROTATION_BOUND && target.standoff() > 50){
        squint = -SQUINT_POSITIVE_EDGE + squint;
        double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.05;
        Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
        SmartDashboard.putString("squint status", "left");
      }
      
       //-----End of John's simple solution-----------------------------------------------------------------------------

       


      else if (Math.abs(squint) > SQUINT_BOUND || Math.abs(target.rotation()) > ROTATION_BOUND) {

        double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.3) * 0.04; //power constant taken from the else if block directly below
       // double rotateAdjustment = Math.signum(rotation) * Math.pow(Math.abs(rotation), 0.5) * 0.05 / (Math.abs(squint) + 1);// / Math.abs(squint);
        double rotateAdjustment = Math.signum(rotation) * .05;
        Robot.drivetrain.setPower(.3 + squintAdjustment - rotateAdjustment, .3 - squintAdjustment + rotateAdjustment);
        SmartDashboard.putString("squint status", "squint correct");
        
      }


      else if (target.standoff() > 20) {

          //double power = 0.035 * target.standoff() + 0.075;
          Robot.drivetrain.setPower(0.25, 0.25);
          SmartDashboard.putString("vision turn status", "not turning");
          
      }

      else {
        Robot.drivetrain.setPower(0,0);
        SmartDashboard.putString("vision turn status", "not moving");
      }

  } catch(NoTargetException e) {

      
      Robot.drivetrain.setPower(0, 0);
      SmartDashboard.putString("vision turn status", "not turning");
      

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
