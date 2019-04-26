/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.hal.util.UncleanStatusException;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.geometry.Rotation2d;
import frc.lightning.logging.DataLogger;
import frc.lightning.testing.SystemTest;
import frc.lightning.util.MovingAverageFilter;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.test.NavXTest;

import java.util.function.BooleanSupplier;

/**
 * Add your docs here.
 */
public class Core extends Subsystem {
    private DigitalInput left_4 = new DigitalInput(4);//9
    private DigitalInput left_3 = new DigitalInput(22);//5
    private DigitalInput left_2 = new DigitalInput(5);//4
    private DigitalInput left_1 = new DigitalInput(21);//10

    private DigitalInput center = new DigitalInput(18);//3

    private DigitalInput right_1 = new DigitalInput(20);//11
    private DigitalInput right_2 = new DigitalInput(2);//2
    private DigitalInput right_3 = new DigitalInput(19);//1
    private DigitalInput right_4 = new DigitalInput(3);//0

    private DigitalInput[] sensors = new DigitalInput[8];

    private AnalogInput airPreasure = new AnalogInput(4);


    private double lineFirstSeen = Timer.getFPGATimestamp();
    // Put methods for controlling this subsystem
    // here. Call these from Commajnds.
    private AHRS navx = new AHRS(SPI.Port.kMXP);

    private Compressor compressor = new Compressor(RobotMap.compressorCANId);
    private Solenoid ring = new Solenoid (RobotMap.compressorCANId,3);
    private PowerDistributionPanel pdp = new PowerDistributionPanel(RobotMap.pdpCANId);
    private AnalogInput groundCollectEncoder = new AnalogInput(5);
    private double linePos;

    private double biasAnalog(double v, double min, double max) {
        final double midPoint = (max + min) / 2.0;
        if (v > midPoint) {
            return 0;
        } else if (v < min) {
            return 1;
        } else {
            final double range = (max - min) / 2;
            return 1 - ((v - min) / range);
        }
    }

    private boolean[] validSensors = new boolean[512];

    private double[] lineWeights = {-5.8125, -3.0, -1.875, -0.875, 0.125, 1.125, 2.125, 3.125, 6.125};

    private BooleanSupplier[] sensorValues = {
        () -> !left_4.get(),
        () -> !left_3.get(),
        () -> !left_2.get(),
        () -> !left_1.get(),
        () -> !center.get(),
        () -> !right_1.get(),
        () -> !right_2.get(),
        () -> !right_3.get(),
        () -> !right_4.get(),
    };

    private boolean sawLine;

    public Core() {
        setName("Core");

        for(int i = 0; i < validSensors.length; ++i) {
            validSensors[i] = false;
        }
        validSensors[0b100000000] = true;
        validSensors[0b110000000] = true;
        validSensors[0b010000000] = true;
        validSensors[0b011000000] = true;
        validSensors[0b011100000] = true;
        validSensors[0b001000000] = true;
        validSensors[0b001100000] = true;
        validSensors[0b001110000] = true;
        validSensors[0b000100000] = true;
        validSensors[0b000110000] = true;
        validSensors[0b000111000] = true;//cntr
        validSensors[0b000010000] = true;//cntr too
        validSensors[0b000011000] = true;
        validSensors[0b000011100] = true;
        validSensors[0b000001000] = true;
        validSensors[0b000001100] = true;
        validSensors[0b000001110] = true;
        validSensors[0b000000100] = true;
        validSensors[0b000000110] = true;
        validSensors[0b000000010] = true;
        validSensors[0b000000011] = true;
        validSensors[0b000000001] = true;

//        for (int i = 0; i < sensors.length; ++i) {
//            System.out.println("Init sensor " + (i + 18));
//            try {
//                sensors[i] = new DigitalInput(i + 18);
//            } catch (UncleanStatusException err) {
//                System.out.println("Unable to initialize " + (i + 18));
//            }
//        }

        //that was fun.

        addChild("Compressor", compressor);

        addChild("NavX", navx);
        DataLogger.addDataElement("Pressure", () -> airPreasure.getVoltage());
        DataLogger.addDataElement("Heading", () -> navx.getFusedHeading());
//        DataLogger.addDataElement("PITCH", () -> getPitch());
//        DataLogger.addDataElement("ROLL", () -> getRoll());
//        DataLogger.addDataElement("AccelX", () -> navx.getRawAccelX());
//        DataLogger.addDataElement("AccelY", () -> navx.getRawAccelY());
//        DataLogger.addDataElement("AccelZ", () -> navx.getRawAccelZ());
//        DataLogger.addDataElement("DisplacementX", () -> navx.getDisplacementX());
//        DataLogger.addDataElement("DisplacementY", () -> navx.getDisplacementY());
//        DataLogger.addDataElement("DisplacementZ", () -> navx.getDisplacementZ());

//        int i = -3;
//        for (var sensor : rawSensorValues) {
//            DataLogger.addDataElement("LineFollow" + i, () -> sensor.getAsDouble());
//            i += 2;
//        }



        // monitor if the heading is exactly the same, there is always
        // some jitter in the reading, so this will not be the case
        // if we are getting valid values from the sensor for >= 3 seconds

        // FaultMonitor.register(new UnchangingFaultMonitor(Codes.NAVX_ERROR, () -> navx.getUpdateCount(),
        //     2.0, 0, "NavX unresponsive"));

        addChild("PDP", pdp);
        // Stops pdp from whining about things we don't care about.
        // EX - CAN Frame timeout, etc.
        LiveWindow.disableTelemetry(pdp);

//        addChild("left_4", left_4);
//        addChild("midLeft", midLeft);
//        addChild("left_3", left_3);
//        addChild("left_2", left_2);
//        addChild("right_2", right_2);
//        addChild("right_3", right_3);
//        addChild("midRight", midRight);
//        addChild("right_4", right_4);

        // addChild("extra motor 1", extra1);
        // addChild("extra motor 2", extra2);

        SystemTest.register(new NavXTest());

        ringOn();
    }

