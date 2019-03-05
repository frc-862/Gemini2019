//Button X on controller
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

public class VisionRotateAndApproach extends Command {


  private final double SQUINT_BOUND = 5;
  private final double SQUINT_DISTANCE_RATIO = 0.05;
  private final double ROBOT_BASE_POWER = .25;
  private final double SQUINT_POWER = 0.5;
  private final double SQUINT_WEIGHT = 0.05;
  private final double STANDOFF_POWER = 0.075;
  private final double MIN_TARGET_DISTANCE = 10;
  private final double SINGLE_RECT_BOUND = 15;


  public VisionRotateAndApproach() {
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
      double squint = target.squint();
      double standoff = target.standoff();
      double leftRectSquint = Robot.vision.getLeftRect().squint();
      double rightRectSquint = 0;
      double leftSquintGoal = 0;

      
      if (Math.abs(squint) >  standoff * SQUINT_DISTANCE_RATIO && standoff > 40) {
        double adjustment = Math.signum(squint) * Math.pow(Math.abs(squint), SQUINT_POWER) * SQUINT_WEIGHT;
        //Robot.drivetrain.setPower(ROBOT_BASE_POWER  + adjustment, ROBOT_BASE_POWER  - adjustment);
        
        //Robot.drivetrain.setPower(.2 * Math.signum(squint), -.2 * Math.signum(squint)); 
          //Robot.drivetrain.setPower(0.4,0.4);
          SmartDashboard.putString("vision turn status", "turning");
      }
      
      
 
     /*
      if (Math.abs(squint) > SQUINT_BOUND) {
   
        
        //0.02 to account for standoff implementation
        Robot.drivetrain.setPower(.2 * Math.signum(squint) * target.standoff() * 0.02, 
         -.2 * Math.signum(squint) * 0.02 * target.standoff()); 
        //Robot.drivetrain.setPower(0.4,0.4);
        SmartDashboard.putString("vision turn status", "turning");
        
      }
    */
      //Keep one rectangle in the view of each camera
      /*
      else if (target.standoff() > MIN_TARGET_DISTANCE){

        if ((leftRectSquint >= -SINGLE_RECT_BOUND) && (rightRectSquint <= SINGLE_RECT_BOUND)){
          Robot.drivetrain.setPower(ROBOT_BASE_POWER, ROBOT_BASE_POWER);
        }
        else if ((leftRectSquint < -SINGLE_RECT_BOUND) && (rightRectSquint <= SINGLE_RECT_BOUND)){
          Robot.drivetrain.setPower(ROBOT_BASE_POWER + 0.25, ROBOT_BASE_POWER - 0.25);
        }
        else if ((leftRectSquint >= -SINGLE_RECT_BOUND) && (rightRectSquint > SINGLE_RECT_BOUND)){
          Robot.drivetrain.setPower(ROBOT_BASE_POWER + 0.25, ROBOT_BASE_POWER - 0.25);
        }
        else {
          SmartDashboard.putString("vision turn status", "lost target");
        }
      }
      */

      else if (standoff > MIN_TARGET_DISTANCE){

        leftSquintGoal = 132.958 * Math.pow(target.standoff(), -0.7778);

        if (leftRectSquint >= leftSquintGoal){
         // Robot.drivetrain.setPower(0.275, 0.325);
          SmartDashboard.putString("vision turn status", "turning left");
        }
        else if (leftRectSquint < leftSquintGoal){
          //Robot.drivetrain.setPower(0.325,0.275);
          SmartDashboard.putString("vision turn status", "turning right");
        }

      }

/*
      else if (target.standoff() > MIN_TARGET_DISTANCE) {

          double power = ROBOT_BASE_POWER * target.standoff() + STANDOFF_POWER;
          Robot.drivetrain.setPower(power, power);
          SmartDashboard.putString("vision turn status", "not turning");
          
      }
*/
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
