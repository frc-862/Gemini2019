/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.robot.Constants;
import frc.robot.RobotConstants;
import frc.robot.RobotMap;
import frc.robot.commands.cargo.CargoCollect;
import frc.robot.commands.cargo.SetCargoPower;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * CargoCollector includes both ground collect
 * and elevator collect wheels
 *
 */
public class CargoCollector extends Subsystem {
    private final WPI_VictorSPX collector;
    public WPI_VictorSPX collectLeft;
    public WPI_VictorSPX collectRight;



    public static CargoCollector create() {
        return new CargoCollector(
                   new WPI_VictorSPX(RobotMap.cargoMotor));
    }


    public CargoCollector(WPI_VictorSPX collector) {
        String name = getClass().getSimpleName();
        setName(name);

        collectLeft = new WPI_VictorSPX(RobotMap.leftCollectCanId);
        addChild("Left Collect", collectLeft);
        collectLeft.setInverted(true);
        collectRight = new WPI_VictorSPX(RobotMap.rightCollectCanId);

        addChild("Right Collect", collectRight);

        this.collector = collector;
        addChild("Intake Motor", collector);

        stop();
    }

    /**
     * Will be linked to TOF (Time of Flight) sensor when
     * electrical/controls get it setup for us
     *
     * TODO link this up
     */
    public double cargoDistanceSensor() {
        return -1;
    }

    public boolean hasCargo() {
        return cargoDistanceSensor() >= 0 && cargoDistanceSensor() <= Constants.hasCargoDistance;
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new SetCargoPower());
    }

    public void collect() {
        System.out.println("collect it");
        collector.set(ControlMode.PercentOutput, Constants.collectPower);
    }

    public void eject() {
        System.out.println("eject it");
        collector.set(ControlMode.PercentOutput, -Constants.collectPower);
    }

    public void stop() {
        collector.set(ControlMode.PercentOutput, 0.0);
    }


    public void setGroundCollectPower(double pwr) {
        collector.set(ControlMode.PercentOutput, pwr);
    }

    public void setElevatorCollectPower(double pwr) {
        collectRight.set(ControlMode.PercentOutput, pwr);
        collectLeft.set(ControlMode.PercentOutput, pwr);
    }

    public void ejectCargo() {
        collectLeft.set(ControlMode.PercentOutput, Constants.ejectDemand*-1);
        collectRight.set(ControlMode.PercentOutput, Constants.ejectDemand*-1);
    }

    public void stopEject() {
        collectLeft.set(ControlMode.PercentOutput, 0.0);
        collectRight.set(ControlMode.PercentOutput, 0.0);
    }

    public void elevatorCargoHoldPower() {
        collectLeft.set(ControlMode.PercentOutput, Constants.elevatorCollectorHoldPower);
        collectRight.set(ControlMode.PercentOutput, Constants.elevatorCollectorHoldPower);
    }

    public void collectCargo() {
        collectLeft.set(ControlMode.PercentOutput, Constants.ejectDemand);
        collectRight.set(ControlMode.PercentOutput, Constants.ejectDemand);
    }

    public void stopCollectCargo() {

    }


}
