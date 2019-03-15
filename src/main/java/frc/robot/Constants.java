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
    public final static int deployShockPositioin = 2500;
    public static final int climberExtenedPosition = 11000; // TODO calibrate this value
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
        //if(Robot.isGemini()) {
        //    //_____GEMINI_____\\
        //    motionPathPIDs = new Gains(4.65, 0.0,  0.0,   0.74,  0);
        //    drivePIDs = new Gains(4.65, 0.0,  0.0,   0.74,  0);
        //} else {
        //    //_____NEBULA_____\\
        //    motionPathPIDs = new Gains(0.0, 0.0,  0.0, 0.0,  0);
        //    drivePIDs = new Gains( 0.0, 0.0, 0.0, 0.0,  0);
        //}

        Gains universalGAINS = new Gains/*(0.1, 0.0, 0.0, 0.1, 0);*/(4.55, 0.0,  0.0,   0.74,  0);//gemini //p = 4.65

        motionPathPIDs = universalGAINS;// new Gains(4.65, 0.0,  0.0,   0.74,  0);// 9!!!

        drivePIDs = universalGAINS;// new Gains( 4.65, 0.0,  0.0, 0.74,  0);
        //drivePIDs = new Gains( 4.65, 0.0,  0.0, 0.74,  0);
    }

    public final static int kPrimaryPIDSlot = 0;


    // Elevator
    public final static int microAdjAmt = 20;

    public static final int elevatorInchHigh = 200;
    public final static int elevatorTopHeight = 6635;//6535;
    public final static int elevatorMiddleHeight = 3666;// 3666;
    public static final int elevatorCollectHeight = 2705;//2360;//2260;//2160;//loading station cargo collect 2160
    public final static int elevatorBottomHeight = 1015;//915;//815;
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
    public final static double velocityMultiplier = 15.0;
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
    public static final double geminiMaxLeftOutside = 2.47;
    public static final double geminiMaxLeftInside = 2.48;
    public static final double geminiMaxRightInside = 2.89;
    public static final double geminiMaxRightOutside = 2.90;
    //min
    public static final double geminiMinLeftOutside = .61;
    public static final double geminiMinLeftInside = .65;
    public static final double geminiMinRightInside = .71;
    public static final double geminiMinRightOutside = .71;

    public static final double movingCurrent = 20;
    public static final double movingVelocity = 0.1;

    public static final int stallDetectLoopDelay = 4;
}
