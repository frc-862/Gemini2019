/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.commands.climber.Climb;
import frc.robot.commands.climber.RetractClimb;
import frc.robot.commands.climber.StatefulAutoClimb;

/**
 * Add your docs here.
 */
public class Climber extends Subsystem {
    private WPI_TalonSRX motor;
    private WPI_VictorSPX motorSlave;
    private WPI_VictorSPX climberDrive;
    private DoubleSolenoid deployer;

    boolean resetEncoderPos=true;

    public Climber() {
        motor = new WPI_TalonSRX(RobotMap.climberMasterID);  // TODO create with correct CAN ID in robot map
        addChild("Motor", motor);
        motorSlave = new WPI_VictorSPX(RobotMap.climberSlaveID);
        motorSlave.setInverted(true);
        climberDrive = new WPI_VictorSPX(RobotMap.climberDriveID);
        addChild("Slave Motor", motorSlave);
        motorSlave.follow(motor);
        deployer = new DoubleSolenoid(RobotMap.compressorCANId, RobotMap.climbFwdChan, RobotMap.climbRevChan);; // TODO create with correct solenoid values
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        /* Factory default hardware to prevent unexpected behavior */
        motor.configFactoryDefault();

        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        /* Configure Sensor Source for Primary PID */
        motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
                                           Constants.kPIDLoopIdx,
                                           Constants.kTimeoutMs);

        /**
         * Configure Talon SRX Output and Sesnor direction accordingly
         * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
         * Phase sensor to have positive increment when driving Talon Forward (Green LED)
         */
        motor.setSensorPhase(true);
        motor.setInverted(true);

        /* Set relevant frame periods to be at least as fast as periodic rate */
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 20, Constants.kTimeoutMs);

        /* Set Motion Magic gains in slot0 - see documentation */
        motor.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
        motor.config_kF(Constants.kSlotIdx, Constants.climberPIDF.kF, Constants.kTimeoutMs);
        motor.config_kP(Constants.kSlotIdx, Constants.climberPIDF.kP, Constants.kTimeoutMs);
        motor.config_kI(Constants.kSlotIdx, Constants.climberPIDF.kI, Constants.kTimeoutMs);
        motor.config_kD(Constants.kSlotIdx, Constants.climberPIDF.kD, Constants.kTimeoutMs);

        /* Set acceleration and vcruise velocity - see documentation */
        motor.configMotionCruiseVelocity(Constants.climberCruiseVelocity, Constants.kTimeoutMs);
        motor.configMotionAcceleration(Constants.climberAcceleration, Constants.kTimeoutMs);

        /* Zero the sensor */
        motor.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    }

    /** Watch limit switches at ends of travel
     * and auto calibrate encoder position
     */

    private SensorCollection sensors;
    @Override
    public void periodic() {
        sensors = motor.getSensorCollection();
        int pos = sensors.getQuadraturePosition();

        SmartDashboard.putNumber("climber encoder", motor.getSelectedSensorPosition());

        if(resetEncoderPos) { //default true
            if (sensors.isFwdLimitSwitchClosed()) {//check if at top - if so, set sensor pos to top height
                motor.setSelectedSensorPosition(Constants.climberMaxHeight);
                resetEncoderPos = false;
            } else if (sensors.isRevLimitSwitchClosed()) {//if at bottom, set to 0
                motor.setSelectedSensorPosition(0);
                resetEncoderPos = false;
            }
        } else if((pos > Constants.climberOffHardStop) && (pos < Constants.climberMaxHeight)) { //if between inch high and top height, reset encoders next cycle
            resetEncoderPos = true;
        }

        if (pos < 0) {
            motor.setSelectedSensorPosition(0);
        } else if(pos > Constants.climberMaxHeight) {
            motor.setSelectedSensorPosition(Constants.climberMaxHeight);
        }
        
        //SmartDashboard.putData(new Climb());
        //SmartDashboard.putData(new RetractClimb());
    }

    @Override
    public void initDefaultCommand() { }

    public int getJackPosition() {
        return motor.getSelectedSensorPosition();
    }

    public void extendJack() {
        motor.set(ControlMode.MotionMagic, Constants.climberExtenedPosition);
    }

    public void retractJack() {
        motor.set(ControlMode.MotionMagic, 0);
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

    public void setClimberDrivePower(double pwr) {
        climberDrive.set(ControlMode.PercentOutput, pwr);
    }
    public void goToPos(int pos){
        motor.set(ControlMode.MotionMagic, pos);
    }
    public boolean isJackSnug() {
        return sensors.isRevLimitSwitchClosed();
    }
}
