/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.LEDs.UpdateLEDState;

/**
 * Add your docs here.
 */

public class LEDs extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public enum State {
        //normal chasers
        NORMAL,
        RAINBOW,

        //game piece/collecting-related
        CARGO_COLLECTED,
        HATCH_COLLECTED,
        HAND_OFF_READY,

        // Indicates that we have detected
        // the stereoVision or line targets and
        // the driver can give up control
        VISION_READY,

        //other stuff
        LINE_FOLLOWING, /*SANDSTORM*/

        OFF
    }

    private DigitalOutput bit1;// = new DigitalOutput(7);
    private DigitalOutput bit2;// = new DigitalOutput(8);
    private DigitalOutput bit3;// = new DigitalOutput(9);

    public LEDs() {
        bit1 = new DigitalOutput(7);
        bit2 = new DigitalOutput(8);
        bit3 = new DigitalOutput(9);
    }

    private State state = State.NORMAL;

    public void setState(State state) {
        this.state = state;
    }

    //7, 8, 9

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new UpdateLEDState());
    }

    @Override
    public void periodic() {
        if(Robot.isDisabled) this.state = State.OFF;
        this.set();
    }

    private void set() {
        switch (state) {
        case OFF:
            bit1.set(false);
            bit2.set(false);
            bit3.set(false);
            break;
        case NORMAL:
            bit1.set(true);
            bit2.set(false);
            bit3.set(false);
            break;
        case CARGO_COLLECTED:
            bit1.set(false);
            bit2.set(true);
            bit3.set(false);
            break;
        case HATCH_COLLECTED:
            bit1.set(true);
            bit2.set(true);
            bit3.set(false);
            break;
        case LINE_FOLLOWING:
            bit1.set(false);
            bit2.set(false);
            bit3.set(true);
            break;
        case RAINBOW:
            bit1.set(true);
            bit2.set(false);
            bit3.set(true);
            break;
        default://NORMAL
            bit1.set(true);
            bit2.set(false);
            bit3.set(false);
            break;

        }
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void setYellow() {
        state = State.HATCH_COLLECTED;

    }

    public void setGreen() {
        //TODO: Make actually change color
    }

    public void setPurple() {
        //TODO: Make actually changr colour
    }

}
