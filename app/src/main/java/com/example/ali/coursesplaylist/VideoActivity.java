package com.example.ali.coursesplaylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
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
    YouTubePlayerView youTubePlayer;
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
        setContentView(R.layout.activity_video);
        title = ((TextView)findViewById(R.id.title));
        description = ((TextView)findViewById(R.id.description));
        Intent intent = getIntent();
        if(intent != null){
            key = intent.getStringExtra("key");
            if (key != null){
                Log.v("Test","Key : : "+key);
            }else {
                Log.v("Test","KEY not found");
            }
        }
        youTubePlayer = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
        youTubePlayer.initialize(BuildConfig.YOUTUPE_API_KEY,this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

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
        if(!b){
//            youTubePlayer.cueVideo("fhWaJi1Hsfo");
            youtubeKeyCall = new YoutubeKeyCall() {
                @Override
                public void getKey(Snippet key) {
                    Log.v("Test","Key Video Ac. :"+key.getResourceId().getVideoId());
                    youTubePlayer.cueVideo(key.getResourceId().getVideoId());
                    title.setText(key.getTitle());
                    description.setText(key.getDescription());

                }
            };
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void notifySuccess(String response) {
        Gson gson = new Gson();
        Playlist p = gson.fromJson(response, Playlist.class);
        List<Item> l = p.getItems();
        urlsList = new ArrayList<>();
        titleList = new ArrayList<>();
        snippets = new ArrayList<>();
        if (l != null){

            youtubeKeyCall.getKey(l.get(0).getSnippet());
            for (Item x: l){
                titleList.add(x.getSnippet().getTitle());
                urlsList.add(x.getSnippet().getThumbnails().getMedium().getUrl());
                snippets.add(x.getSnippet());
                Log.v("Test","Title :"+x.getSnippet().getThumbnails().getDefault().getUrl());
            }
        }
        descriptionAdapter = new DescriptionAdapter(snippets, new DescriptionAdapter.OnClickHandler() {
            @Override
            public void onClick(Snippet s) {
                youtubeKeyCall.getKey(s);
            }
        });
        recyclerView.setAdapter(descriptionAdapter);


    }

    @Override
    public void notifyError(String error) {

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
