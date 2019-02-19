/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.commands;

import edu.wpi.first.wpilibj.Timer;  
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.lightning.util.*;

public class TwoCircularArcOuter extends Command {

  private double leftVel, rightVel;
  private Target target;
  private VisionWaypoint waypoint;
  private Arc arc;
  private double startPos;
  private final double BASE_POWER = 0.3;
  private final double BASE_VELOCITY = 50;
  private double ARC_SQUINT_BOUND = 15;

  private double velocityRatio2;//velocity ratio for the second arc
  private double arcLength2; //arc length for the second arc

  //DriveAndAdjust constants
  private final double SQUINT_BOUND = 3;
  private final double ROTATION_BOUND = 3;
  private final double SQUINT_POSITIVE_EDGE = 10;

  private enum state {
    SEEKING, FIRST_ARC, SECOND_ARC, SEE_TARGET, COMPLETE;
  }

  private state currentState = state.SEEKING;

  public TwoCircularArcOuter() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.vision);
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
      currentState = state.SEEKING;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    SmartDashboard.putString("vision state", currentState.toString());
    switch(currentState) {
      case SEEKING:
      try {
        target = Robot.vision.getBestTarget();
        currentState = state.FIRST_ARC;
      }
      catch(NoTargetException e) {
        SmartDashboard.putString("arc error", e.toString());
      }
        break;
      case FIRST_ARC:
        
      double currentPos = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
      SmartDashboard.putNumber("start pos", startPos);
      SmartDashboard.putNumber("current pos", startPos);
      SmartDashboard.putNumber("distance traveled", (currentPos - startPos));
      if((currentPos - startPos) < arc.length()) {
        /*
        There is a velocity ratio between each motor to get them to drive in an arc. We can put control loop to make sure this ratio is working.
        */
        leftVel = BASE_VELOCITY;
        rightVel = BASE_VELOCITY;
        double leftPower = BASE_POWER, rightPower = BASE_POWER;
        velocityRatio2 = arc.velocityRatio();
        
        if(arc.velocityRatio() > 1) {
          leftVel *= arc.velocityRatio();
          leftPower *= arc.velocityRatio();
        }
        else {
          rightVel /= arc.velocityRatio();
          rightPower /= arc.velocityRatio();
        }
        //Robot.drivetrain.setVelocity(leftVel, rightVel);
        Robot.drivetrain.setPower(leftPower, rightPower);
        //Robot.drivetrain.setVelocity(leftVel, rightVel);//Can scale up ratio - 16:4 will drive same circle as 4:1, just faster
      }

      else{
        currentState = state.SECOND_ARC;
      }
        
        break;

      case SECOND_ARC:
      try {
        target = Robot.vision.getBestTarget();
        currentState = state.SEE_TARGET;
      }
      catch(NoTargetException e) {
        //run the guessed arc
      
        leftVel = BASE_VELOCITY;
        rightVel = BASE_VELOCITY;
        double leftPower = BASE_POWER, rightPower = BASE_POWER;
          if(velocityRatio2 < 1) {
          leftVel *= velocityRatio2;
          leftPower *= velocityRatio2;
        }
          else {
          rightVel /= velocityRatio2;
          rightPower /= velocityRatio2;
        }
       
        Robot.drivetrain.setPower(leftPower, rightPower);
        }

      
        break;

      case SEE_TARGET:
      //run DriveAndAdjust
      try {
        Target target = Robot.vision.getBestTarget();
        double squint = target.squint(), rotation = target.rotation();
  
        //Invert squint adjustment if we start to lose the target (to bring it back into view)
        int squintInverter = target.type() != Target.type.COMPLETE ? -1 : 1;
        if (target.standoff() > 70){
  
          double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.08; //power constant taken from the else if block directly below
          Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
          SmartDashboard.putString("squint status", "only squint");
        }
       
        //----Start of John's solution    ----------------------------------------------------------------------
      
       
        else if (target.rotation() > ROTATION_BOUND && target.standoff() > 30){
          squint = SQUINT_POSITIVE_EDGE + squint;
          double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.05;
         // double squintAdjustment = squintInverter * target.standoff() * target.rotation();
          Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
          SmartDashboard.putString("squint status", "right");
        }
        
      
        else if (target.rotation() < -ROTATION_BOUND && target.standoff() > 30){
          squint = -SQUINT_POSITIVE_EDGE + squint;
          double squintAdjustment = squintInverter * Math.signum(squint) * Math.pow(Math.abs(squint), 0.2) * 0.05;
          //double squintAdjustment = squintInverter * target.standoff() * target.rotation() * 0.00001;
          Robot.drivetrain.setPower(.3 + squintAdjustment, .3 - squintAdjustment);
          SmartDashboard.putString("squint status", "left");
        }
        
        
         //-----End of John's solution-----------------------------------------------------------------------------
  
       
  
        else if (Math.abs(squint) > SQUINT_BOUND || Math.abs(target.rotation()) > ROTATION_BOUND) {
  
          //double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.3) * 0.06; //power constant taken from the else if block directly below
          double squintAdjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.3) * target.standoff() * .002; //power constant taken from the else if block directly below
  
         // double rotateAdjustment = Math.signum(rotation) * Math.pow(Math.abs(rotation), 0.5) * 0.05 / (Math.abs(squint) + 1);// / Math.abs(squint);
          //double rotateAdjustment = Math.signum(rotation) * .03;
          double rotateAdjustment = Math.signum(rotation) / target.standoff();
  
          Robot.drivetrain.setPower(.3 + squintAdjustment - rotateAdjustment, .3 - squintAdjustment + rotateAdjustment);
          SmartDashboard.putString("squint status", "squint correct");
  
  
          
        }
  
  
        else if (target.standoff() > 20) {
  
            //double power = 0.035 * target.standoff() + 0.075;
            Robot.drivetrain.setPower(0.25, 0.25);
            SmartDashboard.putString("vision turn status", " driving straight");
            
        }
  
        else {
          Robot.drivetrain.setPower(0,0);
          SmartDashboard.putString("vision turn status", "not moving");
          currentState = state.COMPLETE;
        }
  
    } catch(NoTargetException e) {
  
        Robot.drivetrain.setPower(0, 0);
        SmartDashboard.putString("vision turn status", "not turning");
        
    }

      case COMPLETE:
      
        return;
    }

    SmartDashboard.putNumber("waypoint standoff", waypoint.standoff());
    SmartDashboard.putNumber("waypoint squint", waypoint.squint());
    arc = new Arc(waypoint);
    startPos = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
    SmartDashboard.putNumber("arc length", arc.length());
    SmartDashboard.putNumber("arc angle", arc.angle());
    SmartDashboard.putNumber("vel ratio", arc.velocityRatio());
    

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
