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
import frc.robot.commands.LineFollow;
import frc.robot.commands.calibration.TestMove;
import frc.robot.commands.climber.Climb;;

public class OI {
    //Drive Joysticks
    private Joystick driverRight = new Joystick(1);
    private Joystick driverLeft = new Joystick(0);
    private Joystick copilot = new Joystick(2);

    //Mechanism Buttons
    private Button cargoCollectButton;
    private Button setElevatorHigh;
    private Button setElevatorLow;
    private Button setElevatorMid;
    private Button setElevatorCargoCollect;
    private Button hatchToggle;
    private Button climb;

    public boolean getElevatorHighPosSelect() {
        return setElevatorHigh != null &&
               setElevatorHigh.get();
    }

    public boolean getElevatorMidPosSelect() {
        return setElevatorMid != null &&
               setElevatorMid.get();
    }

    public boolean getElevatorLowPosSelect() {
        return setElevatorLow != null &&
               setElevatorLow.get();
    }

    public boolean getElevatorCargoCollectPosSelect() {
        return setElevatorCargoCollect != null &&
               setElevatorCargoCollect.get();
    }

    public double getLeftPower() {
        if (driverLeft == null) return 0;

        return (Math.abs(driverLeft.getRawAxis(JoystickConstants.leftThrottleAxis))>0.05) ? -driverLeft.getRawAxis(JoystickConstants.leftThrottleAxis) : 0.00;
    }

    public double getRightPower() {
        if (driverRight == null) return 0;
        return (Math.abs(driverRight.getRawAxis(JoystickConstants.rightThrottleAxis))>0.05) ? -driverRight.getRawAxis(JoystickConstants.rightThrottleAxis) : 0.00;
    }

    public boolean getCargoCollectButton() {
        return cargoCollectButton != null &&
               cargoCollectButton.get();
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

    }

    public boolean fullyInitialized() {
        return driverLeft != null &&
               driverRight != null &&
               copilot != null &&
               cargoCollectButton != null &&
               hatchToggle != null;
    }

    public void initializeButtons() {
        setElevatorHigh = new JoystickButton(copilot, 3);
        setElevatorLow = new JoystickButton(copilot, 6);
        setElevatorMid = new JoystickButton(copilot, 8);
        setElevatorCargoCollect = new JoystickButton(copilot, 8);

        climb = new JoystickButton(copilot, 5);
        climb.whenPressed(new Climb());

        cargoCollectButton = getButton(copilot, 4, cargoCollectButton);

        hatchToggle = getButton(driverRight, JoystickConstants.hatchToggle, hatchToggle);
        hatchToggle.whenPressed(new HatchCollectorStateChange());
    }

    public OI() {
        if (!Constants.bench_test) {
            initalizeControllers();
            initializeButtons();
        }

        SmartDashboard.putData("line follow", new LineFollow());
        SmartDashboard.putData("test move", new TestMove());
    }
}
