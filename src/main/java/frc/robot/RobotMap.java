/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  public static final int shootButton = 6;

  public static final int rightMotor1 = 0;
	public static final int rightMotor2 = 1;
	public static final int leftMotor1 = 2;
  public static final int leftMotor2 = 3;

  public static final int leftEncoder1 = 0;
  public static final int leftEncoder2 = 1;
  public static final int rightEncoder1 = 2;
  public static final int rightEncoder2 = 3;

  public static int compressorCANId = 11;
  public static int pdpCANId = 10;
  
  public static final int kickerCANId1 = 2;
  public static final int kickerCANId2 = 3;

  public static final int leftFlywheelCANId = 4;
  public static final int rightFlywheelCANId = 5;
}