    public boolean isVisionReady() {
        return false;//TODO fix
    }
    public void ringOn() {
        ring.set(true);
    }
    public void ringOff() {
        ring.set(false);
    }
    @Override
    public void periodic() {
        sawLine = false;

        double weight = 0;
        double sensorCount = 0;
        for(int loop=0; loop < sensorValues.length; loop++) {
            boolean value = sensorValues[loop].getAsBoolean();
            double senorWeight = lineWeights[loop];
            // SmartDashboard.putBoolean("LineSenor " + senorWeight, value);
            if (value) {
                sawLine = true;
                weight += lineWeights[loop];
                sensorCount += 1;
            }
        }

        linePos = weight / sensorCount;
        if (sensorCount == sensorValues.length) {
            sawLine = false;
            linePos = 0;
        }

        if (!sawLine) {
            lineFirstSeen=Timer.getFPGATimestamp();
        }

        SmartDashboard.putNumber("Distance from center", linePos);
        if (Math.abs(linePos) <= 1.0) {
            Robot.leds.setState(LEDs.State.CENTERED);
        } else {
            Robot.leds.clearState(LEDs.State.CENTERED);
        }
//        SmartDashboard.putNumber("line first seen", lineFirstSeen);
        // SmartDashboard.putNumber("time saw line", timeOnLine());
    }

    public boolean hasCargo() {
        return false;//TODO - make this not this.
    }

    public boolean hasHatch() {
        return false;//TODO - make this not this.
    }

    public double getYaw() {
        return navx.getYaw();
        // return 0;
    }
    public double getPitch() {
        return navx.getPitch();
    }
    public double getRoll() {
        return navx.getRoll();
    }
    public double getRealYAW() {
        return (navx.getYaw()+180);
    }
    public void resetNavx() {
        navx.reset();//TODO help
    }

    public double getContinuousHeading() {
        return navx.getAngle();
    }

    public double getFlippedContinuousHeading() {
        return -getContinuousHeading();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());

    }
    public boolean hasHatchCollector() {
        return false; //TODO make it return the sensor value
    }

    public double lineSensor() {
        return linePos;
    }

    public double timeOnLine() {

        if (!sawLine) return 0;
         double result = Timer.getFPGATimestamp()-lineFirstSeen;
         if (result < 0.05) {
             return 0;
         }
         return result;
    }

    // TODO implement in a smart way
    public void setHeading(Rotation2d rotation) {
    }

    public boolean isMoving() {
        return navx.isMoving();
    }

    MovingAverageFilter distance = new MovingAverageFilter(10);
    public double moved() {
        return distance.filter(navx.getDisplacementY());
    }

    MovingAverageFilter distance2 = new MovingAverageFilter(10);
    public double movedX() {
        return distance2.filter(navx.getDisplacementX());
    }

    public boolean hasStopped() {
        return (!navx.isMoving() && Robot.drivetrain.tryingToMove());
    }
}
