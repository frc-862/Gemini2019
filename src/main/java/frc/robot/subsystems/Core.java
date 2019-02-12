/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Arrays;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.testing.SystemTest;
import frc.lightning.util.FaultMonitor;
import frc.lightning.util.UnchangingFaultMonitor;
import frc.lightning.util.FaultCode.Codes;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.commands.test.NavXTest;

/**
 * Add your docs here.
 */
public class Core extends Subsystem {
    //private DigitalInput pressure1 = new DigitalInput(0);

    private VictorSPX extra1 = new VictorSPX(RobotMap.extra1CanId);
    private VictorSPX extra2 = new VictorSPX(RobotMap.extra2CanId);

    private DigitalInput outerLeft = new DigitalInput(5);
    private DigitalInput midLeft = new DigitalInput(4);
    private AnalogInput innerLeft = new AnalogInput(3);
    private AnalogInput centerLeft = new AnalogInput(2);

    private AnalogInput centerRight = new AnalogInput(1);
    private AnalogInput innerRight = new AnalogInput(0);
    private DigitalInput midRight = new DigitalInput(3);
    private DigitalInput outerRight = new DigitalInput(2);

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

    private double lineWeights[] = { -7, -5, -3, -1, 1, 3, 5, 7};

    private DoubleSupplier nebulaSensorValues[] = {
        () -> outerLeft.get() ? 0 : 1.0,
        () -> midLeft.get() ? 0 : 1.0,
        () -> biasAnalog(innerLeft.getVoltage(), Constants.nebulaMinLeftOutside, Constants.nebulaMaxLeftOutside),
        () -> biasAnalog(centerLeft.getVoltage(), Constants.nebulaMinLeftInside, Constants.nebulaMaxLeftInside),
        () -> biasAnalog(centerRight.getVoltage(), Constants.nebulaMinRightInside, Constants.nebulaMaxRightInside),
        () -> biasAnalog(innerRight.getVoltage(), Constants.nebulaMinRightOutside, Constants.nebulaMaxRightOutside),
        () -> midRight.get() ? 0 : 1.0,
        () -> outerRight.get() ? 0 : 1.0,
    };

    private DoubleSupplier geminiSensorValues[] = {

        () -> outerLeft.get() ? 0 : 1.0,
        () -> midLeft.get() ? 0 : 1.0,
        () -> biasAnalog(innerLeft.getVoltage(), Constants.geminiMinLeftOutside, Constants.geminiMaxLeftOutside),
        () -> biasAnalog(centerLeft.getVoltage(), Constants.geminiMinLeftInside, Constants.geminiMaxLeftInside),
        () -> biasAnalog(centerRight.getVoltage(), Constants.geminiMinRightInside, Constants.geminiMaxRightInside),
        () -> biasAnalog(innerRight.getVoltage(), Constants.geminiMinRightOutside, Constants.geminiMaxRightOutside),
        () -> midRight.get() ? 0 : 1.0,
        () -> outerRight.get() ? 0 : 1.0,
    };

    private DoubleSupplier sensorValues[] = Robot.isGemini() ? geminiSensorValues : nebulaSensorValues;

    // Put methods for controlling this subsystem
    // here. Call these from Commajnds.
    private AHRS navx;
    private Compressor compressor = new Compressor(RobotMap.compressorCANId);
    private PowerDistributionPanel pdp = new PowerDistributionPanel(RobotMap.pdpCANId);

    public Core() {
        compressor.setSubsystem("Core");
        if (compressor.getCompressorNotConnectedFault()) {
            LiveWindow.disableTelemetry(compressor);
        }

        navx = new AHRS(SPI.Port.kMXP);
        navx.setSubsystem("Core");

        DataLogger.addDataElement("heading", () -> getHeading());

        // monitor if the heading is exactly the same, there is always
        // some jitter in the reading, so this will not be the case
        // if we are getting valid values from the sensor for >= 3 seconds

        // FaultMonitor.register(new UnchangingFaultMonitor(Codes.NAVX_ERROR, () -> navx.getUpdateCount(),
        //     2.0, 0, "NavX unresponsive"));

        addChild("PDP", pdp);
        addChild("NavX", navx);
        addChild("Compressor", compressor);

        LiveWindow.disableTelemetry(pdp);//Stops pdp from whining about things we don't care about. EX - CAN Frame timeout, etc.

        SystemTest.register(new NavXTest());
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Heading", navx.getFusedHeading());
        SmartDashboard.putNumber("Angle", navx.getAngle());

        int pos = -7;
        for (DoubleSupplier sensor : sensorValues) {
            SmartDashboard.putNumber("Line " + pos, sensor.getAsDouble());
            pos += 2;
        }

        SmartDashboard.putNumber("Distance from center", lineSensor());
    }

    public double getHeading() {
        return navx.getFusedHeading();
        // return 0;
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
}
