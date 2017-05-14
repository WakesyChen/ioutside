package com.xiaoxiang.ioutside.mine.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/5/24.
 */
public class TitleLayout extends LinearLayout {

    public TitleLayout(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.custom_title_v2, this);

        ImageView v = (ImageView) findViewById(R.id.iv_back);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).onBackPressed();
            }
        });
    }

    public void setTitleBackgroundColor(int color) {
        findViewById(R.id.ll_title).setBackgroundColor(color);
    }

    public void setTitleText(String titleText) {
        ((TextView)findViewById(R.id.tv_title)).setText(titleText);
    }
}
