/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class HatchCollectorStateChange extends InstantCommand {
    /**
     * Add your docs here.
     */
    public HatchCollectorStateChange() {
        //super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.hatchPanelCollector);
    }

    // Called once when the command executes
    @Override
    protected void initialize() {
        if(Robot.hatchPanelCollector.getPosition().equals(DoubleSolenoid.Value.kReverse)) Robot.hatchPanelCollector.collect();
        else Robot.hatchPanelCollector.eject();
    }

}
