package com.project.jewelmart.swarnsarita;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;

public class BannerDetialActivity extends AppCompatActivity {

    FontBoldTextView textView;
    ZoomageView img;
    String desc = "", imagepath = "",title="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView title2 = (FontBoldTextView) findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView = (FontBoldTextView) findViewById(R.id.description);
        img = (ZoomageView) findViewById(R.id.image);
        if (getIntent() != null) {
            desc = getIntent().getStringExtra("desc");
            title = getIntent().getStringExtra("title");
            imagepath = getIntent().getStringExtra("img");
        }
        title2.setText(title);
        textView.setText(desc);
        Glide.with(this).load(imagepath).error(getDrawable(R.drawable.logo)).into(img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
