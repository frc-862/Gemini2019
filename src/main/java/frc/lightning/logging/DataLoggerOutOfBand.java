package frc.lightning.logging;

import edu.wpi.first.wpilibj.Notifier;
import java.util.Vector;
import java.util.function.DoubleSupplier;

public class DataLoggerOutOfBand implements DoubleSupplier {
    private static Vector<DoubleSupplier> suppliers = new Vector<>();
    private static Object swap = new Object();  // used as a mutex on values&scratch
    private static double[] values = null;
    private static double[] scratch = null;
    private static Notifier notifier;
    int index;

    static {
        notifier = new Notifier(new Runnable() {
            @Override
            public void run() {
                synchronized (DataLoggerOutOfBand.class) {
                    int i = 0;
                    for (DoubleSupplier s : suppliers) {
                        scratch[i++] = s.getAsDouble();
                    }

                    synchronized (swap) {
                        var tmp = scratch;
                        scratch = values;
                        values = tmp;
                    }
                }

            }
        });
    }

    public DataLoggerOutOfBand(DoubleSupplier fn) {
        synchronized(DataLoggerOutOfBand.class) {
            suppliers.add(fn);
            values = new double[suppliers.size()];
            scratch = new double[suppliers.size()];

            if (suppliers.size() == 1) {
                notifier.startPeriodic(0.02);
            }
        }
    }

    @Override
    public double getAsDouble() {
        synchronized(swap) {
            return values[index];
        }
    }
}
