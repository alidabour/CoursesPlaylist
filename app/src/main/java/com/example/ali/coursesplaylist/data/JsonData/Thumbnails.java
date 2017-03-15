
package com.example.ali.coursesplaylist.data.JsonData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails implements Parcelable
{

    @SerializedName("default")
    @Expose
    private ImageQuality _imageQuality;
    @SerializedName("medium")
    @Expose
    private ImageQuality medium;
    @SerializedName("high")
    @Expose
    private ImageQuality high;
    @SerializedName("standard")
    @Expose
    private ImageQuality standard;
    @SerializedName("maxres")
    @Expose
    private ImageQuality maxres;
    public final static Parcelable.Creator<Thumbnails> CREATOR = new Creator<Thumbnails>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Thumbnails createFromParcel(Parcel in) {
            Thumbnails instance = new Thumbnails();
            instance._imageQuality = ((ImageQuality) in.readValue((ImageQuality.class.getClassLoader())));
            instance.medium = ((ImageQuality) in.readValue((ImageQuality.class.getClassLoader())));
            instance.high = ((ImageQuality) in.readValue((ImageQuality.class.getClassLoader())));
            instance.standard = ((ImageQuality) in.readValue((ImageQuality.class.getClassLoader())));
            instance.maxres = ((ImageQuality) in.readValue((ImageQuality.class.getClassLoader())));
            return instance;
        }

        public Thumbnails[] newArray(int size) {
            return (new Thumbnails[size]);
        }

    }
    ;

    public ImageQuality getDefault() {
        return _imageQuality;
    }

    public void setDefault(ImageQuality _imageQuality) {
        this._imageQuality = _imageQuality;
    }

    public ImageQuality getMedium() {
        return medium;
    }

    public void setMedium(ImageQuality medium) {
        this.medium = medium;
    }

    public ImageQuality getHigh() {
        return high;
    }

    public void setHigh(ImageQuality high) {
        this.high = high;
    }

    public ImageQuality getStandard() {
        return standard;
    }

    public void setStandard(ImageQuality standard) {
        this.standard = standard;
    }

    public ImageQuality getMaxres() {
        return maxres;
    }

    public void setMaxres(ImageQuality maxres) {
        this.maxres = maxres;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_imageQuality);
        dest.writeValue(medium);
        dest.writeValue(high);
        dest.writeValue(standard);
        dest.writeValue(maxres);
    }

    public int describeContents() {
        return  0;
    }

}
