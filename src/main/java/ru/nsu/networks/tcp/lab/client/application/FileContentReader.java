package ru.nsu.networks.tcp.lab.client.application;

import ru.nsu.networks.tcp.lab.model.FileContent;

import java.io.IOException;

public interface FileContentReader {
    boolean hasNextContent();
    FileContent read() throws IOException;
}
