package frc.robot;

import frc.lightning.ConstantBase;
import frc.robot.misc.Gains;

public class Constants extends ConstantBase {
    public static int cargoDistance = 4;
    public static int pannelDistance = 7;//TODO make these real


    public static final double TICS_PER_ROTATION = 4 * 360;

    // Cargo Collector
    public static final double collectPower = -0.6;
    public static final double holdPower = -0.2;
    public static final double hasCargoDistance = 0.7;
    // Climber
    public static final int climberRetractedPosition = 0;
    public final static int deployShockPositioin = 100;
    public static final int climberExtenedPosition = 10250; // TODO calibrate this value
    public static final int climberMaxHeight = 11800;
    public static final int climberOffHardStop = 100;
    public static final int climberEpsilon = 100;
    public final static Gains climberPIDF = new Gains(2.54, 0, 1, 0.0, 0, 0);
    public final static int climberCruiseVelocity = 1400;
    public final static int climberAcceleration = 250;

    // PIDs
    public final static int kSensorUnitsPerRotation = 4 * 360;
    public final static double kNeutralDeadband = 0.001;

    public static Gains motionPathPIDs;
    public static final int motionSlot = 1;
    public static Gains drivePIDs;

    public static final int driveSlot = 0;


    static {
        Gains universalGAINS = new Gains(2.0, 0.0,  0.14, 0.99,  0);

        motionPathPIDs = universalGAINS;
        drivePIDs = universalGAINS;
    }

    public final static int kPrimaryPIDSlot = 0;


    // Elevator
    public final static int microAdjAmt = 20;

    public static final int elevatorInchHigh = 200;
    public final static int elevatorTopHeight = 6635;//6535;
    public final static int elevatorMiddleHeight = 3666;// 3666;
    public static final int elevatorCollectHeight = 2605;//2360;//2260;//2160;//loading station cargo collect 2160
    public final static int elevatorBottomHeight = 915;//915;//815;
    public final static double elevatorDownPower=-0.2;
    public final static double elevatorUpPower=0.4;
    public final static double elevatorCollectorHoldPower=0.07;
    public static final int elevatorHatchPanelF = 0;
    public static final int elevatorCargoF = 0;
    public static final int elevatorEmptyF = 0;
    public final static Gains elevatorPIDF = new Gains(4.65, 0, 50.0, 0.0, 0, 0);
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
    public final static double velocityMultiplier = 11.7;
    public final static int drivePowerCurve = 3;

    // Line Follow
    //Nebula
    //max
    public static final double nebulaMaxLeftOutside = 4;
    public static final double nebulaMaxLeftInside = 2.25;
    public static final double nebulaMaxRightInside = 3.65;
    public static final double nebulaMaxRightOutside = 3.67;
    //min
    public static final double nebulaMinLeftOutside =1.52;
    public static final double nebulaMinLeftInside = .83;
    public static final double nebulaMinRightInside = 1.07;
    public static final double nebulaMinRightOutside = 1.04;

    //Gemini
    //max
    public static final double geminiMaxLeftOutside = 4.12;
    public static final double geminiMaxLeftInside = 3.62;
    public static final double geminiMaxRightInside = 3.65;
    public static final double geminiMaxRightOutside = 3.75;
    //min
    public static final double geminiMinLeftOutside = 1.82;
    public static final double geminiMinLeftInside = 1.33;
    public static final double geminiMinRightInside = 1.17;
    public static final double geminiMinRightOutside = 1.25;

    public static final double movingCurrent = 8;
    public static final double movingVelocity = 1;

    public static final int stallDetectLoopDelay = 4;
    public static final double habTwo = 3500;
}
