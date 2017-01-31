package com.laztdev.module.utils;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 08/Dec/2015
 */
public class NfcUtils {

    private static NfcUtils instance;

    public static NfcUtils getInstance() {
        if (instance == null) {
            instance = new NfcUtils();
        }
        return instance;
    }

    private NfcUtils() {

    }
}
