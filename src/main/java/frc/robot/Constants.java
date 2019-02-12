package frc.robot;

import frc.lightning.ConstantBase;
import frc.robot.misc.Gains;

public class Constants extends ConstantBase {
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // NEVER CHECKIN WITH BENCH TEST SET TO TRUE
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static final boolean bench_test = false;

    public static final double TICS_PER_ROTATION = 4 * 360;

    // Cargo Collector
    public static final double collectPower = -0.6;
    public static final double holdPower = -0.2;
    public static final double hasCargoDistance = 0.7;
    // Climber
    public static final int retractedPosition = 0;
    public static final int extendedPosition = 42; // TODO calibrate this value
    // PIDs
    public final static int kSensorUnitsPerRotation = 4 * 360;
    public final static double kNeutralDeadband = 0.001;

    public static Gains motionPathPIDs;
    public static Gains drivePIDs;

    static {
        if(Robot.isGemini()) {
            //_____GEMINI_____\\
            motionPathPIDs = new Gains(0.00000008, 0.0,  0.0,   0.34,  0);
            drivePIDs = new Gains( 0.001, 0.0,  0.0, 10,  400);
        } else {
            //_____NEBULA_____\\
            motionPathPIDs = new Gains(0.00000005, 0.0,  0.0,   0.37,  0);
            drivePIDs = new Gains( 0.001, 0.0,  0.0, 10,  400);
        }
    }

    public final static int kPrimaryPIDSlot = 0;


    // Elevator
    public final static int elevatorTopHeight = 5000;
    public final static int elevatorMiddleHeight = 2500;
    public static final int elevatorCollectHeight = 1000;
    public final static int elevatorBottomHeight = 750;
    public final static double elevatorDownPower=-0.2;
    public final static double elevatorUpPower=0.4;
    public final static double elevatorCollectorHoldPower=0.07;
    public static final int elevatorHatchPanelF = 0;
    public static final int elevatorCargoF = 0;
    public static final int elevatorEmptyF = 0;
    public final static Gains elevatorPIDF = new Gains(0, 0, 0, 0, 0, 0);
    public static final int kPIDLoopIdx = 0;
    public static final int kTimeoutMs = 0;
    public static final int kSlotIdx = 0;
    public static final double cargoEjectTime = 0.2;
    public static final double ejectDemand = 0.5;
    public static final double cargoElevatorDistance=20;
    public static final double hatchPanelElevatorDistance=70;
    public static final double elevatorPieceTolerance = 5;

    // Hatch Panel Collect
    public final static double driveBackwardVelocity = 1;

    // DriveTrain
    public final static double velocityMultiplier = 15.0;// TODO changed from 1
    public final static int drivePowerCurve = 3;

    //public final static Gains drivePIDF = new Gains(862, 0, 0, 8.62, 0);

    // Line Follow
    //Nebula
    //max
    public static final double nebulaMaxLeftOutside = 2.4;
    public static final double nebulaMaxLeftInside = 1.5;
    public static final double nebulaMaxRightInside = 2.0;
    public static final double nebulaMaxRightOutside = 2.2;
    //min
    public static final double nebulaMinLeftOutside =0.62;
    public static final double nebulaMinLeftInside = .47;
    public static final double nebulaMinRightInside = .49;
    public static final double nebulaMinRightOutside = .66;

    //Gemini
    //max
    public static final double geminiMaxLeftOutside = 4.39;
    public static final double geminiMaxLeftInside = 3.95;
    public static final double geminiMaxRightInside = 4.55;
    public static final double geminiMaxRightOutside = 4.18;
    //min
    public static final double geminiMinLeftOutside = 2.31;
    public static final double geminiMinLeftInside = 1.6;
    public static final double geminiMinRightInside = 2.25;
    public static final double geminiMinRightOutside = 1.51;

    public static final double movingCurrent = 20;
    public static final double movingVelocity = 0.1;
}
