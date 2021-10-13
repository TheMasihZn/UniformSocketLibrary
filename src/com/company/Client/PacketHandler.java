package com.company.Client;

import com.company.Atoms.Type;
import com.company.Debug;
import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Packet;
import com.company.Atoms.Source;
import com.company.Client.Threads.FileListenerThread;
import com.company.Client.Threads.PacketSenderThread;

import java.net.Socket;

import static com.company.Atoms.Packet.*;

public abstract class PacketHandler {
    private Socket socket;
    private String serverIp;

    public PacketHandler(String ip) {
        serverIp = ip;
    }

    public void handle(Packet packet) {
        Debug.println("Packet handler : " + packet.getCommand());

        switch (packet.getCommand()) {

            case Clock:
                OnMasterClock(packet.getValue(), System.currentTimeMillis());
                break;
            case ACTION_PLAY:
                OnPlay(packet.getValue(), packet.getTime());
                break;
            case REQUEST_ALLOW_FILE:
                DeviceInfo myInfo = getSlaveInfo();
                new FileListenerThread(serverIp, myInfo) {
                    @Override
                    protected PacketHandler getPacketHandler() {
                        return PacketHandler.this;
                    }

                    @Override
                    protected void ConnectionEstablished() {
                        new PacketSenderThread(socket, new Packet(Type.RESPONSE_ALLOW_FILE)).start();
                    }
                }.start();
                break;
            case FileCommand:
                OnFileReceived(packet.getSource());
                new PacketSenderThread(socket, new Packet(Type.FileReceived)).start();
                break;
            case ACTION_PAUSE:
                OnPause();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + packet.getCommand());
        }
    }


    protected abstract DeviceInfo getSlaveInfo();

    abstract void OnMasterClock(long masterTime, long myTime);

    protected abstract void OnFileReceived(Source source);

    protected abstract void OnPlay(long value, long time);

    protected abstract void OnPause();

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
