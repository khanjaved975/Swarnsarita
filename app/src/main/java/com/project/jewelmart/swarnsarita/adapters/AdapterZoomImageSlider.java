package com.project.jewelmart.swarnsarita.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.balysv.materialripple.MaterialRippleLayout;
import com.project.jewelmart.swarnsarita.models.Image;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.R;

import java.util.List;

/**
 * Created by javedkhan on 8/10/18.
 */
public  class AdapterZoomImageSlider extends PagerAdapter {

    private Activity act;
    private List<String> items;
    String path="";

    private AdapterZoomImageSlider.OnItemClickListener onItemClickListener;

    private interface OnItemClickListener {
        void onItemClick(View view, Image obj);
    }

    public void setOnItemClickListener(AdapterZoomImageSlider.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // constructor
    public AdapterZoomImageSlider(Activity activity, List<String> items,String path) {
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
        final String o = items.get(position);
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_zom_image, container, false);

        ImageView image = (ImageView) v.findViewById(R.id.image);
        MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
        Tools.displayImageOriginal(act, image, o,path);

        ((ViewPager) container).addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}

