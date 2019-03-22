// /*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.lightning.commands.InterruptableVelocityMotionPath;
import frc.lightning.commands.VelocityMotionProfile;
import frc.robot.Robot;
import frc.robot.commands.LineFollow;
import frc.robot.commands.elevator.SetElevatorLow;
import frc.robot.commands.hatch.CloseHatchCollector;
import frc.robot.commands.hatch.ExtendHatchCollector;

public class CenterPath extends CommandGroup {

    public CenterPath() {
        addSequential(new CloseHatchCollector());
        addSequential(new SetElevatorLow());

        addSequential(new InterruptableVelocityMotionPath("ShipM_StartM_EndR", (InterruptableVelocityMotionPath mp) -> { 
            return Robot.core.timeOnLine() > 0.2;
        }));

        addSequential(new LineFollow());

        addSequential(new ConditionalCommand(new ExtendHatchCollector()) {
        
            @Override
            protected boolean condition() {
                return Math.abs(Robot.core.lineSensor()) < 1.5;
            }
        });

    }
}
