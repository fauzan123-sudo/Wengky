package com.example.wengky.model;

public class ModelAbsen {
    private String mTanggal;
    private String mJam;
    private String mStatus;

    public ModelAbsen(String mTanggal, String mJam, String mStatus) {
        this.mTanggal = mTanggal;
        this.mJam = mJam;
        this.mStatus = mStatus;
    }

    public String getmTanggal() {
        return mTanggal;
    }
    public void setmTanggal(String mTanggal) {
        this.mTanggal = mTanggal;
    }
    public String getmJam() {
        return mJam;
    }

    public void setmJam(String mJam) {
        this.mJam = mJam;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
