/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lightning.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.Target;
import frc.lightning.util.NoTargetException;

/**
 * Add your docs here.
 */
public class VisionSystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private SerialPort serialIn;
  private double connected = 0;
  private long lastCameraUpdate = 0; //time the most recent frame was RECIEVED
  private long lastFrameTime = 0; //time the most recent frame was TAKEN
  private ArrayList<Target> targets;
  private ArrayList<Target> cameraData;
  private double latency = 0;
  
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public VisionSystem() {
    DataLogger.addDataElement("Camera connection status", () -> connected);
    try {
			serialIn = new SerialPort(115200, SerialPort.Port.kUSB);
      serialIn.writeString("streamon\n");
      latency = testLatency();
      connected = 1;
		} catch(RuntimeException e) {
      
    }
    
    targets = new ArrayList<Target>(); //running list
    cameraData = new ArrayList<Target>(); //direct from camera

  }

  public void periodic() {
    if(connected == 0) {
      try {
        serialIn = new SerialPort(115200, SerialPort.Port.kUSB);
        serialIn.writeString("streamon\n");
        connected = 1;
      } catch(RuntimeException e) {
        
      }
    }
    if (serialIn != null) {
			try {
        collectData();
			} catch (Exception err) {
        connected = 0;
      }
		}
  }
/*
  public void startDataStream() {

  }

  public void stopDataStream() {
    
  }
*/
  public Target getBestTarget() throws NoTargetException {
    if(cameraData.size() == 0) throw new NoTargetException();
    Target best = new Target(0, 0, 999, 0); //Creating a first target and making squint 999
    for(Target t : cameraData) {
      if(Math.abs(t.squint()) < Math.abs(best.squint())) best = t;
    }
    return best;
  }

  public double getLatency() {
    return latency;
  }

  /**
   * Pings the camera and returns the latency in the camera's response 
   * (half of the time from sending the ping to recieving the response).
   */
  public double testLatency() {
    if(connected == 0) return 0;
    boolean successfulTest = false;
    long sendTime = 0, readTime = 0;
    while(!successfulTest) {
      serialIn.writeString("ping");
      sendTime = System.currentTimeMillis();
      String response = "";
      do {
        response = serialIn.readString();
        readTime = System.currentTimeMillis();
        if(response.indexOf("ALIVE") != -1) successfulTest = true;
      } while(!successfulTest && readTime - sendTime <= 50);
    }
    return (readTime - sendTime) / 2.0;
    
  }

  private void collectData() {
    //Reset data list and read from serial port
    //SmartDashboard.putString("vision step", "start");
    String inData = serialIn.readString();
    long currentTime = System.currentTimeMillis();
    //Check that basic frame information is intact
    if(inData.indexOf("=") != -1) {
      //Collect data
      //SmartDashboard.putString("vision step", "collect");
      String lastFrame = inData.substring(inData.lastIndexOf("Frame"));
      //long frameNum = Long.parseLong(lastFrame.substring(lastFrame.indexOf("Frame:") + 6, lastFrame.indexOf(",")));
      int numTargets = Integer.parseInt(lastFrame.substring(lastFrame.indexOf("Targets:") + 8, lastFrame.indexOf(" (out")));
      
      SmartDashboard.putString("Current data", lastFrame);
      if(numTargets == 0) {
        lastCameraUpdate = currentTime;
        cameraData = new ArrayList<Target>();
        SmartDashboard.putString("vision step", "no targets");
      }
      //Check that target information is intact
      else if(lastFrame.endsWith("]\r\n")) {
        cameraData = new ArrayList<Target>();
        String currentTarget;
        for(int i = 0; i < numTargets; i++) {
          SmartDashboard.putString("vision step", "grab target data");
          currentTarget = lastFrame.substring(lastFrame.indexOf("Target:" + i + "["));
          SmartDashboard.putString("vision step", "begin parse");
          double standoff, rotation, squint;
          standoff = Double.parseDouble(currentTarget.substring(currentTarget.indexOf("standoff:") + 9, currentTarget.indexOf(",", currentTarget.indexOf("standoff:"))));
          SmartDashboard.putString("vision step", "parsed standoff");
          rotation = Double.parseDouble(currentTarget.substring(currentTarget.indexOf("rotation:") + 9, currentTarget.indexOf(",", currentTarget.indexOf("rotation:"))));
          SmartDashboard.putString("vision step", "parsed rotation");
          squint = Double.parseDouble(currentTarget.substring(currentTarget.indexOf("squint:") + 7, currentTarget.indexOf("]", currentTarget.indexOf("squint:"))));
          SmartDashboard.putString("vision step", "parsed squint");
          SmartDashboard.putString("vision step", "end parse");
          cameraData.add(new Target(standoff, rotation, squint, Math.round(currentTime - latency)));
        }
        lastCameraUpdate = currentTime;
        lastFrameTime = Math.round(currentTime - latency);
      }
      else {
        
      }
    }
  }

}
