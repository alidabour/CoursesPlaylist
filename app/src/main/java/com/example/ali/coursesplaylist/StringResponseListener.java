package com.example.ali.coursesplaylist;

/**
 * Created by Ali on 2/18/2017.
 */


public interface StringResponseListener {
    void notifySuccess(String response);
    void notifyError(String error);
}
