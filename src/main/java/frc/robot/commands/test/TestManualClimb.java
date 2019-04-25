package frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.commands.DoNothing;
import frc.robot.commands.climber.ManualClimb;

public class TestManualClimb extends Command {
    public TestManualClimb() {
        super();
    }

    @Override
    public  boolean isFinished(){
        return true;
    }

    @Override
    public  void initialize(){

        //(new ManualClimb()).start();
    }

    @Override
    public void end(){
        Robot.groundCollector.getCurrentCommand().cancel();
        Robot.groundCollector.setDefaultCommand(new DoNothing());

        Robot.elevator.getCurrentCommand().cancel();
        Robot.elevator.setDefaultCommand(new DoNothing());

        //  Robot.climber.getCurrentCommand().cancel();
        Robot.climber.setDefaultCommand(new ManualClimb());
        Command cmd = Robot.climber.getCurrentCommand();
        if (cmd != null) {
            cmd.start();
        } else {
            System.out.println("No climber command!!!");
        }
    }

}
