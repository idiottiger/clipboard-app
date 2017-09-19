package com.i2d2.clipboard;

public class Main {


    private static String extraValue(String[] args, String key) {
        String value = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(key) && i + 1 < args.length) {
                value = args[i + 1];
                break;
            }
        }
        return value;
    }

    //command as follow:
    // --server -p
    // --client -i -p
    public static void main(String[] args) {

        if (args == null) {
            Log.errExit("argument error");
        } else {
            String type = args[0];
            if (type != null) {
                if (type.equals("--server")) {
                    String port = extraValue(args, "-p");
                    ClipboardServer clipboardServer;
                    if (port != null) {
                        int intPort = 0;
                        try {
                            intPort = Integer.valueOf(port);
                        } catch (NumberFormatException e) {
                            Log.errExit("port: [" + port + "] is not number");
                        }
                        clipboardServer = new ClipboardServer(intPort);
                    } else {
                        clipboardServer = new ClipboardServer();
                    }
                    clipboardServer.start();
                } else if (type.equals("--client")) {
                    ClipboardClient clipboardClient = new ClipboardClient();
                    String serverIp = extraValue(args, "-i");
                    if (serverIp == null) {
                        Log.errExit("serverIp IS NULL");
                    } else {
                        String port = extraValue(args, "-p");
                        if (port != null) {
                            int intPort = 0;
                            try {
                                intPort = Integer.valueOf(port);
                            } catch (NumberFormatException e) {
                                Log.errExit("port: [" + port + "] is not number");
                            }
                            clipboardClient.connect(serverIp, intPort);
                        } else {
                            clipboardClient.connect(serverIp);
                        }
                    }
                } else {
                    Log.errExit("type: [" + type + "] is error");
                }
            } else {
                Log.errExit("args[0] is NULL");
            }
        }

    }
}
