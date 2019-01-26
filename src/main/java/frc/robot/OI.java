/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.CollectCargo;
import frc.robot.commands.EjectCargo;
import frc.robot.commands.FourbarCollect;
import frc.robot.commands.FourbarEject;
import frc.robot.commands.HatchCollectorStateChange;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  //Drive Joysticks
  private Joystick driverRight = new Joystick(1);// Constants.driverRightJoy);   //0
  private Joystick driverLeft = new Joystick(0);// Constants.driverLeftJoy);   //1

  //Mechanism Buttons
  private Button collectButton = new JoystickButton(driverLeft, Constants.collectButton);//1
  private Button ejectButton = new JoystickButton(driverLeft, Constants.ejectButton);//2
  //private Button hatchToggle ;//= new JoystickButton(driverRight, Constants.hatchToggle);//1
  private Button fourbarCollect = new JoystickButton(driverRight, 1);
  private Button fourbarEject = new JoystickButton(driverRight, 2);

  public double getLeftPower() { 
    return (Math.abs(driverLeft.getRawAxis(Constants.leftThrottleAxis))>0.05) ? -driverLeft.getRawAxis(Constants.leftThrottleAxis) : 0.00;//glitch
    // return -driverLeft.getRawAxis(Constants.leftThrottleAxis); // GLITCH
    // return -driverLeft.getRawAxis(Constants.leftThrottleAxis); // OBOT
  }

  public double getRightPower() {
    return (Math.abs(driverRight.getRawAxis(Constants.rightThrottleAxis))>0.05) ? -driverRight.getRawAxis(Constants.rightThrottleAxis) : 0.00;//glitch
    // return -driverRight.getRawAxis(Constants.rightThrottleAxis); // GLITCH
    // return driverRight.getRawAxis(Constants.rightThrottleAxis); // OBOT
  }

  public double getRightSlider(){
    return (driverRight.getRawAxis(3)+1)/2;
  }
  
  public OI() {
    //shootButton.whenPressed(new MotionProfile());//used to be shoot
    //hatchToggle.whenPressed(new HatchCollectorStateChange());
    //collectButton.whileHeld(new CollectCargo());
    //ejectButton.whileHeld(new EjectCargo());
    //fourbarCollect.whileHeld(new FourbarCollect());
    //fourbarEject.whileHeld(new FourbarEject());
    //pneumaticButton.whenPressed(new OpenHatchCollector());
    //pneumaticButton.whenReleased(new CloseHatchCollector());
  }
}
