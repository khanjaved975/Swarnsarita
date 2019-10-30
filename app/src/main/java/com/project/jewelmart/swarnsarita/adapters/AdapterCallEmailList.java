package com.project.jewelmart.swarnsarita.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.project.jewelmart.swarnsarita.pojo.CallEmail;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCallEmailList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CallEmail> items = new ArrayList<>();

    private Context ctx;
    //private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

   /* public interface OnItemClickListener {
        void onItemClick(View view, MyCollection obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }*/

    public AdapterCallEmailList(Context context, List<CallEmail> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_count;
        public FontTextView phone, email;
        public FontBoldTextView location;
        public View lyt_parent;
        Button btncall, btnemail;

        public OriginalViewHolder(View v) {
            super(v);
            image_count = (ImageView) v.findViewById(R.id.image_count);
            phone = (FontTextView) v.findViewById(R.id.phone);
            email = (FontTextView) v.findViewById(R.id.email);
            btncall = (Button) v.findViewById(R.id.btncall);
            btnemail = (Button) v.findViewById(R.id.btnemail);
            location = (FontBoldTextView) v.findViewById(R.id.location);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_email, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            final CallEmail p = items.get(position);
            view.location.setText(p.getLocation());
            view.phone.setText("Contact : "+p.getContactNumber());
            view.email.setText("Email : "+p.getEmailId());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(p.getLocation().charAt(0)), color);
            view.image_count.setImageDrawable(drawable);

            view.btncall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+91"+p.getContactNumber()));
                    ctx.startActivity(callIntent);
                    Toast.makeText(ctx,"call",Toast.LENGTH_LONG).show();
                }
            });

            view.btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] TO = {p.getEmailId()};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    try {
                        ctx.startActivity(Intent.createChooser(emailIntent, "Send mail..." ));
                        // Toast.makeText(this, "Mail has been sent", Toast.LENGTH_LONG).show();
                        Log.d("Choice", "Mail Had been Sent");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Snackbar.make(v, "Mail not send ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                }
            });

            // Tools.displayImageRound(ctx, view.image, p.get);
           /* view.lyt_parent.setOnClickListener(badgenew View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });*/
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