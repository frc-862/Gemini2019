package frc.lightning.util;

import edu.wpi.first.wpilibj.SerialPort;

import java.util.function.Consumer;

public class SerialByLine {
    private Consumer<String> fn;
    private SerialPort port;
    private String buffer = "";

    public SerialByLine(SerialPort port, Consumer<String> fn) {
        this.port = port;
        this.fn = fn;
        new Thread(this::loop).start();
    }

    private void loop() {
        try {
            while (true) {
                int bytes = port.getBytesReceived();
                if (bytes > 0) {
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
                }
            }
        }
        catch (InterruptedException e) {
            // just exit
            System.out.println("Vision interrupted");
        } catch (Exception e) {
            System.out.println("Unexpected vision error: " + e);
            e.printStackTrace();
        }
    }
}
