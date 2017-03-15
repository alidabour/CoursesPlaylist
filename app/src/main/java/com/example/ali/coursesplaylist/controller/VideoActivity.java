package com.example.ali.coursesplaylist.controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ali.coursesplaylist.BuildConfig;
import com.example.ali.coursesplaylist.network.DownloadAsyncTask;
import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.network.StringResponseListener;
import com.example.ali.coursesplaylist.YoutubeKeyCall;
import com.example.ali.coursesplaylist.adapter.DescriptionAdapter;
import com.example.ali.coursesplaylist.data.JsonData.Item;
import com.example.ali.coursesplaylist.data.JsonData.Playlist;
import com.example.ali.coursesplaylist.data.JsonData.Snippet;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, StringResponseListener {
    YouTubePlayer youTubePlayer;
    DescriptionAdapter descriptionAdapter;
    YoutubeKeyCall youtubeKeyCall;
    List<String> urlsList;
    List<String> titleList;
    List<Snippet> snippets;
    String key;
    String nextPageToken;
    boolean loadMore = true;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.nestedScrollView) NestedScrollView scrollView;
    @BindView(R.id.youtubeplayer) YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            key = intent.getStringExtra("key");
        }
        youTubePlayerView.initialize(BuildConfig.API_KEY, this);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                        false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = view.getBottom() - (scrollView.getHeight() + scrollY);
                if (diff < 10 && loadMore) {
                    Log.v("Test", "Bo: " + view.getBottom() + " scrollY :" + scrollY);
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
                                            snippets.add(x.getSnippet());
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
        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(this);
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=10&playlistId=" +
                key + "&key=" + BuildConfig.API_KEY;
        try {
            downloadAsyncTask.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        final YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        Log.v("TVideo", "onInitializationFailure");

    }

    @Override
    public void notifySuccess(String response) {
        Gson gson = new Gson();
        Playlist p = gson.fromJson(response, Playlist.class);
        urlsList = new ArrayList<>();
        titleList = new ArrayList<>();
        snippets = new ArrayList<>();
        if (p.getItems() != null) {
            List<Item> l = p.getItems();
            if (l.get(0).getSnippet() != null) {
                if (youTubePlayer != null) {
                    youTubePlayer.cueVideo(l.get(0).getSnippet().getResourceId().getVideoId());
                    description.setText(l.get(0).getSnippet().getDescription());
                    title.setText(l.get(0).getSnippet().getTitle());
                }
                if (youtubeKeyCall != null) {
                    youtubeKeyCall.getKey(l.get(0).getSnippet());
                }
            }
            for (Item x : l) {
                titleList.add(x.getSnippet().getTitle());
                urlsList.add(x.getSnippet().getThumbnails().getMedium().getUrl());
                snippets.add(x.getSnippet());
            }
        }

        if (p != null) {
            if (p.getNextPageToken() != null) {
                nextPageToken = p.getNextPageToken();
            } else {
                loadMore = false;
            }
        }

        descriptionAdapter = new DescriptionAdapter(snippets,
                new DescriptionAdapter.OnClickHandler() {
                    @Override
                    public void onClick(Snippet s) {
                        if (youTubePlayer != null) {
                            youTubePlayer.cueVideo(s.getResourceId().getVideoId());
                            description.setText(s.getDescription());
                            title.setText(s.getTitle());
                        }
                    }
                });
        recyclerView.setAdapter(descriptionAdapter);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayer.setFullscreen(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    public void notifyError(String error) {
        Log.v("TVideo", "notify Error");

    }
}
