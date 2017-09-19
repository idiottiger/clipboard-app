package com.i2d2.clipboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by frodochen on 9/18/17.
 */
public class NormalClipboardManager implements IClipboardManager {

    private Clipboard mClipboard;

    public NormalClipboardManager() {
        mClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    @Override
    public String getClipboardContent() {
        String result = null;
        try {
            if (mClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                result = (String) mClipboard.getData(DataFlavor.stringFlavor);
            }
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void setClipboardContent(String content) {
        StringSelection stringSelection = new StringSelection(content);
        mClipboard.setContents(stringSelection, stringSelection);
    }
}
