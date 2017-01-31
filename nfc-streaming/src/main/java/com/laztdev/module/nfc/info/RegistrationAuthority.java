package com.laztdev.module.nfc.info;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
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
//        try {
//            AssetManager assetManager = context.getAssets();
//            InputStream ims = assetManager.open("icm.json");
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

    private List<Metadata> getMetaData() {
        List<Metadata> data = new ArrayList<>();

        data.add(new Metadata(0, "Not Specified", "Unknown"));
        data.add(new Metadata(1, "Motorola", "UK"));
        data.add(new Metadata(2, "STMicroelectronics SA", "FR"));
        data.add(new Metadata(3, "Hitachi Ltd", "JP"));
        data.add(new Metadata(4, "NXP Semiconductors", "DE"));
        data.add(new Metadata(5, "Infineon Technologies AG", "DE"));
        data.add(new Metadata(6, "Cylink", "US"));
        data.add(new Metadata(7, "Texas Instruments Tag-it™", "FR"));
        data.add(new Metadata(8, "Fujitsu Limited", "JP"));
        data.add(new Metadata(9, "Matsushita Electronics Corporation, Semiconductor Company", "JP"));
        data.add(new Metadata(10, "NEC", "JP"));
        data.add(new Metadata(11, "Oki Electric Industry Co Ltd", "JP"));
        data.add(new Metadata(12, "Toshiba Corp", "JP"));
        data.add(new Metadata(13, "Mitsubishi Electric Corp", "JP"));
        data.add(new Metadata(14, "Samsung Electronics Co Ltd", "KR"));
        data.add(new Metadata(15, "Hynix", "KR"));
        data.add(new Metadata(16, "LG-Semiconductors Co Ltd", "KR"));
        data.add(new Metadata(17, "Emosyn-EM Microelectronics", "US"));
        data.add(new Metadata(18, "INSIDE Technology", "KR"));
        data.add(new Metadata(19, "ORGA Kartensysteme GmbH", "DE"));
        data.add(new Metadata(20, "Sharp Corporation", "JP"));
        data.add(new Metadata(21, "ATMEL", "FR"));
        data.add(new Metadata(22, "EM Microelectronic-Marin", "CH"));
        data.add(new Metadata(23, "KSW Microtec GmbH", "DE"));
        data.add(new Metadata(24, "ZMD AG", "DE"));
        data.add(new Metadata(25, "XICOR Inc", "US"));
        data.add(new Metadata(26, "Sony Corporation", "JP"));
        data.add(new Metadata(27, "Malaysia Microelectronic Solutions Sdn Bhd", "MY"));
        data.add(new Metadata(28, "Emosyn", "US"));
        data.add(new Metadata(29, "Shanghai Fudan Microelectronics Co Ltd", "CN"));
        data.add(new Metadata(30, "Magellan Technology Pty Limited", "AU"));
        data.add(new Metadata(31, "Melexis NV BO", "CH"));
        data.add(new Metadata(32, "Renesas Technology Corp", "JP"));
        data.add(new Metadata(33, "TAGSYS", "FR"));
        data.add(new Metadata(34, "Transcore", "US"));
        data.add(new Metadata(35, "Shanghai Belling Corp Ltd", "CN"));
        data.add(new Metadata(36, "Masktech Germany GmbH", "DE"));
        data.add(new Metadata(37, "Innovision Research and Technology Plc", "UK"));
        data.add(new Metadata(38, "Hitachi ULSI Systems Co Ltd", "JP"));
        data.add(new Metadata(39, "Cypak AB", "SE"));
        data.add(new Metadata(40, "Ricoh", "JP"));
        data.add(new Metadata(41, "ASK", "FR"));
        data.add(new Metadata(42, "Unicore Microsystems LLC", "RU"));
        data.add(new Metadata(43, "Dallas semiconductor/Maxim", "US"));
        data.add(new Metadata(44, "Impinj Inc", "US"));
        data.add(new Metadata(45, "RightPlug Alliance", "US"));
        data.add(new Metadata(46, "Broadcom Corporation", "US"));
        data.add(new Metadata(47, "MStar Semiconductor Inc", "TW"));
        data.add(new Metadata(48, "BeeDar Technology Inc", "US"));
        data.add(new Metadata(49, "RFIDsec", "DK"));
        data.add(new Metadata(50, "Schweizer Electronic AG", "DE"));
        data.add(new Metadata(51, "AMIC Technology Corp", "TW"));
        data.add(new Metadata(52, "Mikron JSC", "RU"));
        data.add(new Metadata(53, "Fraunhofer Institute for Photonic Microsystems", "DE"));
        data.add(new Metadata(54, "IDS Microship AG", "US"));
        data.add(new Metadata(55, "AHMT Microelectronic Ltd", "CH"));
        data.add(new Metadata(56, "Silicon Craft Technology", "TH"));
        data.add(new Metadata(57, "Advanced Film Device Inc.", "JP"));
        data.add(new Metadata(58, "Nitecrest Ltd", "UK"));
        data.add(new Metadata(59, "Verayo Inc.", "US"));
        data.add(new Metadata(60, "HID Global", "US"));
        data.add(new Metadata(61, "Productivity Engineering Gmbh", "DE"));
        data.add(new Metadata(62, "Austriamicrosystems AG (reserved)", "AT"));
        data.add(new Metadata(63, "Gemalto SA", "FR"));
        data.add(new Metadata(64, "Renesas Electronics Corporation", "JP"));
        data.add(new Metadata(65, "3Alogics Inc", "KR"));
        data.add(new Metadata(66, "Top TroniQ Asia Limited", "HK"));
        data.add(new Metadata(67, "Gentag Inc", "KR"));
        data.add(new Metadata(68, "Hynix", "US"));

        return data;
    }

    private class Metadata {
//        @SerializedName("id")
        private int id;

//        @SerializedName("manufacturer")
        private String manufacturer;

//        @SerializedName("location")
        private String location;

        public Metadata(int id, String manufacturer, String location) {
            this.manufacturer = manufacturer;
            this.id = id;
            this.location = location;
        }

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

