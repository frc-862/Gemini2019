/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */

enum State {
    //normal chasers
    NORMAL,

    //game piece/collecting-related
    CARGO_COLLECTED, HATCH_COLLECTED, HAND_OFF_READY,

    // Indicates that we have detected
    // the vision or line targets and
    // the driver can give up control
    VISION_READY, LINE_FOLLOW_READY,

    //other stuff
    LINE_FOLLOWING, /*SANDSTORM*/
}

public class LEDs extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void set(State state) {
        // TODO set light color based on state
    }
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void setYellow(){
        //TODO: Make actually change color

    }

    public void setGreen() {
        //TODO: Make actually change color
    }
    
}
