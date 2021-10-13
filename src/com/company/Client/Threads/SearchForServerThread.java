package com.company.Client.Threads;

import com.company.Atoms.DeviceInfo;
import com.company.Debug;
import com.company.Client.interfaces.ConnectionThreadCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class SearchForServerThread extends Thread {

    private final String serverIp;
    private DeviceInfo myInfo;
    private final ConnectionThreadCallback callback;

    public SearchForServerThread(String serverIp, DeviceInfo myInfo, ConnectionThreadCallback callback) {

        this.serverIp = serverIp;
        this.myInfo = myInfo;
        this.callback = callback;
    }

    @Override
    public void run() {
        Debug.println("search for server");
        boolean run = true;
        while (run) {
            try {
                Socket socket = new Socket(serverIp, 2048);
                if (socket.isConnected()) {

                    ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());
                    ooStream.writeObject(myInfo);

                    Debug.println("Search for server Thread : OnConnectedToServer");

                    callback.OnConnectedToServer(socket);
                    run = false;


//                    try {
//                        InputStream inputStream = socket.getInputStream();
//                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                        Object object = objectInputStream.readObject();
//                        if (object instanceof DeviceInfo) {
//                            run = false;
//                            DeviceInfo hostInfo = (DeviceInfo) object;
//                            callback.OnConnectedToServer(socket, hostInfo);
//                        }
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }

                }
            } catch (ConnectException e){
                Debug.println("Connection timeout: is the ip valid?");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
