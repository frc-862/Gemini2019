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
public class Arc {
    private final double TRACK_WIDTH = 22.0;
    private double x, y, radius, angle, completesRadiusDistance;
    private double centerLength, leftLength, rightLength;
    private double velocityRatio;

    public Arc(VisionWaypoint destination) {
        x = Math.signum(destination.squint()) * destination.standoff() * Math.cos(Math.abs(destination.squint()));
        y = destination.standoff() * Math.sin(Math.abs(destination.squint()));
        radius = Math.pow(destination.standoff(), 2) / (2 * Math.abs(x));
        completesRadiusDistance = radius - x;
        angle =  Math.PI - 2 * (Math.PI / 2 - Math.abs(destination.squint())) - Math.asin(completesRadiusDistance / radius);
        centerLength = radius * angle;
        leftLength = (radius + Math.signum(destination.squint()) * TRACK_WIDTH / 2) * angle;
        rightLength = (radius - Math.signum(destination.squint()) * TRACK_WIDTH / 2) * angle;
        velocityRatio = leftLength / rightLength;
    }

    public double destinationX() {
        return x;
    }
    public double destinationY() {
        return y;
    }
    public double raduis() {
        return radius;
    }
    public double angle() {
        return angle;
    }
    public double length() {
        return centerLength;
    }
    public double leftLength() {
        return leftLength;
    }
    public double rightLength() {
        return rightLength;
    }
    /**
     * Returns the ratio of left velocity to right velocity
     */
    public double velocityRatio() {
        return velocityRatio;
    }
}
