package com.example.wengky.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wengky.R;
import com.example.wengky.model.Model_Berita_Acara;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerListAdapter{
//        (OnLastPositionReached listener)
//        extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {
//    private ArrayList<Model_Berita_Acara> listBerita;
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_berita_acara, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Model_Berita_Acara model_berita_acara = listBerita.get(position);
//        holder.(contentList[position])
//        if (position == contentList.size-1){
//            listener.lastPositionReached()
//        } else {
//            listener.otherPosition()
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return listBerita.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(@NonNull @NotNull View itemView) {
//            super(itemView);
//        }
//    }
//    interface OnLastPositionReached{
//        void lastPositionReached();
//        void otherPosition();
//    }
}

