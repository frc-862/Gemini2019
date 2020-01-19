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
import frc.robot.commands.DriverAssist;
import frc.robot.commands.LineFollow;
import frc.robot.commands.elevator.SetElevatorLow;
import frc.robot.commands.hatch.CloseHatchCollector;
import frc.robot.commands.hatch.ExtendHatchCollector;

public class CenterPath extends CommandGroup {

    public CenterPath() {
        //addSequential(new AwaitButton(Robot.oi.awaitingStart));
        addSequential(new CloseHatchCollector());
        addSequential(new SetElevatorLow());
//        addSequential(new VelocityMotionProfile("ShipM_StartM_EndR"));
        addSequential(new InterruptableVelocityMotionPath("ShipM_StartM_EndR", (mp) ->
                (mp.getDuration() - mp.timeSinceInitialized() < 1.0) &&
                ((Robot.core.timeOnLine() > 0.254) || Robot.simpleVision.getObjectCount() == 2)));
        addSequential(new DriverAssist());
    }
}
