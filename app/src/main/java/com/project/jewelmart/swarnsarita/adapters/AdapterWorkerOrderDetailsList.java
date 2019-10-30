package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.project.jewelmart.swarnsarita.models.OrderDetailModel;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterWorkerOrderDetailsList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderDetailModel.ProductDatum> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    List<String> label=new ArrayList<>();
    List<String> value=new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, OrderDetailModel.ProductDatum obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterWorkerOrderDetailsList(Context context, List<OrderDetailModel.ProductDatum> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.label=items.get(0).getDataKey();
        this.value=items.get(0).getDataValue();
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_count;
        public FontTextView label,value;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            label = (FontTextView) v.findViewById(R.id.label);
            value = (FontTextView) v.findViewById(R.id.value);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
           // OrderDetailModel.ProductDatum data = items.get(position);
            view.label.setText( label.get(position));
            view.value.setText( value.get(position));

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                       // mOnItemClickListener.onItemClick(view, items.get(position), position);
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
        return label.size();
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