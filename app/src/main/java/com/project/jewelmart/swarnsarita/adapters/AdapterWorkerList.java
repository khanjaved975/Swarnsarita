package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.models.WorkerModel;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterWorkerList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WorkerModel.DataList> items = new ArrayList<>();
    private Context ctx;
    Typeface typeface;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AdapterWorkerList(Context context, List<WorkerModel.DataList> items) {
        this.items = items;
        typeface = Typeface.createFromAsset(context.getAssets(), "font/Caviar_Dreams_Bold.ttf");
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public LinearLayout lyt_parent, linear_values;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.cart_image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
            linear_values = (LinearLayout) v.findViewById(R.id.linear_values);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_worker, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final WorkerModel.DataList data = items.get(position);
            List<WorkerModel.DataList.ImageName> img_list = data.getImageName();
            String img_name = img_list.get(0).getImageName();
            Glide.with(ctx).load(Constant.imagepath + "zoom_image/" + img_name)
                    .placeholder(R.drawable.logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view.image);
            List<String> label = data.getDataKey();
            List<String> value = data.getDataValue();
            ((AdapterWorkerList.ViewHolder) holder).linear_values.removeAllViews();

            for (int i = 0; i < label.size(); i++) {
                RelativeLayout llLabel = new RelativeLayout(ctx);
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.setMargins(16,3,16,3);
                llLabel.setLayoutParams(layoutParams);
                /*llLabel.setOrientation(LinearLayout.HORIZONTAL);
                llLabel.setWeightSum(5);
                llLabel.setGravity(Gravity.CENTER);*/
                TextView title = new TextView(ctx);

                title.setLayoutParams(layoutParams);
                title.setGravity(Gravity.LEFT);
                title.setText(label.get(i)+" :");
                title.setTypeface(typeface, Typeface.BOLD);
                title.setTextSize(13);

                TextView titlevalue = new TextView(ctx);
                RelativeLayout.LayoutParams layoutParams2=new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                titlevalue.setLayoutParams(layoutParams2);
                titlevalue.setGravity(Gravity.RIGHT);
                titlevalue.setText(value.get(i));
                titlevalue.setTypeface(typeface, Typeface.BOLD);
                titlevalue.setTextSize(13);

                llLabel.addView(title);
                llLabel.addView(titlevalue);
                ((ViewHolder) holder).linear_values.addView(llLabel);
            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, data, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public WorkerModel.DataList getItem(int position) {
        return items.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, WorkerModel.DataList obj, int pos);
    }
}