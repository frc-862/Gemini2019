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
      double squint = target.squint();

      if (target.standoff() > 60){

        double squintAdjustment = target.squint() * 0.005 * 0.0025; //power constants taken from the else if block directly below
        Robot.drivetrain.setPower(.3 - squintAdjustment, .3 + squintAdjustment);

      }

      else if (Math.abs(squint) > SQUINT_BOUND || Math.abs(target.rotation()) > ROTATION_BOUND) {

          double adjustment = target.squint() * 0.005 + Math.signum(target.rotation()) * Math.pow(Math.abs(target.rotation()), 1.3) * 0.0025;
          Robot.drivetrain.setPower(.3 - adjustment, .3 + adjustment);
          //Robot.drivetrain.setPower(0.4,0.4);
          SmartDashboard.putString("vision turn status", "turning");
        
      }



      else if (target.standoff() > 30) {

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
