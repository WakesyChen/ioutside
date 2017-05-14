package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.model.Subject;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

import java.util.List;

/**
 * Created by oubin6666 on 2016/5/17.
 */
public abstract class BaseSubjectItemAdapter extends PullAddMoreAdapter<Subject> {

    private final String TAG = getClass().getSimpleName();

    public BaseSubjectItemAdapter(List<Subject> mylist) {
        super(mylist);
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(itemView, getOnItemClickListener());
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        bindViewHolder((ViewHolder)holder, position);
    }

    public abstract void bindViewHolder(BaseSubjectItemAdapter.ViewHolder holder, int position);

    /**
     * 获取指定位置的数据项
     *
     * @param position
     * @return
     */
    public Subject getItem(int position) {
        return getDataSet().get(position);
    }

    @Override
    public int getItemCount() {
        return getDataSet() == null ? 1 : getDataSet().size() + 1;
    }

    public class ViewHolder extends NormalViewHolder implements View.OnClickListener {

        public TextView tvName;
        public ImageView ivPhoto;
        public ImageView ivSubscribe;
        public ImageView ivEnter;

        public TextView introduction;
        public OnItemClickListener mListener;

        public ViewHolder(View view, OnItemClickListener l) {

            super(view);

            mListener = l;
            tvName = (TextView) view.findViewById(R.id.tv_title_mysub);
            ivSubscribe = (ImageView) view.findViewById(R.id.iv_subscribe);
            ivEnter = (ImageView) view.findViewById(R.id.iv_enter);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
            introduction = (TextView) view.findViewById(R.id.tv_intro_sub);

            view.setOnClickListener(this);
            ivSubscribe.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(v, getLayoutPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() -1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

}
