package frc.lightning.testing;

import frc.lightning.util.FaultCode;

public class SystemTest {
    FaultCode.Codes code;
    boolean completed;
    int priority;

    public SystemTest(FaultCode.Codes code) {
        this(code, 100);
    }

    public SystemTest(FaultCode.Codes code, int priority) {
        this.code = code;
        this.priority = priority;
        completed = false;
    }

    public int getPriority() {
        return priority;
    }

    public void setup() {}
    public void tearDown() {}

    public boolean didPass() {
        return false;
    }

    public boolean didFail() {
        return false;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted() {
        completed = true;
    }

    public void testLoop() {}

    boolean test() {
        testLoop();

        if (didFail()) {
            failed();
            setCompleted();
        } else if (didPass()) {
            passed();
            setCompleted();
        }

        return getCompleted();
    }

    public void passed() {
        System.out.println("Passed test for " + code);
    }

    public void failed() {
        FaultCode.write(code);
        System.out.println("Test triggered " + code);
    }
}