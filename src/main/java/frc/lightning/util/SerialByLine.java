package frc.lightning.util;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

import java.util.function.Consumer;

public class SerialByLine {
    private final int baud;
    private final SerialPort.Port ptype;
    private Consumer<String> fn;
    private SerialPort port;
    private String buffer = "";
    private double lastGotData = Timer.getFPGATimestamp();

    public SerialByLine(SerialPort port, Consumer<String> fn, int baud, SerialPort.Port ptype) {
        this.baud = baud;
        this.ptype = ptype;
        this.port = port;
        this.fn = fn;
        new Thread(this::loop).start();
    }

    private void loop() {
        while (true) {
            try {
                int bytes = port.getBytesReceived();
                if (bytes > 0) {
                    lastGotData = Timer.getFPGATimestamp();
                    if (buffer.isEmpty()) {
                        var buf = port.read(bytes);
                        buffer = new String(buf);
                    } else {
                        buffer += port.readString();
                    }

                    int pos = buffer.indexOf('\n');
                    while (pos != -1) {
                        // we have a complete line
                        fn.accept(buffer.substring(0, pos));
                        buffer = buffer.substring(pos + 1);
                        pos = buffer.indexOf('\n');
                    }
                } else {
                    Thread.sleep(5);
                    if (Timer.getFPGATimestamp() - lastGotData > 3) {
                        port.close();
                        port = new SerialPort(baud, ptype);
                    }
                }

            } catch (Exception e) {
                System.out.println("Unexpected simpleVision error: " + e);
                e.printStackTrace();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                port.close();
                port = new SerialPort(baud, ptype);
            }
        }
    }
}
