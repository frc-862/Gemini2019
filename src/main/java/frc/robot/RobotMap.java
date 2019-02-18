/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class RobotMap {

    // Core
    public static final int pdpCANId = 10;
    public static final int compressorCANId = 11;

    // Cargo Collector
    public static final int cargoMotor = 21;
    public static final int cargoSolenoidFwdChan = 1;
    public static final int cargoSolenoidRevChan = 2;

    // Elevator
    public static final int pieceDetector = 0;
    public static final int elevatorCanId = 23;
    public static final int leftCollectCanId = 24;
    public static final int rightCollectCanId = 25;

    // Gemini Drive Train
    public static final int geminiLeftMaster = 25;
    public static final int geminiLeftSlave = 26;
    public static final int geminiRightMaster = 4;
    public static final int geminiRightSlave = 5;

    // Hatch Collector
    public static final int extenderFwdChan = 3;
    public static final int extenderRevChan = 4;
    public static final int grabberFwdChan = 5;
    public static final int grabberRevChan = 6;
    public static final int hatchDetector = 9;

    public static final int climberID = 20;

    public static final int extra1CanId = 12;
    public static final int extra2CanId = 13;

}
