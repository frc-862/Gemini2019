/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.LEDs.UpdateLEDState;

/**
 * Add your docs here.
 */

public class LEDs extends Subsystem {
    public void setYellow() {
    }

    public void setGreen() {
    }

    public void setPurple() {
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public enum State {

        //Normal
        OFF,                    //0
        STD_BLUE_ORANGE_CHASE,  //1
        //Vision/Line Ready
        LINE_FOLLOW_READY,      //10
        VISION_READY,           //11
        //Game Piece
        PIECE_COLLECTED,        //100
        //Climb
        TIME_2_CLIMB_KIDS,      //101
        CLIMBING,               //110
        ALL_DONE_NAP_TIME       //111

    }

    private DigitalOutput bit1;
    private DigitalOutput bit2;
    private DigitalOutput bit3;

    public LEDs() {
        bit1 = new DigitalOutput(7);
        bit2 = new DigitalOutput(8);
        bit3 = new DigitalOutput(9);
    }

    private State state = State.OFF;

    public void setState(State state) {
        this.state = state;
    }

    //7, 8, 9

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new UpdateLEDState());
    }

    @Override
    public void periodic() {
        if(Robot.isDisabled) this.state = State.OFF;
        this.set();
    }

    private void set() {
        switch (state) {
        case STD_BLUE_ORANGE_CHASE:
            setBits(0, 0, 1);
            break;
        case LINE_FOLLOW_READY:
            setBits(0, 1, 0);
            break;
        case VISION_READY:
            setBits(0, 1, 1);
            break;
        case PIECE_COLLECTED:
            setBits(1, 0, 0);
            break;
        case TIME_2_CLIMB_KIDS:
            setBits(1, 0, 1);
            break;
        case CLIMBING:
            setBits(1, 1, 0);
            break;
        case ALL_DONE_NAP_TIME:
            setBits(1, 1, 1);
            break;
        default://OFF
            setBits(0, 0, 0);
            break;
        }
    }
    private void setBits(int i1, int i2, int i3) {
        boolean b1 = (i1 == 1) ? true : false;
        boolean b2 = (i2 == 1) ? true : false;
        boolean b3 = (i3 == 1) ? true : false;
        bit1.set(b1);
        bit2.set(b2);
        bit3.set(b3);
    }


}
