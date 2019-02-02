/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.LightningRobot;
import frc.lightning.subsystems.CANDrivetrain;
import frc.lightning.subsystems.LightningDrivetrain;
import frc.lightning.util.FaultMonitor;
import frc.lightning.util.FaultCode.Codes;
import frc.robot.commands.driveTrain.MotionProfile;
import frc.robot.commands.test.TestMove;
import frc.robot.subsystems.*;

import java.io.File;

public class Robot extends LightningRobot {

    //Subsystems
    public static CANDrivetrain drivetrain;
    public static Core core;
    public static LEDs leds;
    public static HatchCollector hatchPanelCollector;
    public static CargoCollector cargoCollector;
    public static HatchGroundCollector hatchGroundCollector;
    public static Elevator elevator;
    public static OI oi;

    public Robot() {
        super();
        System.out.println("Initializing our robot");
        //Create Things
        if(isOBot()){
            drivetrain = OBotDrivetrain.create();
        }else if(isGlitch()){
            drivetrain = GlitchDrivetrain.create();
            hatchPanelCollector = new HatchCollector();
        }else if (isGemini()){
            drivetrain = GeminiDrivetrain.create();
            leds = new LEDs();
            hatchGroundCollector = new HatchGroundCollector();
            hatchPanelCollector = new HatchCollector();
            cargoCollector = CargoCollector.create();
            elevator = new Elevator();
        }else if (isFlash()){
            elevator = new Elevator();
        }
        core = new Core();
        oi = new OI();
        //this.registerAutonomousCommmand(name, command);
        this.registerAutonomousCommmand("T_MotionProfile", new MotionProfile());
    }

    //Drive Train Chooser
    public static boolean isOBot() {
        return (new File("/home/lvuser/obot")).exists();
    }
    public static boolean isGlitch() {
        return (new File("/home/lvuser/glitch")).exists();
    }
    public static boolean isGemini() {
        return (new File("/home/lvuser/gemini")).exists();//TODO make file on robot
    }
    public static boolean isFlash() {
        return (new File("/home/lvuser/flash")).exists();
    }

}
