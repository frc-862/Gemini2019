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
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.Target;
import frc.lightning.util.NoTargetException;
import java.io.FileWriter;
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
  //Shorthand variables for these
  private final Target.Type COMPLETE = Target.Type.COMPLETE;
  private final Target.Type LEFT = Target.Type.LEFT;
  private final Target.Type RIGHT = Target.Type.RIGHT;

  private final SerialPort.Port CAMERA_LEFT_PORT = SerialPort.Port.kUSB;
  //private final SerialPort.Port CAMERA_RIGHT_PORT = SerialPort.Port.kOnboard;
  private final SerialPort.Port CAMERA_RIGHT_PORT = SerialPort.Port.kMXP;

  private enum Camera {
    LEFT, RIGHT;
  }

  ArrayList<Camera> activeCams = new ArrayList<Camera>();

  private boolean isStereo = true;
  private boolean newDataLeft = true, newDataRight = true;
  private final double TRACK_WIDTH = 21.5;

  private String leftPartialFrame1 = "", rightPartialFrame1 = "", leftPartialFrame2 = "", rightPartialFrame2 = "";
  
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
    leftData = new ArrayList<Target>(); //direct from camera
    rightData = new ArrayList<Target>(); //direct from camera

  }

  public void periodic() {
    
    collectRawData();
    processData();
    //SmartDashboard.putString("mergedData", mergedData.toString());
    
    
    //Target bestTarget = new Target(0,0,0,0);
    SmartDashboard.putString("mergedData", mergedData.toString());
    /*
    try {
      bestTarget = getBestTarget();
    }
    catch(NoTargetException e) {}
    SmartDashboard.putString("best target", bestTarget.toString());
    */
  }
  

