package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.ProductGridActivity;
import com.project.jewelmart.swarnsarita.SubCatagoryActivity;
import com.project.jewelmart.swarnsarita.pojo.Collection;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.R;
import com.project.jewelmart.swarnsarita.widgets.SquareImageView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


//
public class CustomCatRecycleAdapter extends RecyclerView.Adapter<CustomCatRecycleAdapter.ViewHolder> {

    Context context;
    private final List<com.project.jewelmart.swarnsarita.pojo.Collection> orderList;

    String todaysDate = "";

    public CustomCatRecycleAdapter(Context context, ArrayList<com.project.jewelmart.swarnsarita.pojo.Collection> catalogueList) {
        this.context = context;
        this.orderList = catalogueList;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        todaysDate = df.format(c);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = null;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout_category, viewGroup, false);
        ViewHolder viewholder = new ViewHolder(itemView, (ArrayList<Collection>) orderList);
        itemView.setOnClickListener(viewholder);

        return viewholder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Collection cat = orderList.get(i);
        holder.title.setText(cat.getColName());
        Tools.displayImageOriginal(context, holder.img, cat.getImage_name(), "public/backend/collection/");
        String s = Tools.getdayscount(cat.getCreated(), todaysDate);
        //Toast.makeText(context,cat.getCreated()+s+todaysDate,Toast.LENGTH_LONG).show();
        if (cat.getLatest() != null) {
            if (cat.getLatest().equalsIgnoreCase("1")) {
               // GlideDrawableImageViewTarget imageViewTarget = new2 GlideDrawableImageViewTarget(holder.gif_new);
               // Glide.with(context).load(R.raw.badgenew).into(imageViewTarget);
                //Glide.with(context).load(R.drawable.new2).into(holder.img);
                Glide.with(context).load(R.drawable.new2).placeholder(R.drawable.new2)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.gif_new);
            }
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ArrayList<com.project.jewelmart.swarnsarita.pojo.Collection> orderList;
        protected TextView title;
        protected ImageView img;
        protected ImageView gif_new;
        protected CardView card;

        public ViewHolder(View v, ArrayList<com.project.jewelmart.swarnsarita.pojo.Collection> orderList) {
            super(v);
            this.orderList = orderList;
            title = (TextView) v.findViewById(R.id.cat_title);
            img = (ImageView) v.findViewById(R.id.cart_image);
            gif_new = (ImageView) v.findViewById(R.id.gif_new);
        }

        @Override
        public void onClick(View view) {

            String catId = "", catProductCount = "", imagetype = "", sortPosition = "";
            int subcat = 0;
            String selected = orderList.get(getAdapterPosition()).getColName();
            ArrayList<Collection.Subcategory> subCatList = (ArrayList<Collection.Subcategory>) orderList.get(getAdapterPosition()).getSubcategory();
            catId = orderList.get(getAdapterPosition()).getId();
            //catProductCount = "" + orderList.get(getAdapterPosition()).count;
            //imagetype = "" + orderList.get(getAdapterPosition()).image_type;
            //sortPosition = "" + orderList.get(getAdapterPosition()).default_sort;
            if (subCatList != null) {
                subcat = subCatList.size();
            }
            if (subcat > 0) {
                Bundle args = new Bundle();
                args.putSerializable("subcat", (Serializable) subCatList);
                args.putString("selectedvalue", selected);
                args.putString("table", "product_master");
                Intent intent = new Intent(context, SubCatagoryActivity.class);
                intent.putExtras(args);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, ProductGridActivity.class);
                intent.putExtra("collection_id", catId);
                intent.putExtra("name", selected);
                intent.putExtra("mode_type", "normal");
                intent.putExtra("table", "product_master");
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
    }
}
