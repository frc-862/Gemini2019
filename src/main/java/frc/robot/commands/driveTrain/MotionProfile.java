/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrain;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.CommandLogger;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.LightningMath;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.paths.Path;

public class MotionProfile extends Command {
    CommandLogger logger = new CommandLogger(getClass().getSimpleName());

    private BufferedTrajectoryPointStream bufferedStreamLeft = new BufferedTrajectoryPointStream();
    private BufferedTrajectoryPointStream bufferedStreamRight = new BufferedTrajectoryPointStream();

    Path path;

    public MotionProfile(Path path) {

        this.path = path;

        System.out.println("left.initBuffer");
        initBuffer(path.getLeftPath(), path.getLeftPath().length, bufferedStreamLeft);
        System.out.println("right.initBuffer");
        initBuffer(path.getRightPath(), path.getRightPath().length, bufferedStreamRight);

        requires(Robot.drivetrain);

        logger.addDataElement("LeftPos");
        logger.addDataElement("RightPos");
        logger.addDataElement("LeftExpected");
        logger.addDataElement("RightExpected");
        logger.addDataElement("RightVelocity");
        logger.addDataElement("LeftVelocity");
        logger.addDataElement("RightExpectedVelocity");
        logger.addDataElement("LeftExpectedVelocity");
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.configurePID(Constants.motionPathPIDs);

        Robot.drivetrain.getLeftMaster().startMotionProfile(bufferedStreamLeft, path.getLeftPath().length, ControlMode.MotionProfile);
        Robot.drivetrain.getRightMaster().startMotionProfile(bufferedStreamRight, path.getRightPath().length, ControlMode.MotionProfile);
        logger.reset();
    }

    @Override
    protected void execute() {
        logger.set("LeftPos", LightningMath.ticks2feet(Robot.drivetrain.getLeftMaster().getSelectedSensorPosition()));
        logger.set("RightPos", LightningMath.ticks2feet(Robot.drivetrain.getRightMaster().getSelectedSensorPosition()));
        logger.set("LeftExpected", LightningMath.ticks2feet(Robot.drivetrain.getLeftMaster().getActiveTrajectoryPosition()));
        logger.set("RightExpected", LightningMath.ticks2feet(Robot.drivetrain.getRightMaster().getActiveTrajectoryPosition()));
        logger.set("RightVelocity", LightningMath.talon2fps(Robot.drivetrain.getRightVelocity()));
        logger.set("LeftVelocity", LightningMath.talon2fps(Robot.drivetrain.getLeftVelocity()));
        logger.set("RightExpectedVelocity", LightningMath.talon2fps(Robot.drivetrain.getRightMaster().getActiveTrajectoryVelocity()));
        logger.set("LeftExpectedVelocity", LightningMath.talon2fps(Robot.drivetrain.getLeftMaster().getActiveTrajectoryVelocity()));

        logger.write();
    }

    @Override
    protected boolean isFinished() {
        return Robot.drivetrain.getLeftMaster().isMotionProfileFinished() && Robot.drivetrain.getRightMaster().isMotionProfileFinished();
    }

    @Override
    protected void end() {
        logger.drain();
        logger.flush();
        Robot.drivetrain.stop();
        System.out.println("we have stopped.");
    }

    @Override
    protected void interrupted() {
        end();
    }

    private void initBuffer(double[][] profile, int totalCnt, BufferedTrajectoryPointStream bufferedStream) {

        boolean forward = true; // set to false to drive in opposite direction of profile (not really needed
        // since you can use negative numbers in profile).

        TrajectoryPoint point = new TrajectoryPoint(); // temp for for loop, since unused params are initialized
        // automatically, you can alloc just one

        /* clear the buffer, in case it was used elsewhere */
        bufferedStream.Clear();

        /* Insert every point into buffer, no limit on size */
        for (int i = 0; i < totalCnt; ++i) {

            double direction = forward ? +1 : -1;
            double positionRot = LightningMath.feet2ticks(profile[i][0]);
            double velocityRPM = LightningMath.fps2talon(profile[i][1]);
            int durationMilliseconds = 20;

            /* for each point, fill our structure and pass it to API */
            point.timeDur = durationMilliseconds;
            point.position = direction * positionRot;
            // Units
            point.velocity = direction * velocityRPM;
            // Units/100ms

            point.auxiliaryPos = 0;
            point.auxiliaryVel = 0;
            point.profileSlotSelect0 = Constants.kPrimaryPIDSlot; /* which set of gains would you like to use [0,3]? */
            point.profileSlotSelect1 = 0; /* auxiliary PID [0,1], leave zero */
            point.zeroPos = (i == 0); /* set this to true on the first point */
            point.isLastPoint = ((i + 1) == totalCnt); /* set this to true on the last point */
            point.arbFeedFwd = 0; /* you can add a constant offset to add to PID[0] output here */

            bufferedStream.Write(point);

            // System.out.println("Position = " + point.position + " ticks");
        }
    }

}
