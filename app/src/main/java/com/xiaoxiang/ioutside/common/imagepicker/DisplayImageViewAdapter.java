package com.xiaoxiang.ioutside.common.imagepicker;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhang on 2016/5/31,0031.
 */
public abstract class DisplayImageViewAdapter<T> {
    public abstract void onDisplayImage(Context context, ImageView imageView, T t);

    public void onItemImageClick(Context context, int index, List<T> list) {

    }

    public void onImageCheckL(String path,boolean isChecked) {

    }



}