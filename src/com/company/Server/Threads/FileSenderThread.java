package com.company.Server.Threads;

import com.company.Atoms.Source;
import com.company.Debug;

import java.io.*;
import java.net.Socket;

public class FileSenderThread extends Thread{
    private final Socket fileSocket;
    private final Source source;

    public FileSenderThread(Socket fileSocket, Source pendingSource) {
        this.fileSocket = fileSocket;
        source = pendingSource;
    }

    @Override
    public void run() {
        try{
            byte[] buffer = new byte[1024];
            int count;
            BufferedInputStream bufferedStream = source.getBufferedStream();

            OutputStream outputStream = fileSocket.getOutputStream();
            BufferedOutputStream boStream = new BufferedOutputStream(outputStream);
            Debug.println("file: send: start");
            long now = System.currentTimeMillis();
            while ((count = bufferedStream.read(buffer)) >0) {
                boStream.write(buffer, 0, count);
            }
            long diff = System.currentTimeMillis() - now;
            Debug.println("file: send: done: "+ diff + " ms.");
            boStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
