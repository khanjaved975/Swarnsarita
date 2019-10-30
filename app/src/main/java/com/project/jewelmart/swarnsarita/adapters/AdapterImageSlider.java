package com.project.jewelmart.swarnsarita.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.project.jewelmart.swarnsarita.LargeViewActivity;
import com.project.jewelmart.swarnsarita.models.Image;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by javedkhan on 11/10/18.
 */
public class AdapterImageSlider extends PagerAdapter {

    private Activity act;
    private List<String> items;
    String path;

    private AdapterImageSlider.OnItemClickListener onItemClickListener;

    private interface OnItemClickListener {
        void onItemClick(View view, Image obj);
    }

    public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // constructor
    public AdapterImageSlider(Activity activity, List<String> items,String path) {
        this.act = activity;
        this.items = items;
        this.path=path;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    public String getItem(int pos) {
        return items.get(pos);
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final String data = items.get(position);
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_slider_image, container, false);

        ImageView image = (ImageView) v.findViewById(R.id.image);
        MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
        Tools.displayImageOriginal(act, image, data,path);
        lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // if (onItemClickListener != null) {
                Log.i("TAG", "This page was clicked: " + position);
                // onItemClickListener.onItemClick(v, o);
                //getPosters();
                  /*  String[] str = badgenew String[list.get(0).getImageName().size()];
                    for (int i = 0; list.get(0).getImageName().size() > i; i++) {
                        str[i] = POSTERS_PATH + list.get(0).getImageName().get(i);
                    }
                    badgenew ImageViewer.Builder(v.getContext(), str)
                            .setStartPosition(0).setBackgroundColorRes(R.color.white)
                            .show();*/

                Intent intent=new Intent(v.getContext(),LargeViewActivity.class);
                intent.putExtra("image_list", (Serializable) items);
                intent.putExtra("path", path);
                v.getContext().startActivity(intent);
                // Toast.makeText(v.getContext(),"clicked",Toast.LENGTH_LONG).show();
                //  }
            }
        });

        ((ViewPager) container).addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}

