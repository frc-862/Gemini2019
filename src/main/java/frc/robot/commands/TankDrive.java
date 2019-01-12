/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TankDrive extends Command {
  public TankDrive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double left = Robot.oi.getLeftPower();
    double right = Robot.oi.getRightPower();
    Robot.drivetrain.setPower(left, right);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
