/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
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
import frc.lightning.util.TwoButtonTrigger;
import frc.robot.commands.DriverAssist;
import frc.robot.commands.LineFollow;
import frc.robot.commands.SafeDefense;
import frc.robot.commands.auto.HatchAuton;
import frc.robot.commands.calibration.TestMove;
import frc.robot.commands.cargo.DeployCargoCollector;
import frc.robot.commands.cargo.RetractCargoCollector;
import frc.robot.commands.climber.*;
import frc.robot.commands.driveTrain.ConfigMotors;
import frc.robot.commands.hatch.*;
import frc.robot.commands.test.LeftDriveZero;
import frc.robot.commands.test.ResetDriveSensors;
import frc.robot.commands.test.RightDriveZero;
import frc.robot.commands.test.TestManualClimb;
import frc.robot.commands.vision.SimpleFollowVision;
import frc.robot.commands.vision.StereoTurn;
import frc.robot.commands.vision.VisionTurn;
import frc.robot.commands.vision.SimpleFollowVision;

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
    private Button cargoCollectOut= new JoystickButton(copilot,4);//needs changed prob
    private POVButton hatchExtend = new POVButton(copilot, 0);
    private POVButton hatchRetract = new POVButton(copilot, 180);

    public JoystickButton awaitingStart = new JoystickButton(driverRight, 14);

    private Button hatchAuto = new JoystickButton(driverRight, 13);

    //private Button waitButton= new JoystickButton(driverRight, 14);//

    public boolean shouldWait() {
        return false;//waitButton.get();
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
    private JoystickFilter driveFilter = new JoystickFilter(deadBand, minPower, maxPower, JoystickFilter.Mode.SQUARED);
    private JoystickFilter leftFilter = new JoystickFilter(deadBand, minPower, maxPower, JoystickFilter.Mode.SQUARED, 0.1);
    private JoystickFilter rightFilter = new JoystickFilter(deadBand, minPower, maxPower, JoystickFilter.Mode.SQUARED   , 0.1);

    public double getLeftPower() {
        if (driverLeft == null) return 0;
        return leftFilter.filter(-driverLeft.getRawAxis(JoystickConstants.leftThrottleAxis));
    }

    public double getRightPower() {
        if (driverRight == null) return 0;
        return rightFilter.filter(-driverRight.getRawAxis(JoystickConstants.leftThrottleAxis));
    }
    public double getGroundCollectPower() {
        return copilot.getRawAxis(5);
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
        hatchRetract.whenPressed(new RetractHatchCollector());
        hatchExtend.whenPressed(new ExtendHatchCollector());

        //StereoVision Things
        if (Robot.stereoVision != null) {
            //(new JoystickButton(driverRight, 3)).whileHeld(new VisionTurn());//TODO - FIx Buttons
            (new JoystickButton(driverLeft, 14)).whenPressed(new StereoTurn());//TODO - FIx Buttons
            //(new JoystickButton(driverLeft, 15)).whenPressed(new DriveAndAdjust());//TODO - FIx Buttons
        }

        if (Robot.simpleVision != null) {
            (new JoystickButton(driverLeft, 3)).whileHeld(new SimpleFollowVision());
            SmartDashboard.putData("simple vision", new SimpleFollowVision());
        }

        // TODO Fix button numbers
        //(new TwoButtonButton(new JoystickButton(driverLeft, 10),
        //                     new JoystickButton(driverRight, 10))).whenPressed(new StatefulAutoClimb());
        (new JoystickButton(driverLeft, 7)).whenPressed(new LeftDriveZero());
        (new JoystickButton(driverRight, 7)).whenPressed(new RightDriveZero());

        (new JoystickButton(copilot, 5)).whenPressed(new HatchShoot());
        (new JoystickButton(copilot, 6)).whenPressed(new SafeDefense());
        //(new JoystickButton(copilot, 6)).whenPressed(new InstantCommand(Robot.hatchPanelCollector, () -> Robot.hatchPanelCollector.collect()));

        (new JoystickButton(driverLeft, 1)).whenPressed(new SmartHatchCollect());

        (new JoystickButton(driverRight, 1)).whileHeld(new DriverAssist());
        (new JoystickButton(driverRight, 4)).whileHeld(new LineFollow());
        (new JoystickButton(driverRight, 3)).whileHeld(new SimpleFollowVision());

        (new JoystickButton(copilot, JoystickConstants.highButton)).            whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToHigh()));
        (new JoystickButton(copilot, JoystickConstants.midButton)).             whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToMid()));
        (new JoystickButton(copilot, JoystickConstants.elevatorCollectButton)). whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToCollect()));
        (new JoystickButton(copilot, JoystickConstants.lowButton)).             whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToLow()));
        (new JoystickButton(copilot, JoystickConstants.bottomButton)).          whenPressed(new InstantCommand(Robot.elevator, () -> Robot.elevator.goToBottom()));
        (new JoystickButton(driverRight, 11)).whenPressed(new JankStatefulClimb());
        (new JoystickButton(driverLeft, 11)).whenPressed(new ClimbGoToPosition());


        (new JoystickButton(copilot, JoystickConstants.highButton)).            whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.midButton)).             whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.elevatorCollectButton)). whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.lowButton)).             whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));
        (new JoystickButton(copilot, JoystickConstants.bottomButton)).          whileHeld(new InstantCommand(() -> Robot.elevator.resetTimer()));

    }

    public double getCargoCollectPower () {
        return (copilot.getRawAxis(3)-copilot.getRawAxis(2))/3;
    }

    public OI() {
        leftFilter.setRampDelta(0.1);
        rightFilter.setRampDelta(0.1);
        initializeCommands();

        SmartDashboard.putData("Stateful Hatch Deploy", new StatefulHatchDeploy());
        SmartDashboard.putData("Shoot Hatch", new HatchShoot());

        SmartDashboard.putData("open hatch", new OpenHatchCollector());
        SmartDashboard.putData("test manual climb", new TestManualClimb());
        SmartDashboard.putData("close hatch", new CloseHatchCollector());

        SmartDashboard.putData("config motors", new ConfigMotors());
        SmartDashboard.putData("line follow", new LineFollow());
        SmartDashboard.putData("test move", new TestMove());
        SmartDashboard.putData("Left Near Rocket", new VelocityMotionProfile("src/main/deploy/paths/LeftNearRocket"));
        SmartDashboard.putData("Auto Climb Hab 3",new JankStatefulClimb());
        SmartDashboard.putData("Auto Climb Hab 2",new JankHabTwoClimb());
        SmartDashboard.putData("RESET_SENSORS", new ResetDriveSensors());

        SmartDashboard.putData("Elevator to collect",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToCollect()));
        SmartDashboard.putData("Elevator to low",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToLow()));
        SmartDashboard.putData("Elevator to mid",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToMid()));
        SmartDashboard.putData("Elevator to high",
                               new InstantCommand(Robot.elevator, () -> Robot.elevator.goToHigh()));
        SmartDashboard.putData(new ClimbGoToPosition());
        SmartDashboard.putData(new AutoClimb());
        SmartDashboard.putData(new SafeDefense());

        SmartDashboard.putData("CLimber To Zero", new InstantCommand(Robot.climber, () -> Robot.climber.goToPos(0)));

        //Vision Command
        //SmartDashboard.putData("VisionTurn", new VisionTurn());
        if (Robot.stereoVision != null) {
            SmartDashboard.putData("StereoTurn", new StereoTurn());
        }

        SmartDashboard.putData("lift drive forward", new driveforward());
        SmartDashboard.putData("manual climb", new ManualClimb());
        SmartDashboard.putData("Extend Shocks",new ExtendShocks());
        SmartDashboard.putData("Retract Shocks", new RetractShocks());
        SmartDashboard.putData("Climb to height", new ClimbToHeight());
    }

    public double groundCollectPwr() {
        return copilot.getRawAxis(5);
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

    public boolean getLeftTrigger() {
        return driverLeft.getRawButton(1);
    }

    public boolean getRightTrigger() {
        return !driverRight.getRawButton(1);
    }

    public void rummbleOn() {
        copilot.setRumble(GenericHID.RumbleType.kLeftRumble,1);
        copilot.setRumble(GenericHID.RumbleType.kRightRumble,1);
    }
    public void rummbleOff() {
        copilot.setRumble(GenericHID.RumbleType.kLeftRumble,0);
        copilot.setRumble(GenericHID.RumbleType.kRightRumble,0);
    }
}
