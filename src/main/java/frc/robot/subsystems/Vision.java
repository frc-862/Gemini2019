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

  private final SerialPort.Port CAMERA_LEFT_PORT = SerialPort.Port.kUSB;
  private final SerialPort.Port CAMERA_RIGHT_PORT = SerialPort.Port.kUSB1;

  private enum Camera {
    LEFT, RIGHT;
  }

  ArrayList<Camera> activeCams = new ArrayList<Camera>();

  private boolean isStereo = false;
  private final double TRACK_WIDTH = 22.0;
  
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
            if(!inData.equals("")) {
              SmartDashboard.putString("Left data", inData);
            }
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
            if(!inData.equals("")) {
              SmartDashboard.putString("Right data", inData);
            }
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
    if(!activeCams.contains(side)) {
      return;
    }
    ArrayList<Target> data = new ArrayList<Target>();
    for(int i = 0; i < data.size(); i++) {
      int offsetMultiplier = 1; //Add or subtract camera offset based on which side we are on
      int squintMultiplier = 1;
      int squintRelationship = -1; //-1 if center squint < camera squint, 1 otherwise
      switch(side) {
        case LEFT:
          data = leftData;
          offsetMultiplier = -1;
          squintMultiplier = -1;
          break;
        case RIGHT:
          data = rightData;
          break;
      }
      Target t = data.get(i);
      double xComponent = Math.cos(Math.abs(t.squintRad())) * t.standoff();
      double yComponent = Math.sin(Math.abs(t.squintRad())) * t.standoff();
      double centerX = 0;
      if(Math.signum(t.squint()) == offsetMultiplier) {
        centerX = xComponent + TRACK_WIDTH / 2;
        squintRelationship *= -1;
      }
      else if(xComponent > TRACK_WIDTH / 2) {
        centerX = xComponent - TRACK_WIDTH / 2;
        squintMultiplier *= -1;
      }
      else {
        centerX = TRACK_WIDTH / 2 - xComponent;
      }
      double centerStandoff = Math.sqrt(Math.pow(centerX, 2) + Math.pow(yComponent, 2));
      double centerSquint = Math.atan(centerX / yComponent) * squintMultiplier;
      //If center squint is larger, subtract camera squint from it (center - cam)
      //If camera squint is larger, sebtract center squint from it (-center + cam)
      double cameraStandoff2centerStandoff = Math.abs(centerSquint) * squintRelationship - Math.abs(t.squintRad()) * squintRelationship;
      double centerRotation = t.rotation() + cameraStandoff2centerStandoff;

      data.set(i, new Target(centerStandoff, Math.toDegrees(centerRotation), Math.toDegrees(centerSquint), t.timestamp()));
    }

    
  }

  public ArrayList<ArrayList<Target>> dataTransformationUnitTest() {
    leftData = new ArrayList<Target>();
    rightData = new ArrayList<Target>();
    leftData.add(new Target(1, rotation, -45, 0));
    leftData.add(new Target(1, rotation, 45, 0));
    leftData.add(new Target(100, rotation, 45, 0));
    
    rightData.add(new Target(standoff, rotation, squint, 0));
    rightData.add(new Target(standoff, rotation, squint, 0));
    rightData.add(new Target(standoff, rotation, squint, 0));

    transformData(Camera.LEFT);
    transformData(Camera.RIGHT);

    ArrayList<ArrayList<Target>> transformedData = new ArrayList<ArrayList<Target>>();
    transformedData.add(leftData);
    transformedData.add(rightData);

    return transformedData;
  }

}
