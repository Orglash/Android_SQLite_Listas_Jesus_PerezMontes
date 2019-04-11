package com.example.sqlitelistas;

import java.io.Serializable;

public class Lugares implements Serializable {

    private String mLugar;
    private String mProvincia;

    public Lugares(String lugar, String provincia) {
        mLugar = lugar;
        mProvincia = provincia;
    }

    public String getLugar() {
        return mLugar;
    }

    public void setLugar(String mLugar) {
        this.mLugar = mLugar;
    }

    public String getProvincia() {
        return mProvincia;
    }

    public void setProvincia(String mProvincia) {
        this.mProvincia = mProvincia;
    }


    @Override
    public String toString() {
        return  ", Lugar='" + mLugar + '\'' +
                ", Provincia='" + mProvincia + '\'';
    }


}

