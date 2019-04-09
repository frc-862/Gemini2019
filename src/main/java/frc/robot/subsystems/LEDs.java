/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class LEDs extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public enum State {
        //Normal
        OFF,
        STD_BLUE_ORANGE_CHASE,
        AUTONOMOUS,
        END_OF_MATCH_WARNING,
        CLIMBING,
        DRIVER_ASSIST_READY,
        CENTERED,
        READY,

        COUNT
    }

    private boolean[] active = new boolean[State.COUNT.ordinal()];
    private State state;

    private DigitalOutput bit1 = new DigitalOutput(6);
    private DigitalOutput bit2 = new DigitalOutput(7);
    private DigitalOutput bit3 = new DigitalOutput(8);
    private boolean climb = false;

    public LEDs() {
        state = State.STD_BLUE_ORANGE_CHASE;
        active[state.ordinal()] = true;
    }

    public void setState(State new_state) {
        active[new_state.ordinal()] = true;
        if (new_state.ordinal() > state.ordinal()) {
            state = new_state;
            set();
        }
    }

    public void clearState(State old_state) {
        state = State.OFF;
        active[state.ordinal()] = false;
        for(int i = State.COUNT.ordinal() - 1; i > 0; --i) {
            if (active[i]) {
                state = State.values()[i];
                break;
            }
        }
        set();
    }

    @Override
    public void initDefaultCommand() { }

    @Override
    public void periodic() {
        SmartDashboard.putString("LED State", this.state.toString());

        DriverStation ds = DriverStation.getInstance();
        if (ds.getMatchTime() < 30 && ds.isOperatorControl()) {
            Robot.leds.setState(State.END_OF_MATCH_WARNING);
        }
    }

    private void set() {
        int bits = state.ordinal();
        bit1.set((bits & 0x1) == 0x1);
        bit2.set((bits & 0x2) == 0x2);
        bit3.set((bits & 0x4) == 0x4);
    }
}

