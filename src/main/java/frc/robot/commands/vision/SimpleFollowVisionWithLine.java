package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;

public class SimpleFollowVision extends Command {
    double kpp = .188;
    double kP = Constants.velocityMultiplier * kpp;
    double kD = .0;
    double minTurnPower = 1;
    double onTargetEpsilon = 0.1;  // scaled 0..1

    public SimpleFollowVision() {
        requires(Robot.drivetrain);
        SmartDashboard.putNumber("VFollow Power", kpp);
        SmartDashboard.putNumber("Vfollow D", kD);
        SmartDashboard.putNumber("dead zone vison", onTargetEpsilon);
    }

    @Override
    protected void execute() {
        kpp = SmartDashboard.getNumber("VFollow Power", kpp);
        kP = Constants.velocityMultiplier * kpp;
        kD = SmartDashboard.getNumber("Vfollow D", kD);
        onTargetEpsilon = SmartDashboard.getNumber("dead zone vison", onTargetEpsilon);
        double velocity = (Robot.oi.getLeftPower() + Robot.oi.getRightPower()) / 2.0 *
                          Constants.velocityMultiplier;
        double gain = 0;

        // TODO add a D term, scale correctly for time! (should that be in SimpleVision?)
        if (Robot.simpleVision.simpleTargetFound()) {
            double error = Robot.simpleVision.getError();

            if (Math.abs(error) > onTargetEpsilon) {
                gain = Robot.simpleVision.getError() * kP;

                if (Math.abs(gain) < minTurnPower) {
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
