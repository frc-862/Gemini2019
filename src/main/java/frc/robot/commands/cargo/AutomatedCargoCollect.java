/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.cargo;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.LEDs.LEDsSetGreen;
import frc.robot.commands.elevator.SetElevatorLow;

public class AutomatedCargoCollect extends CommandGroup {
  /**
   * Add your docs here.
   */
  public AutomatedCargoCollect() {
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
    requires(Robot.cargoCollector);
    requires(Robot.elevator);
    addSequential(new DeployCargoCollector());
    addSequential(new StartCollectCargo());
    addSequential(new WaitForCargo());
    addSequential(new StartEjectCargo());
    addSequential(new SetElevatorLow());
    addSequential(new LEDsSetGreen());
    addSequential(new RetractCargoCollector());
  }
}
