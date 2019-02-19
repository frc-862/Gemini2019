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
import frc.lightning.util.JoystickFilter;
import frc.robot.commands.hatch.HatchCollectorStateChange;
import frc.robot.commands.LineFollow;
import frc.robot.commands.calibration.TestMove;
import frc.robot.commands.climber.Climb;
import frc.robot.commands.driveTrain.ConfigMotors;
import frc.robot.commands.cargo.DeployCargoCollector;
import frc.robot.commands.cargo.RetractCargoCollector;

public class OI {
    //Drive Joysticks
    private Joystick driverLeft = new Joystick(0);
    private Joystick driverRight = new Joystick(1);
    private Joystick copilot = new Joystick(2);

    //Mechanism Buttons
    private Button cargoCollectButton = new JoystickButton(copilot, 6);
    private Button setElevatorHigh = new JoystickButton(copilot, 3);
    private Button setElevatorLow = new JoystickButton(copilot, 11);
    private Button setElevatorMid = new JoystickButton(copilot, 8);

    // TODO - duplicated button number
    private Button setElevatorCargoCollect = new JoystickButton(copilot, 8);
    private Button hatchToggle = new JoystickButton(driverRight, JoystickConstants.hatchToggle);
    private Button climb = new JoystickButton(copilot, 10);
    private Button cargoCollectIn= new JoystickButton(copilot,5);//needs changed prob
    private Button cargoCollectOut= new JoystickButton(copilot,4);//needs changed prob

    public boolean getElevatorHighPosSelect() {
        return setElevatorHigh != null &&
               setElevatorHigh.get();
    }

    public void cargoCollectOut () {
        cargoCollectOut.whenPressed(new DeployCargoCollector());
    }
    public void cargoCollectIn  () {
        cargoCollectOut.whenPressed(new RetractCargoCollector());
    }

    public boolean getElevatorMidPosSelect() {
        return setElevatorMid != null &&
               setElevatorMid.get();
    }

    public boolean getElevatorLowPosSelect() {
        return setElevatorLow != null &&
               setElevatorLow.get();
    }
    public double manualElevatorPwr() {
        return (driverRight.getRawAxis(3) - 1) / -2;
    }
    public double manualElevatorDownPwr() {
        return (driverLeft.getRawAxis(3) - 1) / 2;
    }
    public boolean getElevatorCargoCollectPosSelect() {
        return setElevatorCargoCollect != null &&
               setElevatorCargoCollect.get();
    }

    private final double deadBand = 0.05;
    private final double minPower = 0.1;
    private final double maxPower = 1.0;
    private JoystickFilter driveFilter = new JoystickFilter(deadBand, minPower, maxPower, JoystickFilter.Mode.CUBED);

    public double getLeftPower() {
        if (driverLeft == null) return 0;
        return driveFilter.filter(driverLeft.getRawAxis(JoystickConstants.leftThrottleAxis));
    }

    public double getRightPower() {
        if (driverRight == null) return 0;
        return driveFilter.filter(driverRight.getRawAxis(JoystickConstants.leftThrottleAxis));
    }

    public boolean getCargoCollectButton() {
        return cargoCollectButton != null &&
               cargoCollectButton.get();
    }

    public void initializeCommands() {
        climb.whenPressed(new Climb());
        hatchToggle.whenPressed(new HatchCollectorStateChange());
    }

    public double getCargoCollectPower () {
        return copilot.getRawAxis(2)-copilot.getRawAxis(3);
    }

    public OI() {
        initializeCommands();

        SmartDashboard.putData("config motors", new ConfigMotors());
        SmartDashboard.putData("line follow", new LineFollow());
        SmartDashboard.putData("test move", new TestMove());
    }
}
