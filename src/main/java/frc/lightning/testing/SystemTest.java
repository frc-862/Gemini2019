package frc.lightning.testing;

import frc.lightning.util.FaultCode;

public class SystemTest {
    FaultCode.Codes code;
    boolean completed;
    Priority priority;

    static enum Priority{
        DO_NOW, HIGH, MED, LOW, DONT_CARE
    };

    public SystemTest(FaultCode.Codes code) {
        this(code, Priority.MED);
    }

    public SystemTest(FaultCode.Codes code, Priority priority) {
        this.code = code;
        this.priority = priority;
        completed = false;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setup() {/* Config - Talon Modes */}
    public void tearDown() {/* Set Powers to 0.0d */}

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

    public void periodic() {/* Actuation Here */}

    boolean test() {
        setup();
        try{
            while(!getCompleted()){
                periodic();
                if (didFail()) {
                    failed();
                    setCompleted();
                } else if (didPass()) {
                    passed();
                    setCompleted();
                }
            }
        } finally {
            tearDown();
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