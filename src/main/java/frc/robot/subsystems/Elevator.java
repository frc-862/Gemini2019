/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.lightning.util.LightningMath;
import frc.robot.Constants;
import frc.robot.commands.elevator.SetElevatorHigh;
import frc.robot.commands.elevator.UpdateElevatorState;

/**
 * Add your docs here.
 */
public class Elevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public TalonSRX motor1;
    public VictorSPX motor2;
    public VictorSPX motor3;
    public VictorSPX motor4;

    public AnalogInput pieceDetector;

    public enum HeightState {
        HIGH_ROCKET, MID_ROCKET, LOW_ROCKET, CARGO_COLLECT
    }

    private HeightState heightState = HeightState.CARGO_COLLECT;


    public Elevator() {
        motor1 = null; // TODO init me
        motor2 = null;

        pieceDetector = new AnalogInput(0);
        

        motor2.follow(motor1);

        motor1.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        motor1.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 0);

        		/* Factory default hardware to prevent unexpected behavior */
		motor1.configFactoryDefault();

		/* Configure Sensor Source for Pirmary PID */
		motor1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
											Constants.kPIDLoopIdx, 
											Constants.kTimeoutMs);

		/**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		motor1.setSensorPhase(true);
		motor1.setInverted(false);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		motor1.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		motor1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

		/* Set the peak and nominal outputs */
		motor1.configNominalOutputForward(0, Constants.kTimeoutMs);
		motor1.configNominalOutputReverse(0, Constants.kTimeoutMs);
		motor1.configPeakOutputForward(1, Constants.kTimeoutMs);
		motor1.configPeakOutputReverse(-1, Constants.kTimeoutMs);

		/* Set Motion Magic gains in slot0 - see documentation */
		motor1.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
		motor1.config_kF(Constants.kSlotIdx, Constants.elevatorPIDF.kF, Constants.kTimeoutMs);
		motor1.config_kP(Constants.kSlotIdx, Constants.elevatorPIDF.kP, Constants.kTimeoutMs);
		motor1.config_kI(Constants.kSlotIdx, Constants.elevatorPIDF.kI, Constants.kTimeoutMs);
		motor1.config_kD(Constants.kSlotIdx, Constants.elevatorPIDF.kD, Constants.kTimeoutMs);

		/* Set acceleration and vcruise velocity - see documentation */
		motor1.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
		motor1.configMotionAcceleration(6000, Constants.kTimeoutMs);

		/* Zero the sensor */
		motor1.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());

        setDefaultCommand(new UpdateElevatorState());

    }

    public void selectHighState() {
        heightState = HeightState.HIGH_ROCKET;
    }

    public void selectMidState() {
        heightState = HeightState.MID_ROCKET;
    }

    public void selectLowState() {
        heightState = HeightState.LOW_ROCKET;
    }

    public void selectCargoCollect() {
        heightState = HeightState.CARGO_COLLECT;
    }

    public HeightState getHeightState() {
        return heightState;
    }

    /** Watch limit switches at ends of travel
     * and auto calibrate encoder position
     */
    @Override
    public void periodic() {
        var sensors = motor1.getSensorCollection();

        if (sensors.isFwdLimitSwitchClosed()) {
            motor1.setSelectedSensorPosition(Constants.elevatorTopHeight);
        } else if (sensors.isRevLimitSwitchClosed()) {
            motor1.setSelectedSensorPosition(Constants.elevatorBottomHeight);
        }
    }

    public void stop() {
        motor1.set(ControlMode.PercentOutput, 0.0);
    }

    public void goToCollect() {
        int gravityCompensation = 0;

        if (hasHatchPanel())
            gravityCompensation = Constants.elevatorHatchPanelF;
        else if (hasCargo())
            gravityCompensation = Constants.elevatorCargoF;
        else
            gravityCompensation = Constants.elevatorEmptyF;

        motor1.set(ControlMode.MotionMagic, Constants.elevatorCollectHeight, DemandType.ArbitraryFeedForward, gravityCompensation);

    }

    public void goToLow() {
        int gravityCompensation = 0;

        if (hasHatchPanel())
            gravityCompensation = Constants.elevatorHatchPanelF;
        else if (hasCargo())
            gravityCompensation = Constants.elevatorCargoF;
        else
            gravityCompensation = Constants.elevatorEmptyF;

        motor1.set(ControlMode.MotionMagic, Constants.elevatorBottomHeight, DemandType.ArbitraryFeedForward, gravityCompensation);
    }

    public void goToMid() {
        int gravityCompensation = 0;

        if (hasHatchPanel())
            gravityCompensation = Constants.elevatorHatchPanelF;
        else if (hasCargo())
            gravityCompensation = Constants.elevatorCargoF;
        else
            gravityCompensation = Constants.elevatorEmptyF;

        motor1.set(ControlMode.MotionMagic, Constants.elevatorMiddleHeight, DemandType.ArbitraryFeedForward, gravityCompensation);
    }

    public void goToHigh() {
        int gravityCompensation = 0;

        if (hasHatchPanel())
            gravityCompensation = Constants.elevatorHatchPanelF;
        else if (hasCargo())
            gravityCompensation = Constants.elevatorCargoF;
        else
            gravityCompensation = Constants.elevatorEmptyF;

        motor1.set(ControlMode.MotionMagic, Constants.elevatorTopHeight, DemandType.ArbitraryFeedForward, gravityCompensation);
    }

    public boolean hasHatchPanel() {
        return LightningMath.isInRange(pieceDetector.getValue(), Constants.hatchPanelElevatorDistance, Constants.elevatorPieceTolerance); //TODO method stub
    }

    public boolean hasCargo() {
        return LightningMath.isInRange(pieceDetector.getValue(), Constants.cargoElevatorDistance,Constants.elevatorPieceTolerance); //TODO method stub
    }
    public void ejectCargo() {
        motor3.set(ControlMode.PercentOutput, Constants.ejectDemand*-1);
    }

    public void stopEject(){
        motor3.set(ControlMode.PercentOutput, 0.0);
    }

    public void elevatorCargoHoldPower() {
        motor3.set(ControlMode.PercentOutput, Constants.elevatorCollectorHoldPower);
    }

    public void collectCargo(){
        motor3.set(ControlMode.PercentOutput, Constants.ejectDemand);
    }
    public void stopCollectCargo(){

    }
    



}

