package frc.lightning.commands;

import edu.wpi.first.wpilibj.command.Command;

public class ToggleCommand extends Command {
    Command cmdA;
    Command cmdB;
    boolean toggle = true;

    public ToggleCommand(Command a, Command b) {
        cmdA = a;
        cmdB = b;
    }

    @Override
    protected void initialize() {
        if (toggle) {
            cmdA.start();
        } else {
            cmdB.start();
        }
        toggle = !toggle;
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}


