package frc.lightning.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.logging.CommandLogger;

public abstract class LoggedCommand extends Command {
    protected CommandLogger logger = new CommandLogger(getClass().getSimpleName());

    @Override
    protected void initialize() {
        logger.reset();
    }

    @Override
    protected void execute() {
        logger.write();
    }

    @Override
    protected void end() {
        logger.drain();
        logger.flush();
    }
}
