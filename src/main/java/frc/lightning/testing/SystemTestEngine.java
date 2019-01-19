package frc.lightning.testing;

import java.util.Iterator;
import java.util.PriorityQueue;

public class SystemTestEngine {
    private PriorityQueue<SystemTest> tests = new PriorityQueue<>((t1, t2) -> t1.getPriority() - t2.getPriority() );
    private Iterator<SystemTest> itor;
    private SystemTest current;

    private SystemTestEngine() {
    }

    static SystemTestEngine getEngine() {
        return new SystemTestEngine();
    }

    static void registerTest(SystemTest test) {
        getEngine().addTest(test);
    }

    public void addTest(SystemTest test) {
        tests.add(test);
    }

    public void initialize() {
        itor = tests.iterator();
        current = null;
    }

    public boolean run() {
        if (current == null) {
            current = itor.next();

            if (current != null) {
                current.setup();
            }
        }

        if (current != null) {
            if (current.test()) {
                current.tearDown();
                current = null;
                return itor.hasNext();
            }

            return true;
        }

        return false;
    }

}