package com.i2d2.clipboard;

import java.io.*;

/**
 * Created by frodochen on 9/4/17.
 */
public abstract class Messager {

    private WorkerThread mReaderThread;
    private WorkerThread mWriterThread;

    public void start() {
        mReaderThread = new WorkerThread() {
            @Override
            public void actRun() {
                if (isInClosed()) {
                    stopRun();
                } else {
                    read();
                }
            }

            @Override
            public void interruptRun() {

            }
        };

        mWriterThread = new WorkerThread() {
            @Override
            public void actRun() {
                if (isOutClosed()) {
                    stopRun();
                } else {
                    write();
                }
            }

            @Override
            public void interruptRun() {

            }
        };

        mReaderThread.startRun();
        mWriterThread.startRun();
    }

    public void stop() {
        mReaderThread.stopRun();
        mWriterThread.stopRun();
    }

    private void read() {
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(getIn());
            final String content = inputStream.readUTF();
            System.out.println("CLIENT READ [" + content + "]");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write() {
        try {
            System.out.println("PLEASE WRITE: ");
            String content = new BufferedReader(new InputStreamReader(System.in)).readLine();
            DataOutputStream outputStream = new DataOutputStream(getOut());
            outputStream.writeUTF(content);
            outputStream.flush();

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public abstract InputStream getIn() throws IOException;

    public abstract OutputStream getOut() throws IOException;

    public abstract boolean isInClosed();

    public abstract boolean isOutClosed();

    private static abstract class WorkerThread extends Thread {

        private volatile boolean mIsRunning;

        @Override
        public void run() {
            while (mIsRunning) {
                actRun();
            }
        }

        public void startRun() {
            mIsRunning = true;
            start();
        }

        public void stopRun() {
            mIsRunning = false;
            interruptRun();
        }


        public abstract void actRun();

        public abstract void interruptRun();
    }

}
