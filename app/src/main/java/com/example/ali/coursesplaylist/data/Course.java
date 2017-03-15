package com.example.ali.coursesplaylist.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ali on 2/21/2017.
 */

public class Course implements Parcelable {
    public String key;
    public String name;
    public String url;
    public String channelTitle;
    public String description;


    public Course() {

    }

    public Course(String key, String name, String url, String description, String channelTitle) {
        this.key = key;
        this.name = name;
        this.url = url;
        this.description = description;
        this.channelTitle = channelTitle;
    }

    protected Course(Parcel in) {
        key = in.readString();
        name = in.readString();
        url = in.readString();
        channelTitle = in.readString();
        description = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            Course instance = new Course();
            instance.key = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.channelTitle = ((String) in.readValue((String.class.getClassLoader())));
            instance.description = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public static Course fromCursor(Cursor cursor) {
        Course course = new Course();
        course.name = cursor
                .getString(cursor.getColumnIndex(DataContract.CourseEntry.PLAYLIST_NAME_COLUMN));
        course.url = cursor
                .getString(cursor.getColumnIndex(DataContract.CourseEntry.PLAYLIST_IMAGE_URL));
        course.key = cursor
                .getString(cursor.getColumnIndex(DataContract.CourseEntry.PLAYLIST_KEY_COLUMN));
        course.channelTitle = "not set";
        course.description = "not set";
        return course;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(key);
        parcel.writeValue(name);
        parcel.writeValue(url);
        parcel.writeValue(channelTitle);
        parcel.writeValue(description);
    }
}
