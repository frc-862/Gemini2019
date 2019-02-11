package frc.robot;

import frc.lightning.ConstantBase;
import frc.robot.misc.Gains;

public class Constants extends ConstantBase {
    // !!!!!!!!!!!!!!!!
    // NEVER CHECKIN WITH BENCH TEST SET TO TRUE
    // !!!!!!!!!!!!!!!!
    public static final boolean bench_test = true;

    public static final double TICS_PER_ROTATION = 4 * 360;

    // Cargo Collector
    public static final double collectPower = -0.6;
    public static final double holdPower = -0.2;
    public static final double hasCargoDistance = 0.7;
    // Climber
    public static final int retractedPosition = 0;
    public static final int extendedPosition = 42; // TODO calibrate this value

    public final static int kSensorUnitsPerRotation = 4 * 360;
    public final static double kNeutralDeadband = 0.001;
    /* 	                                    			  kP   kI    kD    kF   Iz  */
    public final static Gains kGains_MotProf2 = new Gains( 0.001, 0.0,  0.0, 10,  400); //1023.0/1300.0 is max raw velocity
    public final static Gains kGains_MotProf = new Gains( 0, 0.0,  0.0, 8,  400); //1023.0/1300.0 is max raw velocity
    //WALK DOWN
    public final static int kPrimaryPIDSlot = 0;
    // public final static Gains drivePIDF = new Gains(862, 0, 0, 8.62, 0, 0); - use kGains_MotProf

    /**
     * Elevator Constants
     */
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

    // for Hatch Panel Collect

    public final static double driveBackwardVelocity = 1;

    // for DriveTrain
    public final static double velocityMultiplier = 1.0;// 15

    public final static Gains drivePIDF = new Gains(862, 0, 0, 8.62, 0);
}
