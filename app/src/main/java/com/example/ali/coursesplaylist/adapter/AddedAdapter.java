package com.example.ali.coursesplaylist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.data.DataContract;

/**
 * Created by Ali on 2/20/2017.
 */

public class AddedAdapter extends RecyclerView.Adapter<AddedAdapter.AddedViewHolder> {
    private Cursor mCursor;

    @Override
    public AddedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_course_item,parent,false);
        return new AddedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddedViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.courseImage.setImageResource(R.mipmap.ic_launcher);
        holder.progressText.setText("90% Completed");
        holder.progressBar.setProgress(90);
        holder.courseName.setText(mCursor.getString(mCursor.getColumnIndex(DataContract.CourseEntry.PLAYLIST_KEY_COLUMN)));
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class AddedViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        ProgressBar progressBar;
        TextView progressText;
        ImageView courseImage;
        public AddedViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            progressText = (TextView) itemView.findViewById(R.id.progressText);
            courseImage = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }

}
