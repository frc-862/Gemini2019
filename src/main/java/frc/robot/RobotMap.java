/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class RobotMap {
    // Core
    public static final int compressorCANId = 11;
    // Cargo Collector
    public static final int cargoMotor = 21;
    public static final int cargoSolenoidFwdChan = 1;
    public static final int cargoSolenoidRevChan = 2;

    // Elevator
    public static final int pieceDetector = 4;
    public static final int elevatorCanId = 23;

    // Gemini Drive Train
    public static final int geminiLeftMaster = 1;
    public static final int geminiLeftSlave = 2;
    public static final int geminiRightMaster = 4;
    public static final int geminiRIghtSlave = 5;

    // Hatch Collector
    public static final int extenderFwdChan = 3;
    public static final int extenderRevChan = 4;
    public static final int grabberFwdChan = 5;
    public static final int grabberRevChan = 6;
    public static final int hatchDetector = 5;
}
