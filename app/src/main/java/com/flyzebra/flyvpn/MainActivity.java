package com.flyzebra.flyvpn;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private int fd;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("flyvpn");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testUDP(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FlyLog.d("UDP sokcet client is run!");
                    DatagramSocket socket = null;
                    socket = new DatagramSocket();
                    FlyLog.d(socket.toString());
                    socket.setSoTimeout(3000);
                    String ssend = "[{\"messageType\":3,\"netType\":4,\"netTypeName\":\"wlan0\",\"sessionid\":110358574}]";
                    DatagramPacket sendpack = new DatagramPacket(ssend.getBytes(),
                            ssend.getBytes().length,
                            InetAddress.getByName("103.5.126.153"),
                            5060);
                    socket.send(sendpack);
                    socket.receive(sendpack);
                    String str = new String(sendpack.getData(), sendpack.getOffset(),sendpack.getLength());
                    FlyLog.d(ByteTools.bytes2HexString(str.getBytes()));
                    socket.close();
                    FlyLog.d("UDP sokcet client is end!");
                } catch (Exception e) {
                    e.printStackTrace();
                    FlyLog.e(e.toString());
                }
            }
        }).start();
    }

    public void openTun(View view) {
        fd = openTunDev();
    }

    public void closeTun(View view) {
        closeTunDev(fd);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int openTunDev();

    public native void closeTunDev(int fd);
}
