package com.laztdev.module.nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.os.Build;
import android.util.Log;

import com.laztdev.module.nfc.info.RegistrationAuthority;
import com.laztdev.module.nfc.stream.NfcStream;
import com.laztdev.module.utils.TagStatus;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 08/Dec/2015
 */
public class Nfc extends NfcWrapper {

    private static Nfc instance;

    private HashMap<String, TagTechnology> mMandatoryTags = new HashMap<>();
    private HashMap<String, TagTechnology> mOptionalTags = new HashMap<>();
    private String tagSelector;
    private TagStatus status;
    private int timeout;

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
            initOptionalTag();
        }
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

    public Tag getTag() {
        return tag;
    }

    public int getMandatoryTagsLength() {
        return mMandatoryTags.size();
    }

    public int getOptionalTagsLength() {
        return mOptionalTags.size();
    }

    public String getTagSelector() {
        return tagSelector;
    }

    public void setTagSelector(String tagSelector) {
        if (mMandatoryTags.containsKey(tagSelector)) {
            this.tagSelector = tagSelector;
        }
    }

    public void setTagSelectorByIndex(int i) {
        if (mMandatoryTags.containsKey(tag.getTechList()[i])) {
            this.tagSelector = tag.getTechList()[i];
        }
    }

    public TagStatus getStatus() {
        return status;
    }

    public boolean isFoundTag() {
        return tag != null;
    }
    /**
     * Checks whether a tag exists in a NFC field.
     *
     * @return true, if a tag exists.
     */
    public boolean isTagExists() {
        boolean isConnected;

        try {
            isConnected = mMandatoryTags.get(tagSelector).isConnected();
            if (isConnected) {
                mMandatoryTags.get(tagSelector).close();
            }
            mMandatoryTags.get(tagSelector).connect();
            if (!isConnected) {
                mMandatoryTags.get(tagSelector).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            status = TagStatus.DISAPPEAR;
        } catch (Exception e) {
            if (e.getMessage() != null) {
                e.printStackTrace();
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
            if (mMandatoryTags.get(tagSelector).isConnected()) {
                mMandatoryTags.get(tagSelector).close();
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
     * @param send_data  raw data sent from device.
     * @return data received from NFC.
     */
    public synchronized byte[] autoTransceive(boolean disconnect, byte[] send_data) {
        byte[] recv = null;
        try {
            if (!mMandatoryTags.get(tagSelector).isConnected()) {
                mMandatoryTags.get(tagSelector).connect();
            }
            setNfcTimeout(timeout);
            recv = transceive(send_data);
            if (disconnect) {
                mMandatoryTags.get(tagSelector).close();
            }
            status = TagStatus.EXCHANGE;
        } catch (IOException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Transceive failed")) {
                status = TagStatus.RESP_FAIL;
            } else {
                status = TagStatus.DISAPPEAR;
            }
        } catch (Exception e) {
            if (e.getMessage() == null) {
                status = TagStatus.DISAPPEAR;
            } else {
                e.printStackTrace();
            }
        }
        return recv;
    }

    public NfcStream begin() {
        return new NfcStream(mMandatoryTags.get(tagSelector), timeout);
    }

    // Basic Tag Technology
    protected int getNfcMaxTransceiveLength() {
        if (mMandatoryTags.get(tagSelector) instanceof NfcA) {
            return ((NfcA) mMandatoryTags.get(tagSelector)).getMaxTransceiveLength();
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcB) {
            return ((NfcB) mMandatoryTags.get(tagSelector)).getMaxTransceiveLength();
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcF) {
            return ((NfcF) mMandatoryTags.get(tagSelector)).getMaxTransceiveLength();
        } else if (mMandatoryTags.get(tagSelector) instanceof IsoDep) {
            return ((IsoDep) mMandatoryTags.get(tagSelector)).getMaxTransceiveLength();
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcV) {
            return ((NfcV) mMandatoryTags.get(tagSelector)).getMaxTransceiveLength();
        }

        return 0;
    }

    protected byte[] transceive(byte[] data) throws IOException {
        if (mMandatoryTags.get(tagSelector) instanceof NfcA) {
            return ((NfcA) mMandatoryTags.get(tagSelector)).transceive(data);
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcB) {
            return ((NfcB) mMandatoryTags.get(tagSelector)).transceive(data);
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcF) {
            return ((NfcF) mMandatoryTags.get(tagSelector)).transceive(data);
        } else if (mMandatoryTags.get(tagSelector) instanceof IsoDep) {
            return ((IsoDep) mMandatoryTags.get(tagSelector)).transceive(data);
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcV) {
            return ((NfcV) mMandatoryTags.get(tagSelector)).transceive(data);
        }

        return null;
    }

    protected void setNfcTimeout(int timeout) {
        if (mMandatoryTags.get(tagSelector) instanceof NfcA) {
            ((NfcA) mMandatoryTags.get(tagSelector)).setTimeout(timeout);
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcF) {
            ((NfcF) mMandatoryTags.get(tagSelector)).setTimeout(timeout);
        } else if (mMandatoryTags.get(tagSelector) instanceof IsoDep) {
            ((IsoDep) mMandatoryTags.get(tagSelector)).setTimeout(timeout);
        }
    }

    protected int getNfcTimeout() {
        if (mMandatoryTags.get(tagSelector) instanceof NfcA) {
            return ((NfcA) mMandatoryTags.get(tagSelector)).getTimeout();
        } else if (mMandatoryTags.get(tagSelector) instanceof NfcF) {
            return ((NfcF) mMandatoryTags.get(tagSelector)).getTimeout();
        } else if (mMandatoryTags.get(tagSelector) instanceof IsoDep) {
            return ((IsoDep) mMandatoryTags.get(tagSelector)).getTimeout();
        }

        return timeout;
    }

    private void initBasicTag() {
        timeout = 0;
        mMandatoryTags.clear();
        tagSelector = tag.getTechList()[0];

        for (String tech : tag.getTechList()) {
            switch (tech) {
                case "android.nfc.tech.NfcA":
                    // Type 1 tag   NfcA (also known as ISO 14443-3A)
                    mMandatoryTags.put(tech, NfcA.get(tag));
                    timeout = getNfcTimeout();
                    break;
                case "android.nfc.tech.NfcB":
                    // Type 2 tag   NfcB (also known as ISO 14443-3B)
                    mMandatoryTags.put(tech, NfcB.get(tag));
                    timeout = getNfcTimeout();
                    break;
                case "android.nfc.tech.NfcF":
                    // Type 3 tag   NfcF (also known as JIS 6319-4)
                    mMandatoryTags.put(tech, NfcF.get(tag));
                    timeout = getNfcTimeout();
                    break;
                case "android.nfc.tech.IsoDep":
                    // Type 4 tag   IsoDep (Smart Card)
                    mMandatoryTags.put(tech, IsoDep.get(tag));
                    timeout = getNfcTimeout();
                    break;
                case "android.nfc.tech.NfcV":
                    // Type 5 tag   NfcV (also known as ISO 15693)
                    mMandatoryTags.put(tech, NfcV.get(tag));
                    timeout = getNfcTimeout();
                    break;
                case "android.nfc.tech.Ndef":
                    // Ndef support
                    mMandatoryTags.put(tech, Ndef.get(tag));
                    timeout = getNfcTimeout();
                    break;
            }
        }
    }

    private void initOptionalTag() {
        mOptionalTags.clear();

        for (String tech : tag.getTechList()) {
            switch (tech) {
                case "android.nfc.tech.MifareClassic":
                    mOptionalTags.put(tech, MifareClassic.get(tag));
                    break;
                case "android.nfc.tech.MifareUltralight":
                    mOptionalTags.put(tech, MifareUltralight.get(tag));
                    break;
                case "android.nfc.tech.NfcBarcode":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        mOptionalTags.put(tech, NfcBarcode.get(tag));
                    }
                    break;
                case "android.nfc.tech.NdefFormatable":
                    // NDEF compatible
                    mOptionalTags.put(tech, NdefFormatable.get(tag));
                    break;
            }
        }
    }


}
