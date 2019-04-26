package frc.robot.commands.hatch;

import frc.lightning.commands.StatefulCommand;
import frc.robot.Robot;

public class SmartHatchCollect extends StatefulCommand {
    enum States {
        TOGGLE_HATCH,
        BACK_UP,
        EXTEND_COLLECTOR,
        DRIVE_FORWARD,
        GRAB,
        DONE
    }

    public SmartHatchCollect() {
        super(States.TOGGLE_HATCH);
    }

    @Override
    public void initialize() {
        if (Robot.oi.getRightTrigger()) {
            setState(States.TOGGLE_HATCH);
        } else {
            setState(States.BACK_UP);
        }
        super.initialize();
    }

    public void toggleHatch() {
        Robot.hatchPanelCollector.toggle();
        setState(States.DONE);
    }

    public void backUp() {
        Robot.drivetrain.setVelocity(-.5, -.5);
        if (timeInState() >= .5) {
            Robot.drivetrain.stop();
            setState(States.EXTEND_COLLECTOR);
        }
    }
    public void extendCollector() {
        Robot.hatchPanelCollector.collect();
        Robot.hatchPanelCollector.extend();
        setState(States.DRIVE_FORWARD);
    }
    public void driveForward(){
        Robot.drivetrain.setVelocity(.5, .5);
        if (timeInState() >= .75) {
            Robot.drivetrain.stop();
            setState(States.GRAB);
        }
    }
    public void grab(){
        Robot.hatchPanelCollector.eject();
        setState(States.DONE);
    }

    @Override
    protected boolean isFinished() {
        return getState()==States.DONE;
    }
}
