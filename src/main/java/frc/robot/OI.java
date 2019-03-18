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
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.commands.ToggleCommand;
import frc.lightning.commands.VelocityMotionProfile;
import frc.lightning.util.JoystickFilter;
import frc.robot.commands.LineFollow;
import frc.robot.commands.auto.HatchAuton;
import frc.robot.commands.calibration.TestMove;
import frc.robot.commands.cargo.DeployCargoCollector;
import frc.robot.commands.cargo.RetractCargoCollector;
import frc.robot.commands.climber.AutoClimb;
import frc.robot.commands.climber.Climb;
import frc.robot.commands.climber.ExtendShocks;
import frc.robot.commands.climber.ClimbGoToPosition;
import frc.robot.commands.climber.ManualClimb;
import frc.robot.commands.climber.RetractShocks;
import frc.robot.commands.climber.StatefulAutoClimb;
import frc.robot.commands.climber.driveforward;
import frc.robot.commands.driveTrain.ConfigMotors;
import frc.robot.commands.hatch.CloseHatchCollector;
import frc.robot.commands.hatch.ExtendHatchCollector;
import frc.robot.commands.hatch.OpenHatchCollector;
import frc.robot.commands.hatch.RetractHatchCollector;
import frc.robot.commands.test.LeftDriveZero;
import frc.robot.commands.test.ResetDriveSensors;
import frc.robot.commands.test.RightDriveZero;
import frc.robot.commands.vision.StereoTurn;
import frc.robot.commands.vision.VisionTurn2;

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
    private Button setShocksout = new JoystickButton(driverRight, 8);

    private Button setElevatorCargoCollect = new JoystickButton(copilot, 8);
    private Button climb = new JoystickButton(copilot, 10);
    private Button cargoCollectOut= new JoystickButton(copilot,4);//needs changed prob
    private POVButton hatchExtend = new POVButton(copilot, 0);
    private POVButton hatchRetract = new POVButton(copilot, 180);

    private Button hatchAuto = new JoystickButton(driverRight, 13);

    private Button waitButton= new JoystickButton(driverRight, 14);//

    public boolean shouldWait() {
        return waitButton.get();
    }

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

    public void setShocksout  () {
        setShocksout.whenPressed(new ExtendShocks());
    }
    public void hatchAuto (String start, String genDestin, String specificDestin, Command elevatorPos) {
        hatchAuto.whenPressed(new HatchAuton(this.selectPath(start, genDestin, specificDestin), elevatorPos));
    }
    public int getLeftDirection() {
        if(copilot.getRawAxis(JoystickConstants.leftJoyYAxis) > 0) return 1;
        else if(copilot.getRawAxis(JoystickConstants.leftJoyYAxis) < 0) return -1;
        else return 0;
    }

    public double getCopilotLeft() {
        return -driveFilter.filter(copilot.getRawAxis(JoystickConstants.leftJoyYAxis));
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

    private final double deadBand = 0.10;
    private final double minPower = 0.1;
    private final double maxPower = 1.0;
    private JoystickFilter driveFilter = new JoystickFilter(deadBand, minPower, maxPower, JoystickFilter.Mode.CUBED);
    private JoystickFilter leftFilter = new JoystickFilter(deadBand, minPower, maxPower, JoystickFilter.Mode.CUBED);
    private JoystickFilter rightFilter = new JoystickFilter(deadBand, minPower, maxPower, JoystickFilter.Mode.CUBED);

    public double getLeftPower() {
        if (driverLeft == null) return 0;
        return leftFilter.filter(-driverLeft.getRawAxis(JoystickConstants.leftThrottleAxis));
    }

    public double getRightPower() {
        if (driverRight == null) return 0;
        return rightFilter.filter(-driverRight.getRawAxis(JoystickConstants.leftThrottleAxis));
    }

    public double getMicroAdjAmt() {
        if(copilot == null) return 0.0;
        return driveFilter.filter(copilot.getRawAxis(JoystickConstants.leftJoyYAxis));
    }

    public boolean getCargoCollectButton() {
        return cargoCollectButton != null &&
               cargoCollectButton.get();
    }

    private Button rainbowButton = new JoystickButton(driverRight, 2);
    public boolean getRainbowButtonPressed() {
        return rainbowButton.get();
    }

    public void initializeCommands() {
        climb.whenPressed(new Climb());
        hatchRetract.whenPressed(new RetractHatchCollector());
        hatchExtend.whenPressed(new ExtendHatchCollector());

        //Vision Things
        (new JoystickButton(driverLeft, 13)).whenPressed(new VisionTurn2());//TODO - FIx Buttons
        (new JoystickButton(driverLeft, 14)).whenPressed(new StereoTurn());//TODO - FIx Buttons
        //(new JoystickButton(driverLeft, 15)).whenPressed(new DriveAndAdjust());//TODO - FIx Buttons

        (new JoystickButton(driverLeft, 7)).whenPressed(new LeftDriveZero());
        (new JoystickButton(driverRight, 7)).whenPressed(new RightDriveZero());

        (new JoystickButton(copilot, 5)).whenPressed(new InstantCommand(Robot.hatchPanelCollector, () -> Robot.hatchPanelCollector.collect()));
        (new JoystickButton(copilot, 6)).whenPressed(new InstantCommand(Robot.hatchPanelCollector, () -> Robot.hatchPanelCollector.eject()));

        (new JoystickButton(driverLeft, 1)).whenPressed(new ToggleCommand(new InstantCommand(Robot.hatchPanelCollector, () -> Robot.hatchPanelCollector.collect()),
                new InstantCommand(Robot.hatchPanelCollector, () -> Robot.hatchPanelCollector.eject())));

        (new JoystickButton(driverRight, 1)).whileHeld(new LineFollow());

        (new JoystickButton(copilot, JoystickConstants.highButton)).            whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToHigh()));
        (new JoystickButton(copilot, JoystickConstants.midButton)).             whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToMid()));
        (new JoystickButton(copilot, JoystickConstants.elevatorCollectButton)). whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToCollect()));
        (new JoystickButton(copilot, JoystickConstants.lowButton)).             whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToLow()));
        (new JoystickButton(copilot, JoystickConstants.bottomButton)).          whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToBottom()));

        (new JoystickButton(copilot, JoystickConstants.highButton)).            whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.midButton)).             whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.elevatorCollectButton)). whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.lowButton)).             whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.bottomButton)).          whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));

    }

    public double getCargoCollectPower () {
        return (copilot.getRawAxis(2)-copilot.getRawAxis(3))/3;
    }

    public OI() {
        leftFilter.setRampDelta(0.1);
        rightFilter.setRampDelta(0.1);
        initializeCommands();

        SmartDashboard.putData("open hatch", new OpenHatchCollector());
        SmartDashboard.putData("close hatch", new CloseHatchCollector());

        SmartDashboard.putData("config motors", new ConfigMotors());
        SmartDashboard.putData("line follow", new LineFollow());
        SmartDashboard.putData("test move", new TestMove());
        SmartDashboard.putData("Left Near Rocket", new VelocityMotionProfile("src/main/deploy/paths/LeftNearRocket"));

        SmartDashboard.putData("RESET_SENSORS", new ResetDriveSensors());

        SmartDashboard.putData("Elevator to collect",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToCollect()));
        SmartDashboard.putData("Elevator to low",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToLow()));
        SmartDashboard.putData("Elevator to mid",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToMid()));
        SmartDashboard.putData("Elevator to high",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToHigh()));
        SmartDashboard.putData(new StatefulAutoClimb());
        SmartDashboard.putData(new ClimbGoToPosition());
        SmartDashboard.putData(new AutoClimb());



        SmartDashboard.putData("lift drive forward", new driveforward());
        SmartDashboard.putData("manual climb", new ManualClimb());
        SmartDashboard.putData("Extend Shocks",new ExtendShocks());
        SmartDashboard.putData("Retract Shocks", new RetractShocks());
    }

    public double forwardClimbForwardPwr() {
        return -copilot.getRawAxis(5);
    }

    public double manualClimbPower() {
        return ((driverRight.getRawAxis(3) - 1) / -2)+((driverLeft.getRawAxis(3) - 1) / 2);
    }

    private String selectPath(String start, String genDestin, String specificDestin) {
        String selectedPath = "";//default left rocket near
        switch(genDestin) {
        case "ROCK_RIGHT":
            selectedPath += ("RocketR_" + selectStart(start, specificDestin));
            break;
        case "SHIP_FRONT":
            selectedPath += ("CargoC_" + selectStart(start, specificDestin));
            break;
        case "SHIP_LEFT":
            selectedPath += ("CargoL_" + selectStart(start, specificDestin));
            break;
        case "SHIP_RIGHT":
            selectedPath += ("CargoR_" + selectStart(start, specificDestin));
            break;
        default://ROCK_LEFT
            selectedPath += ("RocketL_" + selectStart(start, specificDestin));
            break;
        }
        return selectedPath;
    }

    private String selectStart(String start, String specificDestin) {
        String nextSeg = "";
        switch(start) {
        case "CENTER":
            nextSeg += ("StartM_" + selectDestin(specificDestin));
            break;
        case "RIGHT":
            nextSeg += ("StartR_" + selectDestin(specificDestin));
            break;
        default://LEFT
            nextSeg += ("StartL_" + selectDestin(specificDestin));
            break;
        }
        return nextSeg;
    }

    private String selectDestin(String specificDestin) {
        String nextSeg = "";
        switch(specificDestin) {
        case "FAR":
            nextSeg += ("EndF");
            break;
        case "RIGHT":
            nextSeg += ("EndR");
            break;
        case "LEFT":
            nextSeg += ("EndL");
            break;
        case "MID":
            nextSeg += ("EndM");
            break;
        default://NEAR
            nextSeg += ("EndN");
            break;
        }
        return nextSeg;
    }
}
