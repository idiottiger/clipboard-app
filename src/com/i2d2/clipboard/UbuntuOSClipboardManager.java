package com.i2d2.clipboard;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.gnome.gtk.Gtk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by frodochen on 9/18/17.
 */

//the java clipboard on ubuntu has bug
//see: https://stackoverflow.com/questions/14242719/copying-to-global-clipboard-does-not-work-with-java-in-ubuntu

public class UbuntuOSClipboardManager implements IClipboardManager {

    private static final String TAG = "UbuntuOSClipboardManager";

    private static final CommandLine sGetClipCMD = CommandLine.parse("/bin/bash -c xclip -selection c -o");
    private static final CommandLine sPutClipCMD = CommandLine.parse("/bin/bash -c xclip -selection c");

    private static final ByteArrayOutputStream OUT_BYTES_BUFFER = new ByteArrayOutputStream(8 * 1024);
    private static final ByteArrayInputStream IN_BYTES_BUFFER = new ByteArrayInputStream(new byte[1024]);

    private org.gnome.gtk.Clipboard mClipboardManager;

    public UbuntuOSClipboardManager() {
        Gtk.init(null);
        mClipboardManager = org.gnome.gtk.Clipboard.getDefault();
    }

    @Override
    public String getClipboardContent() {
        return mClipboardManager.getText();
    }

    @Override
    public void setClipboardContent(String content) {
        mClipboardManager.setText(content);
        mClipboardManager.store();
    }


    private int runCommand(CommandLine commandLine, ExecuteStreamHandler streamHandler) {
        int resultCode = -1;
        try {
            DefaultExecutor executor = new DefaultExecutor();
            //executor.setWorkingDirectory(new File("~/"));
            executor.setStreamHandler(streamHandler);
            resultCode = executor.execute(commandLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultCode;
    }

    private String _process_get_clipboard_content() {
        OUT_BYTES_BUFFER.reset();
        PumpStreamHandler streamHandler = new PumpStreamHandler(OUT_BYTES_BUFFER);
        int resultCode = runCommand(sGetClipCMD, streamHandler);
        Log.log(TAG, "_process_get_clipboard_content: exitCode=[" + resultCode + "]");
        if (resultCode == 0) {
            return OUT_BYTES_BUFFER.toString();
        }
        return null;
    }

    private int _process_put_clipboard_content(String content) {
        //create a tempfile content the value
        int resultCode = -1;
        try {
            File file = File.createTempFile("clip", "txt");
            FileUtils.write(file, content, "utf-8");
            IN_BYTES_BUFFER.reset();
            byte[] bytes = file.getAbsolutePath().getBytes();
            IN_BYTES_BUFFER.read(bytes, 0, bytes.length);
            PumpStreamHandler streamHandler = new PumpStreamHandler(null, null, IN_BYTES_BUFFER);
            resultCode = runCommand(sPutClipCMD, streamHandler);
            Log.log(TAG, "_process_put_clipboard_content: exitCode=[" + resultCode + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultCode;


    }

}
