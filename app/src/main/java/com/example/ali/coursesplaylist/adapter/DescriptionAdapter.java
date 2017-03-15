package com.example.ali.coursesplaylist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ali.coursesplaylist.R;
import com.example.ali.coursesplaylist.data.JsonData.Snippet;

import java.util.List;

/**
 * Created by Ali on 2/18/2017.
 */

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder> {
    private List<Snippet> snippets;
    private OnClickHandler onClickHandler;

    public interface OnClickHandler {
        void onClick(Snippet s);
    }

    public DescriptionAdapter(List<Snippet> snippets, OnClickHandler onClickHandler) {
        this.snippets = snippets;
        this.onClickHandler = onClickHandler;
    }

    @Override
    public DescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new DescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DescriptionViewHolder holder, int position) {
        holder.videoName.setText(snippets.get(position).getTitle());
        holder.videoNum.setText(String.valueOf(position + 1));

        holder.videoName.setContentDescription(snippets.get(position).getTitle());
        holder.videoNum.setContentDescription(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return snippets.size();
    }

    public class DescriptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoName;
        TextView videoNum;

        public DescriptionViewHolder(View itemView) {
            super(itemView);
            videoName = (TextView) itemView.findViewById(R.id.videoName);
            videoNum = (TextView) itemView.findViewById(R.id.videoNum);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int p = getAdapterPosition();
            onClickHandler.onClick(snippets.get(p));
        }
    }

}
