package com.company.Server;

import com.company.Atoms.Packet;
import com.company.Debug;

public abstract class PacketHandler {

//    todo: search making a class overridable
    public PacketHandler() {}

    public void handle(Client client, Packet packet) {
        Debug.println(client.getClientInfo().getName() + " -> " + packet.getCommand());
        switch (packet.getCommand()) {
            case RESPONSE_ALLOW_FILE:
                OnResponseAllowFile(client);
                break;
            case FileReceived:
                OnFileReceived(client);
                break;
        }
    }

    public abstract void OnResponseAllowFile(Client client);
    public abstract void OnFileReceived(Client client);

}
