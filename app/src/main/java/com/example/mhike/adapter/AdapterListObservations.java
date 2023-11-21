package com.example.mhike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.model.Observation;
import com.example.mhike.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class AdapterListObservations extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Observation> items = new ArrayList<>();

    private Context ctx;

    @LayoutRes
    private int layout_id;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, Observation obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListObservations(Context context, List<Observation> items, @LayoutRes int layout_id) {
        this.items = items;
        ctx = context;
        this.layout_id = layout_id;
    }
    public AdapterListObservations(Context context, List<Observation> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView observation_name;
        public TextView observation_additional_comment;
        public TextView observation_date;
        public TextView dbid;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            observation_name = v.findViewById(R.id.observation_name);
            observation_date = v.findViewById(R.id.observation_date);
            observation_additional_comment = v.findViewById(R.id.observation_additional_comment);
            dbid = v.findViewById(R.id.dbid);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observations_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Observation n = items.get(position);
            view.observation_name.setText(n.getTitle());
            view.observation_date.setText(n.getDatetime());
            view.observation_additional_comment.setText(n.getAdditionalcomment());
            view.dbid.setText(n.getId().toString());
            Tools.displayImageOriginal(ctx, view.image, R.drawable.photo_female_1); // fix yan
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}