package ru.nsu.networks.tcp.lab.server;

import ru.nsu.networks.tcp.lab.server.application.FileReceiverServer;


import java.util.Scanner;

public class ServerRunner {

    public static void main(String[] args)  {
        FileReceiverServer server = new FileReceiverServer(23333);
        server.setDaemon(true);
        server.start();


        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            //server.interrupt();
        }
    }
}
