package com.example.ali.coursesplaylist.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.adapter.AddedAdapter;
import com.example.ali.coursesplaylist.data.Course;
import com.example.ali.coursesplaylist.data.DataContract;


public class AddedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView recyclerView;
    AddedAdapter addedAdapter;
    private static final int CURSOR_LOADER_ID = 0;

    public AddedFragment() {
    }

    public static AddedFragment newInstance() {
        AddedFragment fragment = new AddedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_added, container, false);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        addedAdapter = new AddedAdapter(getContext(), new AddedAdapter.OnClickHandlerAdd() {
            @Override
            public void onClick(Course course) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("key", course.getKey());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(addedAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), DataContract.CourseEntry.CONTENT_URI
                ,
                new String[]{DataContract.CourseEntry.PLAYLIST_KEY_COLUMN, DataContract.CourseEntry.PLAYLIST_NAME_COLUMN, DataContract.CourseEntry.PLAYLIST_IMAGE_URL}
                , null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        addedAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        addedAdapter.swapCursor(null);
    }
}
