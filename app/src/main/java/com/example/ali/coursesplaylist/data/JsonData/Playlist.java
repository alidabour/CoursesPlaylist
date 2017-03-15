
package com.example.ali.coursesplaylist.data.JsonData;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

public class Playlist implements Parcelable {

    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    public final static Parcelable.Creator<Playlist> CREATOR = new Creator<Playlist>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Playlist createFromParcel(Parcel in) {
            Playlist instance = new Playlist();
            instance.nextPageToken = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.items, (Item.class.getClassLoader()));
            return instance;
        }

        public Playlist[] newArray(int size) {
            return (new Playlist[size]);
        }

    };



    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<Item> getItems() {
        return items;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(nextPageToken);
        dest.writeList(items);
    }

    public int describeContents() {
        return 0;
    }

}
