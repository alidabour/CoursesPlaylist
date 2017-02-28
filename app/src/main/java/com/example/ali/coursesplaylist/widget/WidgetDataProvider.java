package com.example.ali.coursesplaylist.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.data.DataContract;

import java.util.concurrent.ExecutionException;

/**
 * Created by Ali on 2/26/2017.
 */

public class WidgetDataProvider extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            private AppWidgetTarget appWidgetTarget;
            @Override
            public void onCreate() {
                Log.v("Widget","onCreateW");
            }

            @Override
            public void onDataSetChanged() {
                Log.v("Widget","onDataChanged");

                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                Uri uri = DataContract.CourseEntry.CONTENT_URI;
                data = getContentResolver().query(uri
                        ,new String[]{DataContract.CourseEntry._ID
                                ,DataContract.CourseEntry.PLAYLIST_IMAGE_URL
                                ,DataContract.CourseEntry.PLAYLIST_NAME_COLUMN,
                                DataContract.CourseEntry.PLAYLIST_KEY_COLUMN}
                        ,null,null,null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                Log.v("Widget","onDestroy");

                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                Log.v("Widget","getCount");
                //test
                if(data == null){
                    Log.v("Widget","getCount Null");
                }else {
                    Log.v("Widget","getCount :" + data.getCount());
                }
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.v("Widget","getViewAt");

                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews view = new RemoteViews(getPackageName(),
                        R.layout.added_course_item);

                view.setTextViewText(R.id.courseName,data.getString(data.getColumnIndex(DataContract.CourseEntry.PLAYLIST_NAME_COLUMN)));
                String imageUri = data.getString(data.getColumnIndex(DataContract.CourseEntry.PLAYLIST_IMAGE_URL));
                try {
                    view.setImageViewBitmap(R.id.imageView,Glide.with(getApplicationContext()).load(imageUri).asBitmap().into(100,100).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                final Intent fillInIntent = new Intent();
//                Uri uri = DataContract.CourseEntry.CONTENT_URI;
//                fillInIntent.setData(uri);
//                fillInIntent.putExtra("id",data.getString(data.getColumnIndex(DataContract.CourseEntry.PLAYLIST_KEY_COLUMN)));
                view.setOnClickFillInIntent(R.id.line1,fillInIntent);

                return view;
            }

            @Override
            public RemoteViews getLoadingView() {
                Log.v("Widget","getLoadingView");

                return new RemoteViews(getPackageName(),R.layout.added_course_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                Log.v("Widget","getItemId");

                if(data.moveToPosition(i)){
                    Log.v("Widget","getItemId");
                    Long l = Long.parseLong(data.getString(data.getColumnIndex(DataContract.CourseEntry._ID)));
                    Log.v("Widget","getItemId : "+i +" Long : "+l);
                    return l;
                }
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
