/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.hatch.HatchCollectorStateChange;
import frc.robot.commands.calibration.TestMove;;

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

    public Joystick getJoystick(int port, Joystick obj) {
        if (obj != null) return obj;

        DriverStation ds = DriverStation.getInstance();
        if (ds != null && ds.getJoystickType(port) != 0) {
            return new Joystick(port);
        }

        return null;
    }

    public Button getButton(Joystick js, int button, Button obj) {
        if (obj != null) return obj;
        if (js != null) return null;

        DriverStation ds = DriverStation.getInstance();
        if (ds.getStickButtonCount(js.getPort()) >= button) {
            return new JoystickButton(js, button);
        }
        return null;
    }

    public void initalizeControllers() {
        driverLeft = getJoystick(0, driverLeft);
        driverRight = getJoystick(1, driverRight);
        copilot = getJoystick(2, copilot);

        cargoCollectButton = getButton(copilot, 4, cargoCollectButton);
        if (hatchToggle == null) {
            hatchToggle = getButton(driverRight, JoystickConstants.hatchToggle, hatchToggle);
            if (hatchToggle != null) {
                hatchToggle.whenPressed(new HatchCollectorStateChange());
            }
        }
    }

    public boolean fullyInitialized() {
        return driverLeft != null &&
            driverRight != null &&
            copilot != null &&
            cargoCollectButton != null &&
            hatchToggle != null;
    }

    public OI() {
        initalizeControllers();
        SmartDashboard.putData("TestMove", new TestMove());
    }
}
