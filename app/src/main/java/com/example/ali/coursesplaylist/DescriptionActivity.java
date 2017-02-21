package com.example.ali.coursesplaylist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ali.coursesplaylist.adapter.DescriptionAdapter;
import com.example.ali.coursesplaylist.data.JsonData.Item;
import com.example.ali.coursesplaylist.data.JsonData.Playlist;
import com.example.ali.coursesplaylist.data.JsonData.Snippet;
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
    List<Snippet> snippetList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        addButton = (Button) findViewById(R.id.add);
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
            channelTitle.setText(intent.getStringExtra("channelTitle"));
           String key = intent.getStringExtra("key");
            DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(this);
            String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" +
                    key+"&key=AIzaSyBxXCY_aLBJpE1Xlazei3PMVycq2j-cCrU";
            try {
                downloadAsyncTask.execute(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
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
                Toast.makeText(getApplicationContext(),"Please Add the course first",Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(descriptionAdapter);
    }

    @Override
    public void notifyError(String error) {

    }
}
