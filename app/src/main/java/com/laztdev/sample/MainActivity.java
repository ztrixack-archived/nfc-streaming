package com.laztdev.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.laztdev.module.nfc.Nfc;
import com.laztdev.module.nfc.stream.NfcCallback;

import java.util.Arrays;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 09/Dec/2015
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Nfc.getInstance().onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Nfc.getInstance().onResume(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Nfc.getInstance().onNewIntent(this, intent);

        Nfc.getInstance()
                .begin()
                .startWith(new byte[]{(byte) 0xb4, (byte) 0xff})
                .data(new byte[]{(byte) 0xb1, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                        new byte[]{(byte) 0xb1, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                        new byte[]{(byte) 0xb1, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                        new byte[]{(byte) 0xb0, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                        new byte[]{(byte) 0xb1, (byte) 0xff, (byte) 0xff, (byte) 0xff}
                )
                .bypassError(true)
                .callback(new NfcCallback() {
                    @Override
                    public void OnComplete() {

                    }

                    @Override
                    public void OnReceive(byte[] send, byte[] recv) {
                        if (send != null && recv != null) {
                            Log.wtf("TAG", "SEND: " + Arrays.toString(send) + " ,RECV: " + Arrays.toString(recv));
                        } else if (send != null) {
                            Log.wtf("TAG", "SEND: " + Arrays.toString(send));
                        } else if (recv != null) {
                            Log.wtf("TAG", "RECV: " + Arrays.toString(recv));
                        }
                    }

                    @Override
                    public void OnError(Exception e) {
                        Log.wtf("ERR", e.getMessage());
                    }
                })
                .stream();
    }
}
