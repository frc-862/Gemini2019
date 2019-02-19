/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.subsystems.CANDrivetrain;
import frc.lightning.testing.SystemTest;
import frc.lightning.util.LightningMath;
import frc.robot.commands.driveTrain.TankDrive;
import frc.robot.commands.driveTrain.VelocityTankDrive;
import frc.robot.commands.test.LeftEncoderTest;
import frc.robot.commands.test.RightEncoderTest;
import frc.robot.Constants;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class GeminiDrivetrain extends CANDrivetrain {
    public static GeminiDrivetrain create() {
        return new GeminiDrivetrain(
                   new WPI_TalonSRX(RobotMap.geminiLeftMaster),//CAN ID 1
                   new WPI_VictorSPX(RobotMap.geminiLeftSlave),//CAN ID 2
                   new WPI_TalonSRX(RobotMap.geminiRightMaster),//CAN ID 4
                   new WPI_VictorSPX(RobotMap.geminiRightSlave));//CAN ID 5
    }

    public GeminiDrivetrain(WPI_TalonSRX left, WPI_VictorSPX left2, WPI_TalonSRX right, WPI_VictorSPX right2) {
        // inverts the left, not the right (the true/false)
        super(left, false, right, true);//1, 4

        // Don't invert the left follower
        // Invert the right
        addLeftFollower(left2, false  );
        addRightFollower(right2,true);

        configureMotors();
        enableLogging();

        SystemTest.register(new LeftEncoderTest());
        SystemTest.register(new RightEncoderTest());
    }

    @Override
    public double getLeftDistance() {
        return LightningMath.ticks2feet(super.getLeftDistance());//convert encoder ticks to feet
    }

    @Override
    public double getRightDistance() {
        return LightningMath.ticks2feet(super.getRightDistance());//convert encoder ticks to feet
    }

    @Override
    public void periodic() {
        super.periodic();
    }

    public void configureMotors() {
        super.configureMotors();

        withEachMotor((m) -> m.setNeutralMode(NeutralMode.Brake));
        withEachMaster((m) -> {
            m.configOpenloopRamp(0.2);

            m.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.driveSlot, 0);
            m.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.motionSlot, 0);

            // TODO TRUE !!! TRUE !!! TRUE !!! TRUE !!! TRUE !!! TRUE !!! TRUE !!! TRUE !!! TRUE !!!
            m.setSensorPhase(true);
        });

        configurePID(Constants.drivePIDs);
    }

    @Override
    public void setVelocity(double left, double right) {
        // convert from ft/s to talon units (enc ticks/ 100ms)
        double right_talon_units = LightningMath.fps2talon(right);
        double left_talon_units = LightningMath.fps2talon(left);

        super.setVelocity(left_talon_units, right_talon_units);
    }

    @Override
    public double getRightVelocity() {
        return ((super.getRightVelocity()));// / 10)/60);//return fps
    }

    @Override
    public double getLeftVelocity() {
        return ((super.getLeftVelocity()));// / 10)/60);//return fps
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TankDrive());
    }

}
