package com.example.ali.coursesplaylist;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

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

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, StringResponseListener
//        ,DescriptionAdapter.OnClickHandler
{
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youTubePlayer;
    RecyclerView recyclerView;
    DescriptionAdapter descriptionAdapter;
    DescriptionAdapter.OnClickHandler onClickHandler;
    YoutubeKeyCall youtubeKeyCall;
    List<String> urlsList;
    List<String> titleList;
    List<Snippet> snippets;
    TextView title;
    TextView description;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TVideo", "OnCreateVideo");
        setContentView(R.layout.activity_video);
        title = ((TextView) findViewById(R.id.title));
        description = ((TextView) findViewById(R.id.description));
        Intent intent = getIntent();
        if (intent != null) {
            key = intent.getStringExtra("key");
            if (key != null) {
                Log.v("TVideo", "Key : : " + key);
            } else {
                Log.v("TVideo", "KEY not found");
            }
        }
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
        youTubePlayerView.initialize(BuildConfig.YOUTUPE_API_KEY, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(this);
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" +
                key + "&key=AIzaSyBxXCY_aLBJpE1Xlazei3PMVycq2j-cCrU";
        try {
            downloadAsyncTask.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        Log.v("TVideo", "onInitializationSuccess");
        this.youTubePlayer=youTubePlayer;
//            youTubePlayerView.cueVideo("fhWaJi1Hsfo");
//        if(!b){
//        youtubeKeyCall = new YoutubeKeyCall() {
//            @Override
//            public void getKey(Snippet key) {
//                Log.v("TVideo", "onInitializationSuccess Inside If");
//                Log.v("TVideo", "Key Video Ac. :" + key.getResourceId().getVideoId());
//                youTubePlayerView.cueVideo(key.getResourceId().getVideoId());
//                youTubePlayerView.setFullscreen(true);
//                title.setText(key.getTitle());
//                description.setText(key.getDescription());
//
//            }
//        };
//    }else {
//            Log.v("TVideo","B = "+b);
//        }

}

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.v("TVideo", "onInitializationFailure");

    }

    @Override
    public void notifySuccess(String response) {
        Log.v("TVideo", "notifySuccess");
        Gson gson = new Gson();
        Playlist p = gson.fromJson(response, Playlist.class);
        List<Item> l = p.getItems();
        urlsList = new ArrayList<>();
        titleList = new ArrayList<>();
        snippets = new ArrayList<>();
        if (l != null) {
            Log.v("TVideo","l not null");
            if (l.get(0).getSnippet() != null) {
                Log.v("TVideo","snippet not null");
                if(youTubePlayer != null){
                    youTubePlayer.cueVideo(l.get(0).getSnippet().getResourceId().getVideoId());
                }
                if (youtubeKeyCall != null) {
                    Log.v("TVideo", "notifySuccess inside if");

                    youtubeKeyCall.getKey(l.get(0).getSnippet());
                }
            }
            for (Item x : l) {
                titleList.add(x.getSnippet().getTitle());
                urlsList.add(x.getSnippet().getThumbnails().getMedium().getUrl());
                snippets.add(x.getSnippet());
//                Log.v("TVideo", "Title :" + x.getSnippet().getThumbnails().getDefault().getUrl());
            }
        }
        descriptionAdapter = new DescriptionAdapter(snippets, new DescriptionAdapter.OnClickHandler() {
            @Override
            public void onClick(Snippet s) {
                if(youTubePlayer != null){
                    youTubePlayer.cueVideo(s.getResourceId().getVideoId());
                }
//                youtubeKeyCall.getKey(s);
            }
        });
        recyclerView.setAdapter(descriptionAdapter);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.v("TVideo","Land");
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.v("TVideo","Port");
        }
    }

    @Override
    public void notifyError(String error) {
        Log.v("TVideo", "notify Error");

    }

//    @Override
//    public void onClick(String s) {
//        new DescriptionAdapter.OnClickHandler() {
//            @Override
//            public void onClick(String s) {
//                Log.v("Test","Out side click");
//            }
//        }
//    }
}
