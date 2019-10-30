package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.pojo.OfferModel;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterOffersList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OfferModel.GiftCoupan> items = new ArrayList<>();

    private Context ctx;
    private int animation_type = 0;
    public String path;

    public AdapterOffersList(Context context, List<OfferModel.GiftCoupan> items, String path, int animation_type) {
        this.items = items;
        ctx = context;
        this.path = path;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public FontTextView coupan_name, weight_from, weight_to, description;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            coupan_name = (FontTextView) v.findViewById(R.id.coupan_name);
            weight_from = (FontTextView) v.findViewById(R.id.weight_from);
            weight_to = (FontTextView) v.findViewById(R.id.weight_to);
            description = (FontTextView) v.findViewById(R.id.description);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offers_layout, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            final OfferModel.GiftCoupan p = items.get(position);
            view.coupan_name.setText("Offer Name : " + p.getCoupanName());
            view.weight_from.setText("Weight From : " + p.getWeightFrom());
            view.weight_to.setText("Weight To : " + p.getWeightTo());
            view.description.setText("Description : " + p.getDescription());
            Tools.displayImageOriginal(ctx, view.image, p.getImage(), path);
            if (!p.getImage().isEmpty()) {
                Glide.with(ctx).load(path + p.getImage())
                        .crossFade().placeholder(R.drawable.logo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(view.image);
            }
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