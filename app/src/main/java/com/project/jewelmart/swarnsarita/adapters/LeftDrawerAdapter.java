package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project.jewelmart.swarnsarita.models.LeftDrawer;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.List;

public class LeftDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context context;
    List<LeftDrawer> drawerItemList;
    int layoutResID;
    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 0;
    private OnItemClickListener mOnItemClickListener;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }


    public LeftDrawerAdapter(Context context,
                             List<LeftDrawer> listItems,int animation_type) {
        // super(context, listItems,animation_type);
        this.context = context;
        this.drawerItemList = listItems;
        this.animation_type=animation_type;
        // this.layoutResID = layoutResourceID;

    }

    public interface OnItemClickListener {
        void onItemClick(View view, LeftDrawer obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_drawer_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LeftDrawerAdapter.OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.ItemName.setText(drawerItemList.get(position).getName());
          //  view.ItemCount.setText(drawerItemList.get(position).getCount());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, drawerItemList.get(position), position);
                    }
                }
            });
            setAnimation(view.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        FontBoldTextView ItemName;
     //   TextView ItemCount;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            ItemName = (FontBoldTextView) v.findViewById(R.id.drawer_itemName);
            //ItemCount = (TextView) v.findViewById(R.id.drawer_itemCount);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }



    public void refresh(List<LeftDrawer> items) {
        this.drawerItemList = items;
        notifyDataSetChanged();
    }
}