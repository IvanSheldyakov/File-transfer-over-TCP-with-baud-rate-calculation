package ru.nsu.networks.tcp.lab.server.application;

import ru.nsu.networks.tcp.lab.model.FileContent;
import ru.nsu.networks.tcp.lab.model.FileInfo;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Timer;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final String pathToUploads = "uploads" + File.separator;

    private final Timer timer;

    private FileInfo fileInfo;

    private long currentReceivedSize;

    private long sizeReceivedForThreeSec = 0;

    private double startTime;

    private boolean timeDisplayedOnce = false;

    public ClientHandler(Socket socket) {

        this.socket = socket;
        timer = new Timer();
    }


    @Override
    public void run() {

        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             OutputStream out = socket.getOutputStream()
        ) {
            initTimeReport();
            fileInfo = (FileInfo) in.readObject();
            File file = createFile();
            writeContentToFileFromInputStream(file,in);
            sendMessageAboutDataIntegrity(out);
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            finishThread();
        }
    }

    private void initTimeReport() {
        timer.schedule(new TimeDisplay(this), 3000, 3000);
        startTime = System.currentTimeMillis();
    }


    private void writeContentToFileFromInputStream(File file, ObjectInputStream in) throws IOException, ClassNotFoundException {
        try (FileWriter writer = new FileWriter(file)) {
            FileContent content;
            while ((content = (FileContent) in.readObject()) != null) {
                writer.write(bytesToChars(content.getContent()));
                updateContentInfo(content);

            }
        }
    }

    private File createFile() throws IOException {
        String filePath = pathToUploads + fileInfo.getFileName();
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }

    private char[] bytesToChars(byte[] bytes){
        Charset charset = Charset.forName("UTF-8");
        CharBuffer charBuffer = charset.decode(ByteBuffer.wrap(bytes));
        return Arrays.copyOf(charBuffer.array(), charBuffer.limit());
    }
    private void updateContentInfo(FileContent content) {
        double lastGotSize = content.getSizeInBytes();
        sizeReceivedForThreeSec += lastGotSize;
        currentReceivedSize += lastGotSize;
    }


    private void sendMessageAboutDataIntegrity(OutputStream out) throws IOException {
        if (currentReceivedSize == fileInfo.getFileSizeInBytes()) {
            out.write("SUCCESS".getBytes());
        } else {
            out.write("FAILED".getBytes());
        }
    }


    private void finishThread() {
        try {
            timer.cancel();
            if (!timeDisplayedOnce) {
                showSpeedInfo();
            }
            socket.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    public void showSpeedInfo() {
        double instantaneousSpeed = sizeReceivedForThreeSec / 3.0;
        double averageSpeed = (currentReceivedSize /
                (System.currentTimeMillis() - startTime) / 1000);
        System.out.printf("client %d instantaneousSpeed %f\n", getId(), instantaneousSpeed);
        System.out.printf("client %d averageSpeed %f\n", getId(), averageSpeed);
        sizeReceivedForThreeSec = 0;
        timeDisplayedOnce = true;
    }




}
