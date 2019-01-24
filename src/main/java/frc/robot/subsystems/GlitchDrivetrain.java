/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.subsystems.CANDrivetrain;
import frc.lightning.util.LightningMath;
import frc.lightning.util.MotorConfig;
import frc.robot.Robot;
import frc.robot.commands.TankDrive;
import frc.robot.misc.Gains;

/**
 * Add your docs here.
 */
public class GlitchDrivetrain extends CANDrivetrain {  
  WPI_VictorSPX leftFollow1;
  WPI_VictorSPX leftFollow2;

  WPI_VictorSPX rightFollow1;
  WPI_VictorSPX rightFollow2;

  public static GlitchDrivetrain create() {
    return new GlitchDrivetrain(
      new WPI_TalonSRX(1), 
      new WPI_VictorSPX(2), 
      new WPI_VictorSPX(3),//
      new WPI_TalonSRX(4),
      new WPI_VictorSPX(5),
      new WPI_VictorSPX(6));//
      
  
  }

  public GlitchDrivetrain(WPI_TalonSRX left, WPI_VictorSPX left2, WPI_VictorSPX left3, WPI_TalonSRX right, WPI_VictorSPX right2, WPI_VictorSPX right3) {
    super(left, right);//1, 4
  
    leftFollow1 = left2;//2
    leftFollow2 = left3;//3
    rightFollow1 = right2;//5
    rightFollow2 = right3;//6

    configureMotors();

    MotorConfig drive = MotorConfig.get("drive.json");
    withEachSendableMotor((m)-> 
      LiveWindow.add(m)
    );
    enableLogging();
    DataLogger.addDataElement("leftVel", () -> LightningMath.talon2fps(getLeftVelocity()));
    DataLogger.addDataElement("rightVel", () -> LightningMath.talon2fps(getRightVelocity()));
    DataLogger.addDataElement("R_Current_Pos", () -> LightningMath.ticks2feet(Robot.drivetrain.getRightMaster().getSelectedSensorPosition()));
    DataLogger.addDataElement("L_Current_Pos", () -> LightningMath.ticks2feet(Robot.drivetrain.getLeftMaster().getSelectedSensorPosition()));
    DataLogger.addDataElement("R_Expected_Pos", () -> LightningMath.ticks2feet(Robot.drivetrain.getRightMaster().getClosedLoopError()+Robot.drivetrain.getRightMaster().getSelectedSensorPosition()));
    DataLogger.addDataElement("L_Expected_Pos", () -> LightningMath.ticks2feet(Robot.drivetrain.getLeftMaster().getClosedLoopError()+Robot.drivetrain.getLeftMaster().getSelectedSensorPosition()));
  }

  public void withEachMotor(Consumer<BaseMotorController> fn)  {
    fn.accept(getLeftMaster());
    fn.accept(getRightMaster());
    fn.accept(leftFollow1);
    fn.accept(leftFollow2);
    fn.accept(rightFollow1);
    fn.accept(rightFollow2);
  }

  public void withEachSendableMotor(Consumer<Sendable> fn)  {
    fn.accept(getLeftMaster());
    fn.accept(getRightMaster());
    fn.accept(leftFollow1);
    fn.accept(leftFollow2);
    fn.accept(rightFollow1);
    fn.accept(rightFollow2);
  }

  public void configureMotors() {
    getLeftMaster().setInverted(false);
    leftFollow1.follow(getLeftMaster());
    leftFollow1.setInverted(true);
    leftFollow2.follow(getLeftMaster());
    leftFollow2.setInverted(false);
    //getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

    getLeftMaster().setNeutralMode(NeutralMode.Brake);
    leftFollow1.setNeutralMode(NeutralMode.Brake);
    leftFollow2.setNeutralMode(NeutralMode.Brake);

    getRightMaster().setInverted(true);
    rightFollow1.follow(getRightMaster());
    rightFollow1.setInverted(false);
    rightFollow2.follow(getRightMaster());
    rightFollow2.setInverted(true);
    //getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

    getRightMaster().setNeutralMode(NeutralMode.Brake);
    rightFollow1.setNeutralMode(NeutralMode.Brake);
    rightFollow2.setNeutralMode(NeutralMode.Brake);

    withEachMaster((m) -> {
      m.configOpenloopRamp(0.2);
      m.configClosedloopRamp(0.2);
    });

    //withEachMotor(fn); // ????????
    withEachMotor((m) -> {
      //m.config_k(slotIdx, value)
      m.config_kP(0, 0.5);
      m.config_kI(0, 0.0);
      m.config_kD(0, 0.0);
      m.config_kF(0, 1.4614284);
    });
    enableLogging();
  }

  public void configurePID(Gains g) {
    withEachMaster((m) -> {
      m.config_kP(0, g.kP);
      m.config_kI(0, g.kI);
      m.config_kD(0, g.kD);
      m.config_kF(0, g.kF);
    });
  } 

  @Override
  public void setVelocity(double left, double right) {
    // convert from ft/s to talon units (enc ticks/ 100ms)
    double lrpm = LightningMath.fps2rpm(left);
    double left_talon_units = lrpm * 60 * 10; // 60 changes rpm to rps (rev per sec) * 10 changes to per 100ms
    double rrpm = LightningMath.fps2rpm(right);
    double right_talon_units = rrpm * 60 * 10;
    super.setVelocity(left_talon_units, right_talon_units);
  }

  @Override
  public double getRightVelocity() {
    return ((super.getRightVelocity() / 10)/60);//return fps
  }

  @Override
  public double getLeftVelocity() {
    return ((super.getLeftVelocity() / 10)/60);//return fps
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new TankDrive());
  }
}
 