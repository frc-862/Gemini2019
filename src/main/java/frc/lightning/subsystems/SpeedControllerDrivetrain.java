package frc.lightning.subsystems;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import frc.lightning.logging.DataLogger;

/**
 * Add your docs here.
 */
public abstract class SpeedControllerDrivetrain extends LightningDrivetrain {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    protected SpeedController leftMaster;
    protected SpeedController rightMaster;
    protected Encoder leftEncoder;
    protected Encoder rightEncoder;

    public SpeedControllerDrivetrain(SpeedController left, SpeedController right) {
        leftMaster = left;
        leftEncoder = new Encoder(24, 25); // could be done much better...
        rightMaster = right;
        rightEncoder = leftEncoder;
    }

    public void enableLogging() {
        DataLogger.addDataElement("leftPosition", () -> getLeftDistance());
        DataLogger.addDataElement("leftVelocity", () -> getLeftVelocity());
        DataLogger.addDataElement("leftPower", () -> leftMaster.get());

        DataLogger.addDataElement("righttPosition", () -> getRightDistance());
        DataLogger.addDataElement("righttVelocity", () -> getRightVelocity());
        DataLogger.addDataElement("righttPower", () -> rightMaster.get());
    }

    public SpeedControllerDrivetrain(SpeedController left, Encoder leftEnc, SpeedController right, Encoder rightEnc) {
        leftMaster = left;
        leftEncoder = leftEnc;
        rightMaster = right;
        rightEncoder = rightEnc;
    }

    protected void withEachMaster(Consumer<SpeedController> fn) {
        fn.accept(leftMaster);
        fn.accept(rightMaster);
    }

    protected SpeedController getLeftMaster() {
        return leftMaster;
    }

    protected SpeedController getRightMaster() {
        return rightMaster;
    }

    @Override
    public void setPower(double left, double right) {
        leftMaster.set(left);
        rightMaster.set(right);
    }

    @Override
    public void setVelocity(double left, double right) {
        // implement me
    }

    @Override
    public void brake() {
        // not really possible, this probably shows this does not belong here...
    }

    @Override
    public void coast() {
        // not really possible, this probably shows this does not belong here...
    }

    @Override
    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    @Override
    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    @Override
    public double getLeftVelocity() {
        return leftEncoder.getRate();
    }

    @Override
    public double getRightVelocity() {
        return rightEncoder.getRate();
    }

    @Override
    public void resetDistance() {
        leftEncoder.reset();
        rightEncoder.reset();
    }
}
