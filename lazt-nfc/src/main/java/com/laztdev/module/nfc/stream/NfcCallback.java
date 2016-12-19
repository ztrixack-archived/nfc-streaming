package com.laztdev.module.nfc.stream;


/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 09/Dec/2015
 */
public abstract class NfcCallback {
    public abstract void OnComplete();

    public abstract void OnReceive(byte[] send, byte[] recv);

    public abstract void OnError(Exception e);
}
