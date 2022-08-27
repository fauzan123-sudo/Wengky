package com.example.wengky.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wengky.R;
import com.example.wengky.helper.Constans;
import com.example.wengky.model.Model_Berita_Acara;
import com.example.wengky.ui.fragment.DetailBerita;
import com.example.wengky.ui.fragment.Home;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterBeritaAcara extends RecyclerView.Adapter<AdapterBeritaAcara.ExampleViewHolder> {
    private final Context mContext;
    public String BERITA_ACARA = Constans.urlImageBerita;
    private final ArrayList<Model_Berita_Acara> listBerita;

    public AdapterBeritaAcara(Context context, ArrayList<Model_Berita_Acara> exampleList) {
        mContext    = context;
        listBerita  = exampleList;
    }
    @NotNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_berita_acara, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Model_Berita_Acara currentItem = listBerita.get(position);
        String gambar       = currentItem.getGambar();
        String judul        = currentItem.getJudul();
        String keterangan   = currentItem.getKeterangan();

        Picasso.with(mContext).load(BERITA_ACARA+gambar).into(holder.Gambar);
        holder.Judul.setText(judul);
        holder.Keterangan.setText(keterangan);

//        DetailBerita fragmentB=new DetailBerita();
//        Bundle bundle=new Bundle();
//        bundle.putString("judul",currentItem.getJudul());
//        bundle.putString("isi",currentItem.getKeterangan());
//        fragmentB.setArguments(bundle);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = currentItem.getJudul();
                String konten = currentItem.getKeterangan();
                String gambar = currentItem.getGambar();
//                Toast.makeText(mContext, ""+value, Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(mContext)
                        .setTitle(value)
                        .setMessage(konten)
                        .setNegativeButton("Ok", (dialogInterface, i) -> {

                        })
                        .show();

//                Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
//                Context context = v.getContext();
//                listBerita.get(position.);
//                DetailBerita fragment2 = new DetailBerita();
//                mContext.getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, fragment2)
//                        .commit();

//                DetailBerita fragmentB=new DetailBerita();
//                Bundle bundle=new Bundle();
//                bundle.putString("NAME",value);
//                bundle.putString("JOB",konten);
//                fragmentB.setArguments(bundle);

//                DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
//
//                int DeviceTotalWidth = metrics.widthPixels;
//                int DeviceTotalHeight = metrics.heightPixels;
//
//                final Dialog dialog = new Dialog(mContext);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.fragment_detail_berita);
//                dialog.getWindow().setLayout(DeviceTotalWidth ,DeviceTotalHeight);
//                dialog.show();
//
//
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBerita.size();
    }
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView Gambar;
        public TextView Judul;
        public TextView Keterangan;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            Gambar      = itemView.findViewById(R.id.gambar);
            Judul       = itemView.findViewById(R.id.judul);
            Keterangan  = itemView.findViewById(R.id.keterangan);
        }
    }

    public interface FragmentCommunication {
        void respond(int position,String name,String job);
    }


}

