/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.commands.StatefulCommand;
import frc.robot.Robot;

public class StatefulHatchDeploy extends StatefulCommand {

    private final double waitTime = 0.25;

    enum States {
        EXTEND,
        DEPLOY,
        RETRACT,
        DONE
    }

    public StatefulHatchDeploy() {
        super(States.EXTEND);
    }

    @Override
    protected void initialize() {
        setState(States.EXTEND);
    }

    @Override
    protected boolean isFinished() {
        return this.getState() == States.DONE;
    }

    //STATE COMMANDS

    private void extend() {
        Robot.hatchPanelCollector.extend();
        if(this.timeInState()>waitTime)setState(States.DEPLOY);
    }

    private void deploy() {
        Robot.hatchPanelCollector.eject();
        if(this.timeInState()>waitTime)setState(States.RETRACT);
    }

    private void retract() {
        Robot.hatchPanelCollector.retract();
        if(this.timeInState()>)setState(States.DONE);
    }

}
