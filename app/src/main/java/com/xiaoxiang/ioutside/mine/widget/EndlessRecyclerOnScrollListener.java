package com.xiaoxiang.ioutside.mine.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = false;
    //list到达 最后一个item的时候 触发加载
    private int visibleThreshold = 1;
    // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    //默认第一页
    private int current_page = 1;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PullAddMoreAdapter mPullAddMoreAdapter;
 
    public EndlessRecyclerOnScrollListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        this.mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        this.mPullAddMoreAdapter = (PullAddMoreAdapter) recyclerView.getAdapter();
    }
 
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
 
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
 
        //判断加载完成了...
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && totalItemCount > visibleItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            current_page++;
            loadMore(current_page);
            loading = true;
        }
    }

    private void loadMore(final int current_page) {
        mPullAddMoreAdapter.setHasMoreData(true);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadMore(current_page);
            }
        }, 500);

    }
 
    public abstract void onLoadMore(int current_page);
}