package com.i2d2.clipboard;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class ClipboardClient extends Listener {

    private static final String TAG = "ClipboardClient";

    private static final int DEFAULT_SERVER_PORT = 10024;
    private static final int DEFAULT_CONNECT_TIMEOUT = 3000;

    private int mPort;
    private String mServerIpAddress;
    private Client mClient;


    public ClipboardClient(String serverIpAddress) {
        this(serverIpAddress, DEFAULT_SERVER_PORT);
    }

    public ClipboardClient(String serverIpAddress, int port) {
        mServerIpAddress = serverIpAddress;
        mPort = port;
    }

    public void connect() {
        Log.log(TAG, "connect server: [" + mServerIpAddress + ":" + mPort + "]");
        mClient = new Client();
        mClient.start();
        mClient.getKryo().register(ClipboardMessage.class);
        try {
            mClient.connect(DEFAULT_CONNECT_TIMEOUT, mServerIpAddress, mPort);
        } catch (IOException e) {
            e.printStackTrace();
            Log.errExit("connect server: [" + mServerIpAddress + ":" + mPort + "] error:" + e.getMessage());
        }

        mClient.addListener(this);
        final Thread thread = mClient.getUpdateThread();
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        Log.log(TAG, "close");
        if (mClient != null) {
            final Thread thread = mClient.getUpdateThread();
            if (thread != null) {
                thread.interrupt();
            }
        }
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
        Log.log(TAG, "connected: " + connection.toString());
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        Log.log(TAG, "disconnected: " + connection.toString());
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        Log.log(TAG, "received: " + connection.toString());
    }

    @Override
    public void idle(Connection connection) {
        super.idle(connection);
        Log.log(TAG, "idle: " + connection.toString());
    }

}
