package com.example.wengky.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.wengky.R;

public class LoadingCustom {
    private AlertDialog dialog;
    private final Activity activity;


    public LoadingCustom(Activity myActivity){
        activity = myActivity;
    }

    @SuppressLint("InflateParams")
    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dissmisDialog(){
        dialog.dismiss();

    }
}
