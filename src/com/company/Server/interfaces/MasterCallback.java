package com.company.Server.interfaces;

import com.company.Server.Client;

import java.util.List;

public interface MasterCallback {
    void ClientListUpdated(List<Client> clients);
    void ClientReady(Client client);
    long sync();
    void OnSync(Client client);
}