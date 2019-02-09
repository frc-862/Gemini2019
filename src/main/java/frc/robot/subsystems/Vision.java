/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

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
public class Vision extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private SerialPort serialInLeft, serialInRight;
  private double connected = 0;
  private long lastCameraUpdate = 0; //time the most recent frame was RECIEVED
  private long lastFrameTime = 0; //time the most recent frame was TAKEN
  private ArrayList<Target> targets;
  private ArrayList<Target> mergedData;
  private ArrayList<Target> leftData;
  private ArrayList<Target> rightData;
  private double latency = 0;

  private final SerialPort.Port CAMERA_LEFT_PORT = SerialPort.Port.kUSB1;
  private final SerialPort.Port CAMERA_RIGHT_PORT = SerialPort.Port.kUSB2;

  private enum Camera {
    LEFT, RIGHT;
  }

  ArrayList<Camera> activeCams = new ArrayList<Camera>();

  private boolean isStereo = false;
  
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public Vision() {
    try {
      serialInLeft = new SerialPort(115200, CAMERA_LEFT_PORT);
      activeCams.add(Camera.LEFT);
      serialInLeft.writeString("streamon\n");
    } catch(RuntimeException e) {}
    try {
      serialInRight = new SerialPort(115200, CAMERA_RIGHT_PORT);
      activeCams.add(Camera.RIGHT);
      serialInRight.writeString("streamon\n");
    } catch(RuntimeException e) {}
  //latency = testLatency();
  latency = 0;
     
    targets = new ArrayList<Target>(); //running list
    mergedData = new ArrayList<Target>(); //direct from camera

  }

  public void periodic() {
    collectRawData();
    processData();
    //SmartDashboard.putString("mergedData", mergedData.toString());
  }
/*
  public void startDataStream() {

  }

  public void stopDataStream() {
    
  }
*/
  public Target getBestTarget() throws NoTargetException {
    if(mergedData.size() == 0) throw new NoTargetException();
    Target best = new Target(0, 0, 999, 0); //Creating a first target and making squint 999
    for(Target t : mergedData) {
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
    boolean successfulTest = false;
    long sendTime = 0, readTime = 0;
    while(!successfulTest) {
      serialInRight.writeString("ping");
      sendTime = System.currentTimeMillis();
      String response = "";
      do {
        response = serialInRight.readString();
        readTime = System.currentTimeMillis();
        if(response.indexOf("ALIVE") != -1) successfulTest = true;
      } while(!successfulTest && readTime - sendTime <= 50);
    }
    return (readTime - sendTime) / 2.0;
    
  }

  private void collectRawData() {
    //Reset data list and read from serial port
    //SmartDashboard.putString("vision step", "start");
    for(Camera inCam : Camera.values()) {
      String inData = "";
      switch(inCam) {
        case LEFT:
          try {
            inData = serialInLeft.readString();
            if(!activeCams.contains(Camera.LEFT)) {
              activeCams.add(Camera.LEFT);
            }
          }
          catch(Exception e) {
            activeCams.remove(Camera.LEFT);
            continue;
          }
          break;
        case RIGHT:
          try {
            inData = serialInRight.readString();
            if(!activeCams.contains(Camera.RIGHT)) {
              activeCams.add(Camera.RIGHT);
            }
          }
          catch(Exception e) {
            activeCams.remove(Camera.RIGHT);
            continue;
          }
          break;
      }
      parseData(inData, inCam);
    }
    
  }

  private void parseData(String data, Camera cam) {
    long currentTime = System.currentTimeMillis();
    //Check that basic frame information is intact
    if(data.indexOf("=") != -1) {
      //Collect data
      //SmartDashboard.putString("vision step", "collect");
      String lastFrame = data.substring(data.lastIndexOf("Frame"));
      //long frameNum = Long.parseLong(lastFrame.substring(lastFrame.indexOf("Frame:") + 6, lastFrame.indexOf(",")));
      int numTargets = Integer.parseInt(lastFrame.substring(lastFrame.indexOf("Targets:") + 8, lastFrame.indexOf(" (out")));
      
      SmartDashboard.putString("Current data", lastFrame);
      ArrayList<Target> parsed = new ArrayList<Target>();
      if(numTargets == 0) {
        lastCameraUpdate = currentTime;
        SmartDashboard.putString("vision step", "no targets");
      }
      //Check that target information is intact
      else if(lastFrame.endsWith("]\r\n")) {
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
          parsed.add(new Target(standoff, rotation, squint, Math.round(currentTime - latency)));
        }
        lastCameraUpdate = currentTime;
        lastFrameTime = Math.round(currentTime - latency);
        switch(cam) {
          case LEFT:
            leftData = parsed;
            break;
          case RIGHT:
            rightData = parsed;
            break;
        }
      }
    }
  }

  private void processData() {
    if(!isStereo) {
      if(activeCams.contains(Camera.LEFT)) {
        mergedData = leftData;
      }
      else {
        mergedData = rightData;
      }
      return;
    }
    transformData(Camera.LEFT);
    transformData(Camera.RIGHT);
  }

  private void transformData(Camera side) {
    ArrayList<Target> data;
    switch(side) {
      case LEFT:
        break;
      case RIGHT:
        break;
    }
  }

}
