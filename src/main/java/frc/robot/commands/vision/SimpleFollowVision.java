package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;

public class SimpleFollowVision extends Command {
    final double kP = Constants.velocityMultiplier * 0.8;
    final double kD = 0;
    final double minTurnPower = 1;
    final double onTargetEpsilon = 0.1;  // scaled 0..1

    public SimpleFollowVision() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void execute() {
        double velocity = (Robot.oi.getLeftPower() + Robot.oi.getRightPower()) / 2.0 *
                          Constants.velocityMultiplier;
        double gain = 0;

        // TODO add a D term, scale correctly for time! (should that be in SimpleVision?)
        if (Robot.simpleVision.simpleTargetFound()) {
            double error = Robot.simpleVision.getError();

            if (Math.abs(error) > onTargetEpsilon) {
                gain = Robot.simpleVision.getError() * kP;

                if (gain < minTurnPower) {
                    gain = minTurnPower * Math.signum(gain);
                }
            }

            gain += Robot.simpleVision.getErrorD() * kD;
        }

        SmartDashboard.putNumber("Simple Vision Gain ", gain);
        SmartDashboard.putNumber("Simple Vision Velocity ", velocity);
        Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);

//        if (Robot.core.timeOnLine() > 0.1) {
//            (new LineFollow()).start();
//        }
    }

    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
