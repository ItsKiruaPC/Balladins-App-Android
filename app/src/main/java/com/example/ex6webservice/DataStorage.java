package com.example.ex6webservice;

import androidx.appcompat.app.AppCompatActivity;

public class DataStorage extends AppCompatActivity {
    private static DataStorage instance;
    private String maDonnee;

    private DataStorage() {
        // Constructeur privé pour empêcher l'instanciation directe
    }

    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public String getMaDonnee() {
        return maDonnee;
    }

    public void setMaDonnee(String donnee) {
        this.maDonnee = donnee;
    }
}
