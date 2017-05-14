package com.xiaoxiang.ioutside.mine.mvp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.adapter.BaseAdapter;

import java.util.List;

/**
 * Created by 15119 on 2016/8/6.
 * you can extend this class for creating a RecyclerView that can load more data
 */
public abstract class PullAddMoreAdapter<T> extends BaseAdapter<T, RecyclerView.ViewHolder> {

    public static int TYPE_FOOTER = 0x00;
    public static int TYPE_NORMAL = 0x01;
    public static int TYPE_HEADER = 0x02;

    private boolean mHasMoreData = false;
    private boolean mHasHeader = false;

    public PullAddMoreAdapter(List<T> dataSet) {
        super(dataSet);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_FOOTER) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else if (viewType == TYPE_NORMAL) {
            return createNormalViewHolder(parent, viewType);
        } else if (viewType == TYPE_HEADER && mHasHeader) {
            return createHeaderViewHolder(parent,  viewType);
        }
        throw new IllegalArgumentException("this view type is not supported !");
    }

    /**
     * child class must implement this method to create normal ViewHolders
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType);

    protected RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent, int viewType){return null;}

    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FooterViewHolder) {
            final FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

            if (! mHasMoreData) {
                //if no data, hide the progress bar
                footerViewHolder.pbLoading.setVisibility(View.GONE);
                footerViewHolder.tvHint.setText("—— 没有更多数据 ——");
                //hide the footer smoothly
                hideFooterView(footerViewHolder.itemView);

            } else {
                footerViewHolder.itemView.setVisibility(View.VISIBLE);
                ((ViewGroup.MarginLayoutParams) footerViewHolder.itemView.getLayoutParams())
                        .bottomMargin = 0;
                footerViewHolder.itemView.requestLayout();
                footerViewHolder.pbLoading.setVisibility(View.VISIBLE);
                footerViewHolder.tvHint.setText("正在加载...");
            }
        } else if (holder instanceof NormalViewHolder) {
            if (mHasHeader)
            bindNormalViewHolder((NormalViewHolder) holder, position - 1);
            else bindNormalViewHolder((NormalViewHolder) holder, position);
        } else if (holder instanceof HeaderViewHolder) {
            bindHeaderViewHolder((HeaderViewHolder) holder);
        }

    }

    private void hideFooterView(final View footerView) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, -footerView.getHeight());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                footerView.setVisibility(View.GONE);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ((ViewGroup.MarginLayoutParams) footerView.getLayoutParams())
                        .bottomMargin = (int) animation.getAnimatedValue();
                footerView.requestLayout();
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    public void setHasMoreData(boolean hasMoreData) {
        mHasMoreData = hasMoreData;
    }

    /**
     * subclass must implement this method to bind data to normal ViewHolder
     * @param holder holder for normal layout
     * @param position
     */
    protected abstract void bindNormalViewHolder(NormalViewHolder holder, int position);

    protected void bindHeaderViewHolder(HeaderViewHolder holder){}

    @Override
    public int getItemCount() {
        if (getDataSet() == null) {
            return 1;
        } else {
            return mHasHeader ? getDataSet().size() + 2 : getDataSet().size() + 1;
           // return getDataSet().size() + 1;
        }
    }

    /**
     * ViewHolder for footer view which can display waiting progress bar and hint
     */
    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar pbLoading;
        private TextView tvHint;

        public FooterViewHolder(View itemView) {
            super(itemView);
            itemView.setVisibility(View.INVISIBLE);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvHint = (TextView) itemView.findViewById(R.id.tv_notice);
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else if (position == 0 && mHasHeader) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
       // return position == getItemCount() - 1 ? TYPE_FOOTER : TYPE_NORMAL;

    }

    protected void setHasHeader(boolean hasHeader) {
        mHasHeader = hasHeader;
    }
}
