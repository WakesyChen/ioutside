package com.xiaoxiang.ioutside.mine.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/5/31.
 */
public class IndicatorLayout extends LinearLayout {

    private int indicatorSpace = 3;

    private BannerLayout bannerLayout;
    private Drawable unSelectedDrawable;
    private Drawable selectedDrawable;
    private int selectedIndicatorColor = 0xffff0000;
    private int unSelectedIndicatorColor = 0x88888888;
    private Shape indicatorShape = Shape.oval;

    private int selectedIndicatorHeight = 6;
    private int selectedIndicatorWidth = 6;
    private int unSelectedIndicatorHeight = 6;
    private int unSelectedIndicatorWidth = 6;

    public IndicatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorLayout(Context context) {
        this(context, null);
    }

    public IndicatorLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.IndicatorLayout, defStyle, 0);
        selectedIndicatorColor = array.getColor(R.styleable.IndicatorLayout_selectedIndicatorColor, selectedIndicatorColor);
        unSelectedIndicatorColor = array.getColor(R.styleable.IndicatorLayout_unSelectedIndicatorColor, unSelectedIndicatorColor);

        int shape = array.getInt(R.styleable.IndicatorLayout_indicatorShape, Shape.oval.ordinal());
        for (Shape shape1 : Shape.values()) {
            if (shape1.ordinal() == shape) {
                indicatorShape = shape1;
                break;
            }
        }

        selectedIndicatorHeight = (int) array.getDimension(R.styleable.IndicatorLayout_selectedIndicatorHeight, selectedIndicatorHeight);
        selectedIndicatorWidth = (int) array.getDimension(R.styleable.IndicatorLayout_selectedIndicatorWidth, selectedIndicatorWidth);
        unSelectedIndicatorHeight = (int) array.getDimension(R.styleable.IndicatorLayout_unSelectedIndicatorHeight, unSelectedIndicatorHeight);
        unSelectedIndicatorWidth = (int) array.getDimension(R.styleable.IndicatorLayout_unSelectedIndicatorWidth, unSelectedIndicatorWidth);

        indicatorSpace = (int) array.getDimension(R.styleable.IndicatorLayout_indicatorSpace, indicatorSpace);

        LayerDrawable unSelectedLayerDrawable;
        LayerDrawable selectedLayerDrawable;
        GradientDrawable unSelectedGradientDrawable;
        unSelectedGradientDrawable = new GradientDrawable();

        //绘制选中状态图形
        GradientDrawable selectedGradientDrawable;
        selectedGradientDrawable = new GradientDrawable();
        switch (indicatorShape) {
            case rect:
                unSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                break;
            case oval:
                unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
                selectedGradientDrawable.setShape(GradientDrawable.OVAL);
                break;
        }
        unSelectedGradientDrawable.setColor(unSelectedIndicatorColor);
        unSelectedGradientDrawable.setSize(unSelectedIndicatorWidth, unSelectedIndicatorHeight);
        Log.d(getClass().getSimpleName(), "unSelectedIndicatorHeight" + "= " + unSelectedIndicatorHeight);
        unSelectedLayerDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        unSelectedDrawable = unSelectedLayerDrawable;

        selectedGradientDrawable.setColor(selectedIndicatorColor);
        selectedGradientDrawable.setSize(selectedIndicatorWidth, selectedIndicatorHeight);
        selectedLayerDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        selectedDrawable = selectedLayerDrawable;

        array.recycle();
    }

    public void switchIndicator(int currentPosition) {

        for (int i = 0; i < getChildCount(); i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(i == currentPosition ? selectedDrawable : unSelectedDrawable);
        }
    }

    public void setupWithBanner(BannerLayout bannerLayout) {
        this.bannerLayout = bannerLayout;
        bannerLayout.setIndicatorContainer(this);
        int itemCount = bannerLayout.getItemCount();
        addIndicators(itemCount);
    }

    private void addIndicators( int indicatorCount) {

        removeAllViews();

        setGravity(Gravity.CENTER);

        for (int i = 0; i < indicatorCount; i++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(indicatorSpace, indicatorSpace, indicatorSpace, indicatorSpace);
            indicator.setImageDrawable(unSelectedDrawable);
            addView(indicator);
        }
    }

    public void onDataUpdated() {
        removeAllViews();
        addIndicators(bannerLayout.getItemCount());
    }

    private enum Shape {
        rect, oval
    }


}
