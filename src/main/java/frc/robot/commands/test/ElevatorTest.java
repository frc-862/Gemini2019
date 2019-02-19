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
public class ElevatorTest extends SystemTest {
    private final static int timeout = 10;
    private final static int allowedError = 300;

    private int initEncoderPos;
    private Position desiredPos;

    public enum Position {
        LOW,
        MED,
        HIGH,
        COLLECT
    }

    public ElevatorTest(Position desiredPos) {
        super(FaultCode.Codes.ELEVATOR_ERROR);
        this.desiredPos = desiredPos;
    }

    @Override
    public void setup() {
        initEncoderPos = Robot.elevator.elevatorMotor.getSelectedSensorPosition();
        if(desiredPos.equals(Position.LOW)) {
            Robot.elevator.goToLow();
        } else if(desiredPos.equals(Position.MED)) {
            Robot.elevator.goToMid();
        } else if(desiredPos.equals(Position.HIGH)) {
            Robot.elevator.goToHigh();
        } else if(desiredPos.equals(Position.COLLECT)) {
            Robot.elevator.goToCollect();
        } else {

        }
    }

    @Override
    public boolean didPass() {
        return Math.abs(Robot.elevator.elevatorMotor.getSelectedSensorPosition() - initEncoderPos) < allowedError;
    }

    @Override
    public boolean isFinished() {
        return didPass() || timeSinceInitialized() > timeout;
    }

    @Override
    public Subsystem requires() {
        return Robot.elevator;
    }
}
