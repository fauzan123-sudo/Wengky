package com.example.wengky.helper;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.wengky.R;

public class LoadingCustomFragment {

    private AlertDialog dialogFragment;
    private final Fragment fragment;


    public LoadingCustomFragment(Fragment myActivity){
        fragment = myActivity;
    }

    public void StartLoadingDialogFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogFragment.getContext());
        LayoutInflater inflater = fragment.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading, null));
        builder.setCancelable(false);

        dialogFragment = builder.create();
        dialogFragment.show();
    }

    public void dissmisDialogFragment(){
        dialogFragment.dismiss();

    }
}
