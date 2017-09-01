package com.i2d2.clipboard;

public class Main {

    public static void main(String[] args) {
	// write your code here
        ClipboardWatcher clipboardWatcher = new ClipboardWatcher();
        clipboardWatcher.startWatch();

        try {
            clipboardWatcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
