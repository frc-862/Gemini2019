/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.DataInput;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.testing.SystemTest;
import frc.lightning.util.LightningMath;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.commands.elevator.ManualElevator;
import frc.robot.commands.elevator.UpdateElevatorState;
import frc.robot.commands.test.ElevatorTest;
import frc.robot.commands.test.ElevatorTest.Position;
import frc.robot.subsystems.Core;

import javax.xml.crypto.Data;

/**
 * Add your docs here.
 */
public class Elevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private boolean resetEncoderPos = true;

    public WPI_TalonSRX elevatorMotor;
    public AnalogInput pieceDetector;

    public enum HeightState {
        HIGH_ROCKET, MID_ROCKET, LOW_ROCKET, CARGO_COLLECT
    }

    private HeightState heightState = HeightState.CARGO_COLLECT;

    public Elevator() {

        pieceDetector = new AnalogInput(7);
        addChild("Cargo Detector", pieceDetector);

        elevatorMotor = new WPI_TalonSRX(RobotMap.elevatorCanId);
        addChild("Elevator Motor", elevatorMotor);

        /* Factory default hardware to prevent unexpected behavior */
        elevatorMotor.configFactoryDefault();

        elevatorMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        /* Configure Sensor Source for Primary PID */
        elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
                Constants.kPIDLoopIdx,
                Constants.kTimeoutMs);

        /**
         * Configure Talon SRX Output and Sesnor direction accordingly
         * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
         * Phase sensor to have positive increment when driving Talon Forward (Green LED)
         */
        elevatorMotor.setSensorPhase(true);
        elevatorMotor.setInverted(true);

        /* Set relevant frame periods to be at least as fast as periodic rate */
        elevatorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
        elevatorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 20, Constants.kTimeoutMs);

        ///* Set the peak and nominal outputs */
        //elevatorMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
        //elevatorMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
        //elevatorMotor.configPeakOutputForward(1, Constants.kTimeoutMs);
        //elevatorMotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);
        ////elevatorMotor.configContinuousCurrentLimit(75);

        /* Set Motion Magic gains in slot0 - see documentation */
        elevatorMotor.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
        elevatorMotor.config_kF(Constants.kSlotIdx, Constants.elevatorPIDF.kF, Constants.kTimeoutMs);
        elevatorMotor.config_kP(Constants.kSlotIdx, Constants.elevatorPIDF.kP, Constants.kTimeoutMs);
        elevatorMotor.config_kI(Constants.kSlotIdx, Constants.elevatorPIDF.kI, Constants.kTimeoutMs);
        elevatorMotor.config_kD(Constants.kSlotIdx, Constants.elevatorPIDF.kD, Constants.kTimeoutMs);

        /* Set acceleration and vcruise velocity - see documentation */
        elevatorMotor.configMotionCruiseVelocity(640, Constants.kTimeoutMs);
        elevatorMotor.configMotionAcceleration(150, Constants.kTimeoutMs);

        /* Zero the sensor */
        elevatorMotor.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

        SystemTest.register(new ElevatorTest(Position.HIGH));
        SystemTest.register(new ElevatorTest(Position.LOW));
        SystemTest.register(new ElevatorTest(Position.MED));
        SystemTest.register(new ElevatorTest(Position.COLLECT));

        DataLogger.addDataElement("Elevator Position", () -> elevatorMotor.getSelectedSensorPosition());
        DataLogger.addDataElement("Elevator Setpoint", () -> elevatorMotor.getActiveTrajectoryPosition());
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());

        //setDefaultCommand(new ManualElevator());
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
        var sensors = elevatorMotor.getSensorCollection();

        SmartDashboard.putNumber("ElevatorEncoder", elevatorMotor.getSelectedSensorPosition());

        if(resetEncoderPos){
            if (sensors.isFwdLimitSwitchClosed()) {
                elevatorMotor.setSelectedSensorPosition(Constants.elevatorTopHeight);
            } else if (sensors.isRevLimitSwitchClosed()) {
                elevatorMotor.setSelectedSensorPosition(0);//Constants.elevatorBottomHeight);
            }
            resetEncoderPos = false;
        }


        int pos = elevatorMotor.getSelectedSensorPosition();
        if((!resetEncoderPos) && ((pos < Constants.elevatorTopHeight) && (Constants.elevatorBottomHeight < pos))){
            resetEncoderPos = true;
        }

    }

    public void stop() {
        elevatorMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void goToCollect() {
        // int gravityCompensation = 0;

        // if (hasHatchPanel())
        //     gravityCompensation = Constants.elevatorHatchPanelF;
        // else if (hasCargo())
        //     gravityCompensation = Constants.elevatorCargoF;
        // else
        //     gravityCompensation = Constants.elevatorEmptyF;

        elevatorMotor.set(ControlMode.MotionMagic, Constants.elevatorCollectHeight);

    }

    public void goToBottom() {
        elevatorMotor.set(ControlMode.MotionMagic, 0.0);
    }

    public void goToLow() {

        elevatorMotor.set(ControlMode.MotionMagic, Constants.elevatorBottomHeight);
    }

    public void goToMid() {

        elevatorMotor.set(ControlMode.MotionMagic, Constants.elevatorMiddleHeight);
    }

    public void goToHigh() {

        elevatorMotor.set(ControlMode.MotionMagic, Constants.elevatorTopHeight);//, DemandType.ArbitraryFeedForward, gravityCompensation);
    }

    public boolean hasHatchPanel() {
//        return LightningMath.isInRange(pieceDetector.getValue(), Constants.hatchPanelElevatorDistance, Constants.elevatorPieceTolerance); //TODO method stub
        return false;
    }

    public boolean hasCargo() {
//        return LightningMath.isInRange(pieceDetector.getValue(), Constants.cargoElevatorDistance,Constants.elevatorPieceTolerance); //TODO method stub
        return false;
    }





}

