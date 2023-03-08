package ru.nsu.networks.tcp.lab.client.application;

import ru.nsu.networks.tcp.lab.client.application.impl.FileContentReaderImpl;
import ru.nsu.networks.tcp.lab.model.FileInfo;
import ru.nsu.networks.tcp.lab.model.InetAddress;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSender {

    private final InetAddress inetAddress;
    private final FileContentReader reader;

    private final String fileName;

    private final long fileSizeInBytes;

    public FileSender(String filePath, InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        Path pathOfFile = Paths.get(filePath);
        fileName = pathOfFile.getFileName().toString();
        fileSizeInBytes = pathOfFile.toFile().length();
        this.reader = new FileContentReaderImpl(pathOfFile);
    }

    public void startConnection() {
        try(Socket socket = new Socket(java.net.InetAddress.getByName(inetAddress.ip()).getHostName(), inetAddress.port())) {
            try(ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                out.writeObject(new FileInfo(fileName,fileSizeInBytes));

                while (reader.hasNextContent()) {
                    out.writeObject(reader.read());
                }
                out.writeObject(null);

                String message = in.readLine();
                System.out.println(message);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
