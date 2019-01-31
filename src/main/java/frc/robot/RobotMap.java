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

    public static BaseMotorController leftSlave1;
    public static BaseMotorController leftSlave2;
    public static BaseMotorController rightSlave1;
    public static BaseMotorController rightSlave2;
    public static BaseMotorController leftSlave;
    public static BaseMotorController rightSlave;

    public static void init() {
        if (Robot.isOBot()) {
            leftSlave1 = new WPI_TalonSRX(2);
            leftSlave2 = new WPI_TalonSRX(3);
            rightSlave1 = new WPI_TalonSRX(5);
            rightSlave2 = new WPI_TalonSRX(6);
        }
        else if (Robot.isGlitch()) {
            leftSlave1 = new WPI_VictorSPX(2);
            leftSlave2 = new WPI_VictorSPX(3);
            rightSlave1 = new WPI_VictorSPX(5);
            rightSlave2 = new WPI_VictorSPX(6);
        }
        else { // if (Robot.isGemini() || Robot.isNebula())  {
            leftSlave = new WPI_VictorSPX(2);
            rightSlave = new WPI_VictorSPX(4);
        }
    }
}
