package com.company.Atoms;

import java.io.Serializable;

public class Packet implements Serializable {
    private final Type commandType;
    private Source source;
    private long value;
    private long time;

    public Packet(Type command) {
        this.commandType = command;
    }

    public Packet(Type command, long value){
        this.commandType = command;
        this.value = value;
        time = System.currentTimeMillis();
    }

    public Packet(Type command, Source source) {
        this.commandType = command;
        this.source = source;
    }

    public Type getCommand() {
        return commandType;
    }

    public Source getSource()
//            throws Exception
    {
//        if( source == null)
//            throw new Exception("Not a Source Packet.");
        return source;
    }

    public long getValue()
//            throws Exception
    {
//        if( source == null)
//            throw new Exception("Not a Source Packet.");

        return value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
