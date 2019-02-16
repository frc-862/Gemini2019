/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.lightning.util.*;


public class TwoCircularArc extends Command {

  private int leftVel, rightVel;
  private int l = 1, d = 1, r = 1;
  private double squint = 0.0;
  private Target target;
  private VisionWaypoint waypoint;
  private Arc arc;
  private boolean hasTarget;
  private double startPos;
  private final double BASE_POWER = 0.2;

  private enum state {
   SEEKING, CIRCLE_1, CIRCLE_2, COMPLETED;
  }

  private state currentState = state.SEEKING;

  public TwoCircularArc() {

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.vision);
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    try {
      target = Robot.vision.getBestTarget();
      waypoint = new AlignedWaypoint(target);
      SmartDashboard.putNumber("waypoint standoff", waypoint.standoff());
      SmartDashboard.putNumber("waypoint squint", waypoint.squint());
      arc = new Arc(waypoint);
      //create second arc with second waypoint
      startPos = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
      SmartDashboard.putNumber("arc length", arc.length());
      SmartDashboard.putNumber("arc angle", arc.angle());
      SmartDashboard.putNumber("vel ratio", arc.velocityRatio());
      hasTarget = true;
    } catch(NoTargetException e) {
      hasTarget = false;
      SmartDashboard.putString("arc error", e.toString());
    }
    currentState = state.SEEKING;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    SmartDashboard.putString("circle state", currentState.toString());
    switch(currentState) {

      case SEEKING:

      

      
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
