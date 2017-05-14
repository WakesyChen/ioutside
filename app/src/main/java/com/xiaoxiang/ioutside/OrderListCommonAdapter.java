package com.xiaoxiang.ioutside;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.activities.retrofit.Bean;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

import java.util.List;

/**
 * Created by lwenkun on 16/9/5.
 */
public class OrderListCommonAdapter extends PullAddMoreAdapter<Bean.OrderList.Data.Order> {

    private final String[] mStatus = {"待支付", "待使用", "交易成功", "订单取消", "退款办理", "退款办理"};
    private final String[] mAction = {"去支付", "查看验证码", "已完成", "已取消", "退款中", "已退款"};

    public OrderListCommonAdapter(List<Bean.OrderList.Data.Order> dataSet) {
        super(dataSet);
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_common, parent, false);
        return new NormalViewHolder(view, getOnItemClickListener());
    }

    @Override
    protected void bindNormalViewHolder(PullAddMoreAdapter.NormalViewHolder holder, int position) {
        NormalViewHolder viewHolder = (NormalViewHolder) holder;

        Bean.OrderList.Data.Order order = getDataSet().get(position);

        Context context = viewHolder.itemView.getContext();

        viewHolder.tvOrderNum.setText(order.orderNumber);
        Glide.with(context).load(order.activityPhoto).into(viewHolder.ivPhoto);
        viewHolder.tvTitle.setText(order.activityTitle);
        viewHolder.tvVisitorCount.setText(context.getString(R.string.visitor_num, order.activityQuantity));
        viewHolder.tvPrice.setText(context.getString(R.string.price, order.orderPrice));

        viewHolder.tvAction.setText(getAction(order.orderStatus));
        viewHolder.tvStatus.setText(getStatus(order.orderStatus));
    }

    private String getStatus(int status) {
        return mStatus[status];
    }

    private String getAction(int status) {
        return mAction[status];
    }

    public static class NormalViewHolder extends PullAddMoreAdapter.NormalViewHolder implements View.OnClickListener {

        private TextView tvOrderNum;
        private TextView tvStatus;
        private TextView tvTitle;
        private ImageView ivPhoto;
        private TextView tvDate;
        private TextView tvVisitorCount;
        private TextView tvPrice;
        private TextView tvAction;

        private OnItemClickListener mOnItemClickListener;

        public NormalViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);

            mOnItemClickListener = l;
            tvOrderNum = (TextView) itemView.findViewById(R.id.tv_oder_num);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvVisitorCount = (TextView) itemView.findViewById(R.id.tv_visitor_count);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvAction = (TextView) itemView.findViewById(R.id.tv_action);

            itemView.setOnClickListener(this);
            tvAction.setOnClickListener(this);
         }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

}
