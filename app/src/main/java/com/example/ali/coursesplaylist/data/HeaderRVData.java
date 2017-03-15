package com.example.ali.coursesplaylist.data;

import java.util.List;

/**
 * Created by Ali on 3/9/2017.
 */

public class HeaderRVData {
    String header;
    List<Course> categoryInformations;

    public HeaderRVData(String header, List<Course> categoryInformations) {
        this.header = header;
        this.categoryInformations = categoryInformations;
    }

    public List<Course> getCategoryInformations() {
        return categoryInformations;
    }

    public String getHeader() {
        return header;
    }

}