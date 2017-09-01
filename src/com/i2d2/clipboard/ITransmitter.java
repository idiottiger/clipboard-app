package com.i2d2.clipboard;

/**
 * Created by frodochen on 9/1/17.
 */
public interface ITransmitter {

    void send(String message, OnSendCallback callback);

    void onReceive(String message);


    public static interface OnSendCallback
    {

    }
}
