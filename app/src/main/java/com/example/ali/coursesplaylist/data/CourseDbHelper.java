package com.example.ali.coursesplaylist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ali.coursesplaylist.data.DataContract.CourseEntry;

/**
 * Created by Ali on 2/21/2017.
 */

public class CourseDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "course.db";
    private static final int VERSION = 1;

    CourseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + CourseEntry.TABLE_NAME + " (" +
                CourseEntry._ID + " INTEGER PRIMARY KEY , " +
                CourseEntry.PLAYLIST_KEY_COLUMN + " TEXT NOT NULL, " +
                CourseEntry.PLAYLIST_NAME_COLUMN + " TEXT NOT NULL, " +
                CourseEntry.PLAYLIST_IMAGE_URL + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
