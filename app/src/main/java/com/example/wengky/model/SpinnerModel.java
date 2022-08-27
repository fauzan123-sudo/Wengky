package com.example.wengky.model;

public class SpinnerModel {
    private String nama;
    private int idJenisIzin;

    public SpinnerModel() {
    }
    public SpinnerModel(String nama, int idJenisIzin) {
        this.nama = nama;
        this.idJenisIzin = idJenisIzin;

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getIdJenisIzin() {
        return idJenisIzin;
    }

    public void setIdJenisIzin(int idJenisIzin) {
        this.idJenisIzin = idJenisIzin;
    }
}
