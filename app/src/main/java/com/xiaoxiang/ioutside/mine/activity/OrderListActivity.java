package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaoxiang.ioutside.OrderListCommonAdapter;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.activity.DetailActivity;
import com.xiaoxiang.ioutside.activities.activity.OrderCheckActivity;
import com.xiaoxiang.ioutside.activities.activity.OrderPayActivity;
import com.xiaoxiang.ioutside.activities.model.OrderInforById;
import com.xiaoxiang.ioutside.activities.retrofit.Bean;
import com.xiaoxiang.ioutside.activities.retrofit.Query;
import com.xiaoxiang.ioutside.common.BaseActivity;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.widget.TitleLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lwenkun on 16/8/29.
 */
public class OrderListActivity extends BaseActivity implements OnItemClickListener {

    public static final int ORDER_TO_BE_USED = 0x00;
    public static final int ORDER_TO_BE_PAID = 0x01;
    public static final int ORDER_COMPLETED = 0x02;
    public static final int ORDER_TO_BE_REFUNDED = 0x03;
    public static final int ORDER_ALL = 0x04;

    private final String[] titles = {"已付款订单", "待支付订单", "已完成订单", "售后处理订单", "全部订单"};

    @Bind(R.id.title_layout)
    TitleLayout titleLayout;
    @Bind(R.id.rv_orders)
    RecyclerView rvOrders;

    private OrderListCommonAdapter mOrderListCommonAdapter;
    private String mToken;
    private int mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_common);
        ButterKnife.bind(this);

        init();
        loadData();
    }

    private void init() {
        mToken = getIntent().getStringExtra("token");
        mType = getIntent().getIntExtra("orderType", -1);

        setupTitle();

        mOrderListCommonAdapter = new OrderListCommonAdapter(new ArrayList<>());
        mOrderListCommonAdapter.setOnItemClickListener(this);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(mOrderListCommonAdapter);

    }

    private void setupTitle() {
        titleLayout.setTitleText(titles[mType]);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_order_common:
                reviewActivity(mOrderListCommonAdapter.getDataSet().get(position).activityId);
                break;
            case R.id.tv_action:
                action(mOrderListCommonAdapter.getDataSet().get(position));

        }
    }

    private void reviewActivity(int activityId) {
        Intent reviewActivity = new Intent(this, DetailActivity.class);
        reviewActivity.putExtra("activityId", activityId);
        reviewActivity.putExtra("token", mToken);
        startActivity(reviewActivity);
    }

    /**
     * see showdoc for more info
     */
    private void loadData() {
        switch (mType) {
            case ORDER_TO_BE_USED :
                Query.getInstance().oderList(mToken, 1, 1, 20)
                        .map(oderList -> oderList.data)
                        .subscribe(data -> mOrderListCommonAdapter.replaceItems(data.orderList));
                break;
            case ORDER_TO_BE_PAID:
                Query.getInstance().oderList(mToken, 0, 1, 20)
                        .map(oderList -> oderList.data)
                        .subscribe(data -> mOrderListCommonAdapter.replaceItems(data.orderList));
                break;
            case ORDER_COMPLETED:
                Query.getInstance().oderList(mToken, 2, 1, 20)
                        .map(oderList -> oderList.data)
                        .subscribe(data -> mOrderListCommonAdapter.addItems(data.orderList));

                Query.getInstance().oderList(mToken, 3, 1, 20)
                        .map(oderList -> oderList.data)
                        .subscribe(data -> mOrderListCommonAdapter.addItems(data.orderList));
                break;
            case ORDER_TO_BE_REFUNDED:
                Query.getInstance().oderList(mToken, 4, 1, 20)
                        .map(oderList -> oderList.data)
                        .subscribe(data -> mOrderListCommonAdapter.addItems(data.orderList));

                Query.getInstance().oderList(mToken, 5, 1, 20)
                        .map(oderList -> oderList.data)
                        .subscribe(data -> mOrderListCommonAdapter.addItems(data.orderList));
                break;
            case ORDER_ALL:
                Query.getInstance().oderList(mToken, 6, 1, 40)
                        .map(oderList -> oderList.data)
                        .subscribe(data -> mOrderListCommonAdapter.addItems(data.orderList));
                break;
        }

    }

    /**
     * 订单状态:
     * 0->新订单,订单已提交，但未付款，
     * 1->买家已付款,等待商家联系,参与活动，
     * 2->已完成,成功参与活动，订单交易成功，
     * 3->已取消,订单提交成功,单未在规定时间内支付，
     * 4->退款中,支付成功了,申请取消交易,卖家等待退款，
     * 5->已退款,退款成功
     */
   private void action(Bean.OrderList.Data.Order order) {
       switch (order.orderStatus) {
           case 0:
               pay(order);
               break;
           case 1:
               viewOrderCode(order);
               break;
       }
   }

    private void viewOrderCode(Bean.OrderList.Data.Order order) {
        Intent viewOrderCode = new Intent(this, OrderCheckActivity.class);
        viewOrderCode.putExtra("token", mToken);
        viewOrderCode.putExtra("orderId", order.id);
        viewOrderCode.putExtra("phone", order.contactPhone);

//        OrderInforById.ListBean orderInfo = new OrderInforById.ListBean();
//        setOrderInfo(order, orderInfo);
//
//        viewOrderCode.putExtra("orderInforById", orderInfo);

        startActivity(viewOrderCode);
    }

    private void setOrderInfo(Bean.OrderList.Data.Order order, OrderInforById.ListBean orderInfo) {
        orderInfo.setId(order.id);
        orderInfo.setActivityID(order.activityId);
        orderInfo.setActivityPhoto(order.activityPhoto);
        orderInfo.setActivityQuantity(order.activityQuantity);
        orderInfo.setActivitySpecDesc(order.activitySpecDesc);
        orderInfo.setActivitySpecID(order.activitySpecId);
        orderInfo.setActivityTime(order.startDate);
        orderInfo.setActivityTitle(order.activityTitle);
        orderInfo.setContactUser(order.contactUser);
        orderInfo.setOrderCode(order.orderCode);
        orderInfo.setOrderNumber(order.orderNumber);
        orderInfo.setContactPhone(order.contactPhone);
        orderInfo.setOrderPrice(Float.valueOf(order.orderPrice).intValue());
        orderInfo.setOrderStatus(order.orderStatus);
        orderInfo.setOrderUserID(order.orderUserId);
    }

    private void pay(Bean.OrderList.Data.Order order) {
        Intent pay = new Intent(this, OrderPayActivity.class);
        pay.putExtra("token", mToken);
        pay.putExtra("orderId", order.id);
        pay.putExtra("phone", order.contactPhone);
        startActivity(pay);
    }

}
