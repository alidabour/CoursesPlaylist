
package com.example.ali.coursesplaylist.data.JsonData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet implements Parcelable
{

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("channelId")
    @Expose
    private String channelId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;
    @SerializedName("channelTitle")
    @Expose
    private String channelTitle;
    @SerializedName("playlistId")
    @Expose
    private String playlistId;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("resourceId")
    @Expose
    private ResourceId resourceId;
    public final static Parcelable.Creator<Snippet> CREATOR = new Creator<Snippet>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Snippet createFromParcel(Parcel in) {
            Snippet instance = new Snippet();
            instance.publishedAt = ((String) in.readValue((String.class.getClassLoader())));
            instance.channelId = ((String) in.readValue((String.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.description = ((String) in.readValue((String.class.getClassLoader())));
            instance.thumbnails = ((Thumbnails) in.readValue((Thumbnails.class.getClassLoader())));
            instance.channelTitle = ((String) in.readValue((String.class.getClassLoader())));
            instance.playlistId = ((String) in.readValue((String.class.getClassLoader())));
            instance.position = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.resourceId = ((ResourceId) in.readValue((ResourceId.class.getClassLoader())));
            return instance;
        }

        public Snippet[] newArray(int size) {
            return (new Snippet[size]);
        }

    }
    ;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public ResourceId getResourceId() {
        return resourceId;
    }

    public void setResourceId(ResourceId resourceId) {
        this.resourceId = resourceId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(publishedAt);
        dest.writeValue(channelId);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(thumbnails);
        dest.writeValue(channelTitle);
        dest.writeValue(playlistId);
        dest.writeValue(position);
        dest.writeValue(resourceId);
    }

    public int describeContents() {
        return  0;
    }

}
