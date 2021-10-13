package com.company.Server.Threads;

import com.company.Server.Client;
import com.company.Atoms.DeviceInfo;
import com.company.Server.interfaces.ConnectionCallback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NewClientListenerThread extends Thread {

    private final ServerSocket serverSocket;
    private final ConnectionCallback callback;
    private DeviceInfo hostInfo;

    public NewClientListenerThread(ServerSocket serverSocket, ConnectionCallback callback, DeviceInfo hostInfo) {
        this.serverSocket = serverSocket;
        this.callback = callback;
        this.hostInfo = hostInfo;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket accept = serverSocket.accept();
                if (accept.isConnected()) {

                    ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
                    Object object = objectInputStream.readObject();

                    if (object instanceof DeviceInfo) {
                        DeviceInfo clientDeviceInfo = (DeviceInfo) object;
                        Client client = new Client(clientDeviceInfo, accept);
                        callback.OnClientJoined(client);
//                        new ObjectOutputStream(accept.getOutputStream()).writeObject(hostInfo);
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
