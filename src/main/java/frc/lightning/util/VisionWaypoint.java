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
public class VisionWaypoint {
    private double standoff;
    private double squint;
    private double rotateToTarget;

    private final double WAYPOINT_DISTANCE_SCALE = 0.4;

    public VisionWaypoint(Target target) {
        double standoff = target.standoff();
        double squint = Math.toRadians(target.squint());
        double rotation = Math.toRadians(target.rotation());
        double targetToWaypoint = standoff * WAYPOINT_DISTANCE_SCALE;

        //Using law of cosines to calculate the distance from the waypoint
        standoff = Math.sqrt(
            Math.pow(targetToWaypoint, 2)
             + Math.pow(standoff, 2) 
             - 2 * standoff * targetToWaypoint * Math.abs(Math.cos(rotation)));
          
          //Using law of sines to calculate squint to waypoint
          double targetToWaypointAngle = Math.abs(Math.asin(
            Math.sin(rotation) / standoff * targetToWaypoint));
          
          if(Math.signum(squint) != Math.signum(rotation)) {
            squint = squint - (targetToWaypointAngle * Math.signum(squint));
          }
          else {
            squint = squint + (targetToWaypointAngle * Math.signum(squint));
          }
          
          rotateToTarget = Math.signum(rotation) * -1 * (Math.PI - (Math.PI - Math.abs(rotation) - targetToWaypointAngle));
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
    public double angleToTaret() {
        return rotateToTarget;
    }
    public double angleToTargetDeg() {
        return Math.toDegrees(rotateToTarget);
    }
}
