/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotConstants;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * CargoCollector includes both ground collect
 * and elevator collect wheels
 *
 */
public class CargoCollector extends Subsystem {
    private final WPI_VictorSPX collector;
    private final DoubleSolenoid deployer;

    public static CargoCollector create() {
        return new CargoCollector(
                   new WPI_VictorSPX(RobotMap.cargoMotor),
                   new DoubleSolenoid(RobotMap.cargoSolenoidModule, RobotMap.cargoSolenoidFwdChan, RobotMap.cargoSolenoidRevChan)
               );
    }

    public CargoCollector(WPI_VictorSPX collector, DoubleSolenoid deployer) {
     
        this.collector = collector;
        this.deployer = deployer;
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
        return cargoDistanceSensor() >= 0 && cargoDistanceSensor() <= RobotConstants.hasCargoDistance;
    }

    @Override
    public void initDefaultCommand() {
        //setDefaultCommand(new HABClimb());
    }

    public void collect() {
        System.out.println("collect it");
        collector.set(ControlMode.PercentOutput, RobotConstants.collectPower);
    }

    public void eject() {
        System.out.println("eject it");
        collector.set(ControlMode.PercentOutput, -RobotConstants.collectPower);
    }

    public void stop() {
        collector.set(ControlMode.PercentOutput, 0.0);
    }


    private void setPower(double pwr) {
        collector.set(ControlMode.PercentOutput, pwr);
    }


    public void deploy() {
        deployer.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        deployer.set(DoubleSolenoid.Value.kReverse);
    }

  
    

}
