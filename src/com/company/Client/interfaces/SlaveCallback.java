package com.company.Client.interfaces;

import com.company.Atoms.Source;

public interface SlaveCallback {
    void OnConnect();
    void OnDisconnect();
    void OnFileReceived(Source source);
    void OnPlay(long time, long commandTime);
    void OnPause();
    void OnMasterClock(long masterTime, long myTime);
}
