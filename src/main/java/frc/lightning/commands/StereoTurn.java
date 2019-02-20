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

public class StereoTurn extends Command {

  private final double SQUINT_BOUND = 3;
  private final double CAMERA_TO_CENTER = 10;
  double centerStandoffFromLeft, centerSquintFromLeft;

  public StereoTurn() {
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

    
    
    //centerStandoffFromLeft = Math.pow(Math.pow( (leftStandoff * Math.cos(leftSquint)), 2) + Math.pow(leftStandoff * Math.sin(leftSquint + CAMERA_TO_CENTER), 2), .5);

    try {
      double leftSquint = Robot.vision.getBestTarget().squint();
      double leftStandoff = Robot.vision.getBestTarget().standoff();

      centerStandoffFromLeft = Math.pow(Math.pow( (leftStandoff * Math.cos(leftSquint)), 2) + Math.pow(leftStandoff * Math.sin(leftSquint + CAMERA_TO_CENTER), 2), .5);
      centerSquintFromLeft = Math.atan((leftStandoff * Math.cos(leftSquint)) / (leftStandoff * Math.sin(leftSquint) + CAMERA_TO_CENTER));

      if (Math.abs(centerSquintFromLeft) > SQUINT_BOUND) {

          Robot.drivetrain.setPower(0.3 * Math.signum(centerSquintFromLeft), 0.3 * -Math.signum(centerSquintFromLeft));
          //Robot.drivetrain.setPower(0.4,0.4);
          SmartDashboard.putString("vision turn status", "turning");
          SmartDashboard.putString("Left Stereo Standoff", Double.toString(centerStandoffFromLeft));
        
      }
      else {
          Robot.drivetrain.setPower(0, 0);
          SmartDashboard.putString("vision turn status", "not turning");
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
