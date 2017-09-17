package com.i2d2.clipboard;

/**
 * Created by frodochen on 9/4/17.
 */
public class Log {

    public static void errExit(String message) {
        errExit(message, null, -1);
    }


    public static void errExit(String message, Exception excep, int exitCode) {
        System.err.println("Error: [" + message + "]");
        if (excep != null) {
            excep.printStackTrace();
        }
        System.exit(exitCode);
    }

    public static void log(String tag, String message) {
        System.out.println(tag + ":" + message);
    }
}
