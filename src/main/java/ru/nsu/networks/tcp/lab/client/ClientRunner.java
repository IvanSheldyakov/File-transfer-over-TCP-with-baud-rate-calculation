package ru.nsu.networks.tcp.lab.client;

import ru.nsu.networks.tcp.lab.client.application.FileSender;
import ru.nsu.networks.tcp.lab.model.InetAddress;

public class ClientRunner {
    public static void main(String[] args) {

        InetAddress address = new InetAddress("127.2.0.245",23333);



        FileSender fileSender = new FileSender("src/main/resources/a.txt",address);
        fileSender.startConnection();
    }
}
