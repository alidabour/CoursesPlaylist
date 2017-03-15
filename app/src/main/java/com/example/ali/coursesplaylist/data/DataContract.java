package com.example.ali.coursesplaylist.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ali on 2/21/2017.
 */

public class DataContract {

    public static final String AUTHORITY = "com.example.ali.coursesplaylist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_COURSE = "course";


    private DataContract() {
    }

    public static class CourseEntry implements BaseColumns {
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSE)
                .build();
        public static final String TABLE_NAME = "course";
        public static final String PLAYLIST_KEY_COLUMN = "playlist";
        public static final String PLAYLIST_NAME_COLUMN = "playlistname";
        public static final String PLAYLIST_IMAGE_URL = "playlistimage";
    }
}
