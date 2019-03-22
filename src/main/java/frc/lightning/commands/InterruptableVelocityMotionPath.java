package frc.lightning.commands;

import frc.lightning.util.InterpolatingDouble;
import frc.lightning.util.InterpolatingMotionPoint;
import frc.lightning.util.InterpolatingTreeMap;

public class InterruptableVelocityMotionPath extends VelocityMotionProfile {
    
    public abstract interface EndCondition {
        public abstract boolean getAsBoolean(InterruptableVelocityMotionPath path);
    }

    EndCondition finsihTest = (mp) -> false;

    public InterruptableVelocityMotionPath(String fname, EndCondition condition) {
        super(fname);
        finsihTest = condition;
    }

    public InterruptableVelocityMotionPath(InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> left, InterpolatingTreeMap<InterpolatingDouble, InterpolatingMotionPoint> right, EndCondition condition) {
        super(left, right);
        finsihTest = condition;
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || finsihTest.getAsBoolean(this);
    }
}
