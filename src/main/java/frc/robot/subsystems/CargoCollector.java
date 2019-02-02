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
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * CargoCollector includes both ground collect
 * and elevator collect wheels
 *
 */
public class CargoCollector extends Subsystem {
    final double collectPower = -0.6;
    final double holdPower = -0.2;
    final double hasCargoDistance = 0.7;


    private final WPI_VictorSPX collector;
    private final DoubleSolenoid deployer;

    public CargoCollector create() {
        return new CargoCollector(
                   new WPI_VictorSPX(21),
                   new DoubleSolenoid(11, 1, 2)
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
        return cargoDistanceSensor() >= 0 && cargoDistanceSensor() <= hasCargoDistance;
    }

    @Override
    public void initDefaultCommand() {
        //setDefaultCommand(new HABClimb());
    }

    public void collect() {
        System.out.println("collect it");
        collector.set(ControlMode.PercentOutput, Constants.collectPower);
    }

    public void eject() {
        System.out.println("eject it");
        collector.set(ControlMode.PercentOutput, Constants.ejectPower);
    }

    public void stop() {
        collector.set(ControlMode.PercentOutput, 0);
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
