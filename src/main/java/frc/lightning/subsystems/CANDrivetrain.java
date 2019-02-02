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
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;

/**
 * Add your docs here.
 */
public abstract class CANDrivetrain extends LightningDrivetrain {
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

    protected WPI_TalonSRX leftMaster;
    protected WPI_TalonSRX rightMaster;
    protected Vector<FollowMotor> leftFollowers = new Vector<>();
    protected Vector<FollowMotor> rightFollowers = new Vector<>();

    private boolean loggingEnabled = false;

    protected CANDrivetrain(WPI_TalonSRX left, WPI_TalonSRX right) {
        leftMaster = left;
        rightMaster = right;

        leftMaster.setSubsystem(getClass().getSimpleName());
        rightMaster.setSubsystem(getClass().getSimpleName());


        SmartDashboard.putData(leftMaster);
        SmartDashboard.putData(rightMaster);
    }

    private void addFollower(BaseMotorController m, WPI_TalonSRX master, boolean inverted) {
        m.setInverted(inverted);
        m.follow(master);

        if (m instanceof Sendable) {
            Sendable s = (Sendable) m;
            s.setSubsystem(getClass().getSimpleName());
            SmartDashboard.putData(s);
        }
    }

    protected void addRightFollower(BaseMotorController m, boolean inverted) {
        rightFollowers.add(new FollowMotor(m, inverted));
        addFollower(m, rightMaster, inverted);
    }

    protected void addRightFollower(BaseMotorController m) {
        this.addRightFollower(m, false);
    }

    protected void addLeftFollower(BaseMotorController m, boolean inverted) {
        leftFollowers.add(new FollowMotor(m, inverted));
        addFollower(m, leftMaster, inverted);
    }

    protected void addLeftFollower(BaseMotorController m) {
        this.addLeftFollower(m, false);
    }

    public void configureMotors() {
        leftFollowers.stream().forEach((m) -> {
            m.motor.setInverted(m.inverted);
            m.motor.follow(leftMaster);
        });

        rightFollowers.stream().forEach((m) -> {
            m.motor.setInverted(m.inverted);
            m.motor.follow(rightMaster);
        });
    }

    protected void enableLogging() {
        if (!loggingEnabled) {
            withEachMaster((label, talon) -> {
                DataLogger.addDelayedDataElement(label + "Position", () -> talon.getSelectedSensorPosition());
                DataLogger.addDelayedDataElement(label + "Velocity", () -> talon.getSelectedSensorVelocity());
                DataLogger.addDelayedDataElement(label + "MasterCurrent", () -> talon.getOutputCurrent());
                DataLogger.addDelayedDataElement(label + "MasterOutputPercent", () -> talon.getMotorOutputPercent());
            });
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
        rightMaster.set(ControlMode.PercentOutput, -right);
    }

    @Override
    public void setVelocity(double left, double right) {
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
}
