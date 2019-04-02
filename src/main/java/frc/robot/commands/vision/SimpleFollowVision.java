package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;

public class SimpleFollowVision extends Command {
    enum Mode { aligning, closing };
    private Mode mode;

    private double gain;
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
    protected void initialize() {
        mode = Mode.aligning;
    }

    @Override
    protected void execute() {
        onTargetEpsilon = SmartDashboard.getNumber("dead zone vison", onTargetEpsilon);
        kpp = SmartDashboard.getNumber("VFollow Power", kpp);
        kD = SmartDashboard.getNumber("Vfollow D", kD);

        switch (mode) {
        case aligning:
            aligning();
            break;
        case closing:
            closing();
            break;
        }
    }

    protected void closing() {
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
        } else if (Robot.simpleVision.getObjectCount() == 2) {
            kP = Constants.velocityMultiplier * kpp;
            double velocity = (Robot.oi.getLeftPower() + Robot.oi.getRightPower()) / 2.0 *
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

    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
