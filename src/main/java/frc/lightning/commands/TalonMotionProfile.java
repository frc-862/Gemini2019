package frc.lightning.commands;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.logging.CommandLogger;
import frc.lightning.util.LightningMath;
import frc.robot.Robot;

public class TalonMotionProfile extends Command {
    protected double[][] leftPath;
    protected double[][] rightPath;

    CommandLogger logger = new CommandLogger(getClass().getCanonicalName());
    BufferedTrajectoryPointStream leftPS = new BufferedTrajectoryPointStream();
    BufferedTrajectoryPointStream rightPS = new BufferedTrajectoryPointStream();

    public TalonMotionProfile(double[][] left, double[][] right) {
        requires(Robot.drivetrain);
        leftPath = left;
        rightPath = right;

        leftPS.Clear();
        rightPS.Clear();

        for (int i = 0; i < leftPath.length; ++i) {
            TrajectoryPoint leftTP = new TrajectoryPoint();
            double[] leftPt = leftPath[i];
            leftTP.zeroPos = (i == 0);
            leftTP.position = LightningMath.feet2talon(leftPt[0]);
            leftTP.velocity = LightningMath.fps2talon(leftPt[1]);
//           leftTP.headingDeg = leftPt[3];  //units?
            leftTP.isLastPoint = (i + 1) == leftPath.length;
            leftTP.timeDur = 20;
            leftPS.Write(leftTP);

            TrajectoryPoint rightTP = new TrajectoryPoint();
            double[] rightPt = rightPath[i];
            rightTP.zeroPos = (i == 0);
            rightTP.position = LightningMath.feet2talon(rightPt[0]);
            rightTP.velocity = LightningMath.fps2talon(rightPt[1]);
//           rightTP.headingDeg = rightPt[3];  //units?
            rightTP.isLastPoint = (i + 1) == rightPath.length;
            leftTP.timeDur = 20;
            rightPS.Write(rightTP);
        }

        logger.addDataElement("leftProjected");
        logger.addDataElement("rightProjected");
        logger.addDataElement("leftActual");
        logger.addDataElement("rightActual");
    }


    /**
     * The initialize method is called just before the first time
     * this Command is run after being started.
     */
    @Override
    protected void initialize() {
        logger.reset();
        Robot.drivetrain.getLeftMaster().startMotionProfile(leftPS, 10, ControlMode.MotionProfile);
        Robot.drivetrain.getRightMaster().startMotionProfile(rightPS, 10, ControlMode.MotionProfile);
    }


    /**
     * The execute method is called repeatedly when this Command is
     * scheduled to run until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        // log something useful
        WPI_TalonSRX left = Robot.drivetrain.getLeftMaster();
        WPI_TalonSRX right = Robot.drivetrain.getRightMaster();
        var ls = Robot.drivetrain.getLeftMaster().getSensorCollection();
        var rs = Robot.drivetrain.getRightMaster().getSensorCollection();

        logger.set("leftProjected", left.getClosedLoopTarget());
        logger.set("rightProjected", right.getClosedLoopTarget());
        logger.set("leftActual", ls.getQuadraturePosition());
        logger.set("rightActual", rs.getQuadraturePosition());

        logger.writeValues();
    }


    /**
     */
    @Override
    protected boolean isFinished() {
        return Robot.drivetrain.getLeftMaster().isMotionProfileFinished() &&
                Robot.drivetrain.getRightMaster().isMotionProfileFinished();
    }

    @Override
    protected void end() {
        logger.drain();
        logger.flush();
        Robot.drivetrain.stop();
    }
}
