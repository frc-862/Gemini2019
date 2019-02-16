//Button Y on controller
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

public class CircleToWaypoint extends Command {

  private double waypointSquint = -1, waypointStandoff = -1, waypointRotationToTarget = -1;
  private double startRotation = 0, startLeftEncoderDist = 0, startRightEncoderDist = 0;
  
  
  private enum state {
    SEEKING, ROTATE_TO_WAYPOINT, APPROACH_WAYPOINT, ROTATE_TO_TARGET, APPROACH_TARGET, COMPLETED;
  }
  

 

  private state currentState = state.SEEKING;
  //The distance from the waypoint to the target is this proportion of standoff.
  private final double WAYPOINT_DISTANCE_SCALE = 0.4;
  private final double TRACK_WIDTH = 22.0;

  public CircleToWaypoint() {
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
          if(Math.abs(target.rotation()) < 5 || target.standoff() < 40) {
            currentState = state.APPROACH_TARGET;
            break;
          }
          double standoff = target.standoff();
          double squint = Math.toRadians(target.squint());
          double rotation = Math.toRadians(target.rotation());
          double targetToWaypoint = standoff * WAYPOINT_DISTANCE_SCALE;
          //Using law of cosines to calculate the distance from the waypoint (waypointStandoff)
          waypointStandoff = Math.sqrt(
            Math.pow(targetToWaypoint, 2)
             + Math.pow(standoff, 2) 
             - 2 * standoff * targetToWaypoint * Math.abs(Math.cos(rotation)));
          
          //Using law of sines to calculate squint to waypoint
          double targetToWaypointAngle = Math.abs(Math.asin(
            Math.sin(rotation) / waypointStandoff * targetToWaypoint));
          
          if(Math.signum(squint) != Math.signum(rotation)) {
            waypointSquint = squint - (targetToWaypointAngle * Math.signum(squint));
          }
          else {
            waypointSquint = squint + (targetToWaypointAngle * Math.signum(squint));
          }
          
          waypointRotationToTarget = Math.signum(rotation) * -1 * (Math.PI - (Math.PI - Math.abs(rotation) - targetToWaypointAngle));
          
          double radius = waypointStandoff / (2 * Math.cos(Math.PI / 2 - Math.abs(squint)));

          squint = Math.toDegrees(squint);
          waypointSquint = Math.toDegrees(waypointSquint);
          waypointRotationToTarget = Math.toDegrees(waypointRotationToTarget);
          targetToWaypointAngle = Math.toDegrees(targetToWaypointAngle);
          rotation = Math.toDegrees(rotation);

            SmartDashboard.putNumber("waypointStandoff", waypointStandoff);
            SmartDashboard.putNumber("waypoint squint", waypointSquint);
            SmartDashboard.putNumber("target to waypoint", targetToWaypointAngle);
            SmartDashboard.putNumber("waypoint rotation to target", waypointRotationToTarget);

          startRotation = Robot.core.getContinuousHeading();
          currentState = state.ROTATE_TO_WAYPOINT;
        } catch(NoTargetException e) {
          Robot.drivetrain.setPower(0, 0);
        }
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
