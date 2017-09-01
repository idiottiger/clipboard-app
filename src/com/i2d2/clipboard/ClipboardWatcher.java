package com.i2d2.clipboard;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Created by frodochen on 8/31/17.
 */
public class ClipboardWatcher extends Thread {

    private volatile boolean isQuit;
    private Clipboard mClipboard;

    public void startWatch() {
        System.out.println("START WATCH CLIPBOARD ... ");
        mClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        start();
    }

    public void stopWatch() {
        System.out.println("STOP WATCH CLIPBOARD ... ");
        isQuit = true;
    }

    @Override
    public void run() {
        String preClipboardContent = null;
        while (!isQuit) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            try {
                if (mClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    String stringClipboard = (String) mClipboard.getData(DataFlavor.stringFlavor);
                    if (preClipboardContent == null || !preClipboardContent.equals(stringClipboard)) {
                        System.out.println("Get Clipboard:" + stringClipboard);
                    }

                    preClipboardContent = stringClipboard;
                    insertContentToClipboard(mClipboard, stringClipboard + "-FUCK");
                }
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void insertContentToClipboard(final Clipboard clipboard, final String clipBoardContent) {
        ClipboardOwner clipboardOwner = new ClipboardOwner() {
            @Override
            public void lostOwnership(Clipboard board, Transferable contents) {

            }
        };

        clipboard.setContents(new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                DataFlavor[] dataFlavors = new DataFlavor[1];
                dataFlavors[0] = DataFlavor.stringFlavor;
                return dataFlavors;
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.equals(DataFlavor.stringFlavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return clipBoardContent;
                }
                return null;
            }
        }, clipboardOwner);
    }

}
