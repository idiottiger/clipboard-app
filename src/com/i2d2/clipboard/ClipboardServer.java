package com.i2d2.clipboard;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class ClipboardServer extends Listener {

    private static final String TAG = "ClipboardServer";

    private static final int DEFAULT_PORT = 10024;

    private int mPort;

    private Server mServer = null;

    public ClipboardServer() {
        this(DEFAULT_PORT);
    }

    public ClipboardServer(int port) {
        mPort = port;
    }


    public void start() {
        Log.log(TAG, "start server ...");
        mServer = new Server();
        mServer.start();
        mServer.getKryo().register(ClipboardMessage.class);
        try {
            mServer.bind(mPort);
        } catch (IOException e) {
            e.printStackTrace();
            Log.errExit("Server with port:[" + mPort + "] error:[" + e.getMessage() + "]");
        }
        mServer.addListener(this);
    }

    public void stop() {
        Log.log(TAG, "stop server ...");
        if (mServer != null) {
            mServer.stop();
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
