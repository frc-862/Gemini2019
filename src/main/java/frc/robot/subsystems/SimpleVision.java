package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.util.SerialByLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleVision extends Subsystem {
    private SerialPort camera;
    private Pattern pattern = Pattern.compile("SV(\\d+) (\\d+)(?:\\.\\d*)? (\\d+)(?:\\.\\d*)? (\\d+) (\\d+)");

    private int x, y, width, height;
    private double previousError;
    private double previousTimestamp;
    private double error;
    private double errorD;

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Vision X", x);
        SmartDashboard.putNumber("Vision Y", y);
        SmartDashboard.putNumber("Vision Width", width);
        SmartDashboard.putNumber("Vision Height", height);
        SmartDashboard.putNumber("Vision Error", getError());
    }

    public SimpleVision(SerialPort.Port port) {
        try {
            camera = new SerialPort(115200, port);
            camera.writeString("setmapping2 YUYV 320 240 50.0 Lightning SimpleVision\n");
//            camera.writeString("setpar serlog None\n");
            camera.writeString("setpar serout USB\n");
            camera.writeString("streamon\n");
            camera.flush();
            new SerialByLine(camera, this::positionUpdate);
        } catch(RuntimeException e) {
            System.out.println("Unable to open vision camera on port " + port);
        }
        System.out.println("Vision camera initialized on port " + port);
    }

    private void positionUpdate(String update) {
        double now = Timer.getFPGATimestamp();

        Matcher m = pattern.matcher(update);
        if (m.find()) {
            final int imageWidth = 320;
            final double imageMiddle = imageWidth / 2.0;

            x = Integer.parseInt(m.group(2));
            y = Integer.parseInt(m.group(3));
            width = Integer.parseInt(m.group(4));
            height = Integer.parseInt(m.group(5));

            double deltaT = previousTimestamp - now;
            previousTimestamp = now;
            error = (imageMiddle - x) / imageMiddle;
            errorD = (previousError - error) / deltaT;
            previousError = error;
        } else {
            error = 0;
            errorD = 0;
            System.out.println("Unable to process Simple Vision: " + update);
        }
    }

    public double getError() {
        return error;
    }

    public double getErrorD() {
        return errorD;
    }

    @Override
    protected void initDefaultCommand() { }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean simpleTargetFound() {
        return height > 0;
    }
}