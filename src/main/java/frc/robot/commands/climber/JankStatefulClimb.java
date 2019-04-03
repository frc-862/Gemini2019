/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import java.sql.Time;

import edu.wpi.first.wpilibj.Timer;
import frc.lightning.commands.StatefulCommand;
import frc.lightning.util.LightningMath;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.LEDs;

public class JankStatefulClimb extends StatefulCommand {
    enum States {
        START_CLIMB,
        WAIT_TO_DEPLOY_SKIDS,
        DEPLOY_SKIDS,
        WAIT_TO_DRIVE_FORWARD,
        DRIVE_FORWARD,
        BACKUP_A_BIT,
        RAISE_JACK,
        SLEEP,
        DONE
    }

    double startedDrivingAt;

    public JankStatefulClimb() {
        super(States.START_CLIMB);
        requires(Robot.climber);
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        setState(States.START_CLIMB);
        Robot.leds.setState(LEDs.State.CLIMBING);
    }

    @Override
    protected boolean isFinished() {
        return this.getState() == States.DONE;
    }

    public void startClimb() {
        Robot.climber.extendJack();
        Robot.climber.extendSkids();
        setState(States.WAIT_TO_DRIVE_FORWARD);
    }

    public void waitToDeploySkids() {
        if (Robot.climber.getJackPosition() >= Constants.deployShockPositioin) {
            setState(States.DEPLOY_SKIDS);
        }
    }

    public void deploySkids() {
        Robot.climber.extendSkids();
        setState(States.WAIT_TO_DRIVE_FORWARD);
    }

    private double startedToClimb;
    public void waitToDriveForwardEnter() {
        startedToClimb = Timer.getFPGATimestamp();
    }

    private double driveForwardTime = 4.5;

    public void waitToDriveForward() {
        if (LightningMath.epsilonEqual(Robot.climber.getJackPosition(),
                                       Constants.climberExtenedPosition, Constants.climberEpsilon)) {
            setState(States.DRIVE_FORWARD);
            driveForwardTime = 4.5;
        } else if (Timer.getFPGATimestamp() - startedToClimb > 5.0) {
            setState(States.DRIVE_FORWARD);
            driveForwardTime = 6;
        }
    }

    public void driveForwardEnter() {
        startedDrivingAt = Timer.getFPGATimestamp();
    }

    public void driveForward() {
        Robot.climber.setClimberDrivePower(1);
        Robot.drivetrain.setPower(0.4, 0.4);
        if (Timer.getFPGATimestamp() - startedDrivingAt > driveForwardTime) {
            startedDrivingAt = Timer.getFPGATimestamp();
            Robot.climber.setClimberDrivePower(0);
            Robot.drivetrain.stop();
            setState(States.BACKUP_A_BIT);
        }
    }

    public void backupABit() {
        Robot.climber.setClimberDrivePower(-1);
        if (Timer.getFPGATimestamp() - startedDrivingAt > .7) {
            Robot.climber.setClimberDrivePower(0);
            Robot.drivetrain.stop();
            setState(States.RAISE_JACK);
        }
    }
    public void raiseJack() {
        Robot.climber.upABit(8000);
        Robot.climber.retractSkids();
        setState(States.SLEEP);
    }

    @Override
    protected void end() {
        Robot.climber.setClimberDrivePower(0);
        Robot.climber.setLiftPower(0);
        Robot.drivetrain.stop();

        // Robot.leds.clearState(LEDs.State.CLIMBING);
    }
}
