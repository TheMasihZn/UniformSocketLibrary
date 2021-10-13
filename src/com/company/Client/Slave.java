package com.company.Client;

import com.company.Debug;
import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Packet;
import com.company.Atoms.Source;
import com.company.Client.Threads.PacketListenerThread;
import com.company.Client.Threads.SearchForServerThread;
import com.company.Client.interfaces.ConnectionThreadCallback;
import com.company.Client.interfaces.SlaveCallback;

import java.net.Socket;

public class Slave implements ConnectionThreadCallback {
    private final DeviceInfo myInfo;
    PacketHandler packetHandler;
    private final SlaveCallback SlaveCallback;
    private PacketListenerThread packetListenerThread;
    private String ip;
    private SearchForServerThread searchForServerThread;

    public Slave(SlaveCallback SlaveCallback, DeviceInfo myInfo) {
        this.myInfo = myInfo;
        this.SlaveCallback = SlaveCallback;
    }

    public void listenOn(String ip) {
        this.ip = ip;
        listen();
    }

    public void stopListening(){
        searchForServerThread.interrupt();
    }

    private void listen(){
        packetHandler = new PacketHandler(ip) {

            @Override
            protected DeviceInfo getSlaveInfo() {
                return myInfo;
            }

            @Override
            void OnMasterClock(long masterTime, long myTime){
                SlaveCallback.OnMasterClock(masterTime, myTime);
            }

            @Override
            protected void OnFileReceived(Source source) {
                SlaveCallback.OnFileReceived(source);
            }

            @Override
            protected void OnPlay(long value, long time) {
                SlaveCallback.OnPlay(value, time);
            }

            @Override
            protected void OnPause() {
                SlaveCallback.OnPause();
            }
        };
        searchForServerThread = new SearchForServerThread(ip, myInfo, this);
        searchForServerThread.start();
    }

    @Override
    public void OnConnectedToServer(Socket socket) {
        Debug.println("register command listener");
        packetListenerThread = new PacketListenerThread(socket, this);
        packetListenerThread.start();
        SlaveCallback.OnConnect();
    }

    @Override
    public void OnConnectionReset(Socket socket) {
        Debug.println("OnConnectionReset");
        packetListenerThread.interrupt();
        SlaveCallback.OnDisconnect();
        listen();
    }

    @Override
    public void OnCommandReceived(Socket socket, Packet packet) {
        packetHandler.setSocket(socket);
        packetHandler.handle(packet);
    }

}
