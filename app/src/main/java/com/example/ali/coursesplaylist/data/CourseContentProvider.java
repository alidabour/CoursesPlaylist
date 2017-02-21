package com.example.ali.coursesplaylist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.ali.coursesplaylist.data.DataContract.CourseEntry.TABLE_NAME;

/**
 * Created by Ali on 2/21/2017.
 */

public class CourseContentProvider extends ContentProvider {

    public static final int COURSES = 100;
    public static final int COURSES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CourseDbHelper courseDbHelper;
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_COURSE, COURSES);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_COURSE + "/#", COURSES_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        courseDbHelper = new CourseDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,  String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = courseDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case COURSES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = courseDbHelper.getWritableDatabase();
        int match  = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case COURSES:
                long id = db.insert(TABLE_NAME,null,contentValues);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(DataContract.CourseEntry.CONTENT_URI , id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = courseDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int coursesDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case COURSES_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                coursesDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (coursesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return coursesDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
