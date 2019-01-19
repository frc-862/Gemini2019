/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;

public class SpinUpFlywheels extends TimedCommand {
  private final double power;

  public SpinUpFlywheels(double pwr, double duration) {
    super(duration, Robot.shooter);
    power = pwr;
  }

  public SpinUpFlywheels() {
    this(1, 2.5);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.shooter.setFlywheelPower(1);
  }

}
