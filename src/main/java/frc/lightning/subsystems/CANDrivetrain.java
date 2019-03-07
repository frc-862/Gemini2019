/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.subsystems;

import java.util.Vector;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.SlotConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.LightningMath;
import frc.robot.Constants;
import frc.robot.misc.Gains;

/**
 * Add your docs here.
 */
public abstract class CANDrivetrain extends LightningDrivetrain {
    private double leftRequestedVelocity = 0;
    private double rightRequestedVelocity = 0;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    class FollowMotor {
        public BaseMotorController motor;
        public boolean inverted;

        FollowMotor(BaseMotorController m, boolean i) {
            motor = m;
            inverted = i;
        }
    }

    private WPI_TalonSRX leftMaster;
    private WPI_TalonSRX rightMaster;
    private Vector<FollowMotor> leftFollowers = new Vector<>();
    private Vector<FollowMotor> rightFollowers = new Vector<>();
    private boolean invertLeftMaster = false;
    private boolean invertRightMaster = false;

    private boolean loggingEnabled = false;

    protected CANDrivetrain(WPI_TalonSRX left, boolean invertL, WPI_TalonSRX right, boolean invertR) {
        leftMaster = left;
        invertLeftMaster = invertL;
        rightMaster = right;
        invertRightMaster = invertR;

        leftMaster.configFactoryDefault();
        rightMaster.configFactoryDefault();

        String name = getClass().getSimpleName();
        setName(name);
        leftMaster.setSubsystem(name);
        rightMaster.setSubsystem(name);
        addChild(leftMaster);
        addChild(rightMaster);

        leftMaster.setName("Left Master");
        SmartDashboard.putData("Left Master", leftMaster);

        rightMaster.setName("Right Master");
        SmartDashboard.putData("Right Master", rightMaster);

        resetDistance();

    }

    protected CANDrivetrain(WPI_TalonSRX left, WPI_TalonSRX right) {
        this(left, false, right, false);
    }

    private void addFollower(BaseMotorController m, WPI_TalonSRX master, boolean inverted, String name) {
        m.configFactoryDefault();
        m.setInverted(inverted);
        m.follow(master);

        if (m instanceof Sendable) {
            Sendable s = (Sendable) m;
            s.setSubsystem(getClass().getSimpleName());
            s.setName(name);
            SmartDashboard.putData(name, s);
        }
    }

    protected void addRightFollower(BaseMotorController m, boolean inverted) {
        rightFollowers.add(new FollowMotor(m, inverted));
        addFollower(m, rightMaster, inverted, "Right " + (rightFollowers.size() + 1));
        LiveWindow.addChild(rightMaster, m);
    }

    protected void addRightFollower(BaseMotorController m) {
        this.addRightFollower(m, false);
    }

    protected void addLeftFollower(BaseMotorController m, boolean inverted) {
        leftFollowers.add(new FollowMotor(m, inverted));
        addFollower(m, leftMaster, inverted, "Left " + (leftFollowers.size() + 1));
        LiveWindow.addChild(leftMaster, m);
    }

    protected void addLeftFollower(BaseMotorController m) {
        this.addLeftFollower(m, false);
    }

    public void configureMotors() {

        leftMaster.setInverted(invertLeftMaster);
        leftFollowers.stream().forEach((m) -> {
            m.motor.setInverted(m.inverted);
            m.motor.follow(leftMaster);
        });

        rightMaster.setInverted(invertRightMaster);
        rightFollowers.stream().forEach((m) -> {
            m.motor.setInverted(m.inverted);
            m.motor.follow(rightMaster);
        });

    }

    protected void enableLogging() {
        if (!loggingEnabled) {
            withEachMaster((label, talon) -> {
                DataLogger.addDataElement(label + "Position", () -> LightningMath.ticks2feet(talon.getSelectedSensorPosition()));
                DataLogger.addDataElement(label + "Velocity", () -> LightningMath.talon2fps(talon.getSelectedSensorVelocity()));
                DataLogger.addDataElement(label + "MasterCurrent", () -> talon.getOutputCurrent());
                DataLogger.addDataElement(label + "MasterOutputPercent", () -> talon.getMotorOutputPercent());
            });
            DataLogger.addDataElement("leftRequestedVelocity", () -> LightningMath.talon2fps(leftRequestedVelocity));
            DataLogger.addDataElement("rightRequestedVelocity", () -> LightningMath.talon2fps(rightRequestedVelocity));
            loggingEnabled = true;
        }
    }

