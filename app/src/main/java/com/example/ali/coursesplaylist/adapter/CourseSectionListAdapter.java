package com.example.ali.coursesplaylist.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.data.HeaderRVData;

import java.util.List;

/**
 * Created by Ali on 3/9/2017.
 */

public class CourseSectionListAdapter extends RecyclerView.Adapter<CourseSectionListAdapter.ItemRowHolder> {
    Context context;
    List<HeaderRVData> headerRVDataList;
    ExploreCoursesAdapter.OnClickHandler onClickHandler;

    public CourseSectionListAdapter(Context context, List<HeaderRVData> headerRVDataList,
                                    ExploreCoursesAdapter.OnClickHandler onClickHandler) {
        this.context = context;
        this.headerRVDataList = headerRVDataList;
        this.onClickHandler = onClickHandler;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.course_section_list_item, null);
        return new ItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        List list = headerRVDataList.get(position).getCategoryInformations();
        holder.sectionHeader.setText(headerRVDataList.get(position).getHeader());
        holder.sectionHeader.setContentDescription(headerRVDataList.get(position).getHeader());

        ExploreCoursesAdapter itemListDataAdapter = new ExploreCoursesAdapter(context, list,
                onClickHandler);

        holder.sectionRV.setHasFixedSize(true);
        holder.sectionRV.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.sectionRV.setAdapter(itemListDataAdapter);


    }

    @Override
    public int getItemCount() {
        return headerRVDataList.size();
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView sectionHeader;
        RecyclerView sectionRV;

        public ItemRowHolder(View itemView) {
            super(itemView);
            sectionHeader = (TextView) itemView.findViewById(R.id.header);
            sectionRV = (RecyclerView) itemView.findViewById(R.id.recycleViewItem);
        }
    }
}