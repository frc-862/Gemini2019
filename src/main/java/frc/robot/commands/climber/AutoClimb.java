/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.lightning.util.LightningMath;
import frc.robot.Constants;
import frc.robot.Robot;

public class AutoClimb extends Command {
    public AutoClimb() {
        requires(Robot.climber);
    }

    @Override
    protected void initialize() {
        Robot.climber.extendJack();
    }

    @Override
    protected void execute() {
        int pos = Robot.climber.getJackPosition();

        if (pos >= Constants.deployShockPositioin) {
            Robot.climber.extendSkids();
        }

        if (LightningMath.epsilonEqual(pos, Constants.climberExtenedPosition, Constants.climberEpsilon)) {
            Robot.climber.setClimberDrivePower(1);
        }

        // TODO needs end of climb logic to stop Bosch motor and raise climber
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.climber.setClimberDrivePower(0);
        Robot.climber.setLiftPower(0);
    }
}
