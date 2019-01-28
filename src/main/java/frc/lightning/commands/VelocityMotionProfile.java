package frc.lightning.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.logging.CommandLogger;
import frc.robot.Robot;

public class VelocityMotionProfile extends Command {
    CommandLogger logger = new CommandLogger(getClass().getCanonicalName());

    protected double[][] leftPath;
    protected double[][] rightPath;
    private int index = 0;

    public VelocityMotionProfile(double[][] left, double[][] right) {
        requires(Robot.drivetrain);
        leftPath = left;
        rightPath = right;

        logger.addDataElement("expectedLeft");
        logger.addDataElement("expectedRight");
        logger.addDataElement("actualLeft");
        logger.addDataElement("actualRight");

        logger.addDataElement("expectedLeftVelocity");
        logger.addDataElement("expectedRightVelocity");
        logger.addDataElement("actualLeftVelocity");
        logger.addDataElement("actualRightVelocity");

        logger.addDataElement("expectedTheta");
        logger.addDataElement("actualTheta");
    }

    /**
     * The initialize method is called just before the first time
     * this Command is run after being started.
     */
    @Override
    protected void initialize() {
        index = 0;
        Robot.drivetrain.resetDistance();
    }

    final double kP = 0.0;
    final double kA = 0.0;
    final double kTheta = 0.0;

    private double calcBaseVelocity(String tag, double[] point, double dist) {
        double error = point[0] - dist;
        double velocity = point[1];
        double acceleration = point[2];

        logger.set("expected" + tag, point[0]);
        logger.set("actual" + tag, dist);
        logger.set("expected" + tag + "Velocity", velocity);

        return velocity + error * kP + acceleration * kA;
    }

    /**
     * The execute method is called repeatedly when this Command is
     * scheduled to run until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        if (index < leftPath.length && index < rightPath.length) {
            final double leftVel = calcBaseVelocity("Left", leftPath[index], Robot.drivetrain.getLeftDistance());
            final double rightVel = calcBaseVelocity("Right", rightPath[index], Robot.drivetrain.getRightDistance());

            logger.set("actualLeftVelocity", Robot.drivetrain.getLeftVelocity());
            logger.set("actualRightVelocity", Robot.drivetrain.getRightVelocity());

            final double thetaError = leftPath[index][3] - Robot.core.getHeading();
            final double turn = thetaError * kTheta;
            logger.set("expectedTheta", leftPath[index][3]);
            logger.set("expectedTheta", Robot.core.getHeading());

            Robot.drivetrain.setVelocity(leftVel - turn, rightVel + turn);

            logger.writeValues();
            index += 1;
        }
    }


    /**
     */
    @Override
    protected boolean isFinished() {
        return index >= leftPath.length;
    }


    /**
     */
    @Override
    protected void end() {
        logger.drain();
        logger.flush();
        Robot.drivetrain.stop();
    }
}
