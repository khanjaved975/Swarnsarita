package com.project.jewelmart.swarnsarita.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.pojo.Productgridpojo;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class AdapterGridScrollProgress extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    private int item_per_display = 0;
    private ImageView viewPager;
    private FontBoldTextView titleTextView;
    private List<Productgridpojo.Result> items = new ArrayList<>();
    private boolean loading;
    public int viewType;
    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;
    private OnLoadMoreListener onLoadMoreListener = null;
    private Activity ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;
    private OnImageClickListener onImageClickListener;
    private int lastPosition = -1;
    private boolean on_attach = true;
    ImageView wishlist_icon, cart_icon;


    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, ItemAnimation.BOTTOM_UP);
            lastPosition = position;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Productgridpojo.Result obj, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, Productgridpojo.Result obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mOnItemLongClickListener = mItemLongClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }


    public void setImageClickListener(final OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public AdapterGridScrollProgress(Activity context, int item_per_display, List<Productgridpojo.Result> items
            , int viewType) {
        this.items = items;
        this.item_per_display = item_per_display;
        selected_items = new SparseBooleanArray();
        this.viewType = viewType;
        ctx = context;
    }

    public void updatelist(int pos, Productgridpojo.Result obj) {
        items.add(pos, obj);
        items.remove(pos);
        notifyItemChanged(pos);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, selected_layer, cart_icon, wishlist_icon;
        public View lyt_parent;
        public View view;
        public TextView prod_code, gw_wt, nt_wt;
        FontBoldTextView qty, product_status;
        public TextView prod_value;
        public ImageButton more;
        public LinearLayout linearValue;
        public CardView cardView;
        public RelativeLayout layout_quantity;
        public Button minus, plus;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            cart_icon = (ImageView) v.findViewById(R.id.cart_icon);
            wishlist_icon = (ImageView) v.findViewById(R.id.wishlist_icon);
            cardView = (CardView) v.findViewById(R.id.card_view);
            selected_layer = (ImageView) v.findViewById(R.id.selected_layer);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            prod_code = (TextView) v.findViewById(R.id.prod_code);
            prod_value = (TextView) v.findViewById(R.id.prod_value);
            gw_wt = (TextView) v.findViewById(R.id.gw_value);
            nt_wt = (TextView) v.findViewById(R.id.nt_value);
            qty = (FontBoldTextView) v.findViewById(R.id.qty);
            product_status = (FontBoldTextView) v.findViewById(R.id.product_status);
            more = (ImageButton) v.findViewById(R.id.more);
            minus = (Button) v.findViewById(R.id.minus);
            plus = (Button) v.findViewById(R.id.plus);
            // layout_quantity = (RelativeLayout) v.findViewById(R.id.layout_quantity);
            linearValue = (LinearLayout) v.findViewById(R.id.layout_cart);
        }


    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public AVLoadingIndicatorView progress_bar;

        public ProgressViewHolder(View v) {
            super(v);
            progress_bar = (AVLoadingIndicatorView) v.findViewById(R.id.progress_bar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_card, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
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
        lastItemViewDetector(recyclerView);

        super.onAttachedToRecyclerView(recyclerView);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Productgridpojo.Result s = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            final OriginalViewHolder view = (OriginalViewHolder) holder;
//           /* if (viewType == 3) {
//                CardView.LayoutParams layoutParams = badgenew CardView.LayoutParams(
//                        (width / 1) - 95, ViewGroup.LayoutParams.WRAP_CONTENT);// / 2 - 5
//                if (!Tools.isLandscape(ctx)) {
//                    layoutParams.setMargins(6, 0, 6, 6);
//                }
//                view.cardView.setLayoutParams(layoutParams);
//                view.image.getLayoutParams().height = (width / 2) + 10;
//            } else if (viewType == 2) {
//                if (Tools.isLandscape(ctx)) {
//                    CardView.LayoutParams layoutParams = badgenew CardView.LayoutParams(
//                            (width / 3) - 5, ViewGroup.LayoutParams.WRAP_CONTENT);// / 2 - 5
//                    layoutParams.setMargins(2, 2, 2, 2);
//                    view.cardView.setLayoutParams(layoutParams);
//                    view.image.getLayoutParams().height = width / 3;
//                } else {
//                    CardView.LayoutParams layoutParams = badgenew CardView.LayoutParams(
//                            (width / 1) - 95, ViewGroup.LayoutParams.WRAP_CONTENT);// / 2 - 5
//                    layoutParams.setMargins(6, 0, 6, 8);
//                    view.cardView.setLayoutParams(layoutParams);
//                    view.image.getLayoutParams().height = width;
//                }
//            } else if (viewType == 1) {
//                if (Tools.isLandscape(ctx)) {
//                    CardView.LayoutParams layoutParams = badgenew CardView.LayoutParams(
//                            (width / 3), ViewGroup.LayoutParams.WRAP_CONTENT);// / 2 - 5
//                    layoutParams.setMargins(2, 4, 0, 2);
//                    view.cardView.setLayoutParams(layoutParams);
//                    view.image.getLayoutParams().height = (height / 2) + 105;
//                } else {
//                    view.image.getLayoutParams().height = width;
//                }
//            } else */
            if (viewType == 0) {
                if (Tools.isLandscape(ctx)) {
                    CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                            (width / 4), ViewGroup.LayoutParams.WRAP_CONTENT);// / 2 - 5
                    layoutParams.setMargins(3, 3, 3, 3);
                    view.cardView.setLayoutParams(layoutParams);
                    view.image.getLayoutParams().height = (width / 6) - 10;
                }
            }
            view.prod_code.setText("Design Code : ");
            view.prod_value.setText(items.get(position).getDesign_number());
            view.gw_wt.setText(items.get(position).getGrossWt());
            view.nt_wt.setText(items.get(position).getGrossWt());
            view.gw_wt.setVisibility(View.GONE);

            if (items.get(position).getProduct_status().equalsIgnoreCase("1")) {
                view.product_status.setText("In Stock");
            } else {
                view.product_status.setText("On Order");
            }
            if (items.get(position).getIn_cart().equals("1")) {
                Glide.with(ctx).load(R.drawable.ic_shopping_cart_black_48dp)
                        .into(view.cart_icon);
                view.cart_icon.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                // view.layout_quantity.setVisibility(View.VISIBLE);
                // view.linearValue.setVisibility(View.GONE);
                // view.qty.setText(items.get(position).getQuantity());
            } else {
                Glide.with(ctx).load(R.drawable.cart)
                        .into(view.cart_icon);
                view.cart_icon.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                //  view.layout_quantity.setVisibility(View.GONE);
                view.linearValue.setVisibility(View.VISIBLE);
            }
            if (items.get(position).getIn_wishlist().equals("1")) {
                Glide.with(ctx).load(R.drawable.fill_wishlist)
                        .into(view.wishlist_icon);
                view.wishlist_icon.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
            }
            displayImageOriginal(ctx, view.image, items.get(position).getImageName(), viewType);
            PeekAndPop peekAndPop = new PeekAndPop.Builder(ctx)
                    .peekLayout(R.layout.peek_view)
                    .longClickViews(view.image)
                    .build();
            View peekView = peekAndPop.getPeekView();
            this.viewPager = (ImageView) peekView.findViewById(R.id.pager);
            this.titleTextView = (FontBoldTextView) peekView.findViewById(R.id.title);
            Glide.with(ctx).load(Constant.imagepath + "zoom_image/" +
                    items.get(position).getImageName()).placeholder(R.drawable.logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewPager);
            titleTextView.setText(items.get(position).getCollectionSkuCode());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(view, s, position);
                }
            });
            view.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(view, s, position);
                }
            });
            view.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener == null) return false;
                    mOnItemLongClickListener.onItemLongClick(v, s, position);
                    return true;
                }
            });
            view.lyt_parent.setActivated(selected_items.get(position, false));
            toggleCheckedIcon(view, position);
            view.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, items.get(position), position);
                }
            });

            view.cart_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "cart", position);
                }
            });


            view.wishlist_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "wishlist", position);
                }
            });

            view.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "cartplus", position);
                }
            });

            view.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "cartminus", position);
                }
            });

            view.wishlist_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener == null) return;
                    onImageClickListener.onItemClick(view, items.get(position), "wishlist", position);
                }
            });

            // setAnimation(view.itemView, position);
        } else {
            //((ProgressViewHolder) holder).progress_bar.setIndeterminate(true);
            ((ProgressViewHolder) holder).progress_bar.show();
        }

       /* if (s.progress) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.set;
        } else {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(false);
        }*/
    }


    private void onMoreButtonClick(final View view, final Productgridpojo.Result p, final int pos) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
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

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).progress ? VIEW_PROGRESS : VIEW_ITEM;
    }

    private void toggleCheckedIcon(OriginalViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.selected_layer.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.selected_layer.setVisibility(View.GONE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
            SingletonSupport.getInstance().multiSelectedQty.remove("1");
            SingletonSupport.getInstance().multiSelectedPos.remove(pos);
            SingletonSupport.getInstance().multiSelectedIds.remove(items.get(pos).getProduct_inventory_id());
        } else {
            selected_items.put(pos, true);
            SingletonSupport.getInstance().multiSelectedQty.add("1");
            SingletonSupport.getInstance().multiSelectedPos.add(pos);
            SingletonSupport.getInstance().multiSelectedIds.add(items.get(pos).getProduct_inventory_id());
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        if (selected_items != null) {
            selected_items.clear();
            SingletonSupport.getInstance().multiSelectedIds.clear();
            SingletonSupport.getInstance().multiSelectedQty.clear();
            SingletonSupport.getInstance().multiSelectedPos.clear();
            notifyDataSetChanged();
        }
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        items.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }

    public void insertData(List<Productgridpojo.Result> items) {
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (items.get(i).progress) {
                items.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            this.items.add(new Productgridpojo.Result(true));
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void resetListData() {
        this.items = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void lastItemViewDetector(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPos = getLastVisibleItem(layoutManager.findLastVisibleItemPosition());
                    if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                        int current_page = getItemCount() / item_per_display;
                        onLoadMoreListener.onLoadMore(current_page);
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, Productgridpojo.Result obj, MenuItem item, int pos);
    }

    public interface OnImageClickListener {
        void onItemClick(OriginalViewHolder view, Productgridpojo.Result obj, String type, int pos);
    }

    private int getLastVisibleItem(int into) {
        int last_idx = into;
       /* for (int i : into) {
            if (last_idx < i) last_idx = i;
        }*/
        return last_idx;
    }

    public static void displayImageOriginal(Context ctx, ImageView img, String path, int viewtype) {
        try {
            if (viewtype == 2 || viewtype == 3 || viewtype == 1) {
                Glide.with(ctx).load(Constant.imagepath + "zoom_image/" + path)
                        .crossFade().placeholder(R.drawable.logo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(img);
            } else {
                if (!Tools.isTablet(ctx)) {
                    Glide.with(ctx).load(Constant.imagepath + "thumb_image/" + path)
                            .crossFade().placeholder(R.drawable.logo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(img);
                } else {
                    Glide.with(ctx).load(Constant.imagepath + "zoom_image/" + path)
                            .crossFade().placeholder(R.drawable.logo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(img);
                }
            }
        } catch (Exception e) {
        }
    }
}