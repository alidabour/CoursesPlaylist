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

import com.bumptech.glide.Glide;
import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.data.Course;
import com.example.ali.coursesplaylist.data.DataContract;
import com.example.ali.coursesplaylist.data.JsonData.Snippet;

/**
 * Created by Ali on 2/20/2017.
 */

public class AddedAdapter extends RecyclerView.Adapter<AddedAdapter.AddedViewHolder> {
    private Cursor mCursor;
    private Context context;
    private OnClickHandlerAdd onClickHandler;
    public interface OnClickHandlerAdd{
        void onClick(Course course);
    }
    public AddedAdapter(Context context,OnClickHandlerAdd onClickHandler){
        this.context = context;
        this.onClickHandler = onClickHandler;
    }
    @Override
    public AddedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_course_item,parent,false);
        return new AddedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddedViewHolder holder, int position) {
        mCursor.moveToPosition(position);
//        holder.courseImage.setImageResource(R.mipmap.ic_launcher);
        holder.progressText.setText("90% Completed");
        holder.progressBar.setProgress(90);

        String url = mCursor.getString(mCursor.getColumnIndex(DataContract.CourseEntry.PLAYLIST_IMAGE_URL));
        Glide.with(context).load(url).into(holder.courseImage);
        holder.courseName.setText(mCursor.getString(mCursor.getColumnIndex(DataContract.CourseEntry.PLAYLIST_NAME_COLUMN)));
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

    public class AddedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Cursor cursor = mCursor;
            cursor.moveToPosition(position);
            Course course = Course.fromCursor(cursor);
            onClickHandler.onClick(course);
        }
    }

}
