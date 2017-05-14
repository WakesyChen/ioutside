package com.xiaoxiang.ioutside.mine.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 15119 on 2016/8/14.
 */
public class AttachedView extends View {

    private View mAnchorView;

    private int mAnchorOnScreenX;
    private int mAnchorOnScreenY;

    private Paint paint;

    public AttachedView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#ff0000"));
    }

    public AttachedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttachedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAnchorView(View v) {
        this.mAnchorView = v;

        Rect r = new Rect();
        v.getGlobalVisibleRect(r);
        mAnchorOnScreenX = r.right;
        mAnchorOnScreenY = r.top;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
