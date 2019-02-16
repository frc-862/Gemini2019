/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.util;

/**
 * Add your docs here.
 */
public abstract class VisionWaypoint {
    private double standoff;
    private double squint;

    public void setStandoff(double standoff) {
        this.standoff = standoff;
    }
    public void setSquint(double squint) {
        this.squint = squint;
    }
    public double standoff() {
        return standoff;
    }
    public double squint() {
        return squint;
    }
    public double squintDeg() {
        return Math.toDegrees(squint);
    }
}
