package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project.jewelmart.swarnsarita.pojo.NotificationModel;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AdapterCartSummary extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<List<String>> items = new ArrayList<>();
    private List<String> quantitylist = new ArrayList<>();
private  JSONArray jsonArray=new JSONArray();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, NotificationModel.Datum obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCartSummary(Context context, List<List<String>> masterDesingList, List<String> quantitylist, JSONArray array) {
        this.items = masterDesingList;
        this.quantitylist = quantitylist;
        this.jsonArray = array;
        ctx = context;
        this.animation_type = 1;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public FontTextView  design, quantity, weight;
        public FontBoldTextView category;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            quantity = (FontTextView) v.findViewById(R.id.quantity);
            category = (FontBoldTextView) v.findViewById(R.id.categoryname);
            design = (FontTextView) v.findViewById(R.id.desing);
            weight = (FontTextView) v.findViewById(R.id.weight);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_cart, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
           // NotificationModel.Datum p = items.get(position);

            List<String> list=items.get(position);
            String des = "";
            for (int i=0;i<list.size();i++){
                des+=list.get(i)+"\n";
            }

            view.quantity.setText(quantitylist.get(position));
            try {
                view.category.setText("category : "+ jsonArray.get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            view.design.setText("Designs : " + des);

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }*/
                }
            });
           // setAnimation(view.itemView, position);
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