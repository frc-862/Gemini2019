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

import java.rmi.server.RemoteObject;

public class DriverAssist extends StatefulCommand {
    private CommandLogger logger = new CommandLogger("DriverAssist");

    enum States {
        VISION_AQUIRE,
        VISION_DRIVE,
        VISION_SEEKING,
        VISION_CLOSING,
        LINE_FOLLOW,
        FOLLOW_BACKWARDS,
        BAD_DUMB_BACKUP,
        FIND_THE_LINE,
        DONE
    }

    boolean offLine=false;
    boolean beenOff =false;
    private double gain;
    double kpp = .254;
    double kppPivot = .118;
    double kP = Constants.velocityMultiplier * kpp;
    double kD = .0;
    double minTurnPower = 1;
    double onTargetEpsilon = 0.1;  // scaled 0..1
    double turnP = 2.;
    double turningVelocity = 1.25;///4
    double straightVelocity = 1.25;//1
    double turnI = 0.001/.02;
    double turnD = 0;
    double prevError = 0;
    double errorAcc = 0;
    boolean seenTwo = false;
    final double minStateStallDetect = 0.04;
    final double lineFollowMinError = 1.5;
    final double backupSpeed = -1.5;
    final double lineLength = 1.5; // 18 in = 1.5 feet
    final double backupTime = Math.abs(lineLength / backupSpeed);

    private double turn;
    private double velocity = 1;
    double stError;
    double timeStamp;

    public DriverAssist() {
        super(States.VISION_AQUIRE);
        requires(Robot.drivetrain);

        logger.addDataElement("gain");
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
        Robot.core.ringOn();
        setState(States.VISION_AQUIRE);
        Robot.hatchPanelCollector.eject();
        Robot.hatchPanelCollector.retract();
        Robot.led.setState(LEDs.State.DRIVER_ASSIST);
        logger.reset();
    }

    private double visionError() {
        if (Robot.simpleVision == null) {
            return 0;
        } else {
            return Robot.simpleVision.getError();
        }
    }

    private double visionObjects() {
        if (Robot.simpleVision == null) {
            return 0;
        } else {
            return Robot.simpleVision.getObjectCount();
        }
    }
    private boolean visionTargetFound() {
        if (Robot.simpleVision == null) {
            return false;
        } else {
            return Robot.simpleVision.simpleTargetFound();
        }
    }

    private void updateLogs() {
        logger.set("lineError", Robot.core.lineSensor());
        logger.set("visionError", visionError());
        logger.set("timeOnLine", Robot.core.timeOnLine());
        logger.set("mode", this.getState().ordinal());
        logger.set("visionCount", visionObjects());
        logger.write();
    }

    private final double lineWait = 0.1;

    public void visionAquire() {

        seenTwo = false;
        if (Robot.core.timeOnLine() > lineWait) {
            setState(States.LINE_FOLLOW);
        } else if (visionTargetFound()) {
            if (visionObjects() == 1) {
                setState(States.VISION_SEEKING);
            } else {
                setState(States.VISION_DRIVE);
            }
        }else{
            setState(States.FIND_THE_LINE);
        }

        logger.set("gain", 0);
        logger.set("gainP", 0);
        logger.set("gainI", 0);
        logger.set("gainD", 0);
        updateLogs();
    }

    public void visionDrive() {
        if (Robot.core.timeOnLine() > lineWait) {
            setState(States.LINE_FOLLOW);

          // assumption, been following two target for a while
          // now lost one to the belt, don't want to turn hard
          // centering on the remaining (now much closer) target,
          // so Vision Closing just uses last error
        } else if (visionObjects() == 1) {
            setState(States.VISION_CLOSING);
        } else {
            kP = Constants.velocityMultiplier * kpp;
            velocity = 2;
            visionUpdate();

            SmartDashboard.putNumber("Simple Vision Gain ", gain);
            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        }

        updateLogs();
    }

        //assumption: far away and only see one target
        //action: navigate towards single target
    public void visionSeeking() {
        if (Robot.core.timeOnLine() > lineWait) {
            setState(States.LINE_FOLLOW);
            return;

          // assume that if we now see more than 1 target, Luke
          // (or vision) has us pointed where we need to be
          // and the center most 2, are the ones we want
        } else if (visionObjects() >= 2) {
            setState(States.VISION_DRIVE);
            return;
        }

        // e.g. kp = 0.188, velocityMultiplier = 11.7 => largest gain ~ 2.2
        // worst case error, ~-.1, ~4 turn
        kP = Constants.velocityMultiplier * kppPivot;
        velocity = 2;
        visionUpdate();
        Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        updateLogs();
    }

