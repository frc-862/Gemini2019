/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

//i dub thee slappy

/**
 * Add your docs here.
 */
public class HatchGroundCollector extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    DoubleSolenoid deployer;

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    public void toggleDeployer() {
        if (deployer.get() == DoubleSolenoid.Value.kForward) {
            deployer.set(DoubleSolenoid.Value.kReverse);
        } else {
            deployer.set(DoubleSolenoid.Value.kForward);
        }
    }
}
