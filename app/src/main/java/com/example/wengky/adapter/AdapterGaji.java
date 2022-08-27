package com.example.wengky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wengky.R;
import com.example.wengky.model.ModelAbsen;
import com.example.wengky.model.Model_Gaji;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterGaji extends RecyclerView.Adapter<AdapterGaji.AdapterBaru> {
    Context context;
    ArrayList<Model_Gaji> model_gajis;


    public AdapterGaji(Context context, ArrayList<Model_Gaji> model_gajis) {
        this.context = context;
        this.model_gajis = model_gajis;
    }

    @NotNull
    @Override
    public AdapterBaru onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gaji,parent,false);
        return new AdapterBaru(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBaru holder, int position) {
        Model_Gaji modelAbsen = model_gajis.get(position);
        String keyGaji      = modelAbsen.getKeyGaji();
        String valueGaji    = modelAbsen.getValueGaji();

        holder.keyGaji.setText(keyGaji);
        holder.valueGaji.setText(valueGaji);
    }

    @Override
    public int getItemCount() {
        return model_gajis.size();
    }

    public class AdapterBaru extends RecyclerView.ViewHolder {
        public TextView keyGaji,valueGaji;
        public AdapterBaru(@NonNull @NotNull View itemView) {
            super(itemView);
            keyGaji     = itemView.findViewById(R.id.keyGaji);
            valueGaji   = itemView.findViewById(R.id.valueGaji);

        }
    }
}
