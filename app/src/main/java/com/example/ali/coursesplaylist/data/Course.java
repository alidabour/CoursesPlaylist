package com.example.ali.coursesplaylist.data;

/**
 * Created by Ali on 2/21/2017.
 */

public class Course {
    public String key;
    public String name;
    public String url;
    public String channelTitle;

    public String description;

    public Course(){

    }
    public Course(String key,String name,String url,String description,String channelTitle){
        this.key = key;
        this.name = name;
        this.url = url;
        this.description = description;
        this.channelTitle = channelTitle;
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
}
