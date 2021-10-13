package com.company.Client.Threads;

import com.company.Atoms.Packet;
import com.company.Atoms.Timeout;
import com.company.Atoms.Type;
import com.company.Debug;
import com.company.Client.interfaces.ConnectionThreadCallback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TimerTask;

public class PacketListenerThread extends Thread {
    Socket socket;
    ConnectionThreadCallback callback;

    Timeout timeout = new Timeout(4500,
            new TimerTask() {
                @Override
                public void run() {
                    try {
                        new ObjectOutputStream(socket.getOutputStream()).
                                writeObject(new Packet(Type.RESPONSE_ALIVE));
                    } catch (IOException e) {
                        cancel();
                    }

                }
            }
    );

    public PacketListenerThread(Socket socket, ConnectionThreadCallback callback) {
        this.socket = socket;
        this.callback = callback;
    }

    @Override
    public void run() {

        boolean run =true;
        while (run) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object object = objectInputStream.readObject();

                if (object instanceof Packet) {
                    Packet packet = (Packet) object;
                    Debug.println("Time Gap is "+Math.abs(System.currentTimeMillis() - packet.getTime()));
                    Debug.println("OnCommandReceived");
                    callback.OnCommandReceived(socket, packet);
                }
            } catch (IOException e) {
                timeout.reset();
                callback.OnConnectionReset(socket);
                run = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
