package com.company.Server.Threads;

import com.company.Atoms.Type;
import com.company.Debug;
import com.company.Server.Client;
import com.company.Atoms.Packet;
import com.company.Atoms.Timeout;
import com.company.Server.interfaces.ConnectionCallback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.TimerTask;

public class PacketListenerThread extends Thread {
    Client client;
    ConnectionCallback callback;
    Timeout timeout = new Timeout(
            100000000,
            new TimerTask() {
                @Override
                public void run() {
                    Debug.println(client.getClientInfo().getName() + " timeout");
                    callback.OnClientLeft(client);
                    cancel();
                }
            }
    );

    public PacketListenerThread(Client client, ConnectionCallback callback) {
        this.client = client;
        this.callback = callback;
    }

    @Override
    public void run() {
        boolean run = true;
        while (run) {
            try {
                ObjectInputStream oiStream = new ObjectInputStream(client.getCommandSocket().getInputStream());
                Object object = oiStream.readObject();
                if (object instanceof Packet) {
                    Packet packet = (Packet) object;
                    if (packet.getCommand() != Type.RESPONSE_ALIVE) {
                        Debug.println(packet.getCommand() + " command recieved");
                        callback.OnCommandReceived(client, packet);
                    }
                    timeout.reset();
                }

            } catch (IOException e) {
                callback.OnClientLeft(client);
                run = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
