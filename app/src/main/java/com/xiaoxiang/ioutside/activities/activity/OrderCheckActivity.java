package com.xiaoxiang.ioutside.activities.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.OrderInforById;
import com.xiaoxiang.ioutside.activities.model.TouristsBean;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/8/31.
 */
public class OrderCheckActivity  extends Activity implements View.OnClickListener{
    @Bind(R.id.order_title_check)
    TextView order_title_check;
    @Bind(R.id.order_type_check)
    TextView order_type_check;
    @Bind(R.id.order_startPlace_check)
    TextView order_startPlace_check;
    @Bind(R.id.listView_travelers_check)
    ListView listView_travelers_check;
    @Bind(R.id.order_contactName_check)
    TextView order_contactName_check;
    @Bind(R.id.order_contactPhone_check)
    TextView order_contactPhone_check;
    @Bind(R.id.order_phone_check)
    ImageView order_phone_check;
    @Bind(R.id.order_extraInfo_check)
    TextView order_extraInfo_check;
    @Bind(R.id.order_checkNo)
    TextView order_checkNo;
    @Bind(R.id.common_back_check)
    ImageView common_back_check;
    @Bind(R.id.orderPay_Amount_check)
    TextView orderPay_Amount_check;
    @Bind(R.id.orderPay_price_detail)
    TextView orderPay_price_detail;
    @Bind(R.id.orderPay_return)
    TextView orderPay_return;

    private OrderInforById.ListBean orderInfo;
    private SimpleAdapter mAdapter;
    private List<Map<String ,Object>> travelerlist;
    private List<TouristsBean> touristsBeanList;
    private static final String TAG = "OrderCheckActivity";
    private String phone;
    private String token;
    private int orderId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_recheck);
        ButterKnife.bind(this);

        initData();
//        initEvent();
    }



    private void initEvent() {
        order_title_check.setText(orderInfo.getActivityTitle()+"");
        order_type_check.setText(orderInfo.getActivityTime()+" "+orderInfo.getActivitySpecDesc());
        order_startPlace_check.setText("从"+orderInfo.getStartPlace()+"出发");
        order_contactName_check.setText(orderInfo.getContactUser()+"");
        order_contactPhone_check.setText(orderInfo.getContactPhone()+"");
        order_extraInfo_check.setText(orderInfo.getRemark()+"");
        order_checkNo.setText(orderInfo.getOrderCode()+"");
        orderPay_Amount_check.setText("¥"+orderInfo.getOrderPrice());
        orderPay_price_detail.setText("(共"+orderInfo.getActivityQuantity()+"人)");

        common_back_check.setOnClickListener(this);
        orderPay_return.setOnClickListener(this);
        order_phone_check.setOnClickListener(this);

        if (orderInfo != null) {


            if (orderInfo.getTourists() == null) return;

            travelerlist=new ArrayList<>();
            touristsBeanList=orderInfo.getTourists();
            for (int i = 0; i < touristsBeanList.size(); i++) {
                Map<String ,Object>map=new HashMap<>();
                map.put("name",touristsBeanList.get(i).getName());
                map.put("phone","电话 "+touristsBeanList.get(i).getPhone());
                map.put("IDCard","身份证 "+touristsBeanList.get(i).getIdCard());
                travelerlist.add(map);
            }

            mAdapter=new SimpleAdapter(OrderCheckActivity.this,travelerlist,R.layout.activity_item_person,new String[] {"name","phone","IDCard"},
                    new int[]{R.id.traveler_name,R.id.traveler_phone,R.id.traveler_personID});
            listView_travelers_check.setAdapter(mAdapter);
            setListViewHeightBasedOnChildren(listView_travelers_check);
        }


    }

    private void initData() {

        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        orderId=intent.getIntExtra("orderId",0);
        token=intent.getStringExtra("token");
//        orderInfo= (OrderInforById.ListBean) intent.getSerializableExtra("orderInforById");

        Log.i(TAG, "initData: phone:"+phone+"-orderId:"+orderId+"-token:"+token);

//          首先通过intent传递订单信息过来，没有的话就访问网络重新获取

        ApiInterImpl api=new ApiInterImpl();
        OkHttpManager okHttpManager=OkHttpManager.getInstance();
        okHttpManager.getStringAsyn(api.getOrderInforByOrderId(token,orderId),new OkHttpManager.ResultCallback<String>(){
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "onResponse: "+response);
                Gson gson=new Gson();
                Type type=new TypeToken<BaseResponse<OrderInforById>>(){}.getType();
                BaseResponse<OrderInforById> baseResponse=gson.fromJson(response,type);
                orderInfo=new OrderInforById.ListBean();
                if (baseResponse.isSuccess()) {

                    orderInfo=baseResponse.getData().getList();

                    initEvent();//得到orderInfo后初始化数据
                } else {
                    ToastUtils.show("获取数据失败，"+baseResponse.getErrorMessage());
                }

            }
        });







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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_back_check:

                finish();
                break;

            case R.id.order_phone_check:
                AlertDialog.Builder dialog = new AlertDialog.Builder(OrderCheckActivity.this);
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
                        if (ActivityCompat.checkSelfPermission(OrderCheckActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        OrderCheckActivity.this.startActivity(intent);
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
            case R.id.orderPay_return:

                final AlertDialog.Builder alertDialog=new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.head_ele)
                        .setTitle("申请退款")
                        .setMessage("桥豆麻袋，您确定要退款么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ApiInterImpl api=new ApiInterImpl();
                                OkHttpManager okHttpManager=OkHttpManager.getInstance();
                                okHttpManager.postAsyn(api.getOrderReturn(token,orderId),new OkHttpManager.ResultCallback<String>(){
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        super.onError(request, e);
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        super.onResponse(response);
                                        Gson gson=new Gson();
                                        Type type=new TypeToken<BaseResponse>(){}.getType();
                                        BaseResponse baseResponse=gson.fromJson(response,type);

                                        if (baseResponse.isSuccess()) {
                                            ToastUtils.show("申请成功，请等待工作人员处理与您联系");
                                        } else {
                                            ToastUtils.show("申请失败，"+baseResponse.getErrorMessage());
                                        }
                                    }
                                });
                                dialog.cancel();
                            }
                        });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });
                alertDialog.create().show();



//                ToastUtils.show("若需要退款，请直接联系客服");
                break;


        }

    }
}
