package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.project.jewelmart.swarnsarita.pojo.MyCollection;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCollectionList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyCollection> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, MyCollection obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCollectionList(Context context, List<MyCollection> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_count;
        public FontTextView product_type;
        public FontBoldTextView colname;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image_count = (ImageView) v.findViewById(R.id.image_count);
            colname = (FontBoldTextView) v.findViewById(R.id.col_name);
            product_type = (FontTextView) v.findViewById(R.id.product_type);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            MyCollection p = items.get(position);
            view.colname.setText( p.getCollectionName());
            view.product_type.setText( p.getProductInventoryType());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(p.getProductCount(), color);
            view.image_count.setImageDrawable(drawable);

            // Tools.displayImageRound(ctx, view.image, p.get);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
            setAnimation(view.itemView, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int lastPosition = -1;
    private boolean on_attach = true;
    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

}