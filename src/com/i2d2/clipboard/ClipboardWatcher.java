package com.i2d2.clipboard;

/**
 * Created by frodochen on 8/31/17.
 */
public class ClipboardWatcher extends Thread {

    private static final int DEFAULT_SLEEP_TIME = 1000;

    private volatile boolean isQuit;
    private IClipboardManager mClipboardManager;
    private OnClipboardContentChangeListener mChangeListener;
    private String mPreClipboardContent;
    private final Object mLock = new Object();

    public ClipboardWatcher() {
        mClipboardManager = ClipboardManagerFactory.createClipboardManager();
    }

    public void startWatch() {
        System.out.println("START WATCH CLIPBOARD ... ");
        start();
    }

    public void setOnClipboardContentChangeListener(OnClipboardContentChangeListener listener) {
        mChangeListener = listener;
    }

    public void stopWatch() {
        System.out.println("STOP WATCH CLIPBOARD ... ");
        isQuit = true;
    }

    @Override
    public void run() {
        while (!isQuit) {
            try {
                Thread.sleep(DEFAULT_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (mLock) {
                String stringClipboard = mClipboardManager.getClipboardContent();
                System.out.println("Get Clipboard: [" + stringClipboard + "]");
                if (stringClipboard != null && (mPreClipboardContent == null || !mPreClipboardContent.equals(stringClipboard))) {
                    //System.out.println("Get Clipboard: [" + stringClipboard + "]");
                    if (mChangeListener != null) {
                        mChangeListener.onClipboardContentChanged(stringClipboard);
                    }
                }
                mPreClipboardContent = stringClipboard;
            }
        }
    }

    public void insertContentToClipboard(final String content) {
        synchronized (mLock) {
            if (content != null) {
                mClipboardManager.setClipboardContent(content);
                mPreClipboardContent = content;
            }
        }
    }

    public static interface OnClipboardContentChangeListener {
        public void onClipboardContentChanged(String content);
    }
}
