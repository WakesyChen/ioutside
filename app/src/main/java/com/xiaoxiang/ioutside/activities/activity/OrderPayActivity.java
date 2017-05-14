package com.xiaoxiang.ioutside.activities.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.OrderInforById;
import com.xiaoxiang.ioutside.activities.model.TouristsBean;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.alipaydemo.PayDemoActivity;
import com.xiaoxiang.ioutside.common.alipaydemo.PayResult;
import com.xiaoxiang.ioutside.common.alipaydemo.SignUtils;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/8/28.
 * 支付界面
 */

public class OrderPayActivity extends Activity implements View.OnClickListener{
    @Bind(R.id.common_back_pay)
    ImageView common_back_pay;
    @Bind(R.id.order_phone_pay)
    ImageView order_phone_pay;
    @Bind(R.id.order_title_pay)
    TextView order_title_pay;
    @Bind(R.id.order_type_pay)
    TextView order_type_pay;
    @Bind(R.id.order_startPlace_pay)
    TextView order_startPlace_pay;
    @Bind(R.id.orderPay_price_detail)
    TextView orderPay_price_detail;
    @Bind(R.id.order_listView_travelers)
    ListView order_listView_travelers;
    @Bind(R.id.order_contactName_pay)
    TextView order_contactName_pay;
    @Bind(R.id.order_contactPhone_pay)
    TextView order_contactPhone_pay;
    @Bind(R.id.order_extraInfo_pay)
    TextView order_extraInfo_pay;
    @Bind(R.id.order_price_pay)
    TextView order_price_pay;
    @Bind(R.id.order_payNow)
    TextView order_payNow;
    @Bind(R.id.swipeRefresh_pay)
    SwipeRefreshLayout swipeRefresh_pay;
    private int orderId;
    private String token;
    private String phone;
    private String orderNumber;
    private SimpleAdapter mAdapter;
    private OrderInforById.ListBean orderInforById;//通过订单id获取到的数据
    private static final String TAG = "OrderPayActivity";
    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(OrderPayActivity.this,OrderCompleteActivity.class);
                        intent.putExtra("orderInforById",orderInforById);
                        intent.putExtra("token",token);
                        intent.putExtra("phone",phone);
                        intent.putExtra("orderId",orderId);
                        startActivity(intent);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_paynow);
        ButterKnife.bind(this);
        initView();


    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
        initEvent();
    }

    private void initView() {


    }

    private void initData() {

        Intent intent=getIntent();
        orderId=intent.getIntExtra("orderId",0);
        token=intent.getStringExtra("token");
        phone=intent.getStringExtra("phone");




//        下拉刷新的接口
       SwipeRefreshLayout.OnRefreshListener onRefreshListener= new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh: "+"刷新");
                ApiInterImpl api=new ApiInterImpl();
                OkHttpManager okHttpManager=OkHttpManager.getInstance();
                okHttpManager.getStringAsyn(api.getOrderInforByOrderId(token,orderId),new OkHttpManager.ResultCallback<String>(){
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);

                        Log.i(TAG, "onResponse: "+response);
                        Gson gson=new Gson();
                        Type type=new TypeToken<BaseResponse<OrderInforById>>(){}.getType();
                        BaseResponse<OrderInforById> baseResponse=gson.fromJson(response,type);
                        orderInforById=new OrderInforById.ListBean();
                        if (baseResponse.isSuccess()) {
                            orderInforById=baseResponse.getData().getList();
                            Log.i(TAG, "orderInforById:"+orderInforById);
                            Log.i(TAG, "onResponse: orderInforId.getName:"+orderInforById.getContactUser());
                            if (orderInforById!=null) {
                                setContent(orderInforById);
                            }


                        } else {
                            ToastUtils.show("获取数据失败，"+baseResponse.getErrorMessage());
                        }
                        int count=1;
//                ToastUtils.show("第几次刷新："+count++);
                        swipeRefresh_pay.setRefreshing(false);


                    }
                });


            }
        };

