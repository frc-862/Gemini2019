/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;

public class HatchShoot extends CommandGroup {

    public HatchShoot() {

        addSequential(new OpenHatchCollector());
        //addSequential(new TimedCommand(0.05));
        addSequential(new TimedCommand(0.0));
        addSequential(new ExtendHatchCollector());
        addSequential(new TimedCommand(0.5));
        addSequential(new RetractHatchCollector());

    }
}
