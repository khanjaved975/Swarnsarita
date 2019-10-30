package com.project.jewelmart.swarnsarita.widgets;

/**
 * Created by javedkhan on 7/19/18.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 */
public class FontBoldItalicTextView extends TextView {


    public FontBoldItalicTextView(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font/CaviarDreams_BoldItalic.ttf");
        this.setTypeface(face);
    }

    public FontBoldItalicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font/CaviarDreams_BoldItalic.ttf");
        this.setTypeface(face);
    }

    public FontBoldItalicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font/CaviarDreams_BoldItalic.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}