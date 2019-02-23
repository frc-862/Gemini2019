/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.commands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.util.Target;
import frc.robot.Robot;

public class VisionTests extends Command {
  public VisionTests() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("\n\n\n\n\nData transformation test:\n");
    ArrayList<ArrayList<Target>> dataTransformTestData = Robot.vision.dataTransformationUnitTest();
    for (ArrayList<Target> l : dataTransformTestData) {
      for(Target t : l) {
        System.out.println(t.toString() + "\n\n");
      }
      System.out.println("\n");
    }
    System.out.println("\n\n\n\n\n");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
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
