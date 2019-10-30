package com.project.jewelmart.swarnsarita.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.pojo.Productgridpojo;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    private int item_per_display = 0;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener = null;

    private List<Productgridpojo.Result> items = new ArrayList<>();

    private Activity activity;
    private ImageView viewPager;
    private TextView titleTextView;
   // private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AdapterGrid(Activity activity, List<Productgridpojo.Result> items) {
        this.items = items;
        this.activity = activity;

    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View view;
        public TextView prod_code,gw_wt,nt_wt;
        public TextView prod_value;
        public ImageButton more;
        public LinearLayout linearValue;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            this.view = v;
            image = (ImageView) v.findViewById(R.id.image);
            prod_code = (TextView) v.findViewById(R.id.prod_code);
            prod_value = (TextView) v.findViewById(R.id.prod_value);
            gw_wt = (TextView) v.findViewById(R.id.gw_value);
            nt_wt = (TextView) v.findViewById(R.id.nt_value);
            more = (ImageButton) v.findViewById(R.id.more);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            linearValue=(LinearLayout)v.findViewById(R.id.linear_values);
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

            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.prod_code.setText("Code : ");
            view.prod_value.setText(items.get(position).getCollectionSkuCode());
            view.gw_wt.setText(items.get(position).getNetWt());
            view.nt_wt.setText(items.get(position).getNetWt());
            displayImageOriginal(activity, view.image,items.get(position).getImageName());

//            LinearLayout llLabel = badgenew LinearLayout(ctx);
//            llLabel.setLayoutParams(badgenew LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            llLabel.setOrientation(LinearLayout.HORIZONTAL);
//            llLabel.setWeightSum(5);
//            llLabel.setGravity(Gravity.CENTER);
//
//            TextView label = badgenew TextView(ctx);
//            label.setLayoutParams(badgenew LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2.9));
//            label.setGravity(Gravity.RIGHT);
//            label.setText("Gross Wt");
//            label.setTypeface(null, Typeface.BOLD);
//            label.setTextSize(13);
//
//            llLabel.addView(label);
//
//            TextView text = badgenew TextView(ctx);
//            text.setLayoutParams(badgenew LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2.9));
//            text.setGravity(Gravity.LEFT);
//
//            text.setText(items.get(position).getGrossWt());
//            text.setTypeface(null, Typeface.BOLD);
//            text.setGravity(Gravity.CENTER);
//            text.setTextColor(Color.BLACK);
//
//            text.setMaxLines(1);
//            text.setMaxEms(10);
//            text.setEllipsize(TextUtils.TruncateAt.END);
//            text.setTextSize(13);
//
//            llLabel.addView(text);
//
//            LinearLayout llLabel2 = badgenew LinearLayout(ctx);
//            llLabel2.setLayoutParams(badgenew LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            llLabel2.setOrientation(LinearLayout.HORIZONTAL);
//            llLabel2.setWeightSum(5);
//            llLabel2.setGravity(Gravity.CENTER);
//
//            TextView label2 = badgenew TextView(ctx);
//            label2.setLayoutParams(badgenew LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2.9));
//            label2.setGravity(Gravity.RIGHT);
//            label2.setText("Net Wt");
//            label2.setTypeface(null, Typeface.BOLD);
//            label2.setTextSize(13);
//
//            llLabel2.addView(label2);
//
//            TextView text2 = badgenew TextView(ctx);
//            text2.setLayoutParams(badgenew LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2.9));
//            text2.setGravity(Gravity.LEFT);
//
//            text2.setText(items.get(position).getNetWt());
//            text2.setTypeface(null, Typeface.BOLD);
//            text2.setGravity(Gravity.CENTER);
//            text2.setTextColor(Color.BLACK);
//
//            text2.setMaxLines(1);
//            text2.setMaxEms(10);
//            text2.setEllipsize(TextUtils.TruncateAt.END);
//            text2.setTextSize(13);
//            text.setTag("" + product.designMasterId);
//            llLabel2.addView(text2);
//
//            view.linearValue.addView(llLabel);
//            view.linearValue.addView(llLabel2);
//
//            view.lyt_parent.setOnLongClickListener(badgenew View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    peekAndPop.setOnGeneralActionListener(badgenew PeekAndPop.OnGeneralActionListener() {
//                        @Override
//                        public void onPeek(View longClickView, int position) {
//                            Glide.with(activity).load(R.drawable.image_shop_2)
//                                    .crossFade()
//                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                    .into(viewPager);
//                        }
//
//                        @Override
//                        public void onPop(View longClickView, int position) {
//
//                        }
//                    });
//                    return true;
//                }
//            });

            PeekAndPop peekAndPop = new PeekAndPop.Builder(activity)
                    .peekLayout(R.layout.peek_view)
                    .longClickViews(view.image)
                    .build();

            View peekView = peekAndPop.getPeekView();

            this.viewPager = (ImageView) peekView.findViewById(R.id.pager);
            this.titleTextView =(TextView) peekView.findViewById(R.id.title);

            Glide.with(activity).load("http://techseed.co.in/bitcoin_new/public/backend/product_images/small_image/"+
                    items.get(position).getImageName())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewPager);

            titleTextView.setText(items.get(position).getCollectionSkuCode());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position).getCollectionSkuCode(), position);
                    }
                }
            });

            view.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, items.get(position).getCollectionSkuCode());
                }
            });
        }
    }

    public static void displayImageOriginal(Context ctx, ImageView img, String path) {
        try {
            Glide.with(ctx).load("http://techseed.co.in/bitcoin_new/public/backend/product_images/small_image/"+path)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }

    private void onMoreButtonClick(final View view, final String p) {
        PopupMenu popupMenu = new PopupMenu(activity, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item);
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
        void onItemClick(View view, String obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, String obj, MenuItem item);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

}