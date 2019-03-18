package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.LineFollow;

public class SimpleFollowVision extends Command {

    public SimpleFollowVision() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void execute() {
        // TODO add a D term, scale correctly for time! (should that be in SimpleVision?)
        if (Robot.vision.simpleTargetFound()) {
            double velocity = (Robot.oi.getLeftPower() + Robot.oi.getRightPower()) / 2.0 * Constants.velocityMultiplier;
            double kP = (Constants.velocityMultiplier * 0.8) / (320.0 / 2.0);  // 320 width of camera view
            double gain = Robot.vision.getError() * kP;

            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        } else {
            Robot.drivetrain.stop();
        }

        if (Robot.core.timeOnLine() > 0.1) {
            (new LineFollow()).start();
        }
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
