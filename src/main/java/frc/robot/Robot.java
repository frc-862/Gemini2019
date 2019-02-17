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
import frc.robot.subsystems.Vision;
import frc.lightning.util.FaultMonitor;
import frc.lightning.util.FaultCode.Codes;
import frc.robot.commands.test.TestMove;
import frc.robot.subsystems.*;

import java.io.File;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LightningRobot {
    public static Core core = new Core();
    public static LEDs leds = new LEDs();

    //Drive Train Chooser
    public static boolean isOBot() {
        return new File("/home/lvuser/obot").exists();
    }
    public static boolean isGlitch() {
        return new File("/home/lvuser/glitch").exists();
    }
    public static OBotDrivetrain drivetrain = OBotDrivetrain.create();
    //public static GlitchDrivetrain drivetrain = GlitchDrivetrain.create();
    //public static GeminiDrivetrain drivetrain = GeminiDrivetrain.create();

    //public static SiriusDrivetrain drivetrain = SiriusDrivetrain.create();
    public static Vision vision = new Vision();
    //Mechanism Objects
    public static HatchCollector hatchPanelCollector;// = new hatch();
    public static CargoCollector cargoCollector;// = new cargo();
    public static HatchGroundCollector hatchGroundCollector;// = new HatchGroundCollector();
    public static Elevator elevator;// = new Elevator();
    public static OI oi = new OI();

    public Robot() {
        super();
        System.out.println("Initializing our robot");
        //if(isOBot){
        //    
        //}
        //this.registerAutonomousCommmand(name, command);
        this.registerAutonomousCommmand("T_DriveVelocity", new TestMove());

    }
}
