/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.LightningRobot;
import frc.robot.commands.auto.CenterPath;
import frc.robot.commands.auto.LeftRocket;
import frc.robot.commands.auto.RghtRocket;
import frc.robot.commands.driveTrain.VelocityTankDrive;
import frc.robot.commands.elevator.SetElevatorCollect;
import frc.robot.commands.elevator.SetElevatorHigh;
import frc.robot.commands.elevator.SetElevatorLow;
import frc.robot.commands.elevator.SetElevatorMed;
import frc.robot.subsystems.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Robot extends LightningRobot {

    //Subsystems
    public static GeminiDrivetrain drivetrain;
    public static Core core;
    public static LEDs leds;
    public static HatchCollector hatchPanelCollector;
    public static CargoCollector cargoCollector;
    public static HatchGroundCollector hatchGroundCollector;
    public static Elevator elevator;
    public static Climber climber;
//    public static Vision vision;
    public static SimpleVision vision;
    public static OI oi;

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
        // vision = new Vision();
        vision = new SimpleVision(SerialPort.Port.kUSB);

        oi = new OI();

        // set a flag, it is either gemini or nebula
        gemini = ((new File("/home/lvuser/gemini")).exists());

        registerAutoOptions();

        registerAutonomousCommmand("Tank", new VelocityTankDrive());
        registerAutonomousCommmand("Left Rocket",new LeftRocket());
        registerAutonomousCommmand("Right Rocket",new RghtRocket());
        registerAutonomousCommmand("Center Ship",new CenterPath());

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
        // registerAutonomousCommmand("Test Left Turn", new VelocityMotionProfile("test_left_turn"));
        // registerAutonomousCommmand("Test Right Turn", new VelocityMotionProfile("test_right_turn"));
        // registerAutonomousCommmand("RightSideNearM", new VelocityMotionProfile("RocketR_StartM_EndN"));


//        var visionCam = new UsbCamera("VisionProcCam", 0);
//        visionCam.setVideoMode(VideoMode.PixelFormat.kYUYV, 320, 254, 50);
//        var camServer = new MjpegServer("VisionCamServer", MJPG_STREAM_PORT);
//        camServer.setSource(visionCam);
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
        this.registerElevatorPos("Low", new SetElevatorLow());
        this.registerElevatorPos("Top", new SetElevatorHigh());
        this.registerElevatorPos("Middle", new SetElevatorMed());
        this.registerElevatorPos("Cargo", new SetElevatorCollect());
        //Game Pieces
        this.registerGamePiece("Hatch Pannel", "HATCH");
        this.registerGamePiece("Cargo", "CARGO");
        //Start Positions
        this.registerStartPos("Left", "LEFT");
        this.registerStartPos("Right", "RIGHT");
        this.registerStartPos("Center", "CENTER");
        //General Destinations
        this.registerGenDestin("Left Rocket", "ROCK_LEFT");
        this.registerGenDestin("Right Rocket", "ROCK_RIGHT");
        this.registerGenDestin("Cargo Ship Front", "SHIP_FRONT");
        this.registerGenDestin("Cargo Ship Right", "SHIP_RIGHT");
        this.registerGenDestin("Cargo Ship Left", "SHIP_LEFT");
        //Specific Destinations
        this.registerSpecificDestin("Near", "NEAR");
        this.registerSpecificDestin("Far", "FAR");
        this.registerSpecificDestin("Middle", "MID");
        this.registerSpecificDestin("Right", "RIGHT");
        this.registerSpecificDestin("Left", "LEFT");
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
