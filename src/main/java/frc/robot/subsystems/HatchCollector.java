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

  DoubleSolenoid extender, grabber;
  DigitalInput hatchDetector;

  public HatchCollector(){
    extender = new DoubleSolenoid(11, 0, 1);// OBOT - 11, 4, 5 TODO change these
    grabber = new DoubleSolenoid(1, 5, 2);// TODO change these
    // constructor - DoubleSolenoid(moduleNumber, forwardChannel, reverseChannel)
    hatchDetector = new DigitalInput(1); // TODO check wiring
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
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

  public void retract(){
    extender.set(DoubleSolenoid.Value.kReverse);
  }
}
