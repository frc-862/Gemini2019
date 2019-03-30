package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;

public class SimpleFollowVision extends Command {
    private double gain;

    enum Mode { aligning, closing };
    private Mode mode;

    final double kP = Constants.velocityMultiplier * 0.1;
    final double kD = 0;
    final double minTurnPower = 1;
    final double onTargetEpsilon = 0.1;  // scaled 0..1

    public SimpleFollowVision() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        mode = Mode.aligning;
    }

    @Override
    protected void execute() {
        switch (mode) {
            case aligning:
                aligning();
                break;

            case closing:
                closing();
                break;
        }
    }

    protected  void closing() {
        if (Robot.simpleVision.getObjectCount() == 2) {
            mode = Mode.aligning;
            aligning();
        } else if (Robot.simpleVision.getObjectCount() == 1) {
            double velocity = (Robot.oi.getLeftPower() + Robot.oi.getRightPower()) / 2.0 *
                    Constants.velocityMultiplier;
            Robot.drivetrain.setVelocity(velocity - gain, velocity + gain);
        }
    }

    protected void aligning() {
        if (Robot.simpleVision.getObjectCount() == 1) {
            mode = Mode.closing;
        } else {
            double velocity = (Robot.oi.getLeftPower() + Robot.oi.getRightPower()) / 2.0 *
                    Constants.velocityMultiplier;
            gain = 0;

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
