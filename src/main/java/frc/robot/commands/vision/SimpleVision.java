package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.commands.LineFollow;

public class SimpleVision extends Command {

    public SimpleVision() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void execute() {
        if (Robot.vision.simpleTargetFound()) {
            double velocity = 5;
            double kP = velocity / (320.0 / 2.0);  // 320 width of camera view
            double gain = Robot.vision.simpleError() * kP;

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
