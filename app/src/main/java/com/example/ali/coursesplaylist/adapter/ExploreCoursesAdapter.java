package com.example.ali.coursesplaylist.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.data.Course;

import java.util.List;

/**
 * Created by Ali on 2/16/2017.
 */

public class ExploreCoursesAdapter extends RecyclerView.Adapter<ExploreCoursesAdapter.ExploreCoursesViewHolder>{
    final private OnClickHandler onClickHandler;
    private List<String> urlsList;
    private List<Course> courses;
    private Context context;
    public interface OnClickHandler {
        void onClick(Course course);
    }
    public ExploreCoursesAdapter(Context context, List<String> urlsList, List<Course> courses, OnClickHandler onClickHandler){
        this.context = context;
        this.urlsList = urlsList;
        this.courses = courses;
        this.onClickHandler = onClickHandler;
    }
    @Override
    public ExploreCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_course_item,parent,false);
        return new ExploreCoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExploreCoursesViewHolder holder, int position) {
//        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        Course course = courses.get(position);
        holder.courseName.setText(course.getName());
        Glide.with(context).load(course.getUrl()).into(holder.imageView);

        holder.courseName.setContentDescription(course.getName());
        holder.imageView.setContentDescription(course.getName());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ExploreCoursesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView courseName;
        public ExploreCoursesViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Course course = courses.get(getAdapterPosition());
            onClickHandler.onClick(course);
        }
    }
}
