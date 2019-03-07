/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.CommandLogger;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.GeminiDrivetrain;



public class LineFollow extends Command {
    CommandLogger logger = new CommandLogger(getClass().getSimpleName());
    double turnP = .45;
    double turningVelocity = 1;//4
    double straightVelocity = 4;//1
    double turnI = 0.001;
    double turnD = .45;
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

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        logger.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        // read & weight the sensors
        final double error = Robot.core.lineSensor();
        if(error==Double.NaN || prevError==Double.NaN)
        {
          Robot.drivetrain.setVelocity(
            (Robot.oi.getLeftPower()*Constants.velocityMultiplier),
            (Robot.oi.getRightPower()*Constants.velocityMultiplier));
            prevError = 0;
            errorAcc = 0;
        } else {
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

        final double turn = (error * turnP) + (errorAcc * turnI)-(prevError-error)*turnD;

        final double velocity = (Math.abs(error) <= 1) ? straightVelocity : turningVelocity;

        logger.set("error", error);
        logger.set("turn", turn);
        logger.set("velocity", velocity);
        logger.write();
        // System.out.println("line follow error = "+error+"/ turn = "+turn+"/ velocity ="+velocity);
        // drive
        Robot.drivetrain.setVelocity(velocity + turn, velocity - turn);
        prevError = error;
    }
    };


    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        logger.drain();
        logger.flush();
    }
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
