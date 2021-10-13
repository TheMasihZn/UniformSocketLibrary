package com.company.Server;

import com.company.Atoms.Packet;
import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Source;
import com.company.Atoms.Type;
import com.company.Debug;
import com.company.Server.Threads.PacketListenerThread;
import com.company.Server.Threads.PacketSenderThread;
import com.company.Server.Threads.FileSenderThread;
import com.company.Server.interfaces.ConnectionCallback;

import java.net.Socket;

public class Client {
    private final DeviceInfo clientDeviceInfo;
    private final Socket commandSocket;
    private Socket fileSocket;

    private PacketListenerThread commandListener;
    private Source pendingSource;

    public Client(DeviceInfo clientDeviceInfo, Socket commandSocket) {
        this.clientDeviceInfo = clientDeviceInfo;
        this.commandSocket = commandSocket;

    }

    public void registerListeners(ConnectionCallback callback) {
        commandListener = new PacketListenerThread(this, callback);
        commandListener.start();
    }

    public void unregisterListeners() {
        commandListener.interrupt();
    }

    public DeviceInfo getClientInfo() {
        return clientDeviceInfo;
    }

    public void setFileSocket(Socket fileSocket) {
        this.fileSocket = fileSocket;
        sendFile();
    }

    public void sendPacket(Packet packet) {

        Debug.println("sending " + packet.getCommand() + ((packet.getSource() == null) ? "" : " with source"));
        if (packet.getCommand() == Type.FileCommand) {
            pendingSource = packet.getSource();
            packet = new Packet(Type.REQUEST_ALLOW_FILE);
        }
        new PacketSenderThread(commandSocket, packet).start();
    }

    private void sendFile() {
        if (fileSocket != null) {
            new FileSenderThread(fileSocket, pendingSource).start();
            pendingSource = null;
        }
    }

    public boolean hasFileListener() {
        return fileSocket != null;
    }

    public Socket getCommandSocket() {
        return commandSocket;
    }
}
