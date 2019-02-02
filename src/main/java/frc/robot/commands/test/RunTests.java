/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.testing.SystemTestEngine;

public class RunTests extends Command {

  // Engine
  private SystemTestEngine engine;
  // Test Objects Here

  public RunTests() {
    engine = SystemTestEngine.makeEngine();
    // Requires all !!!
  }

  @Override
  protected void initialize() {
    // engine.registerTest(test);
    engine.runTests();
  }

  @Override
  protected void execute() {
  }

  @Override
  protected boolean isFinished() {
    return engine.isAllDone();
  }
}