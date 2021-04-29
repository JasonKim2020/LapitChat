package com.jasonkim2020.android.b0427firechat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


// I do not know well about this code
// This code is related into saving image in internal storage
// and retrieving image from internal storage
public class LapitChat extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        /*  Picasso  */
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader((new OkHttpDownloader(this, Integer.MAX_VALUE)));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
