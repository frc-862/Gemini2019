package frc.lightning.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class LightningDrivetrain extends Subsystem {
    public abstract void setPower(double left, double right);
    public abstract void setVelocity(double left, double right);

    public abstract void brake();
    public abstract void coast();

    public abstract double getLeftDistance();
    public abstract double getRightDistance();
    public abstract double getLeftVelocity();
    public abstract double getRightVelocity();

    public abstract void resetDistance();

    public void stop() {
        setPower(0,0);
        brake();
    }

    public double getDistance() {
        return (getLeftDistance() + getRightDistance()) / 2;
    }

    public double getVelocity() {
        return (getLeftVelocity() + getRightVelocity()) / 2;
    }
}