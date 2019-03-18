package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.util.SerialByLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleVision extends Subsystem {
    private SerialPort camera;
    private Pattern pattern = Pattern.compile("SV(\\d+) (\\d+)(?:\\.\\d*)? (\\d+)(?:\\.\\d*)? (\\d+) (\\d+)");

    private int x, y, width, height;

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Vision X", x);
        SmartDashboard.putNumber("Vision Y", y);
        SmartDashboard.putNumber("Vision Width", width);
        SmartDashboard.putNumber("Vision Height", height);
        SmartDashboard.putNumber("Vision Error", getError());
    }

    public double getError() {
        // Might be smarter to return last error before we lost the
        // target, but that is also confusing
        if (!simpleTargetFound()) return 0;

        final int imageWidth = 320;
        final double imageMiddle = imageWidth / 2.0;

        return (imageMiddle - x) / imageMiddle;
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
        Matcher m = pattern.matcher(update);
        if (m.find()) {
            x = Integer.parseInt(m.group(2));
            y = Integer.parseInt(m.group(3));
            width = Integer.parseInt(m.group(4));
            height = Integer.parseInt(m.group(5));
        } else {
            System.out.println("Unable to process Simple Vision: " + update);
        }
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
