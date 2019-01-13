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
import frc.robot.commands.HatchCollectorStateChange;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  //Drive Joysticks
  private Joystick driverRight = new Joystick(Constants.driverRightJoy);//0
  private Joystick driverLeft = new Joystick(Constants.driverLeftJoy);//1

  //Mechanism Buttons
  private Button collectButton = new JoystickButton(driverLeft, Constants.collectButton);//1
  private Button ejectButton = new JoystickButton(driverLeft, Constants.ejectButton);//2
  private Button hatchToggle = new JoystickButton(driverRight, Constants.hatchToggle);//1

  public double getLeftPower() { 
    return -driverLeft.getRawAxis(Constants.leftThrottleAxis);
  }

  public double getRightPower() {
    return -driverRight.getRawAxis(Constants.rightThrottleAxis);
  }
  
  public OI() {
    //shootButton.whenPressed(new MotionProfile());//used to be shoot
    hatchToggle.whenPressed(new HatchCollectorStateChange());
    collectButton.whileHeld(new CollectCargo());
    ejectButton.whileHeld(new EjectCargo());
    //pneumaticButton.whenPressed(new OpenHatchCollector());
    //pneumaticButton.whenReleased(new CloseHatchCollector());
  }
}
