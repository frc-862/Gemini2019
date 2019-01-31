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
 * Add your docs here.
 */
public class CargoCollector extends Subsystem {
    private final WPI_VictorSPX leftGripper;
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

    @Override
    public void initDefaultCommand() {
        //setDefaultCommand(new HABClimb());
    }

    public void collectBall() {
        collector.set(ControlMode.PercentOutput, 1.0);
    }

    public void ejectBall() {
        collector.set(ControlMode.PercentOutput, -1.0);
    }

    public void stop() {
        leftGripper.set(ControlMode.PercentOutput, 0);
        rightGripper.set(ControlMode.PercentOutput, 0);
        collector.set(ControlMode.PercentOutput, 0);
    }

    public void hold() {

    }

    public void setPower(double pwr) {
        System.out.println("power " + pwr);
        leftGripper.set(pwr);
        rightGripper.set(pwr);
    }

    public void toggleDeployer() {
        if (deployer.get() == DoubleSolenoid.Value.kForward) {
            deployer.set(DoubleSolenoid.Value.kReverse);
        } else {
            deployer.set(DoubleSolenoid.Value.kForward);
        }
    }

    public void collect() {
        System.out.println("COLLECT");
        rightGripper.set(-0.6);
        leftGripper.set(0.6);
    }

    public void eject() {
        System.out.println("EJECT");
        rightGripper.set(0.6);
        leftGripper.set(-0.6);
    }

}
