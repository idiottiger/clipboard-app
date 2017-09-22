package com.i2d2.clipboard;

import org.apache.commons.io.FileUtils;
import org.gnome.gtk.Gtk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by frodochen on 9/18/17.
 */

//fuck the ubuntu clipboard design
public class UbuntuOSClipboardManager implements IClipboardManager {

    private static final String CLIPBOARD_FILE = "share_clipboard";

    private org.gnome.gtk.Clipboard mClipboardManager;
    private final File mClipboardFile;
    private String mCurrentClipboardContent;

    public UbuntuOSClipboardManager() {
        Gtk.init(null);
        mClipboardManager = org.gnome.gtk.Clipboard.getDefault();
        Path path = Paths.get("");
        mClipboardFile = new File(path.toFile().getAbsolutePath(), CLIPBOARD_FILE);
    }

    @Override
    public String getClipboardContent() {
        return mClipboardManager.getText();
    }

    //setClipboardContent doesn't work some times, wtf
    //so save the clipboard content to a file
    @Override
    public void setClipboardContent(String content) {
        if (content != null && !content.equals(mCurrentClipboardContent)) {
            mClipboardManager.setText(content);
            mClipboardManager.store();
            mCurrentClipboardContent = content;
            try {
                FileUtils.write(mClipboardFile, mCurrentClipboardContent,"utf-8",true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
