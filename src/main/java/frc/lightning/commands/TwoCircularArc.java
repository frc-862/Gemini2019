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
  private double startPos;
  private final double BASE_POWER = 0.2;

  private enum state {
    NO_TARGET, FIRST_ARC, SECOND_ARC, COMPLETE;
  }

  private state currentState = state.NO_TARGET;

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
      switch(currentState) {
        case NO_TARGET:
          currentState = state.FIRST_ARC;
          waypoint = new MidpointWaypoint(target);
          break;
        case FIRST_ARC:
          currentState = state.SECOND_ARC;
          waypoint = new AlignedWaypoint(target, 10);
          break;
        case SECOND_ARC:
          currentState = state.COMPLETE;
          return;
          break;
        case COMPLETE:
          break;
      }
      SmartDashboard.putNumber("waypoint standoff", waypoint.standoff());
      SmartDashboard.putNumber("waypoint squint", waypoint.squint());
      arc = new Arc(waypoint);
      //create second arc with second waypoint
      startPos = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
      SmartDashboard.putNumber("arc length", arc.length());
      SmartDashboard.putNumber("arc angle", arc.angle());
      SmartDashboard.putNumber("vel ratio", arc.velocityRatio());
    } catch(NoTargetException e) {
      SmartDashboard.putString("arc error", e.toString());
      currentState = state.NO_TARGET;
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    SmartDashboard.putString("circle state", currentState.toString());
    if(currentState == state.FIRST_ARC || currentState == state.SECOND_ARC) {
      double currentPos = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
      if(currentPos - startPos < arc.length()) {
        /*
        There is a velocity ratio between each motor to get them to drive in an arc. We can put control loop to make sure this ratio is working.
        */
        double leftPower = BASE_POWER, rightPower = BASE_POWER;
        if(arc.velocityRatio() > 1) {
          leftPower *= arc.velocityRatio();
        }
        else {
          rightPower *=  1 / arc.velocityRatio();
        }
        Robot.drivetrain.setPower(leftPower, rightPower);
        //Robot.drivetrain.setVelocity(leftVel, rightVel);//Can scale up ratio - 16:4 will drive same circle as 4:1, just faster
      }
      else {
        Robot.drivetrain.setPower(0,0);
        initialize();
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return currentState == state.COMPLETE || currentState == state.NO_TARGET;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drivetrain.setPower(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
