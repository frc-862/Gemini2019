/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class HatchCollector extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    DoubleSolenoid extender, grabber;
    DigitalInput hatchDetector;
    private boolean retracted = true;

    public HatchCollector() {
        extender = new DoubleSolenoid(RobotMap.compressorCANId, RobotMap.extenderFwdChan, RobotMap.extenderRevChan);// OBOT - 11, 4, 5 TODO change these
        addChild("Extender", extender);

        grabber = new DoubleSolenoid(RobotMap.compressorCANId, RobotMap.grabberFwdChan, RobotMap.grabberRevChan);// TODO change these
        addChild("Grabber", grabber);

        // constructor - DoubleSolenoid(moduleNumber, forwardChannel, reverseChannel)
        //hatchDetector = new DigitalInput(RobotMap.hatchDetector); // TODO check wiring
        //addChild("Detector", hatchDetector);
    }

    public void extend() {
        if (retracted) {
            extender.set(DoubleSolenoid.Value.kForward);
        }
        retracted = false;
    }

    private boolean collecting = false;
    public void collect() {
        if (!collecting) {
            grabber.set(DoubleSolenoid.Value.kForward);
        }
        collecting = true;
    }

    public void eject() {
        if (collecting) {
            grabber.set(DoubleSolenoid.Value.kReverse);
        }
        collecting = false;
    }

    public DoubleSolenoid.Value getPosition() {
        return grabber.get();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void retract() {
        if (!retracted) {
            extender.set(DoubleSolenoid.Value.kReverse);
        }
        retracted = true;
    }

    public void toggle() {
        if (!collecting) {
            collect();
        } else {
            eject();
        }
    }
}
