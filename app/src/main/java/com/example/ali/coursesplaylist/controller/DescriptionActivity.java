package com.example.ali.coursesplaylist.controller;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ali.coursesplaylist.BuildConfig;
import com.example.ali.coursesplaylist.network.DownloadAsyncTask;
import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.network.StringResponseListener;
import com.example.ali.coursesplaylist.adapter.DescriptionAdapter;
import com.example.ali.coursesplaylist.data.Course;
import com.example.ali.coursesplaylist.data.DataContract;
import com.example.ali.coursesplaylist.data.JsonData.Item;
import com.example.ali.coursesplaylist.data.JsonData.Playlist;
import com.example.ali.coursesplaylist.data.JsonData.Snippet;
import com.example.ali.coursesplaylist.widget.CollectionWidget;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionActivity extends AppCompatActivity implements StringResponseListener {
    LinearLayoutManager linearLayoutManager;
    DescriptionAdapter descriptionAdapter;
    List<Snippet> snippetList;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.add) Button addButton;
    @BindView(R.id.channelTitle) TextView channelTitle;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.imageView2) ImageView imageView;
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;
    String key;
    String url;
    String playName;
    String nextPageToken;
    FirebaseAnalytics mFirebaseAnalytics;
    boolean loadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        addButton.setEnabled(false);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        snippetList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("course")) {
            Course course = intent.getParcelableExtra("course");
            description.setText(course.getDescription());
            description.setContentDescription(course.getDescription());

            channelTitle.setText(course.getChannelTitle());
            channelTitle.setContentDescription(course.getChannelTitle());

            key = course.getKey();
            url = course.getUrl();

            Glide.with(getApplicationContext())
                    .load(url)
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(imageView);

            playName = course.getName();
            DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(this);
            String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=10&playlistId=" +
                    key + "&key=" + BuildConfig.API_KEY;

            try {
                downloadAsyncTask.execute(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = getContentResolver()
                        .query(DataContract.CourseEntry.CONTENT_URI, new String[]{
                                        DataContract.CourseEntry.PLAYLIST_KEY_COLUMN}
                                , DataContract.CourseEntry.PLAYLIST_KEY_COLUMN + " = ?"
                                , new String[]{key}
                                , null);

                if (c.getCount() > 0) {
                    Snackbar.make(nestedScrollView, R.string.course_already_added,
                            Snackbar.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataContract.CourseEntry.PLAYLIST_KEY_COLUMN, key);
                    contentValues.put(DataContract.CourseEntry.PLAYLIST_NAME_COLUMN, playName);
                    contentValues.put(DataContract.CourseEntry.PLAYLIST_IMAGE_URL, url);
                    getContentResolver()
                            .insert(DataContract.CourseEntry.CONTENT_URI, contentValues);
                    Snackbar.make(nestedScrollView, R.string.course_successfully,
                            Snackbar.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, key);
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, playName);
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "course");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    updateAllWidgets();
                }
                c.close();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                View view = (View) nestedScrollView
                        .getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = view.getBottom() - (nestedScrollView.getHeight() + scrollY);
                if (diff < 10 && loadMore) {
                    DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(
                            new StringResponseListener() {
                                @Override
                                public void notifySuccess(String response) {
                                    Gson gson = new Gson();
                                    Playlist p = gson.fromJson(response, Playlist.class);

                                    if (p != null) {
                                        if (p.getNextPageToken() != null) {
                                            nextPageToken = p.getNextPageToken();
                                        } else {
                                            loadMore = false;
                                        }
                                        List<Item> l = p.getItems();
                                        for (Item x : l) {
                                            snippetList.add(x.getSnippet());
                                        }
                                    }
                                    descriptionAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void notifyError(String error) {

                                }
                            });
                    String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=10&playlistId=" +
                            key + "&pageToken=" + nextPageToken + "&key=" + BuildConfig.API_KEY;
                    try {
                        downloadAsyncTask.execute(new URL(url));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void notifySuccess(String response) {
        Gson gson = new Gson();
        Playlist p = gson.fromJson(response, Playlist.class);
        if (p != null) {
            if (p.getNextPageToken() != null) {
                nextPageToken = p.getNextPageToken();
            } else {
                loadMore = false;
            }
            if (p.getItems() != null) {
                List<Item> l = p.getItems();
                snippetList = new ArrayList<>();
                for (Item x : l) {
                    snippetList.add(x.getSnippet());
                }
            }
        }

        descriptionAdapter = new DescriptionAdapter(snippetList,
                new DescriptionAdapter.OnClickHandler() {
                    @Override
                    public void onClick(Snippet s) {
                        Toast.makeText(getApplicationContext(), R.string.list_course_des,
                                Toast.LENGTH_LONG).show();
                    }
                });
        recyclerView.setAdapter(descriptionAdapter);
        addButton.setEnabled(true);

    }

    @Override
    public void notifyError(String error) {
        Log.e(DescriptionActivity.class.toString(), "notifyError: " + error);
    }

    private void updateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, CollectionWidget.class));
        if (appWidgetIds.length > 0) {
            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.course_app_widget);
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }
}
