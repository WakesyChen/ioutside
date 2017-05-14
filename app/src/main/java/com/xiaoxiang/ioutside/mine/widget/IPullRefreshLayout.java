package com.xiaoxiang.ioutside.mine.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/7/29.
 */
public abstract class IPullRefreshLayout extends LinearLayout {

    private TaskView mFooterViewTask;
    private TaskView mHeaderViewTask;

    private MarginLayoutParams mMLP;

    private Scroller mScroller;

    private View mHeaderView;
    private View mFooterView;

    private int mHeaderViewHeight;

    private boolean loadOnce = false;

    private boolean isFooterAdded = true;

    private View mContentView;

    private int mLastDownY;

    private int mLastY;

    private int mOffsetY;

    private boolean hasFooterView = false;
    private boolean hasHeaderView = false;

    private int status;

    private int STATUS_LOADING = 0;
    private int STATUS_END = 1;
    private int STATUS_START = 2;

    public IPullRefreshLayout(Context context) {
        this(context, null);

    }

    public IPullRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IPullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
      //  setupFooterView(createFooterTask());
    }

    public void setupFooterView(@NonNull TaskView footerView) {
        mFooterViewTask = footerView;
        mFooterView = footerView.getView();
        if (mFooterView != null) {
            this.addView(mFooterView);
            hasFooterView = true;
        }
    }

    public void setupHeaderView(@NonNull TaskView headerView) {
        mHeaderViewTask = headerView;
        mHeaderView = headerView.getView();
        if (mHeaderView != null) {
            this.addView(mHeaderView, 0);
            hasHeaderView = true;
        }
    }



    private void setupContentView(@NonNull View contentView) {
        mContentView = contentView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!loadOnce) {
            setupContentView(getChildAt(0));
            setupHeaderView(createHeaderTask());
            loadOnce = true;
        }

        if (mHeaderViewHeight == 0) {
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
          //  resizeContentView();
            if (hasHeaderView) {
                mMLP = (MarginLayoutParams) mHeaderView.getLayoutParams();
                mMLP.topMargin = -mHeaderViewHeight;
               // scrollTo(0, mHeaderViewHeight);
            }
        }
    }

    private void resizeContentView() {
        ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        layoutParams.height = 1920;
        mContentView.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:


                break;
            case MotionEvent.ACTION_DOWN:
                mLastDownY = (int) ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetY = (int) ev.getRawY() - mLastDownY;
                if (isTop() && mOffsetY > 0) {
                    return true;
                }
                break;
        }
        mLastY = (int) ev.getRawY();
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:

                if (hasHeaderView && mHeaderView.getTop() >= 0) {

                } else if (hasFooterView && mFooterView.getBottom() >= 0) {

                }

                //如果用户释放手指时 header 完全显示
                if (getScrollY() >= mHeaderViewHeight) {
                    mHeaderViewTask.onStartLoading();
                }

                //如果用户释放手指 header 未完全显示
                if (getScrollY() < mHeaderViewHeight) {
                    smoothScrollTo(0, mHeaderViewHeight);
                }

                break;
            case MotionEvent.ACTION_MOVE:

                int dy = (int) event.getRawY() - mLastY;

                //处理滑动
               // changeScrollY(dy);
                changeMarginTop(dy);

                mHeaderViewTask.onPull(getScrollY() / mHeaderViewHeight);

                break;

        }
        mLastY = (int) event.getRawY();
        return super.onTouchEvent(event);
    }

    private void changeMarginTop(int dy) {
        if (dy > 0) {
            mMLP.topMargin += dy;
            requestLayout();
        }
    }

    private void smoothScrollTo(int toX, int toY) {

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            scrollTo(x, y);
            postInvalidate();
        }
    }

    protected abstract TaskView createHeaderTask();
    protected abstract TaskView createFooterTask();

    private void changeScrollY(int distance) {
        if (getScrollY() <= 0) return;
        if (distance > 0) {
            //防止用户下拉时太快超过 header 的高度
            if (getScrollY() > distance) {
                scrollBy(0, -distance);
            } else {
                scrollTo(0, 0);
            }

        } else {
                scrollBy(0, -distance);
        }
    }

    private Rect rect = new Rect();

    //判断是否滑到了顶部
    protected boolean isTop() {
        if (mContentView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) mContentView;
            return absListView.getFirstVisiblePosition() == 0 &&
                    absListView.getChildAt(0).getTop() >= absListView.getPaddingTop();
        } else if (mContentView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView)mContentView;
            return !recyclerView.canScrollVertically(-1);
            //            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            if (layoutManager instanceof LinearLayoutManager) {
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
//
//                Log.d("dfadfasd", rect.toString());
//                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
//                    linearLayoutManager.getChildAt(0).getLocalVisibleRect(rect);
//                    if (rect.top == 0) return true;
//                }
//                System.out.println(recyclerView.getScrollY() + "");
//
//
////                return linearLayoutManager.findFirstVisibleItemPosition() == 0 &&
////                         >= 0;

            }

        return false;
    }

    /**
     * header view 和 footer view 的接口
     */
    public interface TaskView {
        View getView();
        void onStartLoading();
        void onPull(float fraction);
        void onLoading();
        void onLoaded();
    }

    /**
     * 如果想要实现自定义的 header view 或者 header view，那么请继承此类
     */
    public static abstract class AbsTaskView implements TaskView {

        private View mView;

        public AbsTaskView(Context context) {
            mView = instantiateView(context);
        }

        public abstract View instantiateView(Context context);

        @Override
        public View getView() {
            return mView;
        }
    }

    public static class HeaderView extends AbsTaskView {

        public HeaderView(Context context) {
            super(context);
        }

        @Override
        public View instantiateView(Context context) {
            TextView view = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 400);
            view.setGravity(Gravity.CENTER);
            layoutParams.gravity = Gravity.CENTER;
            view.setLayoutParams(layoutParams);
            view.setText("下拉即可刷新");
            initView(view);
            return view;
        }

        private void initView(View view) {

        }

        @Override
        public void onStartLoading() {

        }

        @Override
        public void onPull(float fraction) {

        }

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded() {

        }
    }

    public static class FooterTaskView extends AbsTaskView {

        public FooterTaskView(Context context) {
            super(context);
        }

        private View mView;

        @Override
        public View instantiateView(Context context) {
            mView = LayoutInflater.from(context).inflate(R.layout.acticity_collect_my, null);
            return mView;
        }

        @Override
        public void onStartLoading() {

        }

        @Override
        public void onPull(float fraction) {

        }

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded() {

        }

    }

}
