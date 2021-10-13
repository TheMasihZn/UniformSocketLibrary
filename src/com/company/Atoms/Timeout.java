package com.company.Atoms;

import java.util.Timer;
import java.util.TimerTask;

public class Timeout extends Timer {
    boolean isScheduled = false;
    private final long timeout;
    private TimerTask t;

    public Timeout(long timeout, TimerTask t) {
        this.timeout = timeout;
        this.t = t;
        schedule();
    }

    public void reset() {
        try {
            if (isScheduled) {
                this.cancel();
            }
        } catch (Exception ignored) {}
        schedule();
    }

    private void schedule() {
        TimerTask run = t;
        try {
            this.scheduleAtFixedRate(run, timeout, timeout);
            isScheduled = true;
        } catch (Exception ignored) {}
    }

}
