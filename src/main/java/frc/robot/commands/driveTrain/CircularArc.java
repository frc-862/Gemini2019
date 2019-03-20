/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class CircularArc extends Command {

    private int leftVel, rightVel;
    private int l = 1, d = 1, r = 1;
    private double squint = 0.0;

    public CircularArc() {
        //TODO Re-Config PIDF Gains?
        requires(Robot.drivetrain);//TODO Need Vision
    }

    @Override
    protected void initialize() {
        l = getLinearDistanceFromWaypoint();
        squint = getSquint();
        //Calc Velocitys
    }

    @Override
    protected void execute() {
        /*
        There is a velocity ratio between each motor to get them to drive in an arc. We can put control loop to make sure this ratio is working.
        */
        Robot.drivetrain.setVelocity(leftVel, rightVel);//Can scale up ratio - 16:4 will drive same circle as 4:1, just faster
    }

    @Override
    protected boolean isFinished() {
        return false;//Return true when target is reached
    }

    @Override
    protected void end() {
        Robot.drivetrain.setPower(0.0, 0.0);
    }

    @Override
    protected void interrupted() {
        end();
    }

    //Numbers Needed

    private int getLinearDistanceFromWaypoint() {
        return 1;//TODO get this from simpleVision
    }

    private double getSquint() {
        return 1.0;//TODO get this from simpleVision
    }

    //Maths

    private int getWaypointY() {
        return 1;//TODO x = sin(90-squint) * L (L is linear distance from waypoint)
    }

    private int getWaypointX() {
        return 1;//TODO x = cos(90-squint) * L (L is linear distance from waypoint)
    }

    private int getD() {
        return 1;//TODO D = (x^2 + y^2)/2x  - x
    }

    private int getR() {
        return 1;//TODO r = d + x OR (l^2)/(2x)
    }

}
