package ru.nsu.networks.tcp.lab.server.application;

import java.util.TimerTask;

public class TimeDisplay extends TimerTask {

    private final ClientHandler clientHandler;

    public TimeDisplay(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() {
        clientHandler.showSpeedInfo();
    }
}
