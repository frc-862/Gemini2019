/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.DriveBackDuration;
import frc.robot.commands.LEDs.LEDsSetYellow;
import frc.robot.commands.elevator.setElevatorHigh;
import frc.robot.commands.elevator.setElevatorLow;

public class automatedHatchCollect extends CommandGroup {
  /**
   * Add your docs here.
   */
  public automatedHatchCollect() {
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.
    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.
    //Why does anyone care about gum
    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the arm.
    requires(Robot.hatchPanelCollector); 
    requires(Robot.leds);
    addSequential(new setElevatorLow());
    addSequential(new ExtendHatchCollector());
    addSequential(new DeployGroundHatchCollector());
    addSequential(new OpenHatchCollector());
    addSequential(new waitForHatchPanel());
    addSequential(new LEDsSetYellow());
    addSequential(new RetractHatchCollector()); 
    addSequential(new DriveBackDuration(1));
  }
}