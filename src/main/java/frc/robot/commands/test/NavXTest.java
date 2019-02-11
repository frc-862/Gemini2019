/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.lightning.testing.SystemTest;
import frc.lightning.util.FaultCode;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class NavXTest extends SystemTest {
    final double timeout = 5;
    double initialHeading;

    public NavXTest() {
        super(FaultCode.Codes.NAVX_ERROR);
    }

    @Override
    public void setup() {
        initialHeading = Robot.core.getHeading();
    }

    @Override
    public boolean didPass() {
        return Math.abs(initialHeading - Robot.core.getHeading()) > 0;
    }

    @Override
    public boolean isFinished() {
        return didPass() || timeSinceInitialized() > timeout;
    }

    @Override
    public Subsystem requires() {
        return Robot.core;
    }
}

