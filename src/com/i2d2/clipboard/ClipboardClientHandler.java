package com.i2d2.clipboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by frodochen on 9/4/17.
 */
public class ClipboardClientHandler {


    private Socket mSocket;
    private Messager mMessager;

    public ClipboardClientHandler(Socket socket) {
        mSocket = socket;
    }

    public void process() {

        mMessager = new Messager() {

            @Override
            public InputStream getIn() throws IOException {
                return mSocket.getInputStream();
            }

            @Override
            public OutputStream getOut() throws IOException {
                return mSocket.getOutputStream();
            }

            @Override
            public boolean isInClosed() {
                return mSocket.isClosed();
            }

            @Override
            public boolean isOutClosed() {
                return mSocket.isClosed();
            }
        };
        mMessager.start();
    }

    public void quit() {
        if (mMessager != null) {
            mMessager.stop();
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
