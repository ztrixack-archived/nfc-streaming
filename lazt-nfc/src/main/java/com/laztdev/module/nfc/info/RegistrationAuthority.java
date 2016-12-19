package com.laztdev.module.nfc.info;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 09/Dec/2015
 */
public class RegistrationAuthority {

    private static final String TAG = RegistrationAuthority.class.getSimpleName();
    private static RegistrationAuthority instance;

    private List<Metadata> metadata;

    public static RegistrationAuthority getInstance() {
        if (instance == null) {
            instance = new RegistrationAuthority();
        }
        return instance;
    }

    public void init(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream ims = assetManager.open("icm.json");
            metadata = readJsonStream(ims);

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private List<Metadata> readJsonStream(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        Type collectionType = new TypeToken<Collection<Metadata>>(){}.getType();
        List<Metadata> metadataList = gson.fromJson(reader, collectionType);
        reader.close();
        return metadataList;
    }

    public String getManufacturerFormId(byte icmId) {
        if (metadata == null) {
            Log.e(TAG, "Please init first!!");
            return null;
        }
        if (icmId < metadata.size()) {
            return "Unknown";
        }
        return metadata.get(icmId).getManufacturer();
    }

    public String getManufacturerLocationFormId(byte icmId) {
        if (metadata == null) {
            Log.e(TAG, "Please init first!!");
            return null;
        }
        if (icmId < metadata.size()) {
            return "Unknown";
        }
        return metadata.get(icmId).getLocation();
    }

    private class Metadata {
        @SerializedName("id")
        private int id;

        @SerializedName("manufacturer")
        private String manufacturer;

        @SerializedName("location")
        private String location;

        int getId() {
            return id;
        }

        String getManufacturer() {
            return manufacturer;
        }

        String getLocation() {
            return location;
        }
    }
}

