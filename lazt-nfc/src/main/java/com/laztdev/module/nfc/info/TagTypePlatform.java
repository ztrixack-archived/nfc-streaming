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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tanawat Hongthai - http://www.laztdev.com/
 * @version 1.0.0
 * @since 09/Dec/2015
 */
public class TagTypePlatform {

    private static final String TAG = TagTypePlatform.class.getSimpleName();
    private static TagTypePlatform instance;

    private List<Metadata> metadata;

    public static TagTypePlatform getInstance() {
        if (instance == null) {
            instance = new TagTypePlatform();
        }
        return instance;
    }

    public void init(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream ims = assetManager.open("platform.json");
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

    public String getCompliantFormTagType(int type) {
        if (checkParameter(type)) return "Unknown";
        return metadata.get(type + 1).getCompliant();
    }

    public String getOperationSpecificationFormTagType(int type) {
        if (checkParameter(type)) return "Unknown";
        return metadata.get(type + 1).getBase();
    }

    private boolean checkParameter(int type) {
        if (metadata == null) {
            Log.e(TAG, "Please init first!!");
            return true;
        }
        return type < 1 || type > metadata.size();
    }


    private class Metadata {
        @SerializedName("format")
        private String format;

        @SerializedName("compliant")
        private String compliant;

        @SerializedName("base")
        private String base;

        @SerializedName("speed")
        private String speed;

        @SerializedName("spec")
        private String spec;

        @SerializedName("access")
        private String access;

        @SerializedName("price")
        private String price;

        @SerializedName("products")
        private String products;

        public String getFormat() {
            return format;
        }

        public String getCompliant() {
            return compliant;
        }

        public String getBase() {
            return base;
        }

        public String getSpeed() {
            return speed;
        }

        public String getSpec() {
            return spec;
        }

        public String getAccess() {
            return access;
        }

        public String getPrice() {
            return price;
        }

        public String getProducts() {
            return products;
        }
    }

}

