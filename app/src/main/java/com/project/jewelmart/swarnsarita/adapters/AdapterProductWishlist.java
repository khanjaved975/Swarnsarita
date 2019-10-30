package com.project.jewelmart.swarnsarita.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.pojo.Cart;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.ViewAnimation;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterProductWishlist extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cart.Datum> items = new ArrayList<>();
    APIInterface apiInterface;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AdapterProductWishlist(Context context, List<Cart.Datum> items) {
        this.items = items;
        ctx = context;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, gross, net, quantity, code, remark, price;
        public ImageButton more;
        public View cart_expand_items;
        public LinearLayout Lin_item_detail;
        public ImageButton bt_toggle_items;
        public View lyt_parent;
        public CardView card_view;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.cart_image);
            title = (TextView) v.findViewById(R.id.cart_prod_name);
            code = (TextView) v.findViewById(R.id.cart_prod_code);
            gross = (TextView) v.findViewById(R.id.cart_gross_wt);
            net = (TextView) v.findViewById(R.id.cart_net_wt);
            quantity = (TextView) v.findViewById(R.id.cart_product_quantity);
            remark = (TextView) v.findViewById(R.id.cart_remark);
            price = (TextView) v.findViewById(R.id.cart_total_price);
            Lin_item_detail = (LinearLayout) v.findViewById(R.id.item_detail);
            more = (ImageButton) v.findViewById(R.id.more);
            bt_toggle_items = (ImageButton) v.findViewById(R.id.bt_toggle_items);
            cart_expand_items = (View) v.findViewById(R.id.cart_expand_items);
            card_view=(CardView)v.findViewById(R.id.card_view);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_cart, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view2 = (OriginalViewHolder) holder;
            final Cart.Datum p = items.get(position);
            view2.title.setText(p.getCollectionName());
            view2.code.setText(p.getCollectionSkuCode());
            view2.gross.setText(p.getGrossWt().toString());
//            view2.net.setText(p.getNetWt().toString());
            view2.quantity.setText(p.getQuantity());
            view2.price.setText(p.getTotalPrice());
            view2.remark.setText(p.getRemarks());
            displayImageOriginal(ctx, view2.image, p.getImages());
            view2.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view2.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, p,position);
                }
            });

            view2.Lin_item_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getRotation() == 0) {
                        view2.bt_toggle_items.animate().setDuration(200).rotation(180);
                    } else {
                        view2.bt_toggle_items.animate().setDuration(200).rotation(0);
                    }
                    if (view2.cart_expand_items.getVisibility()==View.VISIBLE){
                        view2.cart_expand_items.setVisibility(View.GONE);
                    }else{
                        view2.cart_expand_items.setVisibility(View.VISIBLE);
                    }
                }
            });


            view2.bt_toggle_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view2.cart_expand_items.getVisibility()==View.VISIBLE){
                        view2.cart_expand_items.setVisibility(View.GONE);
                    }else{
                        view2.cart_expand_items.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    public static void displayImageOriginal(Context ctx, ImageView img, String path) {
        try {
            if (!Tools.isTablet(ctx)) {
                Glide.with(ctx).load(Constant.Url + "public/backend/product_images/zoom_image/" + path)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.logo)
                        .into(img);
            } else {
                Glide.with(ctx).load(Constant.Url + "public/backend/product_images/thumb_image/" + path)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.logo)
                        .into(img);
            }
        } catch (Exception e) {
        }
    }

    private void onMoreButtonClick(final View view, final Cart.Datum p,final int position) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item,position);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_wishlist_more);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Cart.Datum obj, int pos);
    }

    private void toggleSection(View bt, final View lyt,final OriginalViewHolder holder) {
        boolean show = toggleArrow(bt);
        if (!show) {
            ViewAnimation.collapse(lyt);
        } else {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    holder.card_view.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.card_view.scrollTo(500, lyt.getBottom());
                        }
                    });
                }
            });
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, Cart.Datum obj, MenuItem item, int position);
    }

    public void removeAt(int position) {
        Log.wtf("!!!!position", position + "");
        if (items != null && items.size() > 0) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public void Delete(String user_id, final String table,final String id,final int position) {

        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ctx);
        Call<Acknowledge> countriesCall = apiInterface.deleteFormCart(user_id,table,id);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                pdg.dismiss();
                Log.d("Delete Cart", response.code() + "");
                final Acknowledge resource = response.body();
                if (resource.getAck().equals("1")){
                    Toast.makeText(ctx,resource.getMsg(),Toast.LENGTH_SHORT).show();
                    removeAt(position);
                }else{
                    Toast.makeText(ctx,resource.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                Log.d("Delete Cart", t.toString() + "");
                call.cancel();
            }
        });

    }


    public void Move(String user_id, final String from_table,final String to_table
            ,final String id,final int position) {

        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ctx);
        Call<Acknowledge> countriesCall = apiInterface.moveProduct(user_id,from_table,to_table,id);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                pdg.dismiss();
                Log.d("Move Cart", response.code() + "");
                final Acknowledge resource = response.body();
                if (resource.getAck().equals("1")){
                    Toast.makeText(ctx,resource.getMsg(),Toast.LENGTH_SHORT).show();
                    removeAt(position);
                }else{
                    Toast.makeText(ctx,resource.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                Log.d(" Move", t.toString() + "");
                call.cancel();
            }
        });

    }

}