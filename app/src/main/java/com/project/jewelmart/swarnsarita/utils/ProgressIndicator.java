package com.project.jewelmart.swarnsarita.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;

import com.project.jewelmart.swarnsarita.R;

/**
 * Created by javedkhan on 1/16/18.
 */

public class ProgressIndicator {


    @NonNull
    public static ProgressDialog ShowLoading(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        final ProgressDialog pdg = new ProgressDialog(context);
        pdg.setMessage("please wait...");
        pdg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdg.show();
        Window window = pdg.getWindow();
        window.setLayout(width / 3, width / 3);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setContentView(R.layout.item_progress_indicator);
/*
        ImageView imgLoader = (ImageView) window.findViewById(R.id.progress_loader);
        imgLoader.getDrawable().setColorFilter(navigationBg, PorterDuff.Mode.SRC_ATOP);*/
//        // load the animation
//        Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
//        imgLoader.startAnimation(animFadein);
        pdg.setCancelable(false);
        return pdg;
    }


}
