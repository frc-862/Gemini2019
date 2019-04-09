package frc.robot.commands.driveTrain;

import frc.lightning.commands.StatefulCommand;
import frc.robot.Robot;

public class TrickyHAB2 extends StatefulCommand {
    enum State {
      DRIVE_BACK1,
      LOWER_LIFT1,
      DRIVE_BACK2,
      RAISE_LIFT1,
      DRIVE_BACK3,
      LOWER_LIFT2,
      DRIVE_BACK4,
      RAISE_LIFT2,
      DRIVE_BACK5,
      DONE
    }

    TrickyHAB2() {
      super(State.DRIVE_BACK1);
      requires(Robot.drivetrain);
      requires(Robot.climber);
    }

    public void driveBack1() {
        Robot.drivetrain.setVelocity(-5, -5);
        if (timeInState() > 1.0) {
            setState(State.LOWER_LIFT1);
        }
    }

    public void lowerLift1() {
        Robot.climber.extendJackHabTwo();
        if (timeInState() > 1.0) {
            setState(State.DRIVE_BACK2);
        }
    }

    public void driveBack2() {
        Robot.climber.setClimberDrivePower(-1);
        if (timeInState() > 1) {
            setState(State.RAISE_LIFT1);
        }
    }

    public void raiseLift1() {
        Robot.climber.retractJack();
        if (timeInState() > 0.5) {
            setState(State.DRIVE_BACK3);
        }
    }

    public void driveBack3() {
        if (timeInState() > 1) {
            setState(State.LOWER_LIFT2);
        }
    }

    public void lowerLift2() {
        Robot.climber.extendJackHabTwo();
        if (timeInState() > 1) {
            setState(State.DRIVE_BACK4);
        }
    }

    public void driveBack4() {
        if (timeInState() > 1) {
            setState(State.RAISE_LIFT2);
        }
    }

    public void raiseLift2() {
        Robot.climber.retractJack();
        if (timeInState() > 0.5) {
            setState(State.DRIVE_BACK5);
        }
    }

    public void driveBack5() {
        if (timeInState() > 0.5) {
            setState(State.DONE);
        }
    }

    public boolean isFinished() {
        return getState() == State.DONE;
    }

    @Override
    public void end() {
        Robot.climber.retractJack();
        Robot.climber.setClimberDrivePower(0);
        Robot.drivetrain.stop();
    }
}
