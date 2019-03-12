package frc.lightning.util;

import edu.wpi.first.wpilibj.Timer;
import frc.lightning.util.FaultCode.Codes;
import java.util.function.BooleanSupplier;

public class TimedFaultMonitor extends AbstractFaultMonitor {
    final double duration;
    final BooleanSupplier fn;
    final Timer timer = new Timer();

    public TimedFaultMonitor(Codes code, BooleanSupplier fn, double duration, String msg) {
        super(code, msg);
        this.fn = fn;
        this.duration = duration;
    }

    @Override
    public boolean checkFault() {
        if (!fn.getAsBoolean()) {
            timer.reset();
        }
        return timer.get() >= duration;
    }
}
