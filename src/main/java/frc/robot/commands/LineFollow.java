/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.commands.LoggedCommand;
import frc.robot.Constants;
import frc.robot.Robot;

public class LineFollow extends LoggedCommand {
    double turnP = .45;
    double turningVelocity = 4;//might need to go to 5-6 because sometimes does not line up
    double straightVelocity = 1;
    double turnI = 0.001;
    double turnD = .4;
    double prevError = 0;
    double errorAcc = 0;

    public LineFollow() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        logger.addDataElement("error");
        logger.addDataElement("turn");
        logger.addDataElement("velocity");

        SmartDashboard.putNumber("Turn Power", turnP);
        SmartDashboard.putNumber("Straight Vel", straightVelocity);
        SmartDashboard.putNumber("Turning Vel", turningVelocity);
        SmartDashboard.putNumber("turnI", turnI);
        SmartDashboard.putNumber("turnD", turnD);
    }

    // Basic idea, no line return false,
    // If we have see a line for at least
    // a calibrated amount of time (250ms?)
    // return true
    private double timeLineSpotted = 0;
    private boolean lineLastCycle = false;
    private final double lineDetectHysteresis = 0.250;

    public boolean linePresent() {
        final double error = Robot.core.lineSensor();

        if (Double.isNaN(error)) {
            lineLastCycle = false;
        } else if (!lineLastCycle) {
            lineLastCycle = true;
            timeLineSpotted = Timer.getFPGATimestamp();
        } else {
            return (Timer.getFPGATimestamp() - timeLineSpotted) > lineDetectHysteresis;
        }

        return false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (linePresent()) {
            Robot.drivetrain.setVelocity(Robot.oi.getLeftPower() * Constants.velocityMultiplier,
                    Robot.oi.getRightPower()* Constants.velocityMultiplier);
        } else {
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

            final double turn = (error * turnP) + (errorAcc * turnI) - (prevError - error) * turnD;
            final double velocity = (Math.abs(error) < 1) ? straightVelocity : turningVelocity;

            logger.set("error", error);
            logger.set("turn", turn);
            logger.set("velocity", velocity);

            Robot.drivetrain.setVelocity(velocity + turn, velocity - turn);
            prevError = error;
        }
        super.execute();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }
}
