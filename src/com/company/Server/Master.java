package com.company.Server;

import com.company.Atoms.Packet;
import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Source;
import com.company.Atoms.Type;
import com.company.Debug;
import com.company.Server.Threads.FileSocketListenerThread;
import com.company.Server.Threads.NewClientListenerThread;
import com.company.Server.interfaces.ConnectionCallback;
import com.company.Server.interfaces.MasterCallback;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class Master implements ConnectionCallback {

    MasterCallback serverCallback;
    private final NewClientListenerThread newClientListenerThread;

    private final List<Client> clients = new ArrayList<>();

    private final PacketHandler packetHandler;

    public Master(MasterCallback serverCallback, DeviceInfo hostInfo) {
        this.serverCallback = serverCallback;

        ServerSocket serverSocket = null;
        ServerSocket fileServerSocket = null;
        try {
            serverSocket = new ServerSocket(2048);
            fileServerSocket = new ServerSocket(2049);
        } catch (IOException e) {
            e.printStackTrace();
        }

        newClientListenerThread = new NewClientListenerThread(serverSocket, this, hostInfo);

        ServerSocket finalFileServerSocket = fileServerSocket;

        packetHandler = new PacketHandler() {

                @Override
                public void OnResponseAllowFile(Client client) {
                    new FileSocketListenerThread(
                            finalFileServerSocket,
                            client)
                            .start();
                }

                @Override
                public void OnFileReceived(Client client) {
                    long sync = serverCallback.sync();

                    client.sendPacket(new Packet(Type.Clock, System.currentTimeMillis()));

                    if (sync != -1) {

                        Runnable helloRunnable = () -> serverCallback.OnSync(client);
                        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                        executor.scheduleAtFixedRate(helloRunnable, 0, sync, TimeUnit.MILLISECONDS);


                    }
                    serverCallback.ClientReady(client);
                }
            };

    }

    public void listen() {
        if (!newClientListenerThread.isAlive()) {
            newClientListenerThread.start();
        }
    }

    public void sendFile(Source source) {
        Packet packet = new Packet(Type.FileCommand, source);
        for (Client client :
                clients)
            client.sendPacket(packet);
    }


    @Override
    public void OnClientJoined(Client client) {
        client.registerListeners(this);
        clients.add(client);
        serverCallback.ClientListUpdated(clients);
    }

    @Override
    public void OnCommandReceived(Client client, Packet packet) {
        packetHandler.handle(client, packet);
    }

    @Override
    public void OnClientLeft(Client client) {
        Debug.println(client.getClientInfo().getName() + " left.");
        client.unregisterListeners();
        clients.remove(client);
        serverCallback.ClientListUpdated(clients);
    }
//
//    @Override
//    public void OnClientJoined(DeviceInfo clientDeviceInfo, Socket socket) {
//        Client client = new Client(clientDeviceInfo, socket);
//        client.registerListeners(this);
//        clients.add(client);
//    }
//
//    @Override
//    public void OnFileSocketJoined(DeviceInfo clientDeviceInfo, Socket fileSocket) {
//        for (Client client :
//                clients) {
//            if (client.getClientInfo() == clientDeviceInfo) {
//                client.setFileSocket(fileSocket);
//                Debug.println("file socket joined client: " + clientDeviceInfo.getName());
//            } else
//                Debug.println("No claim for file socket");
//        }
//    }
}
