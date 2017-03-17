package com.example.ali.coursesplaylist.controller;


import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.network.StringResponseListener;
import com.example.ali.coursesplaylist.adapter.CourseSectionListAdapter;
import com.example.ali.coursesplaylist.adapter.ExploreCoursesAdapter;
import com.example.ali.coursesplaylist.data.Course;
import com.example.ali.coursesplaylist.data.HeaderRVData;
import com.example.ali.coursesplaylist.data.JsonData.Item;
import com.example.ali.coursesplaylist.data.JsonData.Playlist;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExploreFragment extends Fragment implements StringResponseListener {

    private List<String> urlsList;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private List<HeaderRVData> headerRVDatas;
    CourseSectionListAdapter courseSectionListAdapter;

    public ExploreFragment() {
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        headerRVDatas = new ArrayList<>();


        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("course");
            mDatabase.keepSynced(true);
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView.setAdapter(null);
                headerRVDatas = new ArrayList<>();
                HashMap<String, ArrayList<Course>> data = new HashMap<>();
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    String key = x.getKey();
                    for (DataSnapshot child : x.getChildren()) {
                        if (data.get(key) == null) {
                            ArrayList<Course> arrayList = new ArrayList<>();
                            arrayList.add(child.getValue(Course.class));
                            data.put(key, arrayList);
                        } else {
                            ArrayList<Course> courses = data.get(key);
                            courses.add(child.getValue(Course.class));
                            data.put(key, courses);
                        }
                    }
                    Iterator it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        headerRVDatas.add(new HeaderRVData((String) pair.getKey(),
                                (List) pair.getValue()));
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                    courseSectionListAdapter = new CourseSectionListAdapter(getActivity(),
                            headerRVDatas, new ExploreCoursesAdapter.OnClickHandler() {
                        @Override
                        public void onClick(Course course, ImageView imageView) {
                            Intent intent = new Intent(getContext(), DescriptionActivity.class);
                            intent.putExtra("course", course);
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(getActivity(),
                                            imageView,
                                            ViewCompat.getTransitionName(imageView));
                            startActivity(intent, options.toBundle());

                        }
                    });
                    recyclerView.setAdapter(courseSectionListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(valueEventListener);
    }

    @Override
    public void notifySuccess(String response) {
        Gson gson = new Gson();
        Playlist p = gson.fromJson(response, Playlist.class);
        if (p != null) {
            List<Item> l = p.getItems();
            urlsList = new ArrayList<>();
            for (Item x : l) {
                urlsList.add(x.getSnippet().getThumbnails().getMedium().getUrl());
            }
        }
    }

    @Override
    public void notifyError(String error) {

    }
}
