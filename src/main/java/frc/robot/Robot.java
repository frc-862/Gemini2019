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
import frc.lightning.util.FaultMonitor;
import frc.lightning.util.FaultCode.Codes;
import frc.robot.commands.MotionProfile;
import frc.robot.commands.TestMove;
import frc.robot.subsystems.CargoCollector;
import frc.robot.subsystems.Core;
import frc.robot.subsystems.GlitchDrivetrain;
import frc.robot.subsystems.HatchCollector;
import frc.robot.subsystems.OBotDrivetrain;
import frc.robot.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LightningRobot {
  public static Core core = new Core();
  //Drive Trains
  //public static OBotDrivetrain drivetrain = OBotDrivetrain.create();
  public static GlitchDrivetrain drivetrain = GlitchDrivetrain.create();

    //dont really need...
    //public static GlitchDrivetrain glitchDriveTrain = GlitchDrivetrain.create();
  
  //Mechanism Objects
  public static Shooter shooter;// = new Shooter();
  public static HatchCollector collector = new HatchCollector();
  public static CargoCollector cargoCollector = new CargoCollector();

  public static OI oi = new OI();

  public Robot() {
    super();
    System.out.println("Initializing our robot");

    //this.registerAutonomousCommmand(name, command);
    this.registerAutonomousCommmand("T_MotionProfile", new MotionProfile());
    this.registerAutonomousCommmand("T_DriveVelocity", new TestMove());
  }
/*
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
  }
*/
}
