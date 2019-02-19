package frc.robot.commands.driveTrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.logging.CommandLogger;
import frc.robot.Robot;
import frc.robot.paths.Path;

public class VelocityMotionProfile extends Command {
    CommandLogger logger = new CommandLogger(getClass().getCanonicalName());

    protected double[][] leftPath;
    protected double[][] rightPath;
    private int index = 0;

    private final int POS = 0;
    private final int VEL = 1;
    private final int ACC = 2;
    private final int HDG = 3;

    public VelocityMotionProfile(Path path) {
        requires(Robot.drivetrain);
        
        leftPath = path.getLeftPath();
        rightPath = path.getRightPath();//in P V A H
                                        //   0 1 2 3

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

    final double kP = 4.65;
    final double kA = 0.0;
    final double kTheta = 0.0;

    private double calcBaseVelocity(String tag, double[] point, double dist) {
        double error = point[POS] - dist;
        double velocity = point[VEL];
        double acceleration = point[ACC];

        logger.set("expected" + tag, point[POS]);
        logger.set("actual" + tag, dist);
        logger.set("expected" + tag + "Velocity", velocity);


        /*
        Return target Velocity + scaled error + scaled acceleration
        this should be a good velocity to get to where we are going in future
        */
        return velocity +   (error * kP)                  + (acceleration * kA); 
        //     targetVEL    positionalErrorCompensation   accelerationLookAhead
    }

    /**
     * The execute method is called repeatedly when this Command is
     * scheduled to run until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        if ((index < leftPath.length) && (index < rightPath.length)) {
            final double leftVel = calcBaseVelocity("Left", leftPath[index], Robot.drivetrain.getLeftDistance());
            final double rightVel = calcBaseVelocity("Right", rightPath[index], Robot.drivetrain.getRightDistance());

            logger.set("actualLeftVelocity", Robot.drivetrain.getLeftVelocity());
            logger.set("actualRightVelocity", Robot.drivetrain.getRightVelocity());

            final double thetaError = leftPath[index][HDG] - Robot.core.getHeading();
            final double turn = thetaError * kTheta;

            logger.set("expectedTheta", leftPath[index][HDG]);
            logger.set("expectedTheta", Robot.core.getHeading());

            Robot.drivetrain.setVelocity(leftVel - turn, rightVel + turn);

            logger.writeValues();

            index++;
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
