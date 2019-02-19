/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.lightning.LightningRobot;
import frc.lightning.http.JsonReader;
import frc.lightning.testing.FakeOI;
import frc.robot.commands.driveTrain.MotionProfile;
import frc.robot.commands.driveTrain.VelocityMotionProfile;
import frc.robot.paths.ArcSimple;
import frc.robot.paths.LeftArc90;
import frc.robot.paths.LinePath;
import frc.robot.paths.RightArc90;
import frc.robot.paths.RightFarRocket;
import frc.robot.paths.TenFtPath;
import frc.robot.subsystems.*;
import java.io.File;
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

        this.registerAutonomousCommmand("TEST ME NOW", new VelocityMotionProfile(new LinePath()));
        this.registerAutonomousCommmand("oth3r", new VelocityMotionProfile(new LeftArc90()));
        //this.registerAutonomousCommmand("Profile Arc", new MotionProfile(new ArcSimple()));
        //this.registerAutonomousCommmand("Profile 10FT", new MotionProfile(new TenFtPath()));
        //this.registerAutonomousCommmand("BackRight", new MotionProfile(new RightFarRocket()));
        //this.registerAutonomousCommmand("ArcRight90", new MotionProfile(new RightArc90()));
        this.registerAutonomousCommmand("ArcLeft90", new MotionProfile(new LeftArc90()));
        this.registerAutonomousCommmand("Profile Line", new MotionProfile(new LinePath()));

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
