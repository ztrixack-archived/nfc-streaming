package com.laztdev.module.nfc.info;

import android.content.Context;
import android.util.Log;

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
//        try {
//            AssetManager assetManager = context.getAssets();
//            InputStream ims = assetManager.open("platform.json");
//            metadata = readJsonStream(ims);
//
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//        }
        metadata = getMetaData();
    }

//    private List<Metadata> readJsonStream(InputStream in) throws IOException {
//        Gson gson = new Gson();
//        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
//        Type collectionType = new TypeToken<Collection<Metadata>>(){}.getType();
//        List<Metadata> metadataList = gson.fromJson(reader, collectionType);
//        reader.close();
//        return metadataList;
//    }

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

    public List<Metadata> getMetaData() {
        List<Metadata> data = new ArrayList<>();

        data.add(new Metadata(
                "Type 1",
                "NFC-A",
                "ISO/IEC 14443-3 A",
                "106 kbit/sec",
                "454 Bytes",
                "Read/Write",
                "Low",
                "Broadcom Topaz"));
        data.add(new Metadata(
                "Type 2",
                "NFC-A",
                "ISO/IEC 14443-3 A",
                "106 kbit/sec",
                "48/128/144/504/888/1904 bytes",
                "Read/Write or Read-only",
                "Low",
                "NXP Mifare Ultralight, NXP Mifare Ultralight C, NXP NTAG 21s (F), NXP NTAG I2C"
        ));
        data.add(new Metadata(
                "Type 3",
                "NFC-F",
                "JIS 6319-4",
                "212 kbit/sec",
                "1/4/9 KBytes",
                "Read/Write or Read-only",
                "High",
                "Sony Felica"));
        data.add(new Metadata(
                "Type 4",
                "NFC-A/B",
                "ISO/IEC 14443-4 Smart Card",
                "106 - 424 kbit/sec",
                "2/4/8/106/144 KBytes",
                "Read/Write or Read-only",
                "Medium/High",
                "NXP DESfire, NXP SmartMX with JCOP, ST Microelectronics"));
        data.add(new Metadata(
                "Type 5",
                "NFC-V",
                "ISO/IEC 15693",
                "6.62 - 26.48 kbit/sec",
                "32/112/128/160/256 KBytes",
                "Read/Write",
                "Low/Medium",
                "NXP ICODE SLI(x), Texas Instruments Tag-It, HF-I, EM423x, ST Microelectronics"));

        return data;
    }


    private class Metadata {
        //        @SerializedName("format")
        private String format;

        //        @SerializedName("compliant")
        private String compliant;

        //        @SerializedName("base")
        private String base;

        //        @SerializedName("speed")
        private String speed;

        //        @SerializedName("spec")
        private String spec;

        //        @SerializedName("access")
        private String access;

        //        @SerializedName("price")
        private String price;

        //        @SerializedName("products")
        private String products;

        public Metadata(String format, String compliant, String base, String speed, String spec, String access, String price, String products) {
            this.format = format;
            this.compliant = compliant;
            this.base = base;
            this.speed = speed;
            this.spec = spec;
            this.access = access;
            this.price = price;
            this.products = products;
        }

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

