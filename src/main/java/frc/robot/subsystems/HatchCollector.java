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
<<<<<<< Updated upstream
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
=======
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  DoubleSolenoid extender, grabber;

  public HatchCollector(){
    extender = new DoubleSolenoid(11, 0, 1);// OBOT - 11, 4, 5 TODO change these
    grabber = new DoubleSolenoid(1, 5, 2);// TODO change these
    // constructor - DoubleSolenoid(moduleNumber, forwardChannel, reverseChannel)
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void extend(){
    extender.set(DoubleSolenoid.Value.kForward);
  }
  public void open(){
    grabber.set(DoubleSolenoid.Value.kForward);
  }
  public void close(){
    grabber.set(DoubleSolenoid.Value.kReverse);
  }
  public void toggleExtenderState(){
    DoubleSolenoid.Value state = extender.get();
    if(state == Value.kForward){
      retract();
    }else{
      extend();
>>>>>>> Stashed changes
    }

<<<<<<< Updated upstream
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
=======
  public void retract(){
    extender.set(DoubleSolenoid.Value.kReverse);
  }
>>>>>>> Stashed changes

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
