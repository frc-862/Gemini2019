package frc.robot;

import frc.lightning.ConstantBase;
import frc.robot.misc.Gains;

public class Constants extends ConstantBase {

	public final static int kSensorUnitsPerRotation = 4 * 360;
	public final static double kNeutralDeadband = 0.001;
	/* 	                                    			  kP   kI    kD     kF             Iz    PeakOut */
	public final static Gains kGains_MotProf = new Gains( 0.0, 0.0,  0.0, 1023.0/1850.0,  400,  1.00 ); //1850 is max raw velocity
	public final static int kPrimaryPIDSlot = 0; 
	public final static double driveBackwardVelocity = 1;
	public final static double velocityMultiplier = 15;
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
	public final static double elevatorHoldPower=0.07;
	public static final int elevatorHatchPanelF = 0;
	public static final int elevatorCargoF = 0;
	public static final int elevatorEmptyF = 0;
	public final static Gains elevatorPIDF = new Gains(0, 0, 0, 0, 0, 0);
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 0;
	public static final int kSlotIdx = 0;
}
