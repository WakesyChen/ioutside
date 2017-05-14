package com.xiaoxiang.ioutside.activities.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.OrderInforById;
import com.xiaoxiang.ioutside.util.ToastUtils;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wakesy on 2016/8/31.
 */
public class OrderCompleteActivity extends Activity implements View.OnClickListener{
    @Bind(R.id.common_back_payComplete)
    ImageView common_back_payComplete;
    @Bind(R.id.orderComplete_title)
    TextView orderComplete_title;
    @Bind(R.id.orderComplete_clickCheck)
    LinearLayout orderComplete_clickCheck;
    @Bind(R.id.orderComplete_orderNo)
    TextView orderComplete_orderNo;
    @Bind(R.id.orderComplete_time)
    TextView orderComplete_time;
    @Bind(R.id.orderPay_leaveMsg)
    TextView orderPay_leaveMsg;
    @Bind(R.id.orderComplete_count)
    TextView orderComplete_count;
    @Bind(R.id.orderComplete_Amount)
    TextView orderComplete_Amount;
    @Bind(R.id.orderComplete_checkCode)
    TextView orderComplete_checkCode;
    @Bind(R.id.orderComplete_save)
    TextView orderComplete_save;
    private OrderInforById.ListBean orderInforById;//通过订单id获取到的数据
    private String token;
    private int orderId;
    private String orderTitle;
    private String orderNum;
    private String orderTime;
    private String orderLeaveMsg;
    private int orderQuantity;
    private double orderPrice;
    private String orderCheckNo;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        ButterKnife.bind(this);
        initData();
        initEvent();

    }

    private void initEvent() {

        common_back_payComplete.setOnClickListener(this);
        orderComplete_clickCheck.setOnClickListener(this);
        orderComplete_save.setOnClickListener(this);

        orderComplete_title.setText(orderTitle+"");
        orderComplete_orderNo.setText(orderNum+"");
        orderComplete_time.setText(orderTime+"");
        orderPay_leaveMsg.setText(orderLeaveMsg+"");
        orderComplete_count.setText(""+orderQuantity);
        orderComplete_Amount.setText("¥"+orderPrice+" ("+orderPrice/orderQuantity+"*"+orderQuantity+")");
        orderComplete_checkCode.setText(orderCheckNo+"");


    }

    private void initData() {

        Intent intent=getIntent();
        orderInforById= (OrderInforById.ListBean) intent.getSerializableExtra("orderInforById");
        token=intent.getStringExtra("token");
        phone=intent.getStringExtra("phone");
        orderId=intent.getIntExtra("orderId",0);
        if (orderInforById!=null) {

            orderTitle=orderInforById.getActivityTitle();
            orderNum=orderInforById.getOrderNumber();
            orderTime=orderInforById.getActivityTime();
            orderLeaveMsg=orderInforById.getRemark();
            orderQuantity=orderInforById.getActivityQuantity();
            orderPrice=orderInforById.getOrderPrice();
            orderCheckNo=orderInforById.getOrderCode();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.common_back_payComplete:
                finish();

                break;

            case R.id.orderComplete_clickCheck:
                if (orderInforById!=null) {

                    Intent intent=new Intent(this,OrderCheckActivity.class);
                    intent.putExtra("orderInforById",orderInforById);
                    intent.putExtra("token",token);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("phone",phone+"");
                    startActivity(intent);
                    ToastUtils.show("点击查看");
                }
                break;

            case R.id.orderComplete_save:
//                ScreentShotUtil screentShotUtil=ScreentShotUtil.getInstance();
//                //内置sd卡路径
//                String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//                screentShotUtil.takeScreenshot(OrderCompleteActivity.this,sdcardPath);
                ToastUtils.show("请截屏留存哦~");
                break;



        }


    }
}
