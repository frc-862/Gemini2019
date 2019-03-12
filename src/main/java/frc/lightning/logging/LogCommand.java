package frc.lightning.logging;

import edu.wpi.first.wpilibj.command.Command;
import java.util.function.Supplier;

/**
 * Created by phurley on 12/7/16.
 */
public class LogCommand extends Command {
    final Supplier<String> msg;

    public LogCommand(Supplier<String> msg) {
        this.msg = msg;
    }

    public LogCommand(String msg) {
        this.msg = () -> msg;
    }

    @Override
    public void initialize() {
        System.out.println(msg.get());
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    protected void execute() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }
}

