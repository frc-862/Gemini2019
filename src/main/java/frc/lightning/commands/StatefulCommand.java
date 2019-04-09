package frc.lightning.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatefulCommand extends Command {
    protected Enum<?> state;
    protected Runnable default_action = () -> {};
    private Enum<?> previous_state = null;
    private Enum<?> calling_state = null;
    private double enteredStateAt = 0;

    public void setState(Enum<?> new_state) {
        state = new_state;
    }

    public Enum<?> getState() {
        return state;
    }

    public Enum<?> getCallingState() {
        return calling_state;
    }

    protected void setDefaultAction(Runnable action) {
        default_action = action;
    }

    public StatefulCommand(Enum<?> state) {
        this.state = state;
    }

    @Override
    protected void initialize() {
        previous_state = null;
        enteredStateAt = Timer.getFPGATimestamp();
        calling_state = state;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private boolean call(String method_name) {
        try {
            Method method = getClass().getMethod(method_name);
            method.invoke(this);
        } catch (NoSuchMethodException | SecurityException |
                     IllegalAccessException | IllegalArgumentException |
                     InvocationTargetException e) {
            System.err.println("StatefulCommand missing method: " + method_name);
            return false;
        }
        return true;
    }

    protected String methodName(Enum<?> state) {
        String state_name = state.name().toLowerCase();
        String method_name = Stream.of(state_name.split("[^a-zA-Z0-9]"))
                             .map(v -> v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase())
                             .collect(Collectors.joining());
        method_name = method_name.substring(0, 1).toLowerCase() + method_name.substring(1);
        return method_name;
    }

    protected double timeInState() {
        return Timer.getFPGATimestamp() - enteredStateAt;
    }

    @Override
    protected void execute() {
        if (previous_state != state) {
            if (previous_state != null) {
                String exit_method = methodName(previous_state) + "Exit";
                call(exit_method);
            }

            enteredStateAt = Timer.getFPGATimestamp();
            previous_state = state;
            calling_state = state;
            call(methodName(state) + "Enter");
        }

        if (!call(methodName(state))) {
            this.default_action.run();
        }
    }
}
