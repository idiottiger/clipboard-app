package com.i2d2.clipboard;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ClipboardClient extends Listener implements ClipboardWatcher.OnClipboardContentChangeListener {

    private static final String TAG = "ClipboardClient";
    private static AtomicInteger sClientIndex = new AtomicInteger();

    private static final int DEFAULT_SERVER_PORT = 10024;
    private static final int DEFAULT_CONNECT_TIMEOUT = 3000;

    private int mPort;
    private String mServerIpAddress;
    private Client mClient;
    private final Object mLocker = new Object();
    private ClipboardWatcher mClipboardWatcher;

    public ClipboardClient() {
        mClipboardWatcher = new ClipboardWatcher();
    }


    public void connect(String serverIpAddress) {
        connect(serverIpAddress, DEFAULT_SERVER_PORT);
    }


    public void connect(String serverIpAddress, int port) {
        mServerIpAddress = serverIpAddress;
        mPort = port;
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
        mClipboardWatcher.setOnClipboardContentChangeListener(this);
        mClipboardWatcher.startWatch();
        mClient.setName("client:" + sClientIndex.incrementAndGet());
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
        mClipboardWatcher.stopWatch();
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
        synchronized (mLocker) {
            Log.log(TAG, "onClipboardContentChanged: [" + content+"] ===> TO [SERVER]");
            ClipboardMessage clipboardMessage = new ClipboardMessage();
            clipboardMessage.content = content;
            Log.log(TAG, "sendTCP: " + content);
            mClient.sendTCP(clipboardMessage);
        }
    }
}
