/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.test;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.lightning.testing.SystemTest;
import frc.lightning.util.FaultCode;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class LeftEncoderTest extends SystemTest {
    private final static int timeout = 2;
    private final static double speed = 0.2;

    private int initPos;

    public LeftEncoderTest() {
        super(FaultCode.Codes.LEFT_ENCODER_NOT_FOUND);
    }

    @Override
    public void setup() {
        initPos = Robot.drivetrain.getLeftMaster().getSelectedSensorPosition();
    }

    @Override
    public void periodic() {
        Robot.drivetrain.getLeftMaster().set(ControlMode.PercentOutput, speed);
    }

    @Override
    public boolean didPass() {
        return (Robot.drivetrain.getLeftMaster().getSelectedSensorPosition() != initPos);
    }

    @Override
    public boolean isFinished() {
        return didPass() || timeSinceInitialized() > timeout;
    }

    @Override
    public void tearDown() {
        Robot.drivetrain.stop();
    }

    @Override
    public Subsystem requires() {
        return Robot.drivetrain;
    }

}
