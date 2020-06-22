package org.code;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;


public class Main {

    private final Robot robot = new Robot();
    private final String TOUCH_DOWN = "0";
    private final String TOUCH_UP = "1";
    private final String SHOW_MENU = "2";

    public boolean newClient = false;

    public Main() throws AWTException, IOException {
        final int PORT = 4040;
        final ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("start server at: http://" + InetAddress.getLocalHost().getHostAddress() + ":" + PORT + "/");
        while (true) {
            try {
                Socket client = serverSocket.accept();
                newClient = true;
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                if (reader.ready()) {
                    String data = reader.readLine();
                    System.out.println(data);
                    newClient = false;
                    switch (data) {
                        case TOUCH_DOWN: robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK); break;
                        case TOUCH_UP: robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK); break;
                        case SHOW_MENU: robot.mousePress(MouseEvent.BUTTON3_DOWN_MASK); robot.mouseRelease(MouseEvent.BUTTON3_DOWN_MASK); break;
                        default:
                            String[] read = data.split(Pattern.quote("|"));
                            Point point = MouseInfo.getPointerInfo().getLocation();
                            moveMouseSmooth(
                                    (int)point.getX(),
                                    (int)point.getY(),
                                    (int)Float.parseFloat(read[0]),
                                    (int)Float.parseFloat(read[1]),
                                    2, 5
                            );
                    }
                }
                reader.close();
                client.close();
            } catch (Exception e) {}
        }
    }

    private void moveMouseSmooth(int x1, int y1, int x2, int y2, int t, int n) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Robot r = new Robot();
                    double dx = (x2 - x1) / ((double) n);
                    double dy = (y2 - y1) / ((double) n);
                    double dt = t / ((double) n);
                    for (int step = 1; step <= n; step++) {
                        Thread.sleep((int) dt);
                        r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
                    }
                } catch (AWTException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void main(String[] args) throws IOException, AWTException {
        new Main();
    }
}
