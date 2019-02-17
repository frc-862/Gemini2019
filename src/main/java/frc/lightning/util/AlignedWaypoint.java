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
public class AlignedWaypoint extends VisionWaypoint{
    private double standoff;
    private double squint;
    private double rotateToTarget;

    private static final double WAYPOINT_DISTANCE_SCALE = 0.4;
    public AlignedWaypoint(Target target) {
        this(target, WAYPOINT_DISTANCE_SCALE);
    }
    public AlignedWaypoint(Target target, double distanceToTarget) {
        double targetStandoff = target.standoff();
        double targetSquint = Math.toRadians(target.squint());
        double targetRotation = Math.toRadians(target.rotation());
        double targetToWaypoint = distanceToTarget;

        //Using law of cosines to calculate the distance from the waypoint
        standoff = Math.sqrt(
            Math.pow(targetToWaypoint, 2)
             + Math.pow(targetStandoff, 2) 
             - 2 * targetStandoff * targetToWaypoint * Math.abs(Math.cos(targetRotation)));
          
          //Using law of sines to calculate squint to waypoint
          double targetToWaypointAngle = Math.abs(Math.asin(
            Math.sin(targetRotation) / standoff * targetToWaypoint));
          
          if(Math.signum(targetSquint) != Math.signum(targetRotation)) {
            squint = targetSquint - (targetToWaypointAngle * Math.signum(targetSquint));
          }
          else {
            squint = targetSquint + (targetToWaypointAngle * Math.signum(targetSquint));
          }

          setStandoff(standoff);
          setSquint(squint);
          
          rotateToTarget = Math.signum(targetRotation) * -1 * (Math.PI - (Math.PI - Math.abs(targetRotation) - targetToWaypointAngle));
    }

    public double angleToTaret() {
        return rotateToTarget;
    }
    public double angleToTargetDeg() {
        return Math.toDegrees(rotateToTarget);
    }
}
