package com.laztdev.sample;

import android.app.Application;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;

import com.laztdev.module.nfc.Nfc;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 08/Dec/2015
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Nfc.getInstance().init(getApplicationContext(), NfcA.class, NdefFormatable.class, Ndef.class);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Nfc.getInstance().terminate();
    }
}
