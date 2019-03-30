package frc.robot.commands.vision;

import frc.robot.Robot;
import frc.robot.commands.LineFollow;

public class SimpleFollowVisionWithLine extends SimpleFollowVision {
    protected void execute() {
        if (Robot.core.timeOnLine() > 0.1) {
            (new LineFollow()).start();
        } else {
            super.execute();
        }
    }
}
