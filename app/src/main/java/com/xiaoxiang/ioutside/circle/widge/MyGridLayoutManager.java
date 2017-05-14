package com.xiaoxiang.ioutside.circle.widge;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Wakesy on 2016/9/25.
 * GridVLayoutManager自适应高度
 */
public class MyGridLayoutManager extends GridLayoutManager {
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    private int childCount=0;

    @Override
    public int getChildCount() {
        return childCount;
    }

    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

//    重写
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        int height = 0;
//        int childCount = getItemCount();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recycler.getViewForPosition(i);
            measureChild(child, widthSpec, heightSpec);
            if (i % getSpanCount() == 0) {
                int measuredHeight = child.getMeasuredHeight() + getDecoratedBottom(child);
                height += measuredHeight;
            }
        }
        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
    }

}
