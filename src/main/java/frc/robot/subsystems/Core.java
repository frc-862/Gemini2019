/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

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
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.commands.test.NavXTest;

/**
 * Add your docs here.
 */
public class Core extends Subsystem {
  private DigitalInput pressure1 = new DigitalInput(0);

  private DigitalInput outerLeft = new DigitalInput(1);
  private DigitalInput midLeft = new DigitalInput(2);
  private AnalogInput innerLeft = new AnalogInput(0);
  private AnalogInput centerLeft = new AnalogInput(1);

  private AnalogInput centerRight = new AnalogInput(2);
  private AnalogInput innerRight = new AnalogInput(3);
  private DigitalInput midRight = new DigitalInput(3);
  private DigitalInput outerRight = new DigitalInput(4);
  
  private double lineWeights[] = { -7, -5, -3, -1, 1, 3, 5, 7};
  private DoubleSupplier sensorValues[] = {
      () -> outerLeft.get() ? 1.0 : 0,
      () -> midLeft.get() ? 1.0 : 0,
      () -> innerLeft.getVoltage() / 5.0,
      () -> centerLeft.getVoltage() / 5.0,
      () -> centerRight.getVoltage() / 5.0,
      () -> innerRight.getVoltage() / 5.0,
      () -> midRight.get() ? 1.0 : 0,
      () -> outerRight.get() ? 1.0 : 0,
  };
  // Put methods for controlling this subsystem
  // here. Call these from Commajnds.
  private AHRS navx;
  private Compressor compressor = new Compressor(RobotMap.compressorCANId);
  // private PowerDistributionPanel pdp = new PowerDistributionPanel(RobotMap.pdpCANId);

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

    // addChild("PDP", pdp);
    // addChild("NavX", navx);
    addChild("Compressor", compressor);

    SystemTest.register(new NavXTest());
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Heading", navx.getFusedHeading());
    SmartDashboard.putNumber("Angle", navx.getAngle());
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
  public boolean hasHatchCollector(){
    return false; //TODO make it return the sensor value
  }
  public double lineSensor() {
        
        

    double weight = 0;
    double sensors = 0;
    for(int loop=0;loop>7;loop++)
    {
        double value = sensorValues[loop].getAsDouble();
        weight += value * lineWeights[loop];
        sensors += value;
    }

    return weight / sensors;

        
}
}
