package com.project.jewelmart.swarnsarita.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.pojo.Home;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class RichProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Home.FinalCollection.ProductAssign> items = new ArrayList<>();
    private Activity activity;
    private ImageView viewPager;
    private FontBoldTextView titleTextView;
    private int hometype,linearLayoutPosition;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    private OnImageClickListener onImageClickListener;

    public interface OnImageClickListener {
        void onItemClick(View view, Home.FinalCollection.ProductAssign obj, String type, int pos,int linearLayoutPosition);
    }

    public void setImageClickListener(final OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public RichProductAdapter(Activity activity, List<Home.FinalCollection.ProductAssign> items, int hometype,int linearLayoutPosition) {
        this.items = items;
        this.activity = activity;
        this.hometype = hometype;
        this.linearLayoutPosition = linearLayoutPosition;
    }

    public void updatelist(int pos, Home.FinalCollection.ProductAssign obj) {
        items.remove(pos);
        items.add(pos, obj);
        notifyItemChanged(pos);
    }

    public int getLinearLayoutPosition(){
        return linearLayoutPosition;
    }

    public void setLinearLayoutPosition(int linearLayoutPosition){
        this.linearLayoutPosition = linearLayoutPosition;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, cart_icon, wishlist_icon;
        CardView cardView;
        public View view;
        public FontTextView prod_code, gw_wt, nt_wt;
        public FontTextView prod_value;
        public ImageButton more;
        public LinearLayout linearValue;
        public View lyt_parent;
        FontBoldTextView qty;
        public RelativeLayout layout_quantity;
        public Button minus, plus;

        public OriginalViewHolder(View v) {
            super(v);
            this.view = v;
            image = (ImageView) v.findViewById(R.id.image);
            cart_icon = (ImageView) v.findViewById(R.id.cart_icon);
            minus = (Button) v.findViewById(R.id.minus);
            plus = (Button) v.findViewById(R.id.plus);
            wishlist_icon = (ImageView) v.findViewById(R.id.wishlist_icon);
            prod_code = (FontTextView) v.findViewById(R.id.prod_code);
            prod_value = (FontTextView) v.findViewById(R.id.prod_value);
            gw_wt = (FontTextView) v.findViewById(R.id.gw_value);
            nt_wt = (FontTextView) v.findViewById(R.id.nt_value);
            qty = (FontBoldTextView) v.findViewById(R.id.qty);
            more = (ImageButton) v.findViewById(R.id.more);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            cardView = (CardView) v.findViewById(R.id.card_view);
            linearValue = (LinearLayout) v.findViewById(R.id.layout_cart);
            layout_quantity = (RelativeLayout) v.findViewById(R.id.layout_quantity);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            OriginalViewHolder view = (OriginalViewHolder) holder;
            CardView.LayoutParams layoutParams = null;
            if (hometype == 0) {
                if (Tools.isLandscape(activity)) {
                    layoutParams = new CardView.LayoutParams(
                            (width / 4) + 100, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else {
                    layoutParams = new CardView.LayoutParams(
                            (width / 3) + 100, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            } /*else if (hometype == 1) {
                if (Tools.isLandscape(activity)) {
                    layoutParams = badgenew CardView.LayoutParams(
                            (width / 3) - 5, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.image.getLayoutParams().height = width / 3;

                }else{
                    layoutParams = badgenew CardView.LayoutParams(
                            (width / 2) - 5, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.image.getLayoutParams().height = width / 2;
                }
            } else if (hometype == 2) {
                if (Tools.isLandscape(activity)) {
                    layoutParams = badgenew CardView.LayoutParams(
                            (width / 2) - 5, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.image.getLayoutParams().height = width/2;

                }else {
                    layoutParams = badgenew CardView.LayoutParams(
                            width - 5, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.image.getLayoutParams().height = width;
                }
            }*/


            if (items.get(position).getIncart().equals("1")) {
                Glide.with(activity).load(R.drawable.ic_shopping_cart_black_48dp)
                        .into(view.cart_icon);
                view.cart_icon.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                view.layout_quantity.setVisibility(View.VISIBLE);
                view.linearValue.setVisibility(View.GONE);
                view.qty.setText(items.get(position).getQuantity());
            } else {
                Glide.with(activity).load(R.drawable.cart)
                        .into(view.cart_icon);
                view.cart_icon.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                view.layout_quantity.setVisibility(View.GONE);
                view.linearValue.setVisibility(View.VISIBLE);
            }
            if (items.get(position).getImages().equals("1")) {
                Glide.with(activity).load(R.drawable.fill_wishlist)
                        .into(view.wishlist_icon);
                view.wishlist_icon.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
            }
            layoutParams.setMargins(2, 2, 2, 2);
            view.cardView.setLayoutParams(layoutParams);
            // final ShopProduct p = items.get(position);
            /*if (items.get(position).getIn_cart().equals("1")){
                view.cart_icon.setVisibility(View.VISIBLE);
            }
            if (items.get(position).getIn_wishlist().equals("1")){
                view.wishlist_icon.setVisibility(View.VISIBLE);
            }*/
            view.prod_code.setText("Code : ");
            view.prod_value.setText(items.get(position).getCollectionSkuCode());
            view.gw_wt.setText(items.get(position).getNetWt());
            view.nt_wt.setText(items.get(position).getNetWt());
            if (items.get(position).getImages() != null) {
                if (!items.get(position).getImages().isEmpty()) {
                    displayImageOriginal(activity, view.image, items.get(position).getImages().get(0).getImageName());
                }
            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position,getLinearLayoutPosition());
                    }
                }
            });

            view.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position,getLinearLayoutPosition());
                    }
                }
            });

            view.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, items.get(position), position);
                }
            });


            view.cart_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "cart", position,getLinearLayoutPosition());
                }
            });

            view.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "cartplus", position,getLinearLayoutPosition());
                }
            });

            view.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "cartminus", position,getLinearLayoutPosition());
                }
            });

            PeekAndPop peekAndPop = new PeekAndPop.Builder(activity)
                    .peekLayout(R.layout.peek_view)
                    .longClickViews(view.image)
                    .build();

            View peekView = peekAndPop.getPeekView();

            this.viewPager = (ImageView) peekView.findViewById(R.id.pager);
            this.titleTextView = (FontBoldTextView) peekView.findViewById(R.id.title);

            if (items.get(position).getImages()!=null){
                if (!items.get(position).getImages().isEmpty()){
                    Glide.with(activity).load(Constant.imagepath + "zoom_image/" +
                            items.get(position).getImages().get(0).getImageName())
                            .placeholder(R.drawable.logo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(viewPager);
                }
            }

            titleTextView.setText(items.get(position).getCollectionSkuCode());
        }
    }

    public static void displayImageOriginal(Context ctx, ImageView img, String path) {
        try {
            if (!Tools.isTablet(ctx)) {
                Glide.with(ctx).load(Constant.imagepath + "thumb_image/" + path)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.logo)
                        .into(img);
            } else {
                Glide.with(ctx).load(Constant.imagepath + "zoom_image/" + path)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.logo)
                        .into(img);
            }
        } catch (Exception e) {
        }
    }

    private void onMoreButtonClick(final View view, final Home.FinalCollection.ProductAssign p, final int pos) {
        PopupMenu popupMenu = new PopupMenu(activity, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item, pos);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_product_more);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Home.FinalCollection.ProductAssign obj, int pos,int linearLayoutPosition);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, Home.FinalCollection.ProductAssign obj, MenuItem item, int pos);
    }

}