/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.LightningRobot;
import frc.lightning.commands.VelocityMotionProfile;
import frc.robot.commands.auto.CenterPath;
import frc.robot.commands.auto.HatchAuton;
import frc.robot.commands.auto.LeftRocket;
import frc.robot.commands.auto.RghtRocket;
import frc.robot.commands.driveTrain.VelocityTankDrive;
import frc.robot.commands.elevator.SetElevatorCollect;
import frc.robot.commands.elevator.SetElevatorHigh;
import frc.robot.commands.elevator.SetElevatorLow;
import frc.robot.commands.elevator.SetElevatorMed;
import frc.robot.misc.AutonSelect;
import frc.robot.subsystems.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Robot extends LightningRobot {

    // Subsystems
    public static GeminiDrivetrain drivetrain;
    public static Core core;
    public static LEDs leds;
    public static HatchCollector hatchPanelCollector;
    public static CargoCollector cargoCollector;
    public static HatchGroundCollector hatchGroundCollector;
    public static Elevator elevator;
    public static Climber climber;
    public static Vision vision;
    public static OI oi;
    public static AutonSelect autoSel;

    private static boolean gemini = true;

    public Robot() {
        super();
        System.out.println("Initializing our robot");

        drivetrain = GeminiDrivetrain.create();
        leds = new LEDs();
        hatchGroundCollector = new HatchGroundCollector();
        hatchPanelCollector = new HatchCollector();
        cargoCollector = CargoCollector.create();
        elevator = new Elevator();
        climber = new Climber();
        core = new Core();
        vision = new Vision();
        autoSel = new AutonSelect();// If not good w/ static selectors in LightningRobot, remove static and pass as param

        oi = new OI();

        // set a flag, it is either gemini or nebula
        gemini = ((new File("/home/lvuser/gemini")).exists());

        registerAutoOptions();

        registerAutonomousCommmand("Tank", new VelocityTankDrive());
        registerAutonomousCommmand("Left Rocket", new LeftRocket());
        registerAutonomousCommmand("Right Rocket", new RghtRocket());
        registerAutonomousCommmand("Center Ship", new CenterPath());

        registerAutonomousCommmand("Hatch Auto", new HatchAuton(autoSel.getSelectedPath(), getSelectedElevatorPos(), false));

        // registerAutonomousCommmand("AUTON", new Auto(this.getSelectedElevatorPos(),
        //                            this.getSelectedGamePiece(),
        //                            this.getSelectedStartPos(),
        //                            this.getSelectedGenDestin(),
        //                            this.getSelectedSpecificDestin(),false));

        // //TEST
        // registerAutonomousCommmand("DO THIS NOW NOLAN!", new Auto(new SetElevatorLow(), "test_left_turn", "HATCH", false));
        // registerAutonomousCommmand("RightSideNearR", new VelocityMotionProfile("RocketR_StartR_EndN"));
        // registerAutonomousCommmand("Spin", new Spin());
        // registerAutonomousCommmand("LeftNearLow", new HatchAuton("left_side_near", new SetElevatorLow()));
        // registerAutonomousCommmand("Circle", new VelocityMotionProfile("circle"));
        // registerAutonomousCommmand("Straight", new VelocityMotionProfile("straight"));
        // registerAutonomousCommmand("LeftSideNear", new VelocityMotionProfile("left_side_near"));
        // registerAutonomousCommmand("RightSideNear", new VelocityMotionProfile("right_side_near"));
        registerAutonomousCommmand("Test Left Turn", new VelocityMotionProfile("test_left_turn"));
        registerAutonomousCommmand("Test Right Turn", new VelocityMotionProfile("test_right_turn"));
        // registerAutonomousCommmand("RightSideNearM", new VelocityMotionProfile("RocketR_StartM_EndN"));


        CameraServer.getInstance().startAutomaticCapture();
    }

    @Override
    public void robotPeriodic() {
        super.robotPeriodic();
        //leds.update();
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();
        try {
            writeFile(Scheduler.getInstance().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String toWrite) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(Filesystem.getDeployDirectory().getAbsolutePath()+"auto_commands.txt"));
        writer.write(toWrite);
        writer.close();
    }

    private void registerAutoOptions() {
        //Elevator Positions
        registerElevatorPos("Low", new SetElevatorLow());
        registerElevatorPos("Top", new SetElevatorHigh());
        registerElevatorPos("Middle", new SetElevatorMed());
        registerElevatorPos("Cargo", new SetElevatorCollect());
        //Game Pieces
        registerGamePiece("Hatch Pannel", "HATCH");
        registerGamePiece("Cargo", "CARGO");
        //Start Positions
        registerStartPos("Left", "LEFT");
        registerStartPos("Right", "RIGHT");
        registerStartPos("Center", "CENTER");
        //General Destinations
        registerGenDestin("Left Rocket", "ROCK_LEFT");
        registerGenDestin("Right Rocket", "ROCK_RIGHT");
        registerGenDestin("Cargo Ship Front", "SHIP_FRONT");
        registerGenDestin("Cargo Ship Right", "SHIP_RIGHT");
        registerGenDestin("Cargo Ship Left", "SHIP_LEFT");
        //Specific Destinations
        registerSpecificDestin("Near", "NEAR");
        registerSpecificDestin("Far", "FAR");
        registerSpecificDestin("Middle", "MID");
        registerSpecificDestin("Right", "RIGHT");
        registerSpecificDestin("Left", "LEFT");
    }

    @Override
    public void robotInit() {
        super.robotInit();

//        try {
//            var devices = JsonReader.readJsonFromUrl("http://127.0.0.1:1250/?action=getdevices");
//            System.out.println(devices);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void robotMediumPriorityPeriodic() {
        super.robotMediumPriorityPeriodic();
        SmartDashboard.putBoolean("gemini",gemini);
        SmartDashboard.putBoolean("is gemini", isGemini());
    }

    public static boolean isGemini() {
        return gemini;
    }

    public static boolean isNebula() {
        return !gemini;
    }
}
