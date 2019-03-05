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
public class Target {

    public enum Type {
        COMPLETE, LEFT, RIGHT;
    }

    private double standoff;
    private double rotation;
    private double squint;
    private long timestamp;
    private int x, y;
    private Type type;
    public Target(Type type, int x, int y, double standoff, double rotation, double squint, long timestamp) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.standoff = standoff;
        this.rotation = rotation;
        this.squint = squint;
        this.timestamp = timestamp;

    }
    public Target(Target l, Target r) {
        x = (l.x() + r.x()) / 2;
        y = (l.y() + r.y()) / 2;
        standoff = (l.standoff() + r.standoff()) / 2;
        rotation = (l.rotation() + r.rotation()) / 2;
        squint = (l.squint() + r.squint()) / 2;
        timestamp = (l.timestamp() + r.timestamp()) / 2;
    }

    public Type type() {
        return type;
    }
    public int x() {
        return x;
    }
    public int y() {
        return y;
    }
    public double standoff() {
        return standoff;
    }
    public double rotation() {
        return rotation;
    }
    public double rotRad() {
        return Math.toRadians(rotation);
    }
    public double squint() {
        return squint;
    }
    public double squintRad() {
        return Math.toRadians(squint);
    }
    public long timestamp() {
        return timestamp;
    }
    @Override
    public String toString() {
        return "[Standoff: " + standoff + ", Squint: " + squint + ", Rotation: " + rotation + "] (Seen at " + timestamp + ")";
    }
}
