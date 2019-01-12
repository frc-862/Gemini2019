/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;

public class Kick extends TimedCommand {
  public Kick() {
    super(1.0, Robot.shooter);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.shooter.kick();
  }
}
