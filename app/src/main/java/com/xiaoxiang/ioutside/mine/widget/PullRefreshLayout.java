package com.xiaoxiang.ioutside.mine.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by 15119 on 2016/8/4.
 */
public class PullRefreshLayout extends IPullRefreshLayout {

    public PullRefreshLayout(Context context) {
        super(context);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected TaskView createHeaderTask() {
        return new HeaderView(getContext());
    }

    @Override
    protected TaskView createFooterTask() {
        return null;
    }
}
