/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.commands.StatefulCommand;
import frc.lightning.logging.CommandLogger;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.LEDs;

public class DriverAssist extends StatefulCommand {
    private CommandLogger logger = new CommandLogger("DriverAssist");

    enum States {
        VISION_AQUIRE,
        VISION_DRIVE,
        VISION_CLOSING,
        LINE_FOLLOW,
        FOLLOW_BACKWARDS,
        BAD_DUMB_BACKUP,
        DONE
    }
    boolean offLine=false;
    boolean beenOff =false;
    private double gain;
    double kpp = .188;
    double kppPivot = .188;
    double kP = Constants.velocityMultiplier * kpp;
    double kD = .0;
    double minTurnPower = 1;
    double onTargetEpsilon = .1;  // scaled 0..1
    double turnP = .4;
    double turningVelocity = 1;//4
    double straightVelocity = 6 ;//1
    double turnI = 0.001/.02;
    double turnD = .5;
    double prevError = 0;
    double errorAcc = 0;
    boolean seenTwo = false;
    private double turn;
    private double velocity = 1;
    double stError;
    double timeStamp;

    public DriverAssist() {
        super(States.VISION_AQUIRE);
        requires(Robot.drivetrain);

        logger.addDataElement("gainP");
        logger.addDataElement("gainI");
        logger.addDataElement("gainD");
        logger.addDataElement("lineError");
        logger.addDataElement("visionError");
        logger.addDataElement("timeOnLine");
        logger.addDataElement("mode");
        logger.addDataElement("visionCount");
    }

    @Override
    protected void initialize() {
        setState(States.VISION_AQUIRE);
    }

    private void updateLogs() {
        logger.set("lineError", Robot.core.lineSensor());
        logger.set("visionError", Robot.simpleVision.getError());
        logger.set("timeOnLine", Robot.core.timeOnLine());
        logger.set("mode", this.getState().ordinal());
        logger.set("visionCount", Robot.simpleVision.getObjectCount());
        logger.write();
    }

    public void visionAquire() {
        seenTwo = false;
        if (Robot.core.timeOnLine() > 0.254) {
            setState(States.LINE_FOLLOW);
        } else if (Robot.simpleVision.simpleTargetFound()) {
            setState(States.VISION_DRIVE);
        }

        logger.set("gainP", 0);
        logger.set("gainI", 0);
        logger.set("gainD", 0);
        updateLogs();
    }

    public void visionDrive() {
        if (Robot.core.timeOnLine() > .08) {
            setState(States.LINE_FOLLOW);
        } else if (Robot.simpleVision.getObjectCount() == 1 && seenTwo) {
            setState(States.VISION_CLOSING);
        } else if (Robot.simpleVision.getObjectCount() == 1) {
            //kP = Constants.velocityMultiplier * kpp;
            kP = Constants.velocityMultiplier * kppPivot;
            velocity = 1;
            visionUpdate();

            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        } else if (Robot.simpleVision.getObjectCount() == 2) {
            seenTwo = true;
            kP = Constants.velocityMultiplier * kpp;
            velocity = 3;
            visionUpdate();

            SmartDashboard.putNumber("Simple Vision Gain ", gain);
            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        }

        updateLogs();
    }

    private void visionUpdate() {
        gain = 0;
        double error = Robot.simpleVision.getError();
        double gainP = 0;
        double gainD = 0;

        if (Math.abs(error) > onTargetEpsilon) {
            gainP = error * kP;
            gainD = Robot.simpleVision.getErrorD() * kD;
            gain = gainP + gainD;

            if (Math.abs(gain) < minTurnPower) {
                gain = minTurnPower * Math.signum(gain);
            }
        }

        logger.set("gainP", gainP);
        logger.set("gainI", 0);
        logger.set("gainD", gainD);
        logger.set("gain", gain);
    }

    public void visionClosing() {
        if (Robot.core.timeOnLine() > .08) {
            setState(States.LINE_FOLLOW);
        } else if (Robot.simpleVision.getObjectCount() >= 2) {
            setState(States.VISION_DRIVE);
        } else {
            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        }

//        logger.set("gainP", 0);
//        logger.set("gainI", 0);
//        logger.set("gainD", 0);
        logger.set("gain", gain);
        updateLogs();
    }

    double lastTimeStamp = Timer.getFPGATimestamp();

    private void updateCalculations() {
        double elapsedTime =Timer.getFPGATimestamp() - lastTimeStamp;
        lastTimeStamp = Timer.getFPGATimestamp();
        final double error = Robot.core.lineSensor(); //in off cntr

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

        double gainP = error * turnP;
        double gainI = errorAcc * turnI;
        double gainD = ((prevError - error) / elapsedTime) * turnD;
        turn = gainP + gainI + gainD;

        velocity = (Math.abs(error) <= 1) ? straightVelocity : turningVelocity;
        prevError = error;

        logger.set("gainP", gainP);
        logger.set("gainI", gainI);
        logger.set("gainD", gainD);
        logger.set("gain", turn);
    }

    public void lineFollow() {
        updateCalculations();
        Robot.drivetrain.setVelocity(velocity + turn, velocity - turn);
        System.out.println("forwardssss correct plz!!");
        if (timeInState() > .1 && Robot.drivetrain.isStalled()) {

            if (Math.abs(prevError)> 1) {
                stError=prevError;
                timeStamp=Timer.getFPGATimestamp();
                setState(States.FOLLOW_BACKWARDS);

            } else {

                Robot.leds.setState(LEDs.State.READY);
                Robot.drivetrain.setVelocity(0, 0);
                setState(States.DONE);

            }
        }
        updateLogs();
    }

    public void followBackwards() {
        updateCalculations();
        turn *= -.25;
        velocity = -1;

        Robot.drivetrain.setVelocity(velocity + turn, velocity - turn);
        System.out.println("Back it! back it! back it up!");
        if(Double.isNaN(Robot.core.lineSensor())){
            Robot.drivetrain.setVelocity(1,1);
             offLine =true;
             beenOff=true;
        }else {offLine =false;beenOff=false;}
        if (!offLine && beenOff){
            setState(States.LINE_FOLLOW);
        }
        if ((Math.abs(prevError) <= Math.abs(stError/2)||Math.abs(prevError) <= Math.abs(1.5))||timeInState()>=.5) {
            setState(States.LINE_FOLLOW);
        }


        updateLogs();
    }

//    public void badDumbBackup() {
//        Robot.drivetrain.setVelocity(-1, -1);
//        if (timeStamp-Timer.getFPGATimestamp()>=1) {
//            Robot.drivetrain.setVelocity(0, 0);
//            setState(States.LINE_FOLLOW);
//        }
//
//        updateLogs();
//    }

    // Make this return true when this Command no longer needs to run execute()

    @Override
    protected void interrupted(){
        end();
    }
    @Override
    protected boolean isFinished() {
        return getState() == States.DONE  || (Robot.oi.getLeftPower()>.5|| Robot.oi.getRightPower()>.5);
    }

    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }
}

