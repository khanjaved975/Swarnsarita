package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.project.jewelmart.swarnsarita.models.OrderHistory;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterOrderHistoryList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderHistory> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnItemReOrderClickListener onItemReOrderClickListener;
    private OnItemDeleteClickListener onItemDeleteClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, OrderHistory obj, int position);
    }

    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(View view, OrderHistory obj, int position);
    }

    public interface OnItemReOrderClickListener {
        void onItemReOrderClick(View view, OrderHistory obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemDeleteClickListener(final OnItemDeleteClickListener mItemClickListener) {
        this.onItemDeleteClickListener = mItemClickListener;
    }

    public void setOnItemReOrderClickListener(final OnItemReOrderClickListener mItemClickListener) {
        this.onItemReOrderClickListener = mItemClickListener;
    }

    public AdapterOrderHistoryList(Context context, List<OrderHistory> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_count;
        public FontTextView grosswt, netwt, quantity, delivery_date,
                offer_title,offer_description,status,type;
        public FontBoldTextView Orderid;
        public View lyt_parent;
        public Button btn_reorder,btn_delete;

        public OriginalViewHolder(View v) {
            super(v);
            image_count = (ImageView) v.findViewById(R.id.image_count);
            Orderid = (FontBoldTextView) v.findViewById(R.id.orderid);
            grosswt = (FontTextView) v.findViewById(R.id.grosswt);
            status = (FontTextView) v.findViewById(R.id.status);
            type = (FontTextView) v.findViewById(R.id.type);
            netwt = (FontTextView) v.findViewById(R.id.netwt);
            offer_title = (FontTextView) v.findViewById(R.id.offer_title);
            offer_description = (FontTextView) v.findViewById(R.id.offer_description);
            quantity = (FontTextView) v.findViewById(R.id.quantity);
            delivery_date = (FontTextView) v.findViewById(R.id.delivery_date);
            btn_reorder = (Button) v.findViewById(R.id.btn_reorder);
            btn_delete = (Button) v.findViewById(R.id.btn_delete);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }
    public void removeAt(int position) {
        Log.wtf("!!!!position", position + "");
        if (items != null && items.size() > 0) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            OrderHistory p = items.get(position);
            view.Orderid.setText("Order id : " + p.getOrderId());
            view.grosswt.setText("Gross wt: " + p.getGrossWt());
            view.netwt.setText("Net wt : " + p.getNetWt());
            view.quantity.setText("Quantity : " + p.getQuantity());
            view.delivery_date.setText("Date : " + p.getDeliveryDate());
            view.status.setText("Status : " + p.getOrder_stage());
            view.type.setText("Type : " + p.getProduct_inventory_type());
            if (!p.getGiftData().getCoupanName().isEmpty()) {
                view.offer_title.setVisibility(View.VISIBLE);
                view.offer_title.setText("Offer Name : " + p.getGiftData().getCoupanName());
            }else{
                view.offer_title.setVisibility(View.GONE);
            }
            if (!p.getGiftData().getDescription().isEmpty()) {
                view.offer_description.setVisibility(View.VISIBLE);
                view.offer_description.setText("Offer Description : " + p.getGiftData().getDescription());
            }else{
                view.offer_description.setVisibility(View.GONE);
            }
//            ColorGenerator generator = ColorGenerator.MATERIAL;
//            int color = generator.getRandomColor();
//            TextDrawable drawable = TextDrawable.builder()
//                    .buildRound(p.getQuantity(), color);
//            view.image_count.setImageDrawable(drawable);

            if (p.getImageName() != null) {
                if (!p.getImageName().isEmpty()) {
                    Tools.displayImageOriginal(ctx, view.image_count, p.getImageName().get(0).getImageName(), "public/backend/product_images/zoom_image/");
                }
            }
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemDeleteClickListener != null) {
                        onItemDeleteClickListener.onItemDeleteClick(view, items.get(position), position);
                    }
                }
            });

            view.btn_reorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemReOrderClickListener != null) {
                        onItemReOrderClickListener.onItemReOrderClick(view, items.get(position), position);
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