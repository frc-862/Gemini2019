/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.lightning.LightningRobot;
import frc.lightning.commands.VelocityMotionProfile;
import frc.robot.subsystems.*;
import java.io.File;

import edu.wpi.first.cameraserver.CameraServer;

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
        oi = new OI();

        // set a flag, it is either gemini or nebula
        gemini = ((new File("/home/lvuser/gemini")).exists());

        registerAutonomousCommmand("Straight", new VelocityMotionProfile("straight"));
        registerAutonomousCommmand("LeftSideNear", new VelocityMotionProfile("left_side_near"));
        registerAutonomousCommmand("RightSideNear", new VelocityMotionProfile("right_side_near"));
        registerAutonomousCommmand("Test Left Turn", new VelocityMotionProfile("test_left_turn"));
        registerAutonomousCommmand("Test Right Turn", new VelocityMotionProfile("test_right_turn"));

        //CameraServer.getInstance().startAutomaticCapture();
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
    }

    public static boolean isGemini() {
        return gemini;
    }

    public static boolean isNebula() {
        return !gemini;
    }
}
