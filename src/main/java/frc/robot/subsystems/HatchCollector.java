/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class HatchCollector extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    DoubleSolenoid hatchExtend;
    DoubleSolenoid hatchGrabber;
    DigitalInput hatchDetector;

    public HatchCollector() {
        hatchExtend = new DoubleSolenoid(11, 0, 1);// OBOT - 11, 4, 5
        hatchGrabber = new DoubleSolenoid(11, 2, 3); // TODO find correct values
        // constructor - DoubleSolenoid(moduleNumber, forwardChannel, reverseChannel)

        hatchDetector = new DigitalInput(1); // TODO check wiring
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public boolean hatchDetected() {
        return hatchDetector.get();
    }

    public void extendHatchCollector() {
        hatchExtend.set(DoubleSolenoid.Value.kForward);
    }

    public void retractHatchCollector() {
        hatchExtend.set(DoubleSolenoid.Value.kReverse);
    }

    public void grabHatchPanel() {
        hatchGrabber.set(DoubleSolenoid.Value.kForward);
    }

    public void releaseHatchPanel() {
        hatchGrabber.set(DoubleSolenoid.Value.kReverse);
    }
}