/*
  public void startDataStream() {

  }

  public void stopDataStream() {
    
  }
*/
  public Target getBestTarget() throws NoTargetException {
    if(mergedData.size() == 0) throw new NoTargetException();
    Target best = new Target(COMPLETE, 0, 0, 0, 0, 999, 0); //Creating a first target and making squint 999 
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
    String CamName = "";
    for(Camera inCam : Camera.values()) {
      String inData = "";
      switch(inCam) {
        case LEFT:
        CamName = "leftCam ";
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
            //System.out.println("no left cam");
          //  leftData = new ArrayList<Target>();
            inData = "";
            continue;
          }
          break;
        case RIGHT:
        CamName = "rightCam ";
          try {
            inData = serialInRight.readString();
            //System.out.println(inData);
            if(!inData.equals("")) {
              SmartDashboard.putString("Right data", inData);
            }
            if(!activeCams.contains(Camera.RIGHT)) {
              activeCams.add(Camera.RIGHT);
            }
          }
          catch(Exception e) {
            activeCams.remove(Camera.RIGHT);
            System.out.println("no right cam");
          //  rightData = new ArrayList<Target>();
          inData = "";
            continue;
          }
          break;
      }
      try
{
    String filename= "/home/lvuser/log/CamLog.txt";
    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
    String fileLine = (CamName + inData + "\n");

    fw.write(fileLine);//appends the string to the file
    fw.close();
}
catch(Exception e) 
{
    System.err.println("IOException: " + e.getMessage());
}
      parseData(inData, inCam);
    }
    
  }

  private void parseData(String data, Camera cam) {
    long currentTime = System.currentTimeMillis();
    boolean successfulParse = true;
    String parseData = "";
    //If we got an incomplete frame last time, stitch that onto this frame in hopes of getting the complete frame
    switch(cam) {
      case LEFT:
        parseData = leftPartialFrame2 + leftPartialFrame1 + parseData;
        break;
      case RIGHT:
        parseData = rightPartialFrame2 + rightPartialFrame1 + parseData;
        break;
    }
    try {
      //Discard everything after the last '['
      String truncatedData = parseData.substring(0, parseData.lastIndexOf(']') + 1);
      //Discard everything before the beginning of the last frame
      String lastFrame = truncatedData.substring(truncatedData.lastIndexOf("Frame"));
      SmartDashboard.putString("Current data", lastFrame);
      //If we've gotten this far, we have a complete frame.
      int numTargets = Integer.parseInt(lastFrame.substring(lastFrame.indexOf("Targets:") + 8, lastFrame.indexOf(" (out")));

      ArrayList<Target> parsed = new ArrayList<Target>();
      String currentTarget;
      for(int i = 0; i < numTargets; i++) {
        SmartDashboard.putString("vision step", "grab target data");
        currentTarget = lastFrame.substring(lastFrame.indexOf("Target:" + i + "["));
        SmartDashboard.putString("vision step", "begin parse");
        String typeParse = currentTarget.substring(currentTarget.indexOf("type:") + 5, currentTarget.indexOf(",", currentTarget.indexOf("type:")));
        Target.Type type;
        switch(typeParse) {
          case "complete":
            type = COMPLETE;
            break;
          case "left":
            type = LEFT;
            break;
          case "right":
            type = RIGHT;
            break;
          //Ignore this target if it is of the type "not a target"
          default:
            continue;
        }
        int x, y;
        //x = Integer.parseInt(currentTarget.substring(currentTarget.indexOf("x:") + 2, currentTarget.indexOf(",", currentTarget.indexOf("y:"))));
        //y = Integer.parseInt(currentTarget.substring(currentTarget.indexOf("y:") + 2, currentTarget.indexOf(",", currentTarget.indexOf("y:"))));
        double standoff, rotation, squint;
        standoff = Double.parseDouble(currentTarget.substring(currentTarget.indexOf("standoff:") + 9, currentTarget.indexOf(",", currentTarget.indexOf("standoff:"))));
        SmartDashboard.putString("vision step", "parsed standoff");
        rotation = Double.parseDouble(currentTarget.substring(currentTarget.indexOf("rotation:") + 9, currentTarget.indexOf(",", currentTarget.indexOf("rotation:"))));
        SmartDashboard.putString("vision step", "parsed rotation");
        squint = Double.parseDouble(currentTarget.substring(currentTarget.indexOf("squint:") + 7, currentTarget.indexOf("]", currentTarget.indexOf("squint:"))));
        SmartDashboard.putString("vision step", "parsed squint");
        SmartDashboard.putString("vision step", "end parse");
        parsed.add(new Target(type, 0, 0, standoff, rotation, squint, Math.round(currentTime - latency)));
      }
      lastCameraUpdate = currentTime;
      lastFrameTime = Math.round(currentTime - latency);
      switch(cam) {
        case LEFT:
          leftData = new ArrayList<Target>();
          leftPartialFrame1 = "";
          leftPartialFrame2 = "";
          leftData = parsed;
          break;
        case RIGHT:
          rightData = new ArrayList<Target>();
          rightPartialFrame1 = "";
          rightPartialFrame2 = "";
          rightData = parsed;
          break;
      }
    }
    catch(Exception e) {
      successfulParse = false;
      switch(cam) {
        case LEFT:
        leftPartialFrame2 = leftPartialFrame1;
          leftPartialFrame1 = data;
          break;
        case RIGHT:
          rightPartialFrame2 = rightPartialFrame1;
          rightPartialFrame1 = data;
          break;
      }
    }
    switch(cam) {
      case LEFT:
        newDataLeft = successfulParse;
        break;
      case RIGHT:
        newDataRight = successfulParse;
        break;
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
    else if((leftData.size() == 1)
     && (rightData.size() == 1)
     && (leftData.get(0).type() == LEFT)
     && (rightData.get(0).type() == RIGHT)) {
       reconstructTarget();
    }
    else {
      transformData(Camera.LEFT);
      transformData(Camera.RIGHT);
      mergeData();
    }
    //System.out.println("\n");
    //System.out.println(leftData.toString());
    //System.out.println(rightData.toString());

  }

  private void reconstructTarget() {
    //TODO zero checks
    double standoff = 0, squint = 0, rotation = 0;

    Target l = leftData.get(0), r = rightData.get(0);

    //Calculate x and y of rectangles about robot center. Left is -, right is +.
    double xL, xR, yL, yR, xBar, yBar;

    xL = (l.standoff() * Math.sin(l.squint())) - (TRACK_WIDTH / 2);
    xR = (r.standoff() * Math.sin(r.squint())) + (TRACK_WIDTH / 2);
    yL = l.standoff() * Math.cos(l.squint());
    yR = r.standoff() * Math.cos(r.squint());

    xBar = (xL + xR) / 2;
    yBar = (yL + yR) / 2;

    standoff = Math.sqrt(Math.pow(xBar, 2) + Math.pow(yBar, 2));

    squint = Math.atan(yBar / xBar);

    //Slopes are on the level plane used above.
    double standoffSlope = yBar / xBar;
    double targetSlope = (yR - yL) / (xR - xL);
    //Slope of line orthogonal to center of target
    double normalSlope = -1 / targetSlope;

    if(Math.signum(standoffSlope) != Math.signum(normalSlope)) {
      //triangle from center of target to y-axis
      double target2centerY = Math.abs(xBar * targetSlope);
      //Angle of the above triangle which lays against the y-axis
      double y2targetSupplemental = Math.atan2(Math.abs(xBar), target2centerY);
      double y2target = Math.PI - y2targetSupplemental;
      double rotationComplementary = Math.PI - y2target - Math.abs(squint);
      rotation = (Math.PI / 2) - rotationComplementary;
    }
    else if(Math.abs(standoffSlope) < Math.abs(normalSlope)) {
      //triangle from center of target to y-axis
      double target2centerY = Math.abs(xBar * targetSlope);
      double target2horizontal = Math.atan(target2centerY / Math.abs(xBar));
      double squintComplementary = (Math.PI / 2) - Math.abs(squint);
      rotation = (Math.PI / 2) - target2horizontal - squintComplementary;
    }
    else {
      //triangle from center of target normal line to y-axis
      double normal2centerY = Math.abs(xBar * targetSlope);
      double normal2horizontal = Math.atan(normal2centerY / Math.abs(xBar));
      double squintComplementary = (Math.PI / 2) - Math.abs(squint);
      rotation = (squintComplementary - normal2horizontal) * -1;
    }
    mergedData.add(new Target(COMPLETE, 0, 0, standoff, rotation, squint, l.timestamp()));

  }

  private void transformData(Camera side) {
    if(!activeCams.contains(side)) {
      //System.out.println(side.toString() + "not connected");
      return;
    }
    //System.out.println("transforming");
    ArrayList<Target> data = new ArrayList<Target>();
    switch(side) {
      case LEFT:
        if(!newDataLeft) {
          //If this is the case, the data list contains data from a previous cycle and does not
          //need to be transformed again.
          //System.out.println("aborting transformation");
          return;
        }
        data = leftData;
        break;
      case RIGHT:
        if(!newDataRight) {
          //If this is the case, the data list contains data from a previous cycle and does not
          //need to be transformed again.
          //System.out.println("aborting transformation");
          return;
        }
        data = rightData;
        break;
    }
    for(int i = 0; i < data.size(); i++) {
      //System.out.println("transform " + side + " " + i);
      int offsetMultiplier = 1; //+ if on the right camera, - on left
      int squintMultiplier = 1; //the sign of squint as measured from the center (left -, right +)
      int case1or2 = 1; //-1 case 1, +1 if case 2
      boolean middleCase = false;
      switch(side) {
        case LEFT:
          offsetMultiplier = -1;
          squintMultiplier = -1;
          break;
        case RIGHT:
          break;
      }
      Target t = data.get(i);
      //Calculate absolute value components of camera standoff
      double xComponent = Math.sin(Math.abs(t.squintRad())) * t.standoff(); //side-to-side
      double yComponent = Math.cos(Math.abs(t.squintRad())) * t.standoff(); //forward
      double centerX = 0;

      //Calculate side-to-side component from the robot center
      //There are 3 cases:
        //1: target is farther out than camera
        //2: target is on opposite side of center from camera
        //3: target is between center and camera

      //Case 1
      if(Math.signum(t.squint()) == offsetMultiplier) {
        centerX = xComponent + (TRACK_WIDTH / 2.0);
        case1or2 *= -1;
      }
      //Case 2
      else if(xComponent > TRACK_WIDTH / 2.0) {
        centerX = xComponent - (TRACK_WIDTH / 2.0);
        squintMultiplier *= -1;
      }
      //Case 3
      else {
        centerX = (TRACK_WIDTH / 2.0) - xComponent;
        middleCase = true;
      }
      //Pythagorean theorem
      double centerStandoff = Math.sqrt(Math.pow(centerX, 2) + Math.pow(yComponent, 2));
      
      double centerSquint = 0;
      //Edge case - boundary between cases 1 and 3
      // if(Math.abs(t.squint()) < 0.01) {
      //   centerSquint = Math.atan(centerX / yComponent) * offsetMultiplier;
      //   //Math.atan2
      // }
      // else {
        centerSquint = Math.atan(centerX / yComponent) * squintMultiplier;
      // }
      double cameraStandoff2centerStandoff = 0; //180 - (target-camera-center) - (target-center-camera)
      //Are we in case 3 above?
      if(middleCase) {
        cameraStandoff2centerStandoff = Math.PI - (Math.PI / 2 - Math.abs(t.squintRad())) - (Math.PI / 2 - Math.abs(centerSquint));
      }
      else {
        //One of the squints is obtuse; the other is acute. Which is which depends on which case we have, so we use a multiplier to add or subtract from pi/2
        cameraStandoff2centerStandoff = Math.PI - ((Math.PI / 2) + Math.abs(centerSquint) * case1or2) - ((Math.PI / 2) - Math.abs(t.squintRad()) * case1or2);
      }
      //System.out.println(side + " " + i + " standoff2standoff = " + Math.toDegrees(cameraStandoff2centerStandoff));
      
      double centerRotation = t.rotRad() + (cameraStandoff2centerStandoff * offsetMultiplier);

      data.set(i, new Target(t.type(), t.x(), t.y(), centerStandoff, Math.toDegrees(centerRotation), Math.toDegrees(centerSquint), t.timestamp()));
    }
    switch(side) {
      case LEFT:
        leftData = data;
        break;
      case RIGHT:
        rightData = data;
        break;
    }
  }

  private void mergeData() {
    for(int i = 0; i < leftData.size(); i++) {
      Target l = leftData.get(i);
      for(int j = 0; j < rightData.size(); j++) {
        Target r = rightData.get(j);
        if((Math.abs(l.standoff() - r.standoff()) <= 10) && (Math.abs(l.squint() - r.squint()) < 10)) {
          mergedData.add(new Target(l, r));
          rightData.remove(j);
          leftData.remove(i);
          --i;
          break;
        }
      }
    }
    for(Target t : leftData) {
      mergedData.add(t);
    }
    for(Target t : rightData) {
      mergedData.add(t);
    }
  }

  public ArrayList<ArrayList<Target>> dataTransformationUnitTest() {
    leftData = new ArrayList<Target>();
    rightData = new ArrayList<Target>();

    //standoff, rotation, squint, time
    leftData.add(new Target(COMPLETE, 0, 0, 1, 10, 0, 0));
    leftData.add(new Target(COMPLETE, 0, 0, 1, -10, 0, 0));

    leftData.add(new Target(COMPLETE, 0, 0, 1, 10, -45, 0));
    leftData.add(new Target(COMPLETE, 0, 0, 1, -10, -45, 0));

    leftData.add(new Target(COMPLETE, 0, 0, 1, 10, 45, 0));
    leftData.add(new Target(COMPLETE, 0, 0, 1, -10, 45, 0));

    leftData.add(new Target(COMPLETE, 0, 0, 100, 10, 45, 0));
    leftData.add(new Target(COMPLETE, 0, 0, 100, -10, 45, 0));

    rightData.add(new Target(COMPLETE, 0, 0, 1, 10, 0, 0));
    rightData.add(new Target(COMPLETE, 0, 0, 1, -10, 0, 0));

    rightData.add(new Target(COMPLETE, 0, 0, 1, 10, 45, 0));
    rightData.add(new Target(COMPLETE, 0, 0, 1, -10, 45, 0));

    rightData.add(new Target(COMPLETE, 0, 0, 1, 10, -45, 0));
    rightData.add(new Target(COMPLETE, 0, 0, 1, -10, -45, 0));
    
    rightData.add(new Target(COMPLETE, 0, 0, 100, 10, -45, 0));
    rightData.add(new Target(COMPLETE, 0, 0, 100, -10, -45, 0));
    
    newDataLeft = true;
    newDataRight = true;

    transformData(Camera.LEFT);
    transformData(Camera.RIGHT);

    ArrayList<ArrayList<Target>> transformedData = new ArrayList<ArrayList<Target>>();
    transformedData.add(leftData);
    transformedData.add(rightData);

    return transformedData;
  }

}
