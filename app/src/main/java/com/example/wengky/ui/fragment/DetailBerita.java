package com.example.wengky.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wengky.R;

public class DetailBerita extends Fragment {
    TextView detailJudul,detailKonten,detailGambar;
    String judul,konten, gambar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        judul=getArguments().getString("NAME");
//        konten=getArguments().getString("JOB");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_berita, container, false);

//        detailJudul = view.findViewById(R.id.txtJudul);
//        detailKonten = view.findViewById(R.id.txtIsi);

//        detailJudul.setText(judul);
//        detailKonten.setText(konten);

        return  view;
    }
}