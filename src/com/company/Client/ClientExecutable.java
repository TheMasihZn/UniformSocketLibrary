package com.company.Client;

import com.company.Debug;
import com.company.Atoms.DeviceInfo;
import com.company.Atoms.Source;
import com.company.Client.interfaces.SlaveCallback;


public class ClientExecutable {

    private static final SlaveCallback SlaveCallback = new SlaveCallback() {
        @Override
        public void OnConnect() {
            System.out.println("Connected to command server");
        }

        @Override
        public void OnDisconnect() {
            System.out.println("OnDisconnect");
        }

        @Override
        public void OnFileReceived(Source source) {
            System.out.println("OnFileReceived");

        }

        @Override
        public void OnPlay(long time, long commandTime) {
            System.out.println("OnPlay");

        }

        @Override
        public void OnPause() {
            System.out.println("OnPause");

        }

        @Override
        public void OnMasterClock(long masterTime, long myTime) {
            System.out.println("OnMasterClock");
        }
    };

    public static void main(String[] args) {
        DeviceInfo deviceInfo = new DeviceInfo("MasihClient");

        Debug.showDebug();
        Slave slave = new Slave(SlaveCallback, deviceInfo);
        slave.listenOn("127.0.0.1");

    }
}
