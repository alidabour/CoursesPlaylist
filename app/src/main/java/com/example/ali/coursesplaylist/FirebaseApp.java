package com.example.ali.coursesplaylist;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ali on 2/28/2017.
 */


public class FirebaseApp extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
