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

    private final WPI_VictorSPX  leftGripper;
    private final WPI_VictorSPX rightGripper;
    private final WPI_VictorSPX collector;
    private final DoubleSolenoid deployer;

    public CargoCollector create() {
        return new CargoCollector(
                   new WPI_VictorSPX(21),
                   new WPI_VictorSPX(20),
                   new WPI_VictorSPX(29),
                   new DoubleSolenoid(11, 1, 2)
               );
    }

    public CargoCollector(WPI_VictorSPX collector, WPI_VictorSPX leftGrip, WPI_VictorSPX rightGrip, DoubleSolenoid deployer) {
        leftGripper = leftGrip;
        rightGripper = rightGrip;
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

    public void collectBall() {
        System.out.println("collect it");
        collector.set(ControlMode.PercentOutput, 1.0);
    }

    public void ejectBall() {
        System.out.println("eject it");
        collector.set(ControlMode.PercentOutput, -1.0);
    }

    public void stop() {
        leftGripper.set(0.0);
        rightGripper.set(0.0);
        collector.set(ControlMode.PercentOutput, 0);
    }

    public void hold() {
        setPower(holdPower);
    }

    private void setPower(double pwr) {
        leftGripper.set(ControlMode.PercentOutput, pwr);
        rightGripper.set(ControlMode.PercentOutput, pwr);
    }


    public void deployGroundCollect() {
        deployer.set(DoubleSolenoid.Value.kForward);
    }

    public void retractGroundCollect() {
        deployer.set(DoubleSolenoid.Value.kReverse);
    }

    public void collect() {
        setPower(collectPower);
    }

    public void eject() {
        setPower(-collectPower);
    }

}
