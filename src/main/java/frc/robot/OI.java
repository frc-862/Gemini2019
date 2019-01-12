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
import frc.robot.commands.CloseHatchCollector;
import frc.robot.commands.MotionProfile;
import frc.robot.commands.OpenHatchCollector;
import frc.robot.commands.Shoot;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  private Joystick driverRight = new Joystick(0);
  private Joystick driverLeft = new Joystick(1);
  private Joystick copilot = new Joystick(2);

  private Button shootButton = new JoystickButton(copilot, RobotMap.shootButton);

  private Button pneumaticButton = new JoystickButton(driverRight, 1);

  public double getLeftPower() { 
    return -driverLeft.getRawAxis(Constants.leftThrottleAxis);
  }

  public double getRightPower() {
    return -driverRight.getRawAxis(Constants.rightThrottleAxis);
  }
  
  public OI() {
    //shootButton.whenPressed(new MotionProfile());//used to be shoot
    pneumaticButton.whenPressed(new OpenHatchCollector());
    pneumaticButton.whenReleased(new CloseHatchCollector());
  }
}
