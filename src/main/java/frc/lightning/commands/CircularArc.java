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

public class CircularArc extends Command {

  private int leftVel, rightVel;
  private int l = 1, d = 1, r = 1;
  private double squint = 0.0;
  private Target target;
  private VisionWaypoint waypoint;
  private Arc arc;
  private boolean hasTarget;
  private double startPos;
  private final double BASE_POWER = 0.2;

  public CircularArc() {
    //TODO Re-Config PIDF Gains?
    requires(Robot.drivetrain);
    requires(Robot.vision);
  }

  @Override
  protected void initialize() {
    try {
      target = Robot.vision.getBestTarget();
      waypoint = new VisionWaypoint(target);
      SmartDashboard.putNumber("waypoint standoff", waypoint.standoff());
      SmartDashboard.putNumber("waypoint squint", waypoint.squint());
      arc = new Arc(waypoint);
      startPos = (Robot.drivetrain.getLeftDistance() + Robot.drivetrain.getRightDistance()) / 2;
      SmartDashboard.putNumber("arc length", arc.length());
      SmartDashboard.putNumber("arc angle", arc.angle());
      SmartDashboard.putNumber("vel ratio", arc.velocityRatio());
      hasTarget = true;
    } catch(NoTargetException e) {
      hasTarget = false;
      SmartDashboard.putString("arc error", e.toString());
    }
  }

  @Override
  protected void execute() {
    if(!hasTarget) {
      return;
    }
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
      //initialize();
      hasTarget = false;
    }
  }

  @Override
  protected boolean isFinished() {
    return !hasTarget;
  }

  @Override
  protected void end() {
    Robot.drivetrain.setPower(0.0, 0.0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
