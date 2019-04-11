/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.testing.SystemTest;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.elevator.MicroAdjust;
import frc.robot.commands.test.ElevatorTest;
import frc.robot.commands.test.ElevatorTest.Position;

/**
 * Add your docs here.
 */
public class Elevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private boolean resetEncoderPos = true;

    public WPI_TalonSRX elevatorMotor;
    public AnalogInput pieceDetector;

    public double timeInPos = 0;
    public final double timeoutPosition = 0;

    public double activeTrajectoryPosition = 0;

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
        elevatorMotor.configMotionCruiseVelocity(1400, Constants.kTimeoutMs);
        elevatorMotor.configMotionAcceleration(250, Constants.kTimeoutMs);//500!!!!!

        /* Zero the sensor */
        elevatorMotor.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

        SystemTest.register(new ElevatorTest(Position.HIGH));
        SystemTest.register(new ElevatorTest(Position.LOW));
        SystemTest.register(new ElevatorTest(Position.MED));
        SystemTest.register(new ElevatorTest(Position.COLLECT));

//        DataLogger.addDataElement("ElevatorPosition", () -> elevatorMotor.getSelectedSensorPosition());
//        DataLogger.addDataElement("ElevatorSetpoint", () -> activeTrajectoryPosition);
//        DataLogger.addDataElement("ElevatorCurrent", () -> elevatorMotor.getOutputCurrent());
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());

        setDefaultCommand(new MicroAdjust());
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

        int pos = elevatorMotor.getSelectedSensorPosition();

        if(resetEncoderPos) { //default true
            if (sensors.isFwdLimitSwitchClosed()) {//check if at top - if so, set sensor pos to top height
                elevatorMotor.setSelectedSensorPosition(Constants.elevatorTopHeight);
                resetEncoderPos = false;
            } else if (sensors.isRevLimitSwitchClosed()) {//if at bottom, set to 0
                elevatorMotor.setSelectedSensorPosition(0);//Constants.elevatorBottomHeight);
                resetEncoderPos = false;
            }
        } else if((pos > Constants.elevatorInchHigh) && (pos < Constants.elevatorTopHeight)) { //if between inch high and top height, reset encoders next cycle
            resetEncoderPos = true;
        }

        if (pos < 0) {
            elevatorMotor.setSelectedSensorPosition(0);
        } else if(pos > Constants.elevatorTopHeight) {
            elevatorMotor.setSelectedSensorPosition(Constants.elevatorTopHeight);
        }

        if(timeSinceLastUpdate() > 1.0 && !DriverStation.getInstance().isAutonomous()) {
            stop();
        }

//        SmartDashboard.putBoolean("ResetEncoderPos", resetEncoderPos);

    }

    public void setPosition(double pos) {
        elevatorMotor.set(ControlMode.MotionMagic, pos);
        activeTrajectoryPosition = pos;
    }

    public void MicroAdjustUp() {
        if(Robot.oi.getMicroAdjAmt() != 0.0) {
            timeInPos = Timer.getFPGATimestamp();
            setPosition(activeTrajectoryPosition + (Constants.microAdjAmt * 1));
        }
    }

    public void MicroAdjustDown() {
        if(Robot.oi.getMicroAdjAmt() != 0.0) {
            timeInPos = Timer.getFPGATimestamp();
            setPosition(activeTrajectoryPosition + (Constants.microAdjAmt * -1));
        }
    }
    public void MicroAdjustAmt(double amt) {
        if(Robot.oi.getMicroAdjAmt() != 0.0) {
            timeInPos = Timer.getFPGATimestamp();
            setPosition(activeTrajectoryPosition + amt);
        }
    }

    public double timeSinceLastUpdate() {
        return Timer.getFPGATimestamp() - timeInPos;
    }

    public void goToCollect() {
        // int gravityCompensation = 0;

        // if (hasHatchPanel())
        //     gravityCompensation = Constants.elevatorHatchPanelF;
        // else if (hasCargo())
        //     gravityCompensation = Constants.elevatorCargoF;
        // else
        //     gravityCompensation = Constants.elevatorEmptyF;
        timeInPos = Timer.getFPGATimestamp();
        setPosition(Constants.elevatorCollectHeight);

    }

    public void stop() {
        elevatorMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void resetTimer() {
        timeInPos = Timer.getFPGATimestamp();
    }

    public void goToBottom() {
        timeInPos = Timer.getFPGATimestamp();
        setPosition(0.0);
    }

    public void goToLow() {
        timeInPos = Timer.getFPGATimestamp();
        setPosition(Constants.elevatorBottomHeight);
    }

    public void goToMid() {
        timeInPos = Timer.getFPGATimestamp();
        setPosition(Constants.elevatorMiddleHeight);
    }

    public void goToHigh() {
        timeInPos = Timer.getFPGATimestamp();
        setPosition(Constants.elevatorTopHeight);//, DemandType.ArbitraryFeedForward, gravityCompensation);
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

