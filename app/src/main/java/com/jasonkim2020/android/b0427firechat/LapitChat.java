package com.jasonkim2020.android.b0427firechat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class LapitChat extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
