package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SimpleVision extends Subsystem {
    int openTries = 0;
    SerialPort camera;
    String buffer;

    public SimpleVision() {
    }

    private void openCamera() {
        try {
            camera = new SerialPort(115200, SerialPort.Port.kMXP);
            camera.writeString("streamon\n");
        } catch(RuntimeException e) {}
    }

    @Override
    public void periodic() {
        if (camera == null) {
            if (openTries < 1000) {
                openCamera();
                openTries += 1;
            }
        } else {
            var msg = camera.readString();
            buffer += msg;
            int pos = buffer.indexOf('\n');
            while (pos != -1) {
                String line = buffer.substring(0, pos);
                processLine(line);
                buffer = buffer.substring(pos + 1);
                pos = buffer.indexOf('\n');
            }
        }
    }

    public void processLine(String line) {
        //TODO process line
    }

    @Override
    protected void initDefaultCommand() {

    }
}
