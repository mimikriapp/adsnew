package com.mimikridev.ad.sdk.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.mimikridev.ad.sdk.R;

public class MediumNativeAdView extends LinearLayout {

    private Context mContext;
    private AttributeSet attrs;
    private int styleAttr;
    private View view;

    private Drawable startAppDrawable;


    private Button btnNativeStartApp;

    public MediumNativeAdView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public MediumNativeAdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.attrs = attrs;
        initView();
    }

    public MediumNativeAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    public MediumNativeAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("CustomViewStyleable")
    private void initView() {
        this.view = this;
        inflate(mContext, R.layout.view_native_ad_medium, this);
        TypedArray arr = mContext.obtainStyledAttributes(attrs, R.styleable.NativeAdView, styleAttr, 0);

        startAppDrawable = arr.getDrawable(R.styleable.NativeAdView_startappNativeButton);

        btnNativeStartApp = findViewById(R.id.startapp_native_button);



        if (startAppDrawable != null) {
            setStartAppNativeButtonColor(startAppDrawable);
        }

        arr.recycle();

    }



    public void setStartAppNativeButtonColor(Drawable background) {
        btnNativeStartApp.setBackground(background);
    }

}
