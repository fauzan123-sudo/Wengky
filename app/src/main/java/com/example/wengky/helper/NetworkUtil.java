package com.example.wengky.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static String getNetworkState(Context context) {
        String status;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi connected";
                return status;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data  connected";
                return status;
            }

        } else {
            status = "No internet";
            return status;
        }

        return null;
    }
}

