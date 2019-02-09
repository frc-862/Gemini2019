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

      
      if (Math.abs(squint) > SQUINT_BOUND) {
        double adjustment = Math.signum(squint) * Math.pow(Math.abs(squint), 0.5) * 0.05;
        Robot.drivetrain.setPower(.3  + adjustment, .21  - adjustment);
        
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
      else if (target.standoff() > 30) {

          double power = 0.015 * target.standoff() + 0.075;
          Robot.drivetrain.setPower(power, power);
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
