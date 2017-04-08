package server;

import actions.ActionTypes;
import utils.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by caoquan on 4/4/17.
 */
public class MasterOrdinary extends Thread {
    private ServerSocket serverSocket;
    private ExecutorService service;
    private int port;

    public MasterOrdinary(int port) {
        try {
            serverSocket = new ServerSocket(port);
            this.port = port;
            service = Executors.newFixedThreadPool(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Server Master Ordinary is listen at " + port);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Accept");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Constant.CHARSET));
                // Read command
                String input = reader.readLine();

                // parse command
                String[] splittedStr = input.split(" ");

                String command = splittedStr[0];
                switch (command) {
                    case ActionTypes.REQUEST_RENTAL:
                        SlaveQueryRentals slaveQueryAppartments = null;
                        String param1 = splittedStr[1];
                        int param2 = Integer.parseInt(splittedStr[2]);
                        if (splittedStr.length == 2) {
                            slaveQueryAppartments =
                                    new SlaveQueryRentals(socket.getOutputStream(), param1);
                        } else {
                            slaveQueryAppartments =
                                    new SlaveQueryRentals(socket.getOutputStream(), splittedStr[1], param2);
                        }
                        service.submit(slaveQueryAppartments);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
