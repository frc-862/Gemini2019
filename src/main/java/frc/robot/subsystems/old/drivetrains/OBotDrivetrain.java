/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.old.drivetrains;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.lightning.subsystems.CANDrivetrain;
import frc.lightning.util.MotorConfig;
import frc.robot.RobotMap;
import frc.robot.commands.driveTrain.TankDrive;

/**
 * Add your docs here.
 */
public class OBotDrivetrain extends CANDrivetrain {

    public static OBotDrivetrain create() {
        return new OBotDrivetrain(
                   new WPI_TalonSRX(RobotMap.obotLeftMaster),
                   new WPI_TalonSRX(RobotMap.obotLeftSlave),
                   new WPI_TalonSRX(RobotMap.obotLeftSlave2),
                   new WPI_TalonSRX(RobotMap.obotRightMaster),
                   new WPI_TalonSRX(RobotMap.obotRightSlave),
                   new WPI_TalonSRX(RobotMap.obotRightSlave2)
               );
    }

    public OBotDrivetrain(WPI_TalonSRX left, WPI_TalonSRX left2, WPI_TalonSRX left3, WPI_TalonSRX right, WPI_TalonSRX right2, WPI_TalonSRX right3) {
        super(left, right);
        getLeftMaster().setInverted(true);

        addLeftFollower(left2);
        addLeftFollower(left3);

        addRightFollower(right2, true);
        addRightFollower(right3, true);

        MotorConfig drive = MotorConfig.get("drive.json");
        withEachMotor((m) -> drive.registerMotor(m));
    }


    public void configAllSettings(TalonSRXConfiguration config) {
        withEachMaster((m) -> m.configAllSettings(config));
    }

    public void configureMotors() {
        getLeftMaster().setInverted(true);
        super.configureMotors();

        withEachMaster((m) -> {
            m.configOpenloopRamp(0.2);
            m.configClosedloopRamp(0.2);
        });

        enableLogging();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TankDrive());
    }
}
