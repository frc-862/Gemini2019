/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.commands.StatefulCommand;
import frc.lightning.logging.CommandLogger;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.climber.driveforward;

public class DriverAssist extends StatefulCommand {
    // private CommandLogger logger = new CommandLogger("DriverAssist");

    enum States {
        VISION_AQUIRE,
        VISION_DRIVE,
        VISION_CLOSING,
        LINE_FOLLOW,
        FOLLOW_BACKWARDS,
        BAD_DUMB_BACKUP,
        DONE
    }

    private double gain;
    double kpp = .188;
    double kP = Constants.velocityMultiplier * kpp;
    double kD = .0;
    double minTurnPower = 1;
    double onTargetEpsilon = 0.1;  // scaled 0..1
    double turnP = .475;
//linefollow
    double turningVelocity = .5;//4
    double straightVelocity = 4 ;//1
    double turnI = 0.001/.02;
    double turnD = .5;
    double prevError = 0;
    double errorAcc = 0;
    boolean seenTwo = false;
    private double turn;
    private double velocity;
    double stError;
    double timeStamp;

    public DriverAssist() {
        super(States.VISION_AQUIRE);
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        setState(States.VISION_AQUIRE);
    }

    public void visionAquire() {
        seenTwo = false;
        if (Robot.core.timeOnLine() > 0.254) {
            setState(States.LINE_FOLLOW);
        } else if (Robot.simpleVision.simpleTargetFound()) {
            setState(States.VISION_DRIVE);
        }
    }

    public void visionDrive() {
        if (Robot.core.timeOnLine() > 0.254) {
            setState(States.LINE_FOLLOW);
        } else if (Robot.simpleVision.getObjectCount() == 1 && seenTwo) {
            setState(States.VISION_CLOSING);
        } else if (Robot.simpleVision.getObjectCount() == 1) {
            kP = Constants.velocityMultiplier * kpp;
            double velocity = 0;
            gain = 0;
            double error = Robot.simpleVision.getError();

            if (Math.abs(error) > onTargetEpsilon) {
                gain = error * kP;

                if (Math.abs(gain) < minTurnPower) {
                    gain = minTurnPower * Math.signum(gain);
                }

                gain += Robot.simpleVision.getErrorD() * kD;
            }

            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        } else if (Robot.simpleVision.getObjectCount() == 2) {
            seenTwo = true;
            kP = Constants.velocityMultiplier * kpp;
            double velocity = 3;
            gain = 0;
            double error = Robot.simpleVision.getError();

            if (Math.abs(error) > onTargetEpsilon) {
                gain = error * kP;

                if (Math.abs(gain) < minTurnPower) {
                    gain = minTurnPower * Math.signum(gain);
                }

                gain += Robot.simpleVision.getErrorD() * kD;
            }

            SmartDashboard.putNumber("Simple Vision Gain ", gain);
            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        }
    }

    public void visionClosing() {
        if (Robot.core.timeOnLine() > 0.254) {
            setState(States.LINE_FOLLOW);
        } else if (Robot.simpleVision.getObjectCount() == 2) {
            setState(States.VISION_DRIVE);
        } else {
            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        }
    }

    double lastTimmeStamp = Timer.getFPGATimestamp();

    private void updateCalculations() {
        double elapsedTime =Timer.getFPGATimestamp()-lastTimmeStamp;
        lastTimmeStamp = Timer.getFPGATimestamp();
        final double error = Robot.core.lineSensor();//in off cntr

        turnP = SmartDashboard.getNumber("Turn Power", turnP);
        straightVelocity = SmartDashboard.getNumber("Straight Vel", straightVelocity);
        turningVelocity = SmartDashboard.getNumber("Turning Vel", turningVelocity);
        turnI = SmartDashboard.getNumber("turnI", turnI);
        turnD = SmartDashboard.getNumber("turnD", turnD);

        if (Double.isNaN(error) || Math.abs(error) <= 1) {
            errorAcc = 0;
        } else {
            errorAcc += error * elapsedTime;
        }

        turn = (error * turnP) + (errorAcc * turnI)-(((prevError-error)/elapsedTime)*turnD);
        velocity = (Math.abs(error) <= 1) ? straightVelocity : turningVelocity;



        prevError = error;
    }

    public void lineFollow() {
        updateCalculations();
        Robot.drivetrain.setVelocity(velocity + turn, velocity - turn);

        if (!Robot.core.hasStopped()) {
            if (Math.abs(prevError)> 1.5) {
                stError=prevError;
                timeStamp=Timer.getFPGATimestamp();
                setState(States.FOLLOW_BACKWARDS);
            } else {
                Robot.drivetrain.setVelocity(0, 0);
            }
          }
    }

    public void followBackward() {
        updateCalculations();
        turn *= -.5;
        velocity *= .25;
        Robot.drivetrain.setVelocity(-velocity + turn, -velocity - turn);

        if (Math.abs(prevError) < stError/2) {
            setState(States.LINE_FOLLOW);
        }
    }
    public void badDumbBackup(){
        Robot.drivetrain.setVelocity(-1, -1);
        if (timeStamp-Timer.getFPGATimestamp()>=1){
            Robot.drivetrain.setVelocity(0, 0);
            setState(States.LINE_FOLLOW);
        }
    }
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return getState() == States.DONE;
    }

    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }
}
