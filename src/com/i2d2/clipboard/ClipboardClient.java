package com.i2d2.clipboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by frodochen on 9/4/17.
 */
public class ClipboardClient {

    private int mLocalPort;

    private Socket mSocket;
    private ClipboardClientHandler mHandler;


    public ClipboardClient(int port) {
        mLocalPort = port;

        mSocket = new Socket();
    }

    public void connect(String host, int port) {
        try {

            InetSocketAddress serverEndAddress = null;
            try {
                serverEndAddress = new InetSocketAddress(InetAddress.getByName(host), port);
            } catch (IOException e) {
                Log.errExit("SERVER: [" + host + ":" + port + "] DOESN'T EXISTED", e, -1);
            }
            if (serverEndAddress != null) {
                mSocket.connect(serverEndAddress, 10 * 1000);
            }

            mHandler = new ClipboardClientHandler(mSocket);
            mHandler.process();
        } catch (IOException e) {
            Log.errExit("SERVER: [" + host + ":" + port + "] CAN NOT CONNECT IT", e, -1);
        }
    }

    public void quit() {
        if (mHandler != null) {
            mHandler.quit();
        }
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
