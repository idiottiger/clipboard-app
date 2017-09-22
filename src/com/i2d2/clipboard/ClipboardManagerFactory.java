package com.i2d2.clipboard;

/**
 * Created by frodochen on 9/18/17.
 */
public class ClipboardManagerFactory {

    public static IClipboardManager createClipboardManager() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("Current os: " + os);
        IClipboardManager clipboardManager = null;
        if (os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0) {
            clipboardManager = new UbuntuOSClipboardManager();
        } else {
            clipboardManager = new NormalClipboardManager();
        }
        return clipboardManager;

    }

}
