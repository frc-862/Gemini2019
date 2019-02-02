package frc.lightning.util;

import frc.robot.Robot;
import frc.robot.RobotConstants;

public class LightningMath {
    public static double wheelRadius;
    static{
        if(Robot.isOBot()) wheelRadius = RobotConstants.obotWheelDiameter/2;
        else if(Robot.isGlitch()) wheelRadius = RobotConstants.glitchWheelDiameter/2;
        else if(Robot.isGemini()) wheelRadius = RobotConstants.geminiWheelDiameter/2;
    };

    public static double wheelCircumference = wheelRadius * Math.PI * 2;//1.5707 ft
    public static final double TICS_PER_ROTATION = 4 * 360;

    public static double talon2ips(double talon) {
        // multiply 100ms by 10 to get seconds
        return ticks2inches(talon * 10);
    }

    public static double talon2fps(double talon) {
        // ticks /  100ms = talon
        double ticksps = talon * 10;  // ticks / sec
        double fps = ticks2feet(ticksps);
        return fps; 
    }

    public static double fps2talon(double fps) {
        return ips2talon(fps*12);//fps*12 = ips
    }

    public static double ips2talon(double ips) {
        double ip100ms = ips / 10;
        return inches2ticks(ip100ms);
    }

    public static double inches2ticks(double inches) {
        return in2ft(inches) / wheelCircumference * TICS_PER_ROTATION;
    }

    public static double feet2ticks(double feet) {
        return feet / wheelCircumference * TICS_PER_ROTATION;
    }

    public static double ticks2feet(double ticks) {
        return ticks / TICS_PER_ROTATION * wheelCircumference;
    }

    public static double ticks2inches(double ticks) {
        return ticks2feet(ticks) * 12;
    }

    public static double meters2feet(double meters) {
        return meters * 0.3048;
    }

    public static double feet2meters(double feet) {
        return feet * 3.28084;
    }

    public static double limit(double v, double low, double high) {
        return (v < low) ? low : ((v > high) ? high : v);
    }

    public static double limit(double v, double limit) {
        return limit(v, -limit, limit);
    }

    public static double limit(double input) {
        return limit(input, -1, 1);
    }

    public static double boundThetaNegPiToPi(double theta) {
        return theta - (Math.ceil((theta + Math.PI) / (Math.PI * 2)) - 1) * (Math.PI * 2); // (-π;π]
    }

    public static double boundTheta0To2Pi(double theta) {
        return theta - Math.floor(theta / (Math.PI * 2)) * (Math.PI * 2); // [0;2π)
    }

    public static double boundThetaNeg180to180(double theta) {
        return theta - (Math.ceil((theta + 180)/360)-1)*360; // (-180;180]
    }

    public static double boundTheta0to360(double theta) {
        return theta - Math.floor(theta/360)*360;  // [0;360)
    }

    public static double deltaThetaInDegrees(double from, double to) {
        return boundThetaNeg180to180(to - from);
    }

    public static double deltaThetaInRadians(double from, double to) {
        return boundThetaNegPiToPi(to - from);
    }

    public static int scale(int input,
                            int lowInput, int highInput, int lowOutput, int highOutput)
    {
        final int inputRange = highInput - lowInput;
        final int outputRange = highOutput - lowOutput;

        return (input - lowInput) * outputRange / inputRange + lowOutput;
    }

    public static double scale(double input,
                               double lowInput, double highInput, double lowOutput, double highOutput)
    {
        final double inputRange = highInput - lowInput;
        final double outputRange = highOutput - lowOutput;

        return  (input - lowInput) * outputRange / inputRange + lowOutput;
    }

    public static double deadZone(double input, double deadband)
    {
        return Math.abs(input) >= deadband ? input : 0;
    }

    public static double constrain(double n, double min, double max) {
        return Math.max(Math.min(n, max), min);
    }

    public static boolean isInRange(double a, double b, double epsilon) {

        return Math.abs(a - b) < epsilon;

    }

    public static double rotations2feet(double rotations) {
        return rotations * wheelCircumference;
    }

    public static double feet2rotations(double feet) {
        return feet / wheelCircumference;
    }

    public static double rpm2fps(double rpm) {
        // rpm * circumference will be feet / minute
        // 60 is the number of seconds in a minute
        return rpm * wheelCircumference / 60.0;
    }

    public static double fps2rpm(double fps) {
        // fps * 60 will be feet / minute
        // feet / minute * circumference is rpm
        return fps * 60 / wheelCircumference;
    }

    public static double in2ft(double in) {
        return in / 12;
    }

    public static double mm2ft(double dist) {
        return dist / 304.8;
    }

    public static boolean isZero(double val) {
        return Math.abs(val) < 0.0000001;
    }

    public static boolean isEqual(double v1, double v2) {
        return isZero(v1 - v2);
    }

    public static boolean epsilonEqual(double v1, double v2, double epsilon) {
        return Math.abs(v1 - v2) < epsilon;
    }

    public static double feet2talon(double ft) {
        final double rotations = feet2rotations(ft);
        return rotations * TICS_PER_ROTATION;
    }

}
