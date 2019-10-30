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
import com.project.jewelmart.swarnsarita.pojo.AccountDetails;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterAccountDetailsList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AccountDetails.Datum> items = new ArrayList<>();

    private Context ctx;
    //private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

   /* public interface OnItemClickListener {
        void onItemClick(View view, MyCollection obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }*/

    public AdapterAccountDetailsList(Context context, List<AccountDetails.Datum> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_count;
        public FontTextView bankname, ifsc, micr, branch_name, city,account;
        public FontBoldTextView Name;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image_count = (ImageView) v.findViewById(R.id.image_count);
            bankname = (FontTextView) v.findViewById(R.id.bankname);
            ifsc = (FontTextView) v.findViewById(R.id.ifsc);
            micr = (FontTextView) v.findViewById(R.id.micr);
            city = (FontTextView) v.findViewById(R.id.city);
            account = (FontTextView) v.findViewById(R.id.account);
            branch_name = (FontTextView) v.findViewById(R.id.branchname);
            Name = (FontBoldTextView) v.findViewById(R.id.name);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_detail, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            final AccountDetails.Datum p = items.get(position);
            view.Name.setText(p.getName());
            view.bankname.setText("Bank Name : " + p.getBankName());
            view.ifsc.setText("IFSC : " + p.getIfscCode());
            view.micr.setText("MICR : " + p.getMicr());
            view.city.setText("City : " + p.getCity());
            view.account.setText("Account No : " + p.getAccount_number());
            view.branch_name.setText("Branch Name : " + p.getBranchName());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(p.getName().charAt(0)), color);
            view.image_count.setImageDrawable(drawable);

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