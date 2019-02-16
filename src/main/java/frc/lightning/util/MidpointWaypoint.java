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
<<<<<<< HEAD:src/main/java/frc/lightning/util/MidpointWaypoint.java
public class MidpointWaypoint {
=======
public abstract class VisionWaypoint {
>>>>>>> 9a41061cef88373a0cb763e570007b8abef71b04:src/main/java/frc/lightning/util/VisionWaypoint.java
    private double standoff;
    private double squint;

<<<<<<< HEAD:src/main/java/frc/lightning/util/MidpointWaypoint.java
    private final double WAYPOINT_DISTANCE_SCALE = 0.4;
    public MidpointWaypoint(Target target) {
        standoff = target.standoff();
        squint = Math.toRadians(target.squint());
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
=======
    public void setStandoff(double standoff) {
        this.standoff = standoff;
    }
    public void setSquint(double squint) {
        this.squint = squint;
>>>>>>> 9a41061cef88373a0cb763e570007b8abef71b04:src/main/java/frc/lightning/util/VisionWaypoint.java
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
