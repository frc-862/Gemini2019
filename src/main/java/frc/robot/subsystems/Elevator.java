/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Elevator extends Subsystem {
    private final int topOfTravel = 100;// TODO make these not magical
    private final int mediumDeployPosition = 50;
    private final int lowDeployPosition = 10;
    private final int bottomOfTravel = 0;
    private final int gravityCompensation = 9;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public TalonSRX motor1;
    public VictorSPX motor2;

    public Elevator() {
        motor1 = null; // TODO init me
        motor2 = null;

        motor2.follow(motor1);

        motor1.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        motor1.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 0);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());

    }

    /** Watch limit switches at ends of travel
     * and auto calibrate encoder position
     */
    @Override
    public void periodic() {
        var sensors = motor1.getSensorCollection();

        if (sensors.isFwdLimitSwitchClosed()) {
            motor1.setSelectedSensorPosition(topOfTravel);
        } else if (sensors.isRevLimitSwitchClosed()) {
            motor1.setSelectedSensorPosition(bottomOfTravel);
        }
    }

    public void stop() {
        motor1.set(ControlMode.PercentOutput, 0.0);
    }

    public void goToCollect() {
        motor1.set(ControlMode.MotionMagic, bottomOfTravel, DemandType.ArbitraryFeedForward,gravityCompensation);
    }

    public void goToLow() {
        motor1.set(ControlMode.MotionMagic, lowDeployPosition, DemandType.ArbitraryFeedForward, gravityCompensation);
    }

    public void goToMedium() {
        motor1.set(ControlMode.MotionMagic, mediumDeployPosition, DemandType.ArbitraryFeedForward, gravityCompensation);
    }

    public void goToHigh() {
        motor1.set(ControlMode.MotionMagic, topOfTravel, DemandType.ArbitraryFeedForward, gravityCompensation);
    }
}

