package com.company.Client.Threads;

import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Packet;
import com.company.Atoms.Source;
import com.company.Atoms.Type;
import com.company.Debug;
import com.company.Client.PacketHandler;

import java.io.*;
import java.net.Socket;

public abstract class FileListenerThread extends Thread {

    private final String serverIp;
    private DeviceInfo myInfo;

    public FileListenerThread(String serverIp, DeviceInfo myInfo) {

        this.serverIp = serverIp;
        this.myInfo = myInfo;
    }

    @Override
    public void run() {

        try {
            Socket fileSocket = new Socket(serverIp, 2048 + 1);
            if (fileSocket.isConnected()) {
                Debug.println("connected to file server ");

                ObjectOutputStream ooStream = new ObjectOutputStream(fileSocket.getOutputStream());
                ooStream.writeObject(myInfo);


                byte[] buffer = new byte[1024];
                int count;

//                todo: expand to support other file types
                File tempFile = File.createTempFile("audio", ".mp3");
                FileOutputStream tempOS = new FileOutputStream(tempFile);

                InputStream inputStream = fileSocket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                ConnectionEstablished();
//                play(bufferedInputStream);
                long now = System.currentTimeMillis();
                while ((count = bufferedInputStream.read(buffer)) > 0) {
                    tempOS.write(buffer, 0, count);
                }
                tempOS.flush();
                long diff = System.currentTimeMillis() - now;
                Debug.println(tempFile.getName() + " : " + tempFile.length() + " bytes : " + diff + " ms.");

                Source source = new Source(tempFile.getPath());
                getPacketHandler().handle(new Packet(Type.FileCommand, source));
            }
        } catch (IOException ignored) {
            interrupt();
        }
    }

    protected abstract PacketHandler getPacketHandler();

    protected abstract void ConnectionEstablished();

}
