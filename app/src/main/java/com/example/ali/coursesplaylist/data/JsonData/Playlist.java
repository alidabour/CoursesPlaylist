
package com.example.ali.coursesplaylist.data.JsonData;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Playlist implements Parcelable
{

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("pageInfo")
    @Expose
    private PageInfo pageInfo;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    public final static Parcelable.Creator<Playlist> CREATOR = new Creator<Playlist>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Playlist createFromParcel(Parcel in) {
            Playlist instance = new Playlist();
            instance.kind = ((String) in.readValue((String.class.getClassLoader())));
            instance.etag = ((String) in.readValue((String.class.getClassLoader())));
            instance.pageInfo = ((PageInfo) in.readValue((PageInfo.class.getClassLoader())));
            in.readList(instance.items, (Item.class.getClassLoader()));
            return instance;
        }

        public Playlist[] newArray(int size) {
            return (new Playlist[size]);
        }

    }
    ;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(kind);
        dest.writeValue(etag);
        dest.writeValue(pageInfo);
        dest.writeList(items);
    }

    public int describeContents() {
        return  0;
    }

}
