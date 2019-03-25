/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.CommandLogger;
import frc.robot.Constants;
import frc.robot.Robot;

public class LineFollow extends Command {
    enum State { driving, followForward, followBackward };
    State state = State.driving;
    CommandLogger logger = new CommandLogger(getClass().getSimpleName());
    double turnP = .475;
    double turningVelocity = .5;//4
    double straightVelocity = 4 ;//1
    double turnI = 0.001;
    double turnD = .5;
    double prevError = 0;
    double errorAcc = 0;

    public LineFollow() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        logger.addDataElement("error");
        logger.addDataElement("turn");
        logger.addDataElement("velocity");
        logger.addDataElement("stalled");
        logger.addDataElement("state");
        SmartDashboard.putNumber("Turn Power", turnP);
        SmartDashboard.putNumber("Straight Vel", straightVelocity);
        SmartDashboard.putNumber("Turning Vel", turningVelocity);
        SmartDashboard.putNumber("turnI", turnI);
        SmartDashboard.putNumber("turnD", turnD);
    }

    double timeout = -1;
    public LineFollow(double timeout) {
        this();
        this.timeout = timeout;
    }
    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        state = State.driving;
        logger.reset();
    }

    private void driving() {
        Robot.drivetrain.setVelocity(
            (Robot.oi.getLeftPower()*Constants.velocityMultiplier),
            (Robot.oi.getRightPower()*Constants.velocityMultiplier));
        prevError = 0;
        errorAcc = 0;

        if (Robot.core.timeOnLine() > 0.2) {
            state = State.followForward;
        }
    }

    private double turn;
    private double velocity;
    private void updateCalculations() {
        // read & weight the sensors
        final double error = Robot.core.lineSensor();

        turnP = SmartDashboard.getNumber("Turn Power", turnP);
        straightVelocity = SmartDashboard.getNumber("Straight Vel", straightVelocity);
        turningVelocity = SmartDashboard.getNumber("Turning Vel", turningVelocity);
        turnI = SmartDashboard.getNumber("turnI", turnI);
        turnD = SmartDashboard.getNumber("turnD", turnD);

        if (Double.isNaN(error) || Math.abs(error) <= 1) {
            errorAcc = 0;
        } else {
            errorAcc += error;
        }

        turn = (error * turnP) + (errorAcc * turnI)-(prevError-error)*turnD;
        velocity = (Math.abs(error) <= 1) ? straightVelocity : turningVelocity;

        logger.set("error", error);
        logger.set("turn", turn);
        logger.set("velocity", velocity);
        logger.set("stalled", (Robot.drivetrain.isStalled() ? 10 : 0));
        logger.set("state", state.ordinal());
        logger.write();

        SmartDashboard.putString("LineFollowState", state.name());

        prevError = error;
    }

    private double backupError = 0;
    private void followForward() {
        updateCalculations();
        Robot.drivetrain.setVelocity(velocity + turn, velocity - turn);

        if (Robot.drivetrain.isStalled()) {
            if (Math.abs(prevError)> 1.5) {
                state = State.followBackward;
                backupError = prevError;
            } else {
                Robot.drivetrain.setVelocity(0, 0);
            }
        }
    }

    private void followBackward() {
        updateCalculations();
        Robot.drivetrain.setVelocity(-velocity - (turn*.3), -velocity + (.3*turn));
        //Robot.drivetrain.setVelocity(-velocity, -velocity);
        if (Math.abs(prevError) < 1) {
            state = State.followForward;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        switch (state) {
        case driving:
            driving();
            break;

        case followForward:
            followForward();
            break;

        case followBackward:
            followBackward();
            break;
        }
    };

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (Math.abs(Robot.core.lineSensor()) < 1.5 && Math.abs(Robot.drivetrain.getVelocity()) < 0.25)
            return true;

        return timeout > 0 && timeSinceInitialized() > timeout;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        logger.drain();
        logger.flush();
        Robot.drivetrain.setPower(0,0);
    }
}
