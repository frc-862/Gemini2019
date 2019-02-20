/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.cargo;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.CommandLogger;
import frc.robot.Robot;
//import frc.robot.subsystems.CargoCollector;

public class CargoCollect extends Command {
    CommandLogger logger = new CommandLogger(getClass().getSimpleName());
    //double groundcollectpwr =.6;
    double elevatordeploypwr =.5;
    public CargoCollect() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.cargoCollector);
        SmartDashboard.putNumber("elevator deploy power", elevatordeploypwr);
        //SmartDashboard.putNumber("groundcollectpwr",groundcollectpwr);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double pwr=Robot.oi.getCargoCollectPower();
        Robot.cargoCollector.setGroundCollectPower(pwr*elevatordeploypwr);
        //Robot.cargoCollector.setElevatorCollectPower(pwr*groundcollectpwr);
        //groundcollectpwr = SmartDashboard.getNumber("groundcollectpwr",groundcollectpwr);
        elevatordeploypwr = SmartDashboard.getNumber("elevator deploy power", elevatordeploypwr);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
