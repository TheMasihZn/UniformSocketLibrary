package com.company.Server;

import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Packet;
import com.company.Atoms.Source;
import com.company.Atoms.Type;
import com.company.Debug;
import com.company.Server.interfaces.MasterCallback;

import java.util.List;
import java.util.Scanner;

public class ServerExecutable {
    static Source source = new Source("C:\\Users\\masih\\OneDrive\\Desktop\\darya.wav");
    static Packet packet = new Packet(Type.FileCommand, source);

    static MasterCallback serverCallback = new MasterCallback() {
        @Override
        public void ClientListUpdated(List<Client> clients) {
            System.out.println("Client list: ");
            for (Client client :
                    clients) {
                System.out.println(client.getClientInfo().getName() + " ");
                client.sendPacket(packet);
            }
        }

        @Override
        public void ClientReady(Client client) {
            System.out.println(client.getClientInfo().getName() + " is ready to play");
        }

        @Override
        public long sync() {
            return 5000;
        }

        long l = -5000;
        @Override
        public void OnSync(Client client) {
            l += 5000;
//            long now = System.currentTimeMillis();
            client.sendPacket(new Packet(Type.ACTION_PLAY, l));
        }


    };

    public static void main(String[] args) {
        System.out.println("Server has started...");
        Debug.showDebug();

        DeviceInfo hostInfo = new DeviceInfo("MasihServer");
        Master master = new Master(serverCallback, hostInfo);
        master.listen();

//        Scanner scanner = new Scanner(System.in);
//        while (true)
//            if (scanner.hasNext())
//                master.sendFile(new Source(scanner.next()));

//        todo: implement master.stopListening()
    }

}
