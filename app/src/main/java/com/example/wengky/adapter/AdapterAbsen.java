package com.example.wengky.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wengky.R;
import com.example.wengky.model.ModelAbsen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterAbsen extends RecyclerView.Adapter<AdapterAbsen.AdapterBaru> {
    Context context;
    ArrayList<ModelAbsen> modelAbsens;

    public AdapterAbsen(Context context, ArrayList<ModelAbsen> modelAbsens) {
        this.context = context;
        this.modelAbsens = modelAbsens;
    }

    @NotNull
    @Override
    public AdapterBaru onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_absensi,parent,false);
        return new AdapterBaru(view);
    }

    @Override
    public void onBindViewHolder(AdapterBaru holder, int position) {

        ModelAbsen modelAbsen = modelAbsens.get(position);
        String jam      = modelAbsen.getmJam();
        String tanggal  = modelAbsen.getmTanggal();
        String badge    = modelAbsen.getmStatus();

        holder.badge.setText(badge);
        holder.Tanggal.setText(tanggal);
        holder.Jam.setText(jam);
        holder.imgFace.setBackgroundResource(R.drawable.ic_alpa);

        if (badge.equals("1")){
            holder.badge.setText("Masuk");
            holder.badge.setTextColor(Color.parseColor("#0FAF3C"));
            holder.imgFace.setBackgroundResource(R.drawable.ic_masuk);
//            holder.imgFace.setImageDrawable(R.drawable.);
        }else if(badge.equals("2")){
            holder.badge.setText("Sakit");
            holder.badge.setTextColor(Color.parseColor("#3CC4FF"));
            holder.imgFace.setBackgroundResource(R.drawable.ic_izin);
//            holder.imgFace.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_sakit));
        }else if(badge.equals("3")){
            holder.badge.setText("Izin");
            holder.badge.setTextColor(Color.parseColor("#ffca2c"));
            holder.imgFace.setBackgroundResource(R.drawable.ic_sakit);
//            holder.imgFace.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_izin));
        }else if(badge.equals("4")){
            holder.badge.setText("Telat");
            holder.badge.setTextColor(Color.parseColor("#FF0000"));
            holder.imgFace.setBackgroundResource(R.drawable.ic_alpa);
//            holder.imgFace.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_alpa));
        }
        else if(badge.equals("0")){
            holder.badge.setText("Belum ada absen");
            holder.badge.setTextColor(Color.parseColor("#ffca2c"));
            holder.imgFace.setBackgroundResource(R.drawable.ic_gak_ada);
        }
    }

    @Override
    public int getItemCount() {
        return modelAbsens.size();
    }

    public static class AdapterBaru extends RecyclerView.ViewHolder {
        public TextView Jam,Tanggal, badge;
        public ImageView imgFace;
        public AdapterBaru(View itemView) {
            super(itemView);
            Jam     = itemView.findViewById(R.id.jam);
            Tanggal = itemView.findViewById(R.id.tanggal);
            badge   = itemView.findViewById(R.id.badges);
            imgFace = itemView.findViewById(R.id.icon_girl_blue);
        }
    }
}
