package com.example.wengky.model;

public class Model_Gaji {
    String KeyGaji, ValueGaji;

    public Model_Gaji(String keyGaji, String valueGaji) {
        KeyGaji = keyGaji;
        ValueGaji = valueGaji;
    }

    public String getKeyGaji() {
        return KeyGaji;
    }

    public void setKeyGaji(String keyGaji) {
        KeyGaji = keyGaji;
    }

    public String getValueGaji() {
        return ValueGaji;
    }

    public void setValueGaji(String valueGaji) {
        ValueGaji = valueGaji;
    }
}
