/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.commands;

import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.util.NoTargetException;
import frc.lightning.util.Target;
import frc.robot.Robot;

public class WaypointThenTarget extends Command {

  private double waypointSquint = -1, waypointStandoff = -1, waypointRotationToTarget = -1;
  private double startRotation = 0, startLeftEncoderDist = 0, startRightEncoderDist = 0;
  
  private enum state {
    SEEKING, ROTATE_TO_WAYPOINT, APPROACH_WAYPOINT, ROTATE_TO_TARGET, APPROACH_TARGET, COMPLETED;
  }

  private state currentState = state.SEEKING;
  //The distance from the waypoint to the target is this proportion of standoff.
  private final double WAYPOINT_DISTANCE_SCALE = 0.2;

  public WaypointThenTarget() {
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
          Target target = Robot.vision.getBestTarget();
          SmartDashboard.putString("waypoint data frame", "standoff: " + target.standoff() + ", squint: " + target.squint() + ", rotation: " + target.rotation());
          if(Math.abs(target.rotation()) < 5) {
            currentState = state.APPROACH_TARGET;
            break;
          }
          //Using law of cosines to calculate the distance from the waypoint (waypointStandoff)
          waypointStandoff = Math.sqrt(
            Math.pow(target.standoff() * WAYPOINT_DISTANCE_SCALE, 2)
             + Math.pow(target.standoff(), 2) 
             + 2 * target.standoff() * WAYPOINT_DISTANCE_SCALE * target.standoff() 
             * Math.cos(Math.toRadians(90 - Math.abs(target.squint()) - (90 - Math.abs(target.rotation())))));
          
          //Using law of sines to calculate squint to waypoint
          double targetToWaypointAngle = Math.toDegrees(Math.asin(
            Math.sin(
              Math.toRadians(90 - Math.abs(target.squint()) - (90 - Math.abs(target.rotation()))))
               / waypointStandoff * target.standoff() * WAYPOINT_DISTANCE_SCALE));
          
          if((Math.signum(target.squint()) == Math.signum(target.rotation()) && Math.abs(target.squint()) > Math.abs(target.rotation())) || Math.signum(target.squint()) != Math.signum(target.rotation())) {
            SmartDashboard.putNumber("squint case", 1);
            waypointSquint = Math.abs(targetToWaypointAngle) + Math.abs(target.squint());
            waypointSquint *= Math.signum(target.squint());
          }
          else if(Math.abs(target.squint()) > Math.abs(targetToWaypointAngle)) {
            SmartDashboard.putNumber("squint case", 2);
            waypointSquint = Math.abs(target.squint()) - Math.abs(targetToWaypointAngle);
            waypointSquint *= Math.signum(target.squint()) * -1;
          }
          else {
            SmartDashboard.putNumber("squint case", 3);
            waypointSquint = Math.abs(targetToWaypointAngle) - Math.abs(target.squint());
            waypointSquint *= Math.signum(target.squint()) * -1;
          }
          
          waypointRotationToTarget =
            (180 - 
            (180 - 
            targetToWaypointAngle - 
            (90 - Math.abs(target.squint()) - (90 - Math.abs(target.rotation())))));
            
            //(180 - (90 - Math.abs(target.rotation())) - (90 - Math.abs(waypointSquint)));

          if(Math.signum(waypointSquint) == Math.signum(target.squint()) && Math.abs(waypointSquint) > Math.abs(target.squint())) {
            waypointRotationToTarget *= Math.signum(target.squint()) * -1;
          }
          else {
            waypointRotationToTarget *= Math.signum(target.squint());
          }

            SmartDashboard.putNumber("waypointStandoff", waypointStandoff);
            SmartDashboard.putNumber("waypoint squint", waypointSquint);
            SmartDashboard.putNumber("target to waypoint", targetToWaypointAngle);
            SmartDashboard.putNumber("waypoint rotation to target", waypointRotationToTarget);

          startRotation = Robot.core.getFlippedContinuousHeading();
          currentState = state.ROTATE_TO_WAYPOINT;
        } catch(NoTargetException e) {
          Robot.drivetrain.setPower(0, 0);
        }
        break;
      case ROTATE_TO_WAYPOINT:
        SmartDashboard.putNumber("waypoint squint", waypointSquint);
        SmartDashboard.putNumber("start rotation", startRotation);
        SmartDashboard.putNumber("degrees turned", Robot.core.getFlippedContinuousHeading() - startRotation);
        if(Math.abs(waypointSquint - (Robot.core.getFlippedContinuousHeading() - startRotation)) > 3) {
          Robot.drivetrain.setPower(0.3 * -Math.signum(waypointSquint - (Robot.core.getFlippedContinuousHeading() - startRotation)), 0.3 * Math.signum(waypointSquint - (Robot.core.getFlippedContinuousHeading() - startRotation)));
        }
        else {
          Robot.drivetrain.setPower(0, 0);
          startLeftEncoderDist = Robot.drivetrain.getLeftDistance();
          startRightEncoderDist = Robot.drivetrain.getRightDistance();
          currentState = state.APPROACH_WAYPOINT;
        }
        break;
      case APPROACH_WAYPOINT:
        double distanceTraveled = (Robot.drivetrain.getLeftDistance() - startLeftEncoderDist + Robot.drivetrain.getRightDistance() - startRightEncoderDist) / 2.0;
        SmartDashboard.putNumber("waypoint standoff", waypointStandoff);
       
        if(Math.abs(waypointStandoff - distanceTraveled) > 12) {
          Robot.drivetrain.setPower(0.25, 0.25);
        }
        else {
          Robot.drivetrain.setPower(0, 0);
          startRotation = Robot.core.getFlippedContinuousHeading();
          currentState = state.ROTATE_TO_TARGET;
        }
        break;
      case ROTATE_TO_TARGET:
        SmartDashboard.putNumber("rotation to target", waypointRotationToTarget);
        SmartDashboard.putNumber("start rotation", startRotation);
        SmartDashboard.putNumber("degrees turned", Robot.core.getFlippedContinuousHeading() - startRotation);
        if(Math.abs(waypointRotationToTarget - (Robot.core.getFlippedContinuousHeading() - startRotation)) > 3) {
          Robot.drivetrain.setPower(0.4 * -Math.signum(waypointRotationToTarget - (Robot.core.getFlippedContinuousHeading() - startRotation)), 0.4 * Math.signum(waypointRotationToTarget - (Robot.core.getFlippedContinuousHeading() - startRotation)));
        }
        else {
          Robot.drivetrain.setPower(0, 0);
          startLeftEncoderDist = Robot.drivetrain.getLeftDistance();
          startRightEncoderDist = Robot.drivetrain.getRightDistance();          
          currentState = state.APPROACH_TARGET;
        }
        break;
      case APPROACH_TARGET:
        try {
          Target target = Robot.vision.getBestTarget();
          if(Math.abs(target.rotation()) > 10) {
            Robot.drivetrain.setPower(0, 0);
            currentState = state.SEEKING;
            break;
          }
          else {
            if (Math.abs(target.squint()) > 3) {
              Robot.drivetrain.setPower(0.25 * Math.signum(target.squint()), 0.25 * -Math.signum(target.squint()));
            }
            else {
              //double power = 0.035 * target.standoff() + 0.075;
              Robot.drivetrain.setPower(0.25, 0.25);
              
          }
          }

        } catch(NoTargetException e) {
          Robot.drivetrain.setPower(0, 0);
          currentState = state.SEEKING;
        }
        break;
      case COMPLETED:
        Robot.drivetrain.setPower(0,0);                      
        break;
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
