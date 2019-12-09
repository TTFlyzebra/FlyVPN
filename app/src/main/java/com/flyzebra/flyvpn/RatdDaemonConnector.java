package com.flyzebra.flyvpn;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.SystemClock;

import com.flyzebra.utils.FlyLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RatdDaemonConnector implements Runnable {
    private final static String SOCKET_NAME = "socket_ratd";
    private OutputStream mOutputStream;
    private final Object mDaemonLock = new Object();
    private int BUFFER_SIZE = 4096;

    @Override
    public void run() {
        FlyLog.d("RatdDaemonConnector start! ");
        while (true) {
            try {
                listenToSocket();
            } catch (Exception e) {
                FlyLog.e("Error in RatdDaemonConnector: " + e);
                SystemClock.sleep(5000);
            }
        }
    }

    private void listenToSocket() throws IOException {
        LocalSocket socket = null;
        try {
            socket = new LocalSocket();
            LocalSocketAddress address = new LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
            socket.connect(address);
            InputStream inputStream = socket.getInputStream();
            synchronized (mDaemonLock) {
                mOutputStream = socket.getOutputStream();
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            int start = 0;
            while (true) {
                int count = inputStream.read(buffer, start, BUFFER_SIZE - start);
                if (count < 0) {
                    FlyLog.d("got " + count + " reading with start = " + start);
                    break;
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(count);
                byteBuffer.put(buffer,0,count);
                String retString = new String(byteBuffer.array(),"UTF-8");
                FlyLog.d("recv mpc:"+retString);
            }
        } catch (IOException ex) {
            FlyLog.d("Communications error: " + ex);
            throw ex;
        } finally {
            synchronized (mDaemonLock) {
                if (mOutputStream != null) {
                    try {
                        FlyLog.d("closing stream for " + SOCKET_NAME);
                        mOutputStream.close();
                    } catch (IOException e) {
                        FlyLog.d("Failed closing output stream: " + e);
                    }
                    mOutputStream = null;
                }
            }

            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                FlyLog.d("Failed closing socket: " + ex);
            }
        }
    }

    public void sendMessage(String message) {
        FlyLog.d("send mpc:" + message);
        synchronized (mDaemonLock) {
            if (mOutputStream == null) {
                FlyLog.d("mOutputStream = null");
            } else {
                try {
                    mOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    FlyLog.d(e.toString());
                }
            }
        }
    }
}