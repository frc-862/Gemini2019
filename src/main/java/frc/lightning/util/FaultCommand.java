package frc.lightning.util;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Created by phurley on 12/7/16.
 */
public class FaultCommand extends Command {
    final FaultCode.Codes code;

    public FaultCommand(FaultCode.Codes code) {
        this.code = code;
    }

    @Override
    public void initialize() {
        System.out.println("FaultCommand initialize: " + code);
        FaultCode.write(code);
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
