package com.laztdev.module.nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.util.Log;

import com.laztdev.module.nfc.info.RegistrationAuthority;
import com.laztdev.module.nfc.stream.NfcStream;
import com.laztdev.module.utils.TagStatus;
import com.laztdev.module.utils.TagType;

import java.io.IOException;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 08/Dec/2015
 */
public class Nfc extends NfcWrapper {

    private static final String TAG = Nfc.class.getSimpleName();


    private static Nfc instance;
    protected TagTechnology mBasicTag;
    protected TagStatus status;
    protected TagType type = TagType.NULL;
    protected int timeout;

    public static Nfc getInstance() {
        if (instance == null) {
            instance = new Nfc();
        }
        return instance;
    }

    protected Nfc() {
        super();
    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {
        super.onNewIntent(activity, intent);
        if (isFoundTag()) {
            initBasicTag();
        }
    }

    public String getTagType() {
        return type.name();
    }

    public String[] getRfTechnology() {
        return tag.getTechList();
    }

    public byte[] getUid() {
        return tag.getId();
    }

    public String getManufacturer() {
        byte[] uid = getUid();
        if (uid == null || uid.length <= 0) {
            return "Unknown";
        }
        return RegistrationAuthority.getInstance().getManufacturerFormId(uid[0]);
    }


    private void initBasicTag() {
        for (String tech : tag.getTechList()) {
            if (tech.contains("NfcA")) {
                // Type 1 tag   NfcA (also known as ISO 14443-3A)
                mBasicTag = NfcA.get(tag);
                type = TagType.NFC_A;
                timeout = getNfcTimeout();
                return;
            } else if (tech.contains("NfcB")) {
                // Type 2 tag   NfcB (also known as ISO 14443-3B)
                mBasicTag = NfcB.get(tag);
                type = TagType.NFC_B;
                timeout = getNfcTimeout();
                return;
            } else if (tech.contains("NfcF")) {
                // Type 3 tag   NfcF (also known as JIS 6319-4)
                mBasicTag = NfcF.get(tag);
                type = TagType.NFC_F;
                timeout = getNfcTimeout();
                return;
            } else if (tech.contains("IsoDep")) {
                // Type 4 tag   IsoDep (Smart Card)
                mBasicTag = IsoDep.get(tag);
                type = TagType.ISODEP;
                timeout = getNfcTimeout();
                return;
            } else if (tech.contains("NfcV")) {
                // Type 5 tag   NfcV (also known as ISO 15693)
                mBasicTag = NfcV.get(tag);
                type = TagType.NFC_V;
                timeout = getNfcTimeout();
                return;
            }
        }
        Log.e(TAG, "Tag is not support");
        mBasicTag = null;
        type = TagType.NULL;
        timeout = 0;
    }

    public Tag getTag() {
        return tag;
    }

    /**
     * Checks whether a tag exists in a NFC field.
     *
     * @return true, if a tag exists.
     */
    public boolean isTagExists() {
        boolean isConnected;

        try {
            isConnected = mBasicTag.isConnected();
            if (isConnected) {
                mBasicTag.close();
            }
            mBasicTag.connect();
            if (!isConnected) {
                mBasicTag.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            status = TagStatus.DISAPPEAR;
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.e(TAG, e.getMessage());
            }
            status = TagStatus.DISAPPEAR;
        }

        return status != TagStatus.DISAPPEAR;
    }

    /**
     * Gets the NFC timeout in milliseconds.
     *
     * @return integer, timeout value in milliseconds
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the NFC timeout in milliseconds.
     *
     * @param timeout timeout value in milliseconds
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public int getMaxTransceiveLength() {
        return getNfcMaxTransceiveLength();
    }

    /**
     * This function is used for disconnect the NFC device.
     */
    public void close() {
        try {
            if (mBasicTag.isConnected()) {
                mBasicTag.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = TagStatus.DISAPPEAR;
        }
    }

    public byte[] autoTransceive(byte[] send_data) {
        return autoTransceive(true, send_data);
    }

    /**
     * NFC transceive function connects and closes NFC software service
     * automatically.
     *
     * @param disconnect auto disconnection.
     * @param send_data raw data sent from device.
     * @return data received from NFC.
     */
    public synchronized byte[] autoTransceive(boolean disconnect, byte[] send_data) {
        byte[] recv = null;
        try {
            if (!mBasicTag.isConnected()) {
                mBasicTag.connect();
            }
            setNfcTimeout(timeout);
            recv = transceive(send_data);
            if (disconnect) {
                mBasicTag.close();
            }
            status = TagStatus.EXCHANGE;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            if (e.getMessage().contains("Transceive failed")) {
                status = TagStatus.RESP_FAIL;
            } else {
                status = TagStatus.DISAPPEAR;
            }
        } catch (Exception e) {
            if (e.getMessage() == null) {
                status = TagStatus.DISAPPEAR;
            }
        }
        return recv;
    }

    public NfcStream begin() {
        return new NfcStream(mBasicTag, timeout);
    }

    // Basic Tag Technology
    protected int getNfcMaxTransceiveLength() {
        switch (type) {
            case NULL:
                return 0;
            case NFC_A:
                return ((NfcA) mBasicTag).getMaxTransceiveLength();
            case NFC_B:
                return ((NfcB) mBasicTag).getMaxTransceiveLength();
            case NFC_F:
                return ((NfcF) mBasicTag).getMaxTransceiveLength();
            case ISODEP:
                return ((IsoDep) mBasicTag).getMaxTransceiveLength();
            case NFC_V:
                return ((NfcV) mBasicTag).getMaxTransceiveLength();
        }
        return 0;
    }

    protected byte[] transceive(byte[] data) throws IOException {
        switch (type) {
            case NULL:
                return null;
            case NFC_A:
                return ((NfcA) mBasicTag).transceive(data);
            case NFC_B:
                return ((NfcB) mBasicTag).transceive(data);
            case NFC_F:
                return ((NfcF) mBasicTag).transceive(data);
            case ISODEP:
                return ((IsoDep) mBasicTag).transceive(data);
            case NFC_V:
                return ((NfcV) mBasicTag).transceive(data);
        }
        return null;
    }

    protected void setNfcTimeout(int timeout) {
        switch (type) {
            case NULL:
                break;
            case NFC_A:
                ((NfcA) mBasicTag).setTimeout(timeout);
                break;
            case NFC_B:
                break;
            case NFC_F:
                ((NfcF) mBasicTag).setTimeout(timeout);
                break;
            case ISODEP:
                ((IsoDep) mBasicTag).setTimeout(timeout);
                break;
            case NFC_V:
                break;
        }
    }

    protected int getNfcTimeout() {
        switch (type) {
            case NULL:
                return 0;
            case NFC_A:
                return ((NfcA) mBasicTag).getTimeout();
            case NFC_B:
                return 0;
            case NFC_F:
                return ((NfcF) mBasicTag).getTimeout();
            case ISODEP:
                return ((IsoDep) mBasicTag).getTimeout();
            case NFC_V:
                return 0;
        }
        return 0;
    }

    protected boolean isFoundTag() {
        return tag != null;
    }

}
