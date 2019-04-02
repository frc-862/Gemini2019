/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.commands.StatefulCommand;
import frc.lightning.logging.CommandLogger;
import frc.robot.Constants;
import frc.robot.Robot;

public class DriverAssist extends StatefulCommand {
  // private CommandLogger logger = new CommandLogger("DriverAssist");

  enum States {
    VISION_AQUIRE,
    VISION_DRIVE,
    VISION_CLOSING,
    LINE_FOLLOW,
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
  double turnI = 0.001;
  double turnD = .5;
  double prevError = 0;
  double errorAcc = 0;
  private double turn;
  private double velocity;

  public DriverAssist() {
    super(States.VISION_AQUIRE);
    requires(Robot.drivetrain);
  }

  @Override
  protected void initialize() {
    setState(States.VISION_AQUIRE);
  }
  
  public void visionAquire() {
    if (Robot.core.timeOnLine() > 0.254) {
      setState(States.LINE_FOLLOW);
    } else if (Robot.simpleVision.simpleTargetFound()) {
      setState(States.VISION_DRIVE);
    }
  }

  public void visionDrive() {
    if (Robot.core.timeOnLine() > 0.254) {
      setState(States.LINE_FOLLOW);
    } else if (Robot.simpleVision.getObjectCount() == 1) {
      setState(States.VISION_CLOSING);
    } else if (Robot.simpleVision.getObjectCount() == 2) {
      kP = Constants.velocityMultiplier * kpp;
      double velocity = 4*
                        Constants.velocityMultiplier;
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

    private void updateCalculations() {
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

      

      prevError = error;
    }

    public void lineFollow(){
      updateCalculations();
      Robot.drivetrain.setVelocity(velocity + turn, velocity - turn);

        //if (!Robot.core.isMoving() && Math.abs(prevError)> 1.5) {
        //    if (Math.abs(prevError)> 1.5) {
        //        state = State.followBackward;
        //        backupError = prevError;
        //    } else {
        //        Robot.drivetrain.setVelocity(0, 0);
        //    }
        //  }
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
