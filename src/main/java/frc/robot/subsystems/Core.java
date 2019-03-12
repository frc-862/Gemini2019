/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.CommandLogger;
import frc.lightning.logging.DataLogger;
import frc.lightning.testing.SystemTest;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.test.NavXTest;
import java.util.function.DoubleSupplier;

/**
 * Add your docs here.
 */
public class Core extends Subsystem {

    CommandLogger logger = new CommandLogger(getClass().getSimpleName());
    //private DigitalInput pressure1 = new DigitalInput(0);

//    private WPI_VictorSPX extra1 = new WPI_VictorSPX(RobotMap.extra1CanId);
//     private WPI_VictorSPX extra2 = new WPI_VictorSPX(RobotMap.extra2CanId);

    private DigitalInput outerLeft = new DigitalInput(5);
    private DigitalInput midLeft = new DigitalInput(4);
    private AnalogInput innerLeft = new AnalogInput(3);
    private AnalogInput centerLeft = new AnalogInput(2);

    private AnalogInput centerRight = new AnalogInput(1);
    private AnalogInput innerRight = new AnalogInput(0);
    private DigitalInput midRight = new DigitalInput(3);
    private DigitalInput outerRight = new DigitalInput(2);



    private double lineFirstSeen = Timer.getFPGATimestamp();
    // Put methods for controlling this subsystem
    // here. Call these from Commajnds.
    private AHRS navx;
    private Compressor compressor = new Compressor(RobotMap.compressorCANId);
    private PowerDistributionPanel pdp = new PowerDistributionPanel(RobotMap.pdpCANId);


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

    private double[] lineWeights = {-7, -5, -3, -1, 1, 3, 5, 7};


    private DoubleSupplier[] nebulaSensorValues = {
        () -> outerLeft.get() ? 0 : 1.0,
        () -> midLeft.get() ? 0 : 1.0,
        () -> biasAnalog(innerLeft.getVoltage(), Constants.nebulaMinLeftOutside, Constants.nebulaMaxLeftOutside),
        () -> biasAnalog(centerLeft.getVoltage(), Constants.nebulaMinLeftInside, Constants.nebulaMaxLeftInside),
        () -> biasAnalog(centerRight.getVoltage(), Constants.nebulaMinRightInside, Constants.nebulaMaxRightInside),
        () -> biasAnalog(innerRight.getVoltage(), Constants.nebulaMinRightOutside, Constants.nebulaMaxRightOutside),
        () -> midRight.get() ? 0 : 1.0,
        () -> outerRight.get() ? 0 : 1.0,
    };

    private DoubleSupplier[] geminiSensorValues = {

        () -> outerLeft.get() ? 0 : 1.0,
        () -> midLeft.get() ? 0 : 1.0,
        () -> biasAnalog(innerLeft.getVoltage(), Constants.geminiMinLeftOutside, Constants.geminiMaxLeftOutside),
        () -> biasAnalog(centerLeft.getVoltage(), Constants.geminiMinLeftInside, Constants.geminiMaxLeftInside),
        () -> biasAnalog(centerRight.getVoltage(), Constants.geminiMinRightInside, Constants.geminiMaxRightInside),
        () -> biasAnalog(innerRight.getVoltage(), Constants.geminiMinRightOutside, Constants.geminiMaxRightOutside),
        () -> midRight.get() ? 0 : 1.0,
        () -> outerRight.get() ? 0 : 1.0,
    };

    private DoubleSupplier[] rawSensorValues = {
        () -> innerLeft.getVoltage(),
        () -> centerLeft.getVoltage(),
        () -> centerRight.getVoltage(),
        () -> innerRight.getVoltage(),

    };

    private DoubleSupplier[] sensorValues = Robot.isGemini() ? geminiSensorValues : nebulaSensorValues;

    public Core() {
        setName("Core");

        addChild("Compressor", compressor);

        navx = new AHRS(SPI.Port.kMXP);
        addChild("NavX", navx);
        DataLogger.addDataElement("YAW", () -> getYaw());
        DataLogger.addDataElement("PITCH", () -> getPitch());
        DataLogger.addDataElement("ROLL", () -> getRoll());

        int i = -3;
        for (var sensor : rawSensorValues) {
            DataLogger.addDataElement("LineFollow" + i, () -> sensor.getAsDouble());
            i += 2;
        }

        logger.addDataElement("YAW");
        logger.addDataElement("PITCH");
        logger.addDataElement("ROLL");

        // monitor if the heading is exactly the same, there is always
        // some jitter in the reading, so this will not be the case
        // if we are getting valid values from the sensor for >= 3 seconds

        // FaultMonitor.register(new UnchangingFaultMonitor(Codes.NAVX_ERROR, () -> navx.getUpdateCount(),
        //     2.0, 0, "NavX unresponsive"));

        addChild("PDP", pdp);
        // Stops pdp from whining about things we don't care about.
        // EX - CAN Frame timeout, etc.
        LiveWindow.disableTelemetry(pdp);

        addChild("outerLeft", outerLeft);
        addChild("midLeft", midLeft);
        addChild("innerLeft", innerLeft);
        addChild("centerLeft", centerLeft);
        addChild("centerRight", centerRight);
        addChild("innerRight", innerRight);
        addChild("midRight", midRight);
        addChild("outerRight", outerRight);

        // addChild("extra motor 1", extra1);
        // addChild("extra motor 2", extra2);

        SystemTest.register(new NavXTest());
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Heading", navx.getFusedHeading());
        SmartDashboard.putNumber("Angle", navx.getAngle());

        SmartDashboard.putNumber("YAW", navx.getYaw());
        SmartDashboard.putNumber("PITCH", navx.getPitch());
        SmartDashboard.putNumber("ROLL", navx.getRoll());

        SmartDashboard.putNumber("X", navx.getRawGyroX());
        SmartDashboard.putNumber("Y", navx.getRawGyroY());
        SmartDashboard.putNumber("Z", navx.getRawGyroZ());

        logger.set("YAW", getYaw());
        logger.set("PITCH", getPitch());
        logger.set("ROLL", getRoll());


        boolean sawLine = false;

        int pos = -7;
        for (DoubleSupplier sensor : sensorValues) {
            double value=sensor.getAsDouble();
            SmartDashboard.putNumber("Line " + pos, value);
            if(value>.15) {
                sawLine=true;
            }
            pos += 2;
        }

        if(!sawLine) {
            lineFirstSeen=Timer.getFPGATimestamp();
        }

        pos = -3;
        for (DoubleSupplier sensor : rawSensorValues) {

            SmartDashboard.putNumber("Raw Line " + pos, sensor.getAsDouble());

            pos += 2;
        }
        SmartDashboard.putNumber("Distance from center", lineSensor());
        SmartDashboard.putNumber("line first seen", lineFirstSeen);
        SmartDashboard.putNumber("time saw line", timeOnLine());
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
        double weight = 0;
        double sensors = 0;
        for(int loop=0; loop <= 7; loop++) {
            double value = sensorValues[loop].getAsDouble();
            weight += value * lineWeights[loop];
            sensors += value;
        }

        return weight / sensors;
    }

    public double timeOnLine() {

        return Math.max(0, Timer.getFPGATimestamp()-lineFirstSeen);
    }

    public DoubleSupplier getRawData(int a) {
        return rawSensorValues[a];
    }
}
