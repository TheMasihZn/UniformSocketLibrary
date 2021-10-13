package com.company.Client.Threads;


import com.company.Atoms.Packet;
import com.company.Debug;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PacketSenderThread extends Thread {
    Socket socket;
    private Packet packet;
    private boolean done = false;

    public PacketSenderThread(Socket socket, Packet packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            while (!done) {
                if (!socket.isClosed()) {
                    Debug.println("sending " + packet.getCommand() + " command...");
                    ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());
                    ooStream.writeObject(packet);
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
