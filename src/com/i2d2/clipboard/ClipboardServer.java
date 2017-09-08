package com.i2d2.clipboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by frodochen on 9/4/17.
 */
public class ClipboardServer {

    private ServerSocket mServerSocket;

    private volatile boolean mIsRunning;

    private final Set<ClipboardClientHandler> mHandlerSet = new HashSet<>();

    public ClipboardServer(int port) {

        try {
            mServerSocket = new ServerSocket(port);
            System.out.println("SERVER PORT ["+port+"] OK");
        } catch (IOException e) {
            Log.errExit("CREATE SERVER ERROR: [" + port + "]", e, -1);
        }
    }

    public void listen() {
        if (!mIsRunning) {
            mIsRunning = true;
            new Thread(() -> {
                while (mIsRunning) {
                    try {
                        System.out.println("START LISTEN CLIENT ...");
                        final Socket socket = mServerSocket.accept();
                        if (socket != null) {
                            ClipboardClientHandler handler = new ClipboardClientHandler(socket);
                            synchronized (mHandlerSet) {
                                mHandlerSet.add(handler);
                            }
                            handler.process();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void quit() {
        mIsRunning = false;
        synchronized (mHandlerSet) {
            for (ClipboardClientHandler handler : mHandlerSet) {
                if (handler != null) {
                    handler.quit();
                }
            }
        }
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
