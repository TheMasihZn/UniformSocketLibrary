package com.company.Client.interfaces;

import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Packet;

import java.net.Socket;

public interface ConnectionThreadCallback {
    void OnConnectedToServer(Socket socket);
    void OnConnectionReset(Socket socket);
    void OnCommandReceived(Socket socket, Packet packet);
}