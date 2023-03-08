package ru.nsu.networks.tcp.lab.server.application;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

public class FileReceiverServer extends Thread{

    private final int port;
    public FileReceiverServer(int port) {
        this.port = port;
    }
    public void run(){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }


}
