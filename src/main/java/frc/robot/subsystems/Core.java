/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.FaultMonitor;
import frc.lightning.util.UnchangingFaultMonitor;
import frc.lightning.util.FaultCode.Codes;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Core extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commajnds.
  // private AHRS navx;
  private Compressor compressor = new Compressor(RobotMap.compressorCANId);
  // private PowerDistributionPanel pdp = new PowerDistributionPanel(RobotMap.pdpCANId);

  public Core() {
    // navx = new AHRS(SPI.Port.kMXP);
    // DataLogger.addDataElement("heading", () -> getHeading());

    // monitor if the heading is exactly the same, there is always 
    // some jitter in the reading, so this will not be the case
    // if we are getting valid values from the sensor for >= 3 seconds
    
    // FaultMonitor.register(new UnchangingFaultMonitor(Codes.NAVX_ERROR, () -> navx.getUpdateCount(), 
    //     2.0, 0, "NavX unresponsive"));

    // addChild("PDP", pdp);
    // addChild("NavX", navx);
    addChild("Compressor", compressor);
  }

  public double getHeading() {
    // return navx.getFusedHeading();
    return 0;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
