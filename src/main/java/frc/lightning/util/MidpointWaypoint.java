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
public class MidpointWaypoint extends VisionWaypoint {
    public MidpointWaypoint(Target target) {
        setStandoff(target.standoff() / 2);
        setSquint(Math.toRadians(target.squint()) * 1.2);
    }
    public MidpointWaypoint(VisionWaypoint destination) {
        setStandoff(destination.standoff() / 2);
        setSquint(destination.squint() * 1.2);
    }
}