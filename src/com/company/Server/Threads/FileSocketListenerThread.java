package com.company.Server.Threads;

import com.company.Debug;
import com.company.Server.Client;
import com.company.Atoms.DeviceInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSocketListenerThread extends Thread {

    private final ServerSocket fileServerSocket;
    private final Client client;

    public FileSocketListenerThread(ServerSocket fileServerSocket, Client client) {
        this.fileServerSocket = fileServerSocket;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Debug.println("file socket listener thread");
            Socket accept = fileServerSocket.accept();
            if (accept.isConnected()) {
                Debug.println("file socket connected");
                ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
                Object object = objectInputStream.readObject();

                if (object instanceof DeviceInfo) {
                    DeviceInfo clientDeviceInfo = (DeviceInfo) object;
                    if (client.getClientInfo().getName().equals(clientDeviceInfo.getName())) {
                        Debug.println("client: " + clientDeviceInfo.getName() + ": fileSocket: register");
                        client.setFileSocket(accept);
                    }
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }
}
