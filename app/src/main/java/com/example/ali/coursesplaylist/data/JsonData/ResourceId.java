
package com.example.ali.coursesplaylist.data.JsonData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceId implements Parcelable
{


    @SerializedName("videoId")
    @Expose
    private String videoId;
    public final static Parcelable.Creator<ResourceId> CREATOR = new Creator<ResourceId>() {
        @SuppressWarnings({
            "unchecked"
        })
        public ResourceId createFromParcel(Parcel in) {
            ResourceId instance = new ResourceId();
            instance.videoId = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public ResourceId[] newArray(int size) {
            return (new ResourceId[size]);
        }

    }
    ;
    public String getVideoId() {
        return videoId;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(videoId);
    }

    public int describeContents() {
        return  0;
    }

}
