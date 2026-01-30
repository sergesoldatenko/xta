package com.github.sergesoldatenko.xta.Model.Resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class XtaFile {
    private static volatile XtaFile instance;
    private RandomAccessFile fileHandle;
    private boolean isFileOpened = false;

    private XtaFile() {
    }

    public static XtaFile getInstance() {
        if (instance == null) {
            /* 
             * to do:
             * add additional thread-safe check
             * if this class is used more than in one thread in future
             */
            instance = new XtaFile();
        }
        return instance;
    }

    public boolean isOpened() {
        return isFileOpened;
    }

    public void open(String xtaFilename) throws FileNotFoundException, HandleInUseException {
        if (isFileOpened) {
            throw new HandleInUseException("Xta handle is already in use. Close prior opening a new handle");
        }
        fileHandle = new RandomAccessFile(xtaFilename, "rw");
        isFileOpened = true;
    }

    public void close() throws IOException {
        fileHandle.close();
        isFileOpened = false;
    }

    public int write(ByteBuffer buffer) throws IOException {
        int bytesWritten = 0;
        FileChannel channel = fileHandle.getChannel();
        while (buffer.hasRemaining()) {
            bytesWritten += channel.write(buffer);
        }
        return bytesWritten;
    }
    public int write(ByteBuffer buffer, long seek) throws IOException {
        fileHandle.seek(seek);
        return write(buffer);
    }
    public int read(ByteBuffer buffer) throws IOException {
        FileChannel channel = fileHandle.getChannel();
        return channel.read(buffer);
    }
    public int read(ByteBuffer buffer, long seek) throws IOException {
        fileHandle.seek(seek);
        return read(buffer);
    }
}
