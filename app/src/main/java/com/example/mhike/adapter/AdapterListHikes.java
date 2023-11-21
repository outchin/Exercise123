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
import com.example.mhike.model.Hike;
import com.example.mhike.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class AdapterListHikes extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Hike> items = new ArrayList<>();
    private int[] image_array = {R.drawable.no_image_found, R.drawable.hike_image_1, R.drawable.hike_image_2,R.drawable.hike_image_3,R.drawable.hike_image_4,R.drawable.hike_image_5,R.drawable.hike_image_6,R.drawable.hike_image_7,R.drawable.hike_image_8,R.drawable.hike_image_9,R.drawable.hike_image_10}; // Replace with your image resource IDs from drawable
    private Context ctx;

    @LayoutRes
    private int layout_id;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, Hike obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListHikes(Context context, List<Hike> items, @LayoutRes int layout_id) {
        this.items = items;
        ctx = context;
        this.layout_id = layout_id;
    }
    public AdapterListHikes(Context context, List<Hike> items) {
        this.items = items;
        ctx = context;
    }

    // method for filtering our recyclerview items.
    public void filterList(List<Hike> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        items = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView date;
        public TextView dbid;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            date = v.findViewById(R.id.date);
            dbid = v.findViewById(R.id.dbid);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hikes_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Hike n = items.get(position);
            view.title.setText(n.getName());
            view.subtitle.setText(n.getLocation());
            view.date.setText(n.getDate());

            view.dbid.setText(n.getId()+"");
            Tools.displayImageOriginal(ctx, view.image, image_array[Integer.parseInt(n.getImage_link())]); // fix yan
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