/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.misc;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.LightningRobot;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class AutonSelect{
    public AutonSelect(){}
    
    public String getSelectedPath(){

        String start = Robot.getSelectedStartPos();
        String gen   = Robot.getSelectedGenDestin();
        String spec  = Robot.getSelectedSpecificDestin();
        String path  = this.selectPath(start, gen, spec);
        SmartDashboard.putString("Selected_Path", path);
        return path;
        
    }

    private String selectPath(String start, String genDestin, String specificDestin) {
        String selectedPath = "";//default left rocket near
        switch(genDestin) {
        case "ROCK_RIGHT":
            selectedPath += ("RocketR_" + selectStart(start, specificDestin));
            break;
        case "SHIP_FRONT":
            selectedPath += ("CargoC_" + selectStart(start, specificDestin));
            break;
        case "SHIP_LEFT":
            selectedPath += ("CargoL_" + selectStart(start, specificDestin));
            break;
        case "SHIP_RIGHT":
            selectedPath += ("CargoR_" + selectStart(start, specificDestin));
            break;
        default://ROCK_LEFT
            selectedPath += ("RocketL_" + selectStart(start, specificDestin));
            break;
        }
        return selectedPath;
    }

    private String selectStart(String start, String specificDestin) {
        String nextSeg = "";
        switch(start) {
        case "CENTER":
            nextSeg += ("StartM_" + selectDestin(specificDestin));
            break;
        case "RIGHT":
            nextSeg += ("StartR_" + selectDestin(specificDestin));
            break;
        default://LEFT
            nextSeg += ("StartL_" + selectDestin(specificDestin));
            break;
        }
        return nextSeg;
    }

    private String selectDestin(String specificDestin) {
        String nextSeg = "";
        switch(specificDestin) {
        case "FAR":
            nextSeg += ("EndF");
            break;
        case "RIGHT":
            nextSeg += ("EndR");
            break;
        case "LEFT":
            nextSeg += ("EndL");
            break;
        case "MID":
            nextSeg += ("EndM");
            break;
        default://NEAR
            nextSeg += ("EndN");
            break;
        }
        return nextSeg;
    }


}