//        首次刷新界面
        swipeRefresh_pay.setOnRefreshListener(onRefreshListener);
        setSwipeRefreshStyle();
        swipeRefresh_pay.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh_pay.setRefreshing(true);
            }
        });
        onRefreshListener.onRefresh();


    }

    //  设置下拉进度条样式
    private void setSwipeRefreshStyle() {
        swipeRefresh_pay.setColorSchemeColors(0xfffb9b20);
        swipeRefresh_pay.setProgressBackgroundColorSchemeColor(Color.WHITE);
//设置下拉出现的小圈圈是否缩放出现，出现的位置，最大的下拉位置
        swipeRefresh_pay.setProgressViewOffset(true, 50, 200);

    }

    //    给各个TextView设置内容
    private void setContent(OrderInforById.ListBean orderInforById) {

        order_title_pay.setText(orderInforById.getActivityTitle()+"");
        order_type_pay.setText(orderInforById.getActivityTime()+" "+orderInforById.getActivitySpecDesc());
        order_startPlace_pay.setText("从"+orderInforById.getStartPlace()+"出发");
        order_contactName_pay.setText(orderInforById.getContactUser()+"");
        order_contactPhone_pay.setText(orderInforById.getContactPhone()+"");

//        设置出行人数据源
       List<TouristsBean> tourists=orderInforById.getTourists();//出行人列表

        if (tourists!=null) {
            List<Map<String ,Object>>touristlist=new ArrayList<>();
            for (int i = 0; i <tourists.size(); i++) {
                Map<String ,Object>map=new HashMap<>();
                map.put("touristName",tourists.get(i).getName());
                map.put("touristPhone","电话 "+tourists.get(i).getPhone());
                map.put("touristID","身份证 "+tourists.get(i).getIdCard());
                touristlist.add(map);
            }
//        设置适配器
            mAdapter=new SimpleAdapter(OrderPayActivity.this,touristlist,R.layout.activity_item_person,new String[] {"touristName","touristPhone","touristID"},
                    new int[]{R.id.traveler_name,R.id.traveler_phone,R.id.traveler_personID});

            order_listView_travelers.setAdapter(mAdapter);
            setListViewHeightBasedOnChildren(order_listView_travelers);
//        显示价钱
            order_price_pay.setText("¥"+orderInforById.getOrderPrice()+"元");
        }
        orderPay_price_detail.setText("("+ orderInforById.getOrderPrice()/orderInforById.getTourists().size()+"*" +orderInforById.getTourists().size()+")");


//         留言
        order_extraInfo_pay.setText(orderInforById.getRemark()+"");

    }

    private void initEvent() {
        common_back_pay.setOnClickListener(this);
        order_phone_pay.setOnClickListener(this);
        order_payNow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.common_back_pay:
                finish();

                break;


            case R.id.order_phone_pay:
                AlertDialog.Builder dialog = new AlertDialog.Builder(OrderPayActivity.this);
                dialog.setTitle("联系我们");
                dialog.setMessage("呼叫爱户外工作人员，SOS!");
                dialog.setIcon(R.drawable.head_ele);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        拨打电话
                        if (!FormatUtil.isPhoneNum(phone)) {
                            phone="13294174985";
                        }
                        Uri uri = Uri.parse("tel:" + phone);
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        if (ActivityCompat.checkSelfPermission(OrderPayActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        OrderPayActivity.this.startActivity(intent);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.create().show();

                break;
//          点击支付
            case R.id.order_payNow:
              orderNumber=orderInforById.getOrderNumber();
                Log.i(TAG, "orderNumber: "+orderNumber);

                if (TextUtils.isEmpty(PayDemoActivity.PARTNER) || TextUtils.isEmpty(PayDemoActivity.RSA_PRIVATE) || TextUtils.isEmpty(PayDemoActivity.SELLER)) {
                    new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    //
                                    finish();
                                }
                            }).show();
                    return;
                }
//        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
                String orderInfo = getOrderInfo(orderInforById.getActivityTitle(), orderInforById.getActivityTitle(), orderInforById.getOrderPrice() + "");
//                String orderInfo = getOrderInfo( orderInforById.getActivityTitle(), orderInforById.getActivityTitle(), 0.1+ "");

                /**
                 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
                 */
                String sign = sign(orderInfo);
                try {
                    /**
                     * 仅需对sign 做URL编码
                     */
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                /**
                 * 完整的符合支付宝参数规范的订单信息
                 */
                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(OrderPayActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo, true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();

                break;

        }

    }

    //    设置listView的高度，否则在scollview中只能显示一条
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }



    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PayDemoActivity.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + PayDemoActivity.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
        orderInfo += "&notify_url=" + "\"" + "http://ioutside.com/xiaoxiang-backend/alipay/pay-notify" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
//
    private String getOutTradeNo() {

        return orderNumber;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, PayDemoActivity.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
