/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;



import frc.lightning.logging.CommandLogger;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;


public class LineFollow extends Command {
  CommandLogger logger = new CommandLogger(getClass().getCanonicalName());
  public LineFollow() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    logger.addDataElement("error");
        logger.addDataElement("turn");
        logger.addDataElement("velocity");
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    logger.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    final double turnP = .35;
        final double turningVelocity = 2.5;
        final double straightVelocity = 7;
       

        // read & weight the sensors
        final double error = Robot.core.lineSensor();
        final double turn = error * turnP;
        final double velocity = (error < 0.5) ? straightVelocity : turningVelocity;

        logger.set("error", error);
        logger.set("turn", turn);
        logger.set("velocity", velocity);
        logger.writeValues();

        // drive
        Robot.drivetrain.setVelocity(velocity - turn, velocity + turn);

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
