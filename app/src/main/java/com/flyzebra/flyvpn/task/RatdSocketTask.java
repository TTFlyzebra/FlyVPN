package com.flyzebra.flyvpn.task;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.utils.FlyLog;
import com.flyzebra.utils.GsonTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: RatdSocketTask
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:00
 */
public class RatdSocketTask implements Runnable {
    private Thread mThread;
    private final static String SOCKET_NAME = "socket_ratd";
    private OutputStream mOutputStream;
    private final Object mDaemonLock = new Object();
    private int BUFFER_SIZE = 4096;
    private static final String RATD_TAG = "RatdConnector";
    private Handler mHandler = new Handler(Looper.getMainLooper());


    private static final HandlerThread mSendMpcThread = new HandlerThread("SendToMpcTask");

    static {
        mSendMpcThread.start();
    }

    private static final Handler mSendMpcHandler = new Handler(mSendMpcThread.getLooper());


    public RatdSocketTask() {
        mThread = new Thread(this, RATD_TAG);
        mThread.setDaemon(true);
        mThread.start();
    }

    @Override
    public void run() {
        FlyLog.d("RatdSocketTask start! ");
        while (true) {
            try {
                //初始化
                mSendMpcHandler.removeCallbacksAndMessages(null);
                //开始监听ratd并交互
                listenToSocket();
            } catch (Exception e) {
                FlyLog.e("Error in RatdSocketTask: " + e);
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
            while (true) {
                FlyLog.e("read 1");
                int count = inputStream.read(buffer, 0, BUFFER_SIZE);
                FlyLog.e("read 2");
                if (count < 0) {
                    break;
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(count);
                byteBuffer.put(buffer, 0, count);
                String tempStr = new String(byteBuffer.array(), "UTF-8");
                int start = -1;
                do {
                    start = tempStr.indexOf("}]");
                    if (start != tempStr.length() - 2) {
                        String retStr = tempStr.substring(0, start + 2);
                        tempStr = tempStr.substring(start + 2);
                        notifyRecvMessage(retStr);
                        FlyLog.d("recv mpc:" + retStr);
                    } else {
                        notifyRecvMessage(tempStr);
                        FlyLog.d("recv mpc:" + tempStr);
                        break;
                    }
                } while (start == -1);

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

    public void sendMessage(final String message) {
        mSendMpcHandler.post(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    private List<OnRecvMessage> onRecvMessageList = new ArrayList<>();

    public void register(OnRecvMessage onRecvMessage) {
        onRecvMessageList.add(onRecvMessage);
    }

    public void unRegister(OnRecvMessage onRecvMessage) {
        onRecvMessageList.remove(onRecvMessage);
    }

    private void notifyRecvMessage(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (OnRecvMessage onRecvMessage : onRecvMessageList) {
                    onRecvMessage.recv(GsonTools.json2Object(message, MpcMessage.class));
                }
            }
        });
    }
}