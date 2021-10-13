package com.company.Server.interfaces;

import com.company.Server.Client;
import com.company.Atoms.Packet;

import java.io.IOException;

public interface ConnectionCallback {
    void OnClientJoined(Client client) throws IOException;
    void OnCommandReceived(Client socket, Packet packet);
    void OnClientLeft(Client client);
//    void OnClientJoined(DeviceInfo clientDeviceInfo, Socket fileSocket);
//    void OnFileSocketJoined(DeviceInfo clientDeviceInfo, Socket socket);
}