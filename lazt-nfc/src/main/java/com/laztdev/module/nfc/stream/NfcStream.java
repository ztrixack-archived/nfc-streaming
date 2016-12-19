package com.laztdev.module.nfc.stream;

import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 09/Dec/2015
 */
public class NfcStream {
    private final TagTechnology mBasicTag;
    private final int timeout;
    private byte[] startData = new byte[0];
    private byte[] endData = new byte[0];
    private List<byte[]> transferData = new ArrayList<>();
    private boolean autoDisconnect = true;
    private boolean bypassError = false;
    private NfcCallback nfcCallback;

    public NfcStream(TagTechnology basicTag, int timeout) {
        this.mBasicTag = basicTag;
        this.bypassError = false;
        this.autoDisconnect = true;
        this.timeout = timeout;
    }


    public NfcStream startWith(byte[] buffer) {
        startData = new byte[0];
        if (buffer == null || buffer.length == 0) {
            return this;
        }
        startData = buffer.clone();
        return this;
    }

    public NfcStream endWith(byte[] buffer) {
        endData = new byte[0];
        if (buffer == null || buffer.length == 0) {
            return this;
        }
        endData = buffer.clone();
        return this;
    }

    public NfcStream data(byte[]... buffer) {
        transferData.clear();
        if (buffer == null || buffer.length == 0) {
            return this;
        }
        Collections.addAll(transferData, buffer);
        return this;
    }

    public NfcStream bypassError(boolean bypassError) {
        this.bypassError = bypassError;
        return this;
    }

    public NfcStream autoDisconnect(boolean disconnect) {
        this.autoDisconnect = disconnect;
        return this;
    }

    public void stream() {
        if (startData != null && startData.length > 0) {
            transferData.add(0, startData);
        }
        if (endData != null && endData.length > 0) {
            transferData.add(endData);
        }

        try {
            if (!mBasicTag.isConnected()) {
                mBasicTag.connect();
            }

            // for fastest transceiver
            if (mBasicTag instanceof NfcA) {
                streamNfcA(transferData);
            } else if (mBasicTag instanceof NfcB) {
                streamNfcB(transferData);
            } else if (mBasicTag instanceof NfcF) {
                streamNfcF(transferData);
            } else if (mBasicTag instanceof IsoDep) {
                streamIsoDep(transferData);
            } else if (mBasicTag instanceof NfcV) {
                streamNfcV(transferData);
            }

            if (autoDisconnect) {
                mBasicTag.close();
            }
        } catch (Exception e) {
            nfcCallback.OnError(e);
        }
    }

    private void streamNfcA(List<byte[]> transferData) throws Exception {
        NfcA nfc = (NfcA) mBasicTag;
        nfc.setTimeout(timeout);

        for (byte[] send : transferData) {
            byte[] recv = new byte[0];
            try {
                recv = nfc.transceive(send);
            } catch (Exception e) {
                if (!bypassError) {
                    throw new Exception(e);
                }
            }
            nfcCallback.OnReceive(send, recv);
        }
        nfcCallback.OnComplete();
    }

    private void streamNfcB(List<byte[]> transferData) throws Exception {
        NfcB nfc = (NfcB) mBasicTag;

        for (byte[] send : transferData) {
            byte[] recv = new byte[0];
            try {
                recv = nfc.transceive(send);
            } catch (Exception e) {
                if (!bypassError) {
                    throw new Exception(e);
                }
            }
            nfcCallback.OnReceive(send, recv);
        }
        nfcCallback.OnComplete();
    }

    private void streamNfcF(List<byte[]> transferData) throws Exception {
        NfcF nfc = (NfcF) mBasicTag;
        nfc.setTimeout(timeout);

        for (byte[] send : transferData) {
            byte[] recv = new byte[0];
            try {
                recv = nfc.transceive(send);
            } catch (Exception e) {
                if (!bypassError) {
                    throw new Exception(e);
                }
            }
            nfcCallback.OnReceive(send, recv);
        }
        nfcCallback.OnComplete();
    }

    private void streamIsoDep(List<byte[]> transferData) throws Exception {
        IsoDep nfc = (IsoDep) mBasicTag;
        nfc.setTimeout(timeout);

        for (byte[] send : transferData) {
            byte[] recv = new byte[0];
            try {
                recv = nfc.transceive(send);
            } catch (Exception e) {
                if (!bypassError) {
                    throw new Exception(e);
                }
            }
            nfcCallback.OnReceive(send, recv);
        }
        nfcCallback.OnComplete();
    }

    private void streamNfcV(List<byte[]> transferData) throws Exception {
        NfcV nfc = (NfcV) mBasicTag;

        for (byte[] send : transferData) {
            byte[] recv = new byte[0];
            try {
                recv = nfc.transceive(send);
            } catch (Exception e) {
                if (!bypassError) {
                    throw new Exception(e);
                }
            }
            nfcCallback.OnReceive(send, recv);
        }
        nfcCallback.OnComplete();
    }

    public NfcStream callback(NfcCallback nfcCallback) {
        this.nfcCallback = nfcCallback;
        return this;
    }
}
