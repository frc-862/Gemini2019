package frc.robot;

import frc.lightning.ConstantBase;
import frc.robot.misc.Gains;

public class Constants extends ConstantBase {

    //For Joystick Input
    public static int leftThrottleAxis = 1;
    public static int rightThrottleAxis = 1;
    public static int driverRightJoy = 0;
    public static int driverLeftJoy = 1;
    public static int collectButton = 1;
    public static int ejectButton = 2;
    public static int hatchToggle = 1;

    /**
     * How many sensor units per rotation.
     * Using CTRE Magnetic Encoder.
     * @see <a href='https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor'>External Units</a>
     */
    public final static int kSensorUnitsPerRotation = 4 * 360;//??????? find all units

    /**
     * Motor neutral dead-band, set to the minimum 0.1%.
     */
    public final static double kNeutralDeadband = 0.001;

    // kF  1.4614284

    /**
     * PID Gains may have to be adjusted based on the responsiveness of control loop
     * 	                                    			  kP   kI    kD     kF             Iz    PeakOut */
    public final static Gains kGains_MotProf = new Gains( 0.0, 0.0,  0.0, 1023.0/1850.0,  400,  1.00 ); /* measured 6800 velocity units at full motor output */
    //6800  -> 3400 -> 17     -> 2000   ->
    //.5 ft -> 2 ft -> 5.5 ft -> 4.5 ft ->
    // num is max raw velocity
    public final static int kPrimaryPIDSlot = 0; // any slot [0,3]

    //max velocity 1.2 fps? units . . . . . . . . . . .

    /**
<<<<<<< Updated upstream
     * Elevator Constants
     */
    public final static int elevatorTopHeight = 5000;
    public final static int elevatorMiddleHeight = 2500;
    public final static int elevatorBottomHeight = 1000;
    public final static double elevatorDownPower=-0.2;
    public final static double elevatorUpPower=0.4;
    public final static double elevatorHoldPower=0.07;
    // Cargo Collector Constants
    public final static double collectPower = 1;
    public final static double ejectPower = -1;
=======
	 * How many sensor units per rotation.
	 * Using CTRE Magnetic Encoder.
	 * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
	 */
	public final static int kSensorUnitsPerRotation = 4 * 360;//??????? find all units
	
	/**
	 * Motor neutral dead-band, set to the minimum 0.1%.
	 */
	public final static double kNeutralDeadband = 0.001;
	
	// kF  1.4614284

	/**
	 * PID Gains may have to be adjusted based on the responsiveness of control loop
	 * 	                                    			  kP   kI    kD     kF             Iz    PeakOut */
	public final static Gains kGains_MotProf = new Gains( 0.0, 0.0,  0.0, 1023.0/1850.0,  400,  1.00 ); /* measured 6800 velocity units at full motor output */
	//6800  -> 3400 -> 17     -> 2000   ->
	//.5 ft -> 2 ft -> 5.5 ft -> 4.5 ft ->
	// num is max raw velocity
	public final static int kPrimaryPIDSlot = 0; // any slot [0,3]

	//max velocity 1.2 fps? units . . . . . . . . . . .
  
	/**
	 * Elevator Constants
	 */
	public final static int elevatorTopHeight = 5000;
	public final static int elevatorMiddleHeight = 2500;
	public final static int elevatorBottomHeight = 1000;
	public final static double elevatorDownPower=-0.2;
	public final static double elevatorUpPower=0.4;
	public final static double elevatorHoldPower=0.07;
	// Cargo Collector Constants
	public final static double collectPower = 1;
	public final static double ejectPower = -1;


	// for Hatch Panel Collect

	public final static double driveBackwardVelocity = 1;
>>>>>>> Stashed changes
}
