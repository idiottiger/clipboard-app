package com.i2d2.clipboard;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class ClipboardServer extends Listener implements ClipboardWatcher.OnClipboardContentChangeListener {

    private static final String TAG = "ClipboardServer";

    private static final int DEFAULT_PORT = 10024;

    private int mPort;

    private Server mServer = null;

    private ClipboardWatcher mClipboardWatcher;

    public ClipboardServer() {
        this(DEFAULT_PORT);
    }

    public ClipboardServer(int port) {
        mPort = port;
        mClipboardWatcher = new ClipboardWatcher();
        mClipboardWatcher.setOnClipboardContentChangeListener(this);

    }


    public void start() {
        Log.log(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> start server >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        mServer = new Server();
        mServer.start();
        mServer.getKryo().register(ClipboardMessage.class);
        try {
            mServer.bind(mPort);
        } catch (IOException e) {
            e.printStackTrace();
            Log.errExit("Server with port:[" + mPort + "] error:[" + e.getMessage() + "]");
        }
        mClipboardWatcher.startWatch();
        mServer.addListener(this);
    }

    public void stop() {
        Log.log(TAG, "stop server ...");
        Log.log(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< stop server <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        mClipboardWatcher.stopWatch();
        if (mServer != null) {
            mServer.stop();
        }
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
        Log.log(TAG, "connected: [" + connection.toString() + "]");
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        Log.log(TAG, "disconnected: [" + connection.toString() + "]");
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (object != null && object instanceof ClipboardMessage) {
            String content = ((ClipboardMessage) object).content;
            Log.log(TAG, "-------------- received ------------------ \n" + content);
            Log.log(TAG, "------------------------------------------");
            mClipboardWatcher.insertContentToClipboard(content);
        }
    }

    @Override
    public void onClipboardContentChanged(String content) {
        Log.log(TAG, "onClipboardContentChanged: [" + content+"] ===> TO [CLIENTS]");
        ClipboardMessage clipboardMessage = new ClipboardMessage();
        clipboardMessage.content = content;
        mServer.sendToAllTCP(clipboardMessage);
    }
}
