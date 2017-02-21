package com.example.ali.coursesplaylist;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ali.coursesplaylist.adapter.DescriptionAdapter;
import com.example.ali.coursesplaylist.adapter.ExploreCoursesAdapter;
import com.example.ali.coursesplaylist.data.Course;
import com.example.ali.coursesplaylist.data.DataContract;
import com.example.ali.coursesplaylist.data.JsonData.Item;
import com.example.ali.coursesplaylist.data.JsonData.Playlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements StringResponseListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<String> urlsList;
    private List<Course> courseList;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    public ExploreFragment() {
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("course");
        mDatabase.keepSynced(true);
        courseList = new ArrayList<>();
        //Test
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataContract.CourseEntry.PLAYLIST_KEY_COLUMN,"firstV");
//        Uri uri = getActivity().getContentResolver().insert(DataContract.CourseEntry.CONTENT_URI,contentValue);
//        if(uri != null){
//            Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_LONG).show();
//        }{
//            Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
//        }
        Cursor c = getActivity().getContentResolver().query(DataContract.CourseEntry.CONTENT_URI,null,null,null,null);
        c.moveToFirst();
        while(c.moveToNext()){
            Log.v("Test","Key Db: "+c.getString(c.getColumnIndex(DataContract.CourseEntry.PLAYLIST_KEY_COLUMN)));
        }
        //end test
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

//        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(this);
//        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLXqaWKDQpdPn4UJ2fOFxl6Yl_DC51FFUL&key=AIzaSyBxXCY_aLBJpE1Xlazei3PMVycq2j-cCrU";
//        try {
//            downloadAsyncTask.execute(new URL(url));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("Test","DataSnapshot to string:"+dataSnapshot.toString());
                Log.v("Test","Data child count" + dataSnapshot.getChildrenCount());
                for (DataSnapshot x:dataSnapshot.getChildren()){
                    Log.v("Test","Child : " +x.toString() );
                    Course c = x.getValue(Course.class);
                    courseList.add(c);
                    Log.v("Test","Course : "+c.name);
                }
                recyclerView.setAdapter(new ExploreCoursesAdapter(getActivity().getApplicationContext()
                ,urlsList,courseList
                ,new ExploreCoursesAdapter.OnClickHandler() {
            @Override
            public void onClick(Course course) {
                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                intent.putExtra("description",course.getDescription());
                intent.putExtra("channelTitle",course.getChannelTitle());
                intent.putExtra("key",course.getKey());
                startActivity(intent);
            }
        }));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(valueEventListener);
    }

    @Override
    public void notifySuccess(String response) {
//            Log.v("Log","Res :"+response);
        Gson gson = new Gson();
        Playlist p = gson.fromJson(response, Playlist.class);
        if (p != null){
            List<Item> l = p.getItems();
            urlsList = new ArrayList<>();
            for (Item x: l){
                urlsList.add(x.getSnippet().getThumbnails().getMedium().getUrl());
//                Log.v("Test","Title :"+x.getSnippet().getThumbnails().getDefault().getUrl());
            }
        }

//        recyclerView.setAdapter(new ExploreCoursesAdapter(getActivity().getApplicationContext()
//                ,urlsList
//                ,new ExploreCoursesAdapter.OnClickHandler() {
//            @Override
//            public void onClick(int i) {
//                startActivity(new Intent(getActivity(),VideoActivity.class));
//            }
//        }));
    }

    @Override
    public void notifyError(String error) {

    }
}
