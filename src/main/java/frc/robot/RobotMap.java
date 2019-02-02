/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class RobotMap {
    // Core
    public static final int compressorCANId = 11;
    // Cargo Collector
    public static final int cargoMotor = 21;
    public static final int cargoSolenoidModule = 11;
    public static final int cargoSolenoidFwdChan = 1;
    public static final int cargoSolenoidRevChan = 2;
    // Elevator
    public static final int pieceDetector = 0;
    // Gemini Drive Train
    public static final int geminiLeftMaster = 1;
    public static final int geminiLeftSlave = 2;
    public static final int geminiRightMaster = 4;
    public static final int geminiRIghtSlave = 5;
    // Glitch Drive Train
    public static final int glitchLeftMaster = 1;
    public static final int glitchLeftSlave = 2;
    public static final int glitchLeftSlave2 = 3;
    public static final int glitchRightMaster = 4;
    public static final int glitchRIghtSlave = 5;
    public static final int glitchRightSlave2 = 6;
    // Hatch Collector
    public static final int extenderModule = 11;
    public static final int extenderFwdChan = 0;
    public static final int extenderRevChan = 1;
    public static final int grabberModule = 11;
    public static final int grabberFwdChan = 2;
    public static final int grabberRevChan = 3;
    public static final int hatchDetector = 1;
    // Obot Drive Train
    public static final int obotLeftMaster = 1;
    public static final int obotLeftSlave = 2;
    public static final int obotLeftSlave2 = 3;
    public static final int obotRightMaster = 4;
    public static final int obotRightSlave = 5;
    public static final int obotRightSlave2 = 6;
}
