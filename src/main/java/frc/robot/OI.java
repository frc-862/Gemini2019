/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.hatch.HatchCollectorStateChange;
import frc.robot.commands.test.RunTests;
import frc.robot.commands.test.TestMove;

public class OI {
    //Drive Joysticks
    private Joystick driverRight = new Joystick(1);
    private Joystick driverLeft = new Joystick(0);
    private Joystick copilot = new Joystick(2);

    //Buttons
    private Button cargoCollectButton = new JoystickButton(copilot, 4);
    private Button hatchToggle = new JoystickButton(driverRight, JoystickConstants.hatchToggle); 

    public double getLeftPower() {
        return (Math.abs(driverLeft.getRawAxis(JoystickConstants.leftThrottleAxis))>0.05) ? -driverLeft.getRawAxis(JoystickConstants.leftThrottleAxis) : 0.00;
    }

    public double getRightPower() {
        return (Math.abs(driverRight.getRawAxis(JoystickConstants.rightThrottleAxis))>0.05) ? -driverRight.getRawAxis(JoystickConstants.rightThrottleAxis) : 0.00;
    }

    public boolean getCargoCollectButton(){
        return cargoCollectButton.get();
    }

    public OI() {
        //Add Button Command Mapping (Dashboard & Joystick) Here
        //Use whenPressed() and whileHeld()
        hatchToggle.whenPressed(new HatchCollectorStateChange());
        SmartDashboard.putData("TestMove", new TestMove());
        //SmartDashboard.putData("SYSTEM_TESTS", new RunTests());
    }
}
