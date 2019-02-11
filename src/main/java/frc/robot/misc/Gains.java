/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.misc;

import com.ctre.phoenix.motorcontrol.can.SlotConfiguration;

/**
 * Add your docs here.
 */
public class Gains extends SlotConfiguration {
    public Gains(double _kP, double _kI, double _kD, double _kF, int _kIzone, double _kPeakOutput) {
        kP = _kP;
        kI = _kI;
        kD = _kD;
        kF = _kF;
        this.integralZone = _kIzone;
        closedLoopPeakOutput = _kPeakOutput;
    }

    public Gains(double _kP, double _kI, double _kD, double _kF, int _kIzone) {
        this(_kP, _kI, _kD, _kF, _kIzone, 1.0);
    }
}
