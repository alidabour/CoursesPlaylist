
package com.example.ali.coursesplaylist.data.JsonData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet implements Parcelable {


    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;
    @SerializedName("resourceId")
    @Expose
    private ResourceId resourceId;
    public final static Parcelable.Creator<Snippet> CREATOR = new Creator<Snippet>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Snippet createFromParcel(Parcel in) {
            Snippet instance = new Snippet();
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.description = ((String) in.readValue((String.class.getClassLoader())));
            instance.thumbnails = ((Thumbnails) in.readValue((Thumbnails.class.getClassLoader())));
            instance.resourceId = ((ResourceId) in.readValue((ResourceId.class.getClassLoader())));
            return instance;
        }

        public Snippet[] newArray(int size) {
            return (new Snippet[size]);
        }

    };


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }


    public ResourceId getResourceId() {
        return resourceId;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(thumbnails);
        dest.writeValue(resourceId);
    }

    public int describeContents() {
        return 0;
    }

}
