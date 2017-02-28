package com.example.ali.coursesplaylist;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ali.coursesplaylist.adapter.DescriptionAdapter;
import com.example.ali.coursesplaylist.data.Course;
import com.example.ali.coursesplaylist.data.DataContract;
import com.example.ali.coursesplaylist.data.JsonData.Item;
import com.example.ali.coursesplaylist.data.JsonData.Playlist;
import com.example.ali.coursesplaylist.data.JsonData.Snippet;
import com.example.ali.coursesplaylist.widget.CollectionWidget;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity implements StringResponseListener{
    RecyclerView recyclerView;
    DescriptionAdapter descriptionAdapter;
    Button addButton;
    TextView channelTitle;
    TextView description;
    ImageView imageView;
    List<Snippet> snippetList;
    NestedScrollView nestedScrollView;
    String key;
    String url;
    String playName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        nestedScrollView = (NestedScrollView)findViewById(R.id.activity_description);
        imageView = (ImageView) findViewById(R.id.imageView2);
        addButton = (Button) findViewById(R.id.add);
        addButton.setEnabled(false);
        channelTitle = (TextView) findViewById(R.id.channelTitle);
        description = (TextView) findViewById(R.id.description);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
//        recyclerView.setAdapter(new DescriptionAdapter());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        snippetList = new ArrayList<>();
        Intent intent = getIntent();
        if(intent != null){
            description.setText(intent.getStringExtra("description"));
            description.setContentDescription(intent.getStringExtra("description"));

            channelTitle.setText(intent.getStringExtra("channelTitle"));
            channelTitle.setContentDescription(intent.getStringExtra("channelTitle"));

            key = intent.getStringExtra("key");
            url = intent.getStringExtra("url");
            Glide.with(getApplicationContext()).load(url).into(imageView);
            playName = intent.getStringExtra("name");
            DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(this);
            String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" +
                    key+"&key=AIzaSyBxXCY_aLBJpE1Xlazei3PMVycq2j-cCrU";
            try {
                downloadAsyncTask.execute(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c =getContentResolver().query(DataContract.CourseEntry.CONTENT_URI,new String[]{
                    DataContract.CourseEntry.PLAYLIST_KEY_COLUMN}
                        , DataContract.CourseEntry.PLAYLIST_KEY_COLUMN + " = ?"
                        ,new String[]{key}
                        ,null);

                if (c.getCount()>0){
                    Snackbar.make(nestedScrollView, R.string.course_already_added,Snackbar.LENGTH_LONG).show();
                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataContract.CourseEntry.PLAYLIST_KEY_COLUMN,key);
                    contentValues.put(DataContract.CourseEntry.PLAYLIST_NAME_COLUMN,playName);
                    contentValues.put(DataContract.CourseEntry.PLAYLIST_IMAGE_URL,url);
                    Log.v("Test","Content Value:"+contentValues);
                    getContentResolver().insert(DataContract.CourseEntry.CONTENT_URI,contentValues);
                    Snackbar.make(nestedScrollView, R.string.course_successfully,Snackbar.LENGTH_LONG).show();

                    updateAllWidgets();
                }

                c.close();

            }
        });
    }

    @Override
    public void notifySuccess(String response) {
        Gson gson = new Gson();
        Playlist p = gson.fromJson(response, Playlist.class);
        if (p != null){
            List<Item> l = p.getItems();
            snippetList = new ArrayList<>();
            for (Item x: l){
                snippetList.add(x.getSnippet());
            }
        }
        descriptionAdapter = new DescriptionAdapter(snippetList, new DescriptionAdapter.OnClickHandler() {
            @Override
            public void onClick(Snippet s) {
                Toast.makeText(getApplicationContext(), R.string.list_course_des,Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(descriptionAdapter);
        addButton.setEnabled(true);

    }

    @Override
    public void notifyError(String error) {

    }
    private void updateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, CollectionWidget.class));
        if (appWidgetIds.length > 0) {
            //new CollectionWidget().onUpdate(this, appWidgetManager, appWidgetIds);
            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.course_app_widget);

            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }
}
