package frc.lightning.commands;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.logging.CommandLogger;
import frc.lightning.util.InterpolatingDouble;
import frc.lightning.util.InterpolatingMotionPoint;
import frc.lightning.util.InterpolatingTreeMap;
import frc.robot.Robot;
import frc.robot.paths.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class VelocityMotionProfile extends Command {
    CommandLogger logger = new CommandLogger(getClass().getSimpleName());

    InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> leftPath;
    InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> rightPath;
    double finishedAt = 0;

    private static double kP = 0.0;
    private static double kA = 0.0;
    private static double kTheta = 0.0;

    static public void setKp(double _kP) {
        kP = _kP;
    }

    static public void setKa(double _ka) {
        kA = _ka;
    }

    static public void setKTheta(double _kTheta) {
        kTheta = _kTheta;
    }

    private void configProfile(InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> left,
                               InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> right) {
        System.out.println("Config velo profile");
        requires(Robot.drivetrain);
        leftPath = left;
        rightPath = right;

        finishedAt = leftPath.lastKey().value;

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

    private static InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> emptyPath = new InterpolatingTreeMap<>();

    public VelocityMotionProfile(String fname) {
        File deploy = new File(Filesystem.getDeployDirectory(), "paths");
        File left = new File(deploy, fname + "_left.csv");
        File right = new File(deploy, fname +"_right.csv");

        System.out.println("VMP from " + left);
        System.out.println("VMP from " + right);
        if (left.canRead() && right.canRead()) {
            logger = new CommandLogger("VMP-" + fname);
            configProfile(
                readCSVPoints(left),
                readCSVPoints(right)
            );
            System.out.println("VMP " + fname + " built.");
        } else {
            left = new File(fname +"_left.csv");
            right = new File(fname +"_right.csv");

            if (left.canRead() && right.canRead()) {
                logger = new CommandLogger("VMP-" + fname);
                configProfile(
                    readCSVPoints(left),
                    readCSVPoints(right)
                );
            } else {
                System.err.println("Unable to load path: " + fname);
                configProfile(emptyPath, emptyPath);
            }
        }
    }

    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> readCSVPoints(File fname) {
        InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> result = new InterpolatingTreeMap<>();

        try {
            Scanner sc = new Scanner(fname);
            double time = 0;
            try {

                while (sc.hasNextLine()) {
                    Scanner csv = new Scanner(sc.nextLine());
                    csv.useDelimiter(",");
                    double position = csv.nextDouble();
                    double velocity = csv.nextDouble();
                    double acceleration = csv.nextDouble();
                    double heading = csv.nextDouble();

                    result.put(new InterpolatingDouble(time),
                            new InterpolatingMotionPoint(position, velocity, acceleration, heading));
                    time += 0.2;
                }

            } finally {
                sc.close();
            }

        } catch (FileNotFoundException e) {
            System.err.println("Unable to read csv file " + fname);
        }

        return result;
    }

    public VelocityMotionProfile(InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> left,
                                 InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> right) {
        configProfile(left, right);
    }

    /**
     * The initialize method is called just before the first time
     * this Command is run after being started.
     */
    @Override
    protected void initialize() {
        logger.reset();
        Robot.drivetrain.resetDistance();
    }

    private double calcBaseVelocity(String tag, InterpolatingMotionPoint point, double dist) {
        double error = point.position - dist;
        double velocity = point.velocity;
        double acceleration = point.acceleration;

        logger.set("expected" + tag, point.position);
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
        InterpolatingDouble index = new InterpolatingDouble(timeSinceInitialized());

        InterpolatingMotionPoint leftPoint = leftPath.getInterpolated(index);
        final double leftVel = calcBaseVelocity("Left", leftPoint, Robot.drivetrain.getLeftDistance());
        final double rightVel = calcBaseVelocity("Right", rightPath.getInterpolated(index), Robot.drivetrain.getRightDistance());

        logger.set("actualLeftVelocity", Robot.drivetrain.getLeftVelocity());
        logger.set("actualRightVelocity", Robot.drivetrain.getRightVelocity());

        final double thetaError = leftPoint.heading - Robot.core.getHeading();
        final double turn = thetaError * kTheta;

        logger.set("expectedTheta", leftPoint.heading);
        logger.set("actualTheta", Robot.core.getHeading());

        Robot.drivetrain.setVelocity(leftVel - turn, rightVel + turn);

        logger.write();
    }

    /**
     */
    @Override
    protected boolean isFinished() {
        return timeSinceInitialized() > finishedAt;
    }

    /**
     */
    @Override
    protected void end() {
        logger.drain();
        logger.flush();
        Robot.drivetrain.setVelocity(0, 0);
    }
}