    private void visionUpdate() {
        gain = 0;
        double error = visionError();
        double gainP = 0;
        double gainD = 0;

        if (Math.abs(error) > onTargetEpsilon) {
            gainP = error * kP;
            gainD = visionErrorD() * kD;
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

    public double visionErrorD(){
        if(Robot.simpleVision == null) return 0;
        return Robot.simpleVision.getErrorD();
    }

    //assumption, had good vision, but one target went bad (belt blocking target)
    // continue based on last valid target received
    // primarily that we are very close and if we adjust on one target it will
    // cause us to over correct
    public void visionClosing() {
        if (Robot.core.timeOnLine() > lineWait) {
            setState(States.LINE_FOLLOW);
        } else if (visionObjects() >= 2) {
            setState(States.VISION_DRIVE);
        } else {

            // assume that the previous gain is probably the best gain until
            // we either see the line, or get both vision targets back
            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        }

//        logger.set("gainP", 0);
//        logger.set("gainI", 0);
//        logger.set("gainD", 0);
        logger.set("gain", gain);
        updateLogs();
    }

    double lastTimeStamp = Timer.getFPGATimestamp();

    double gainP=0;
    double gainI=0;
    double gainD=0;

    private void updateCalculations() {
        double elapsedTime =Timer.getFPGATimestamp() - lastTimeStamp;
        lastTimeStamp = Timer.getFPGATimestamp();
        final double error = Robot.core.lineSensor(); //in off cntr

        turnP = SmartDashboard.getNumber("Turn Power", turnP);
        straightVelocity = SmartDashboard.getNumber("Straight Vel", straightVelocity);
        turningVelocity = SmartDashboard.getNumber("Turning Vel", turningVelocity);
        turnI = SmartDashboard.getNumber("turnI", turnI);
        turnD = SmartDashboard.getNumber("turnD", turnD);

        if (Double.isNaN(error)) {
            if(Math.abs(error)<= 1){
            errorAcc = 0;
            }
        } else {
            errorAcc += error * elapsedTime;
            gainP = error * turnP;
            gainI = errorAcc * turnI;
            gainD = ((prevError - error) / elapsedTime) * turnD;
            turn = gainP + gainI + gainD;

            if (Math.abs(error) > 0.99) {
                if (Math.abs(turn) < .75) {
                    turn = Math.signum(turn) * .75;
                } else if (turn > 4) {
                    turn = Math.signum(turn) * 4;
                }
            }
        }




        velocity = (Math.abs(error) <= 1) ? straightVelocity : turningVelocity;
        prevError = error;

        logger.set("gainP", gainP);
        logger.set("gainI", gainI);
        logger.set("gainD", gainD);
        logger.set("gain", turn);
    }

    public void lineFollow() {
        updateCalculations();
    velocity=1.5;
        // check, did we lose the line? 
        // if so restart the process, keeps us
        // from stopping and gives both
        // vision and line follow a chance to 
        // restart
        if (Double.isNaN(prevError)) {
            setState(States.VISION_AQUIRE);
            return;
        }

        // only add power to one side, don't subtract from the other
        // this keeps enough power to turn the robot when one side is
        // pinned against the rocket/loader/cargo wall
        if (turn > 0) {
            Robot.drivetrain.setVelocity(velocity + (turn * 1.50), velocity);
        } else {
            // the -2 is because in this case turn is already less than zero
            // (see condition above), and we actually need to add the power to
            // this side, in most of our other control loops we add to one side
            // and subtract from the other, which would make this subtracting a
            // negative, which is the same as adding...
            Robot.drivetrain.setVelocity(velocity, velocity + (turn * -1.50));
        }

        // have we been in line follow at least for a tiny bit and we are
        // stalled...
        if ((timeInState() > minStateStallDetect) && Robot.drivetrain.isStalled()) {

            if (Math.abs(prevError) > lineFollowMinError) {
                // Should be against the wall, but not close enough
                // to deploy/collect a hatch
                stError=prevError;
                timeStamp=Timer.getFPGATimestamp();
                setState(States.FOLLOW_BACKWARDS);

            } else {
                // We are well aligned, and should be safe to 
                // deploy/collect a hatch
                //Robot.leds.setState(LEDs.State.READY);
                Robot.drivetrain.setVelocity(0, 0);
                setState(States.DONE);

            }
        }
        updateLogs();
    }

    public void followBackwards() {
        velocity = backupSpeed;

        Robot.drivetrain.setVelocity(velocity, velocity);

        // went too far and lost the line
        if(Double.isNaN(Robot.core.lineSensor())) {
            setState(States.FIND_THE_LINE);

          // we have backed up long enough, let's
          // go forward again
        } else if (timeInState() > backupTime) {
            setState(States.LINE_FOLLOW);
        }

        updateLogs();
    }

    public void findTheLine() {
        Robot.drivetrain.setVelocity(2, 2);

        // as soon as we see a line, we should follow it
        if (Robot.core.timeOnLine() > lineWait) {
            setState(States.LINE_FOLLOW);

          // no line, but hey vision can see where we are going
        } else if (visionTargetFound()) {
            if (visionObjects() == 1) {
                setState(States.VISION_SEEKING);
            } else {
                setState(States.VISION_DRIVE);
            }
        }

    }

    public void done() {
            Robot.core.ringOff();
            Robot.led.setState(LEDs.State.CENTERED);
            Robot.oi.rummbleOn();
    }

    // Make this return true when this Command no longer needs to run execute()

    @Override
    protected void interrupted(){
        end();
    }

    @Override
    protected boolean isFinished() {
        // exit driver assist, if Luke (or anyone) uses the joysticks beyond half power
//        return getState() == States.DONE  ||
//            ((Math.abs(Robot.oi.getLeftPower()) > 0.5) || (Math.abs(Robot.oi.getRightPower()) > 0.5));
        return false;
    }

    @Override
    protected void end() {

        Robot.core.ringOff();
        Robot.oi.rummbleOff();
        Robot.led.clearState(LEDs.State.DRIVER_ASSIST);
        Robot.led.clearState(LEDs.State.CENTERED);
        Robot.drivetrain.stop();
        logger.drain();
        logger.flush();
    }
}

