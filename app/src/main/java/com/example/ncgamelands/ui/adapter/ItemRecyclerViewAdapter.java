package com.example.ncgamelands.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ncgamelands.R;
import com.example.ncgamelands.model.CustomInfo;
import com.example.ncgamelands.ui.inteface.ItemCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // An Activity's Context.
    private final Context mContext;
    private ItemCallback mCallback;

    List<Object> itemList = new ArrayList<>();
    List<Object> filteredFeeds = new ArrayList<>();

    public ItemRecyclerViewAdapter(Context context, ItemCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void setItems(List<Object> itemList) {
        this.itemList = itemList;
        filteredFeeds.clear();
        filteredFeeds.addAll(itemList);
        notifyDataSetChanged();
    }

    public void applyFilter(String searchString) {
        filteredFeeds.clear();
        if (searchString.isEmpty()) {
            filteredFeeds.addAll(itemList);
        } else {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i) instanceof CustomInfo) {
                    CustomInfo item = (CustomInfo) itemList.get(i);
                    if (item.getLabel().toLowerCase().contains(searchString.toLowerCase())) {
                        filteredFeeds.add(item);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvitem)
        TextView tvitem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return filteredFeeds.size();
    }

    /**
     * Creates a new view for a menu item view or a banner ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View menuItemLayoutView = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.adapter_item,
                viewGroup, false);
        return new ViewHolder(menuItemLayoutView);
    }


    /**
     * Replaces the content in the views that make up the menu item view and the
     * banner ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        if (filteredFeeds.get(position) instanceof CustomInfo) {

            final CustomInfo item = (CustomInfo) filteredFeeds.get(position);

            viewHolder.tvitem.setText(item.getLabel());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onItem(item);
                }
            });

        }

    }

}