package com.laztdev.module.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.laztdev.module.nfc.info.RegistrationAuthority;
import com.laztdev.module.nfc.info.TagTypePlatform;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static android.nfc.NfcAdapter.ACTION_NDEF_DISCOVERED;
import static android.nfc.NfcAdapter.ACTION_TAG_DISCOVERED;
import static android.nfc.NfcAdapter.ACTION_TECH_DISCOVERED;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 08/Dec/2015
 */
public abstract class NfcWrapper {

    private static final String TAG = NfcWrapper.class.getSimpleName();

    protected Tag tag;

    private NfcAdapter mNfcAdapter;
    private Set<Class> mNfcClasses;
    private IntentFilter[] mIntentFilters;
    private String[][] mTechLists;

    protected NfcWrapper() {
        mNfcClasses = new HashSet<>();
    }

    public void init(Context context) {
        init(context, new IntentFilter[] {new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED), new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)}, null);
    }

    public void init(Context context, Class<?>... nfcClasses) {
        initRegister(context);
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        Collections.addAll(this.mNfcClasses, nfcClasses);
        initTechLists(this.mNfcClasses);
        initIntentFilters();
    }

    public void init(Context context, IntentFilter[] intentFilters, String[][] techLists) {
        initRegister(context);
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.mIntentFilters = intentFilters;
        this.mTechLists = techLists;
    }

    public void terminate() {
        tag = null;
        mNfcAdapter = null;
    }

    /**
     * Call this method in your Activity's onResume() method body.
     *
     * @param activity locale activity
     */
    public void onResume(Activity activity) {
        if (mNfcAdapter != null) {
            synchronized (this) {
                installNfcHandler(activity);
            }
        }
    }

    /**
     * Call this method in your Activity's onPause() method body.
     *
     * @param activity locale activity
     */
    public void onPause(Activity activity) {
        if (mNfcAdapter != null) {
            synchronized (this) {
                mNfcAdapter.disableForegroundDispatch(activity);
            }
        }
    }

    /**
     * Call this method in your activity's onNewIntent(Intent) method body.
     *
     * @param activity locale activity
     * @param intent   locale intent
     */
    public void onNewIntent(Activity activity, Intent intent) {
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            disable(activity);
        }
    }

    public void callNfcSetting(Context context) {
        if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    public boolean isNfcActivated() {
        if (mNfcAdapter == null) {
            Log.e(TAG, "NfcWrapper implementation not available.");
            return false;
        }
        return mNfcAdapter.isEnabled();
    }

    /**
     * Get the Tag Identifier (if it has one).
     *
     * @return UID as byte array, never null
     */
    public byte[] getUid() {
        if (tag == null) {
            return null;
        }
        return tag.getId();
    }

    private void initRegister(Context context) {
        RegistrationAuthority.getInstance().init(context);
        TagTypePlatform.getInstance().init(context);
    }

    private void initTechLists(Set<Class> mNfcClasses) {
        mTechLists = new String[mNfcClasses.size()][];
        Enumeration e = new Vector<>(mNfcClasses).elements();
        for (int i = 0; e.hasMoreElements(); ++i) {
            mTechLists[i] = new String[]{e.nextElement().getClass().getName()};
        }
    }

    private void initIntentFilters() {
        mIntentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
    }

    /**
     * Requests any foreground NFC activity. This method must be called from the
     * main thread.
     *
     * @param activity locale activity
     */
    private void installNfcHandler(Activity activity) {
        Intent activityIntent = new Intent(activity, activity.getClass());
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(activity, 0,
                activityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mNfcAdapter.enableForegroundDispatch(activity, intent,
                mIntentFilters, mTechLists);
    }

    /**
     * Put the interface in mode {NFC_MODE.DISABLE}.
     */
    private void disable(Activity activity) {
        synchronized (this) {
            try {
                mNfcAdapter.disableForegroundDispatch(activity);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "Your phone is not support a NFC Technology.");
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

    }

}
