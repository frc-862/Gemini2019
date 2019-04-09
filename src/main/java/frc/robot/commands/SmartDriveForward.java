

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.lightning.commands.StatefulCommand;
import frc.robot.Robot;
import frc.robot.commands.climber.driveforward;

public class SmartDriveForward extends StatefulCommand {
    enum States {
        DRIVE_FORWARD,
        CLOSE_COLLECTOR,
        RETRACT_COLLECTOR,
        DRIVE_BACKWARDS,
        DONE

    }
    double timeStamp;
    public SmartDriveForward() {
        super(States.DRIVE_FORWARD);
        requires(Robot.drivetrain);
        requires(Robot.hatchPanelCollector);
    }

    public void driveForward() {
        Robot.hatchPanelCollector.extend();
        Robot.drivetrain.setVelocity(4,4);
        if(Robot.core.hasStopped()) {
            setState(States.CLOSE_COLLECTOR);
            timeStamp = Timer.getFPGATimestamp();
        }
    }
    public void closeCollector() {
        Robot.hatchPanelCollector.close();
        if(Timer.getFPGATimestamp()-timeStamp > .5) {
            setState(States.RETRACT_COLLECTOR);
            timeStamp = Timer.getFPGATimestamp();
        }
    }
    public void retractCollector() {
        Robot.hatchPanelCollector.retract();
        if(Timer.getFPGATimestamp()-timeStamp > .5) {
            setState(States.DRIVE_BACKWARDS);
            timeStamp = Timer.getFPGATimestamp();
        }
    }
    public void driveBackwards() {
        Robot.drivetrain.setVelocity(1, 1);
        if(Timer.getFPGATimestamp()-timeStamp > 2) {
            setState(States.DONE);
            timeStamp = Timer.getFPGATimestamp();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if(getState()==States.DONE) {
            return true;
        } else {
            return false;
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }


}
