/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import java.sql.Time;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Climber extends Subsystem {

    WPI_TalonSRX motor;
    WPI_VictorSPX motorSlave;
    WPI_VictorSPX climberDrive;
    DoubleSolenoid deployer;

    public Climber() {
        motor = new WPI_TalonSRX(RobotMap.climberMasterID);  // TODO create with correct CAN ID in robot map
        addChild("Motor", motor);
        motorSlave = new WPI_VictorSPX(RobotMap.climberSlaveID);
        climberDrive = new WPI_VictorSPX(RobotMap.climberDriveID);
        addChild("Slave Motor", motorSlave);
        motorSlave.follow(motor);
        deployer = new DoubleSolenoid(RobotMap.compressorCANId, RobotMap.climbFwdChan, RobotMap.climbRevChan);; // TODO create with correct solenoid values
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
    }

    /** Watch limit switches at ends of travel
     * and auto calibrate encoder position
     */
    @Override
    public void periodic() {
        var sensors = motor.getSensorCollection();
        // Set the default command for a subsystem here.
        if (sensors.isFwdLimitSwitchClosed()) {
            motor.setSelectedSensorPosition(Constants.extendedPosition);
        } else if (sensors.isRevLimitSwitchClosed()) {
            motor.setSelectedSensorPosition(Constants.retractedPosition);
        }
    }

    @Override
    public void initDefaultCommand() { }

    public double getJackPosition() {
        return motor.getSelectedSensorPosition();
    }

    public void extendJack() {
        motor.set(ControlMode.MotionMagic, Constants.extendedPosition);
    }

    public void retractJack() {
        motor.set(ControlMode.MotionMagic, Constants.retractedPosition);
    }

    public void stopJack() {
        motor.set(ControlMode.PercentOutput, 0);
    }

    public void extendSkids() {
        deployer.set(DoubleSolenoid.Value.kForward);
    }

    public void retractSkids() {
        deployer.set(DoubleSolenoid.Value.kReverse);
    }

    public void setLiftPower(double pwr) {
        motor.set(ControlMode.PercentOutput, pwr);
    }

    public void setFwrPower(double pwr) {
        climberDrive.set(ControlMode.PercentOutput, pwr);
    }
    public double getEncoderValue(){
        return motor.getSelectedSensorPosition();
    }
    
}
