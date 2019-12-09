package com.flyzebra.flyvpn.task;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.SystemClock;

import com.flyzebra.utils.FlyLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
                count += start;
                start = 0;
                for (int i = 0; i < count; i++) {
                    if (buffer[i] == 0) {
                        boolean releaseWl = false;
                        try {
//                                mResponseQueue.add(event.getCmdNumber(), event);
                        } catch (IllegalArgumentException e) {
                            FlyLog.d("Problem parsing message " + e);
                        } finally {
                            if (releaseWl) {
//                                mWakeLock.acquire();
                            }
                        }
                        start = i + 1;
                    }
                }

                if (start == 0) {
                    FlyLog.d("RCV incomplete");
                }
                if (start != count) {
                    final int remaining = BUFFER_SIZE - start;
                    System.arraycopy(buffer, start, buffer, 0, remaining);
                    start = remaining;
                } else {
                    start = 0;
                }
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

//    public NativeDaemonEvent[] executeForList(long timeoutMs, String cmd, Object... args) throws NativeDaemonConnectorException {
//        final long startTime = SystemClock.elapsedRealtime();
//
//        final ArrayList<NativeDaemonEvent> events = Lists.newArrayList();
//
//        final StringBuilder rawBuilder = new StringBuilder();
//        final StringBuilder logBuilder = new StringBuilder();
//        final int sequenceNumber = mSequenceNumber.incrementAndGet();
//
//        makeCommand(rawBuilder, logBuilder, sequenceNumber, cmd, args);
//
//        final String rawCmd = rawBuilder.toString();
//        final String logCmd = logBuilder.toString();
//
//        log("SND -> {" + logCmd + "}");
//
//        synchronized (mDaemonLock) {
//            if (mOutputStream == null) {
//                throw new NativeDaemonConnectorException("missing output stream");
//            } else {
//                try {
//                    mOutputStream.write(rawCmd.getBytes(StandardCharsets.UTF_8));
//                } catch (IOException e) {
//                    throw new NativeDaemonConnectorException("problem sending command", e);
//                }
//            }
//        }
//
//        NativeDaemonEvent event = null;
//        do {
//            event = mResponseQueue.remove(sequenceNumber, timeoutMs, logCmd);
//            if (event == null) {
//                loge("timed-out waiting for response to " + logCmd);
//                throw new NativeDaemonTimeoutException(logCmd, event);
//            }
//            if (VDBG) log("RMV <- {" + event + "}");
//            events.add(event);
//        } while (event.isClassContinue());
//
//        final long endTime = SystemClock.elapsedRealtime();
//        if (endTime - startTime > WARN_EXECUTE_DELAY_MS) {
//            loge("NDC Command {" + logCmd + "} took too long (" + (endTime - startTime) + "ms)");
//        }
//
//        if (event.isClassClientError()) {
//            throw new NativeDaemonArgumentException(logCmd, event);
//        }
//        if (event.isClassServerError()) {
//            throw new NativeDaemonFailureException(logCmd, event);
//        }
//
//        return events.toArray(new NativeDaemonEvent[events.size()]);
//    }
}