package com.company.Server.Threads;

import com.company.Atoms.Packet;
import com.company.Debug;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PacketSenderThread extends Thread {
    Socket socket;
    Packet packet;

    public PacketSenderThread(Socket socket, Packet packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            Debug.println("sending " + packet.getCommand());
            ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());

//            write time in the very last step
            packet.setTime(System.currentTimeMillis());
            ooStream.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
