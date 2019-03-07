/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.lightning.commands.VelocityMotionProfile;
import frc.robot.commands.elevator.SetElevatorLow;
import frc.robot.commands.hatch.CloseHatchCollector;
import frc.robot.commands.hatch.ExtendHatchCollector;
import frc.robot.commands.hatch.OpenHatchCollector;
import frc.robot.commands.hatch.RetractHatchCollector;

public class HatchAuton extends CommandGroup {

  private Command elevatorPos;
  private String pathFile;

  public HatchAuton(String pathFile, Command elevatorPos) {

   this.elevatorPos = elevatorPos;
   this.pathFile = pathFile;

   addSequential(new CloseHatchCollector());
   addSequential(new SetElevatorLow());  
   addSequential(new VelocityMotionProfile(pathFile));
   addSequential(elevatorPos);
   addSequential(new TimedCommand(2));
   addSequential(new ExtendHatchCollector());
   addSequential(new TimedCommand(0.25));
   addSequential(new OpenHatchCollector());
   addSequential(new TimedCommand(0.25));
   addSequential(new RetractHatchCollector());

  }
}
