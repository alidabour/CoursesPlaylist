package com.example.ali.coursesplaylist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ali.coursesplaylist.adapter.AddedAdapter;
import com.example.ali.coursesplaylist.data.Course;
import com.example.ali.coursesplaylist.data.CourseContentProvider;
import com.example.ali.coursesplaylist.data.DataContract;



public class AddedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    RecyclerView recyclerView;
    AddedAdapter addedAdapter;
    private static final int CURSOR_LOADER_ID = 0;
    public AddedFragment() {}
    public static AddedFragment newInstance() {
        AddedFragment fragment = new AddedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Test","OnCreate");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("Test","OnCreateView");
        View view =  inflater.inflate(R.layout.fragment_added, container, false);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        addedAdapter = new AddedAdapter(getContext(),new AddedAdapter.OnClickHandlerAdd() {
            @Override
            public void onClick(Course course) {
                Intent intent = new Intent(getActivity(),VideoActivity.class);
                intent.putExtra("key",course.getKey());
                Log.v("Test","Key AddFragment: "+course.getKey());


                startActivity(intent);
            }
        });
        recyclerView.setAdapter(addedAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("Test","OnPause");
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("Test","OnPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("Test","OnDestroyView");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("Test","OnStart");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("Test","OnAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Test","OnDestory");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("Test","OnDetach");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("Test","OnCreateLoader");
        return new CursorLoader(getContext(), DataContract.CourseEntry.CONTENT_URI
                ,new String[]{DataContract.CourseEntry.PLAYLIST_KEY_COLUMN,DataContract.CourseEntry.PLAYLIST_NAME_COLUMN, DataContract.CourseEntry.PLAYLIST_IMAGE_URL}
        ,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Test","OnLoadFinished");
        addedAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v("Test","OnLoaderReset");
        addedAdapter.swapCursor(null);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
