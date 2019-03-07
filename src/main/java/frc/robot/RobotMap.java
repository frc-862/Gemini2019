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
    public static final int cargoMotor = 26;
    //public static final int cargoSolenoidFwdChan = 2;//1
    //public static final int cargoSolenoidRevChan = 5;//2

    // Elevator
    public static final int pieceDetector = 0;
    public static final int elevatorCanId = 23;
    public static final int leftCollectCanId = 24;
    public static final int rightCollectCanId = 25;

    // Gemini Drive Train
    public static final int geminiLeftMaster = 1;
    public static final int geminiLeftSlave = 2;
    public static final int geminiRightMaster = 4;
    public static final int geminiRightSlave = 5;

    // Hatch Collector
    public static final int extenderFwdChan = 0;
    public static final int extenderRevChan = 7;
    public static final int grabberFwdChan = 1;//5
    public static final int grabberRevChan = 6;
    public static final int hatchDetector = 9;
    //climber
    public static final int climberMasterID = 20;
    public static final int climberSlaveID = 21;
    public static final int climberDriveID = 22;
    public static final int climbFwdChan = 5;
    public static final int climbRevChan = 2;

    public static final int extra1CanId = 12;
    public static final int extra2CanId = 13;
}
