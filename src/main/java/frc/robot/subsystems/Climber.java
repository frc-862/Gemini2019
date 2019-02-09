/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotConstants;
import frc.robot.RobotMap;
import frc.robot.commands.climber.Move;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
/**
 * Add your docs here.
 */
public class Climber extends Subsystem {
   
    VictorSP motor;//TODO these were talons
    VictorSP motor2;
    DoubleSolenoid deployer;

    public Climber() {

        motor = new VictorSP(RobotMap.climberID);  // TODO create with correct CAN ID in robot map
        motor2 = new VictorSP(RobotMap.obotWinch2);
        //motor2.follow(motor);

        deployer = null; // TODO create with correct solenoid values

        //motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        //motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 0);
    }

    /** Watch limit switches at ends of travel
     * and auto calibrate encoder position
     */
    /*
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
    */

    public void setPwr(double pow) {
        
        motor.set(pow);
        motor2.set(pow);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Move());
     }

    public double getJackPosition() {
        return 0.0;//motor.getSelectedSensorPosition();
    }

    public void extendJack() {
        //motor.set(ControlMode.MotionMagic, Constants.extendedPosition);
    }

    public void retractJack() {
        //motor.set(ControlMode.MotionMagic, Constants.retractedPosition);
    }

    public void stopJack() {
        motor.set(0.0);//need to specifiy control mode
    }

    public void extendSkids() {
        deployer.set(DoubleSolenoid.Value.kForward);
    }

    public void retractSkids() {
        deployer.set(DoubleSolenoid.Value.kReverse);
    }
}
