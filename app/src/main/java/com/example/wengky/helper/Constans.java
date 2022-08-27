package com.example.wengky.helper;

public class Constans {
    //TODO URL
    //base url
    public static final String URL              = "http://192.168.0.113:70/wengky_new/";
    //login
    public static final String URL_LOGIN        = URL + "api/login";
    //profile
    public static final String urlImagePegawai  = URL+"upload/image/pegawai/";
    // berita acara
    public static final String urlImageBerita   = URL+"upload/berita/";
    //    scanning
//    gaji
    public static final String CekScanning      = URL+"api/scan";
    public static final String SCAN             = URL+"Api/scan";
    public static final String read_absensi     = URL +"api/read_absensi_personal";
    public static final String inputToken       = URL+"cekToken";
    public static String PROFILE                = URL +"api/read";
    public static String gaji                   = URL +"api/gaji";
    public static String berita                 = URL +"api/beritaAcara";
    public static final String URL_InserScan    = URL + "api/InsertAbsen"; //error
    public static final String Total_Absensi    = URL+ "api/statistic_absensi_page";
    public static final String URL_Spinner      = URL+ "api/spinnerizin";
    public static final String izin             = URL+ "api/perijinan";
    public static final String data             = URL+ "api/statistic_pegawai";
    public static final String updateToken      = URL+ "api/updateTokenPegawai";

    //TODO REQUEST STATIC
//    public static final String CHANNEL_ID       = "channel_id";
    public static final String CHANNEL_NAME     = "channel_name";
    public static final String CHANNEL_DESC     = "channel_desc";
    public static final String TAG_SUCCESS      = "success";
    public static final String TAG_MESSAGE      = "message";
    public static final String TAG_QR           = "qr";
    public static final String TAG_JSON_OBJECT  = "json_obj_req";

}

