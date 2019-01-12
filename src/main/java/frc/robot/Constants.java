package frc.robot;

import frc.lightning.ConstantBase;

public class Constants extends ConstantBase {    
	public static int leftThrottleAxis = 1;
    public static int rightThrottleAxis = 1;  
    
    
    /**
	 * How many sensor units per rotation.
	 * Using CTRE Magnetic Encoder.
	 * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
	 */
	public final static int kSensorUnitsPerRotation = 4096;//??????? find all units
	
	/**
	 * Motor neutral dead-band, set to the minimum 0.1%.
	 */
	public final static double kNeutralDeadband = 0.001;
	
	/**
	 * PID Gains may have to be adjusted based on the responsiveness of control loop
	 * 	                                    			  kP   kI    kD     kF             Iz    PeakOut */
	public final static Gains kGains_MotProf = new Gains( 1.0, 0.0,  0.0, 1023.0/6800.0,  400,  1.00 ); /* measured 6800 velocity units at full motor output */
	
	public final static int kPrimaryPIDSlot = 0; // any slot [0,3]

}