    protected void withEachMaster(Consumer<WPI_TalonSRX> fn) {
        fn.accept(leftMaster);
        fn.accept(rightMaster);
    }

    protected void withEachMaster(BiConsumer<String,WPI_TalonSRX> fn) {
        fn.accept("left", leftMaster);
        fn.accept("right", rightMaster);
    }

    protected void withEachMotor(Consumer<BaseMotorController> fn) {
        fn.accept(leftMaster);
        leftFollowers.stream().forEach((m) -> fn.accept(m.motor));
        fn.accept(rightMaster);
        rightFollowers.stream().forEach((m) -> fn.accept(m.motor));
    }

    protected void withEachMotor(BiConsumer<String,BaseMotorController> fn) {
        fn.accept("left", leftMaster);
        leftFollowers.stream().forEach((m) -> fn.accept("left", m.motor));
        fn.accept("right", rightMaster);
        rightFollowers.stream().forEach((m) -> fn.accept("right", m.motor));
    }

    public WPI_TalonSRX getLeftMaster() {
        return leftMaster;
    }

    public WPI_TalonSRX getRightMaster() {
        return rightMaster;
    }

    @Override
    public void setPower(double left, double right) {
        leftMaster.set(ControlMode.PercentOutput, left);
        rightMaster.set(ControlMode.PercentOutput, right);
    }

    @Override
    public void setVelocity(double left, double right) {
        leftRequestedVelocity = left;
        rightRequestedVelocity = right;
        leftMaster.set(ControlMode.Velocity, left);
        rightMaster.set(ControlMode.Velocity, right);
    }

    @Override
    public void brake() {
        withEachMotor((m) -> m.setNeutralMode(NeutralMode.Brake));
    }

    @Override
    public void coast() {
        withEachMotor((m) -> m.setNeutralMode(NeutralMode.Coast));
    }

    @Override
    public double  getLeftDistance() {
        return leftMaster.getSelectedSensorPosition();
    }

    @Override
    public double getRightDistance() {
        return rightMaster.getSelectedSensorPosition();
    }

    public void configurePID(SlotConfiguration g, int slot) {
        withEachMaster((m) -> {
            m.config_kP(slot, g.kP);
            m.config_kI(slot, g.kI);
            m.config_kD(slot, g.kD);
            m.config_kF(slot, g.kF);
            m.config_IntegralZone(slot, g.integralZone);
            m.configAllowableClosedloopError(slot, g.allowableClosedloopError);
            m.configMaxIntegralAccumulator(slot, g.maxIntegralAccumulator);
            m.configClosedLoopPeakOutput(slot, g.closedLoopPeakOutput);
            m.configClosedLoopPeriod(slot, g.closedLoopPeriod);

            m.selectProfileSlot(slot, 0);
        });
    }

    public void configurePID(SlotConfiguration g) {
        configurePID(g, 0);
    }

    @Override
    public double getLeftVelocity() {
        return leftMaster.getSelectedSensorVelocity();
    }

    @Override
    public double getRightVelocity() {
        return rightMaster.getSelectedSensorVelocity();
    }

    @Override
    public void resetDistance() {
        withEachMaster((m) -> m.setSelectedSensorPosition(0));
    }

    public boolean isStalled() {
        return leftMaster.getOutputCurrent() > Constants.movingCurrent &&
               leftMaster.getSelectedSensorVelocity() < Constants.movingVelocity &&
               rightMaster.getOutputCurrent() > Constants.movingCurrent &&
               rightMaster.getSelectedSensorVelocity() < Constants.movingVelocity;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("RawLeftEncoder", getLeftMaster().getSelectedSensorPosition());
        SmartDashboard.putNumber("RawRightEncoder", getRightMaster().getSelectedSensorPosition());
    }
}
