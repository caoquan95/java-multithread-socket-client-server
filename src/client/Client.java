package client;

import actions.Action;
import utils.Constant;

import java.io.*;
import java.net.Socket;

/**
 * Created by caoquan on 4/11/17.
 */
abstract public class Client {
    private Socket socket;

    private BufferedWriter writer;
    private BufferedReader reader;

    public void doAction(Action action) {
        try {
            System.out.println("Send request: " + action.command());

            this.socket = new Socket("localhost", Constant.PORT);
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Constant.CHARSET));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Constant.CHARSET));

            this.writer.write(action.command());
            this.writer.newLine();
            this.writer.flush();

            InputStream inputStream = socket.getInputStream();

            int attempts = 0;
            while (inputStream.available() == 0 && attempts < 1000) {
                attempts++;
                Thread.sleep(10);
            }
            if (attempts == 1000) {
                System.out.println("Request Timeout");
            }

//            String line = reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                System.out.println(line);
//                line = reader.readLine();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}