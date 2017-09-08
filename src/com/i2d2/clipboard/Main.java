package com.i2d2.clipboard;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        ClipboardWatcher clipboardWatcher = new ClipboardWatcher();
//        clipboardWatcher.startWatch();
//
//        try {
//            clipboardWatcher.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        ClipboardClient clipboardClient = new ClipboardClient(30000);
        clipboardClient.connect("192.168.188.1",10089);
//        ClipboardServer server = new ClipboardServer(10089);
//        server.listen();
    }
}
