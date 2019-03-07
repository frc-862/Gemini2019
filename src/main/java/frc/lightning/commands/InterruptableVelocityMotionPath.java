package frc.lightning.commands;

import frc.lightning.util.InterpolatingDouble;
import frc.lightning.util.InterpolatingMotionPoint;
import frc.lightning.util.InterpolatingTreeMap;

import java.util.function.BooleanSupplier;

public class InterruptableVelocityMotionPath extends VelocityMotionProfile {
    BooleanSupplier finsihTest = () -> false;

    public InterruptableVelocityMotionPath(String fname, BooleanSupplier condition) {
        super(fname);
        finsihTest = condition;
    }

    public InterruptableVelocityMotionPath(InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> left, InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> right, BooleanSupplier condition) {
        super(left, right);
        finsihTest = condition;
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || finsihTest.getAsBoolean();
    }
}
