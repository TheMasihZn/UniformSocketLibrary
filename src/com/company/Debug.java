package com.company;

import java.util.logging.Level;

public class Debug {
    private static boolean showDebug = false;
    private static boolean androidMode = false;

    public static void p(String s){
        print(s);
    }

    public static void println(String s){
        if (showDebug)
        print(s);
    }

    public static void showDebug() {
        Debug.showDebug = true;
    }

    public static void androidMode() {
        Debug.androidMode = true;
    }

    private static void print(String s) {
        s = "       debug       :   " + s;
        if (androidMode)
        java.util.logging.Logger.getLogger("UniformSocketLibrary").log(Level.INFO, s);
        else
            System.out.println(s);
    }
}
