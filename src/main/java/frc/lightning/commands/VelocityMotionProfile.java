package frc.lightning.commands;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.logging.CommandLogger;
import frc.robot.Robot;
import frc.robot.paths.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class VelocityMotionProfile extends Command {
    CommandLogger logger = new CommandLogger(getClass().getSimpleName());

    protected double[][] leftPath;
    protected double[][] rightPath;
    private int index = 0;

    private final static int POS = 0;
    private final static int VEL = 1;
    private final static int ACC = 2;
    private final static int HDG = 3;

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

    private void configProfile(double[][] left, double[][] right) {
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

    private static double[][] emptyPath = new double[2][4];
    public VelocityMotionProfile(String fname) {
        File deploy = new File(Filesystem.getDeployDirectory(), "paths)");
        File left = new File(deploy, fname + "_left.csv");
        File right = new File(deploy, fname +"_right.csv");

        if (left.canRead() && right.canRead()) {
            logger = new CommandLogger("VMP-" + fname);
            configProfile(
                    readCSVPath(left),
                    readCSVPath(right)
            );
        } else {
            left = new File(fname +"_left.csv");
            right = new File(fname +"_right.csv");

            if (left.canRead() && right.canRead()) {
                logger = new CommandLogger("VMP-" + fname);
                configProfile(
                        readCSVPath(left),
                        readCSVPath(right)
                );
            } else {
                System.err.println("Unable to load path: " + fname);
                configProfile(emptyPath, emptyPath);
            }
        }
    }

    private List<String> readFile(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        try {
            List<String> lines = new ArrayList<>();

            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }

            return lines;
        } finally {
            sc.close();
        }
    }

    private double[][] readCSVPath(File fname) {
        try {
            var lines = readFile(fname);
            double[][] result = new double[lines.size()][4];

            for (int index = 0; index < lines.size(); ++index) {
                Scanner scan = new Scanner(lines.get(index));
                scan.useDelimiter(",");
                for (int j = 0; j < 4; ++j) {
                    result[index][j] = scan.nextDouble();
                }
                scan.close();
            }

            return result;

        } catch (FileNotFoundException e) {
            System.err.println("Unable to read csv file " + fname);
            return emptyPath;
        }
    }

    public VelocityMotionProfile(Path path) {
        this(path.getLeftPath(), path.getRightPath());
    }

    public VelocityMotionProfile(double[][] left, double[][] right) {
        configProfile(left, right);
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
