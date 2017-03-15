
package com.example.ali.coursesplaylist.data.JsonData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    @SerializedName("snippet")
    @Expose
    private Snippet snippet;
    public final static Parcelable.Creator<Item> CREATOR = new Creator<Item>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Item createFromParcel(Parcel in) {
            Item instance = new Item();
            instance.snippet = ((Snippet) in.readValue((Snippet.class.getClassLoader())));
            return instance;
        }

        public Item[] newArray(int size) {
            return (new Item[size]);
        }

    };


    public Snippet getSnippet() {
        return snippet;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(snippet);
    }

    public int describeContents() {
        return 0;
    }

}
