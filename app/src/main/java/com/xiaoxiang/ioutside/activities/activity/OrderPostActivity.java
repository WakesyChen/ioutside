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
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.adapter.TravelerListAdapter;
import com.xiaoxiang.ioutside.activities.model.OrderId;
import com.xiaoxiang.ioutside.activities.model.OrderInfor;
import com.xiaoxiang.ioutside.activities.model.TravelerInfor;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/8/17.
 */
public class OrderPostActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.common_back)
    ImageView common_back;
    @Bind(R.id.order_phone)
    ImageView order_phone;
    @Bind(R.id.order_title)
    TextView order_title;
    @Bind(R.id.activity_remainNum)
    TextView activity_remainNum;
    @Bind(R.id.activity_startPlace)
    TextView activity_startPlace;
    @Bind(R.id.order_activityType)
    TextView order_activityType;
    @Bind(R.id.order_personList)
    LinearLayout order_personList;

    @Bind(R.id.order_contactName)
    EditText order_contactName;
    @Bind(R.id.order_contactPhone)
    EditText order_contactPhone;
    @Bind(R.id.order_extraInfo)
    EditText order_extraInfo;
    @Bind(R.id.order_agreeRules)
    CheckBox order_agreeRules;
    @Bind(R.id.order_priceAll)
    TextView order_priceAll;
    @Bind(R.id.order_post)
    TextView order_post;
    @Bind(R.id.order_listviewPerson)
    ListView order_listviewPerson;
    private OrderInfor orderInfor;
    private TravelerListAdapter travelerListAdapter;
    private List<TravelerInfor> travelerInfors;//从适配器中取出都是一样的
//    订单相关数据
    private String token;
    private int activityID;
    private int sellerID;
    private int activityQuantity;//商品数量
    private String contactUser;//联系人姓名
    private int activitySpecID;//购买活动规格id
    private String contactPhone;//联系人电话
    private String participants;//活动参与人，逗号隔开如1,2,3(touristId1,touristId2,touristId3)
    private String remark;//备注
    private double needPayAmount;
    private String activityTime;//活动开始时间
    private double dicountPrice;//单价
    private int orderId;//订单id

    public final int REQUEST_CODE=101;
    public final int RESULT_CODE=200;
    private static final String TAG = "OrderPostActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activties_order_post);
//        防止软键盘将底部顶起
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        order_listviewPerson.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(OrderPostActivity.this)
                        .setTitle("是否删除该出行人？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    travelerListAdapter.deleteItem(position);
                                activityQuantity=travelerListAdapter.getTravelerList().size();//人数
                                needPayAmount=dicountPrice*activityQuantity;
                                order_priceAll.setText("¥"+needPayAmount+"元");
//                                travelerInfors.remove(position);
                                setListViewHeightBasedOnChildren(order_listviewPerson);//动态设置listView高度
                            }
                        });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


                return false;
            }
        });

    }

    private void initData() {
//        获取ActivityDetail传过来的数据
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        orderInfor= (OrderInfor) intent.getSerializableExtra("orderInfor");
        activityID=orderInfor.getActivityId();
        sellerID=orderInfor.getSellerID();
        activitySpecID=orderInfor.getActivitySpecID();
        activityTime=orderInfor.getStartDate();
        dicountPrice=orderInfor.getDiscountPrice();




        order_title.setText(orderInfor.getTitle());
        activity_startPlace.setText("从"+orderInfor.getStartPlace()+"出发");
        activity_remainNum.setText("剩余名额"+orderInfor.getRemainNum()+"人");
        order_activityType.setText(orderInfor.getStartDate()+" "+orderInfor.getContent());
        order_priceAll.setText("¥"+"0.0"+"元");
//
//        出行人列表设置数据
        travelerInfors =new ArrayList<>();
        travelerListAdapter=new TravelerListAdapter();

        travelerListAdapter.setTravelerList(travelerInfors);//传的是引用
        order_listviewPerson.setAdapter(travelerListAdapter);
        setListViewHeightBasedOnChildren(order_listviewPerson);//动态设置listView高度
    }

    private void initView() {
        common_back.setOnClickListener(this);//返回
        order_phone.setOnClickListener(this);//电话
        order_personList.setOnClickListener(this);//出行人列表
        order_post.setOnClickListener(this);//提交订单
        order_agreeRules.setOnClickListener(this);//同意条款
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back: {
                finish();
                break;
            }
            case R.id.order_phone: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(OrderPostActivity.this);
                dialog.setTitle("联系我们");
                dialog.setMessage("呼叫爱户外工作人员，SOS!");
                dialog.setIcon(R.drawable.head_ele);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        拨打电话
                        String phone =orderInfor.getSellerPhone();
                        if (!FormatUtil.isPhoneNum(phone)) {
                            phone="13294174985";
                        }
                        Uri uri = Uri.parse("tel:" + phone);
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        if (ActivityCompat.checkSelfPermission(OrderPostActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        OrderPostActivity.this.startActivity(intent);
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
            }
            case R.id.order_personList:{
                    Intent intent=new Intent(this,PersonListActivity.class);
                intent.putExtra("token",token );
               startActivityForResult(intent,REQUEST_CODE);

                break;
            }
            case R.id.order_post:{
//                添加订单需要的参数
//               String token,int activityID,int sellerID,int activityQuantity,String contactUser,
//                int activitySpecID,String contactPhone,String participants,String remark,
//                double needPayAmount,String activityTime


                remark=order_extraInfo.getText().toString().trim();
                participants="";//每次要清空
//                出行人的touristId

                if (travelerInfors.size() > 0) {
                    for (int i = 0; i < travelerInfors.size() - 1; i++) {
                        participants += travelerInfors.get(i).getTouristId() + ",";

                    }
                    participants += travelerInfors.get(travelerInfors.size() - 1).getTouristId();//最后一个不加,
                }
//              联系人姓名电话
                contactUser=order_contactName.getText().toString().trim();
                contactPhone=order_contactPhone.getText().toString().trim();

                Log.i(TAG, "travelerlist.size:"+travelerInfors.size()+"adapter:"+travelerListAdapter.getTravelerList().size());
                Log.i(TAG, "participants: "+participants);
                Log.i(TAG, "activityQuantity: "+activityQuantity);
                ApiInterImpl api=new ApiInterImpl();
                OkHttpManager okHttpManager=OkHttpManager.getInstance();

                if (activityQuantity < 1) {
                    ToastUtils.show("请先添加出行人！");
                } else {
                    if (!FormatUtil.isLegalNickName(contactUser)) {
                        ToastUtils.show("请添加联系人姓名！");
                    } else {
                        if (!FormatUtil.isPhoneNum(contactPhone)) {
                            ToastUtils.show("请添加正确联系人电话！");

                        } else {
                            okHttpManager.postAsyn(api.addActivityOrder(token,activityID,sellerID,activityQuantity,contactUser,activitySpecID,contactPhone,
                                    participants,remark,needPayAmount,activityTime),new OkHttpManager.ResultCallback<String>(){

                                @Override
                                public void onError(Request request, Exception e) {
                                    super.onError(request, e);
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Gson gson=new Gson();
                                    Type type=new TypeToken<BaseResponse<OrderId>>(){}.getType();
                                    BaseResponse<OrderId> baseResponse=gson.fromJson(response,type);
                                    if (baseResponse.isSuccess()) {
                                        String phone =orderInfor.getSellerPhone();
                                        orderId = baseResponse.getData().getOrderID();
                                        ToastUtils.show("下单成功！订单号为：" + orderId);
                                        Intent intent = new Intent(OrderPostActivity.this, OrderPayActivity.class);
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("token",token);
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);
                                    } else {
                                        ToastUtils.show("下单失败，"+baseResponse.getErrorMessage());
                                    }

                                }
                            });
                        }
                    }
                }







                break;
            }
            case R.id.order_agreeRules:{
                if (!order_agreeRules.isChecked()) {
                    order_post.setBackgroundColor(Color.GRAY);
                    order_post.setEnabled(false);
                } else {
                    order_post.setBackgroundResource(R.drawable.actangle_orange_bg);
                    order_post.setEnabled(true);
                }

            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE&&resultCode==RESULT_CODE) {
                TravelerInfor travelerInfor= (TravelerInfor) data.getSerializableExtra("travelerInfor");
                boolean isExist=false;//判断此出行人是否存在
            for (int i = 0; i < travelerListAdapter.getTravelerList().size(); i++) {

                if ( travelerInfor.getTouristId()==travelerListAdapter.getTravelerList().get(i).getTouristId()) {
                    isExist=true;
                }
            }
            if (!isExist) {
//                travelerInfors.add(travelerInfor);
                travelerListAdapter.addItem(travelerInfor);
                setListViewHeightBasedOnChildren(order_listviewPerson);//动态设置listView高度
            } else {
                ToastUtils.show("所选出行人已存在");
            }


            activityQuantity=travelerListAdapter.getTravelerList().size();//人数
            needPayAmount=dicountPrice*activityQuantity;
            order_priceAll.setText("¥"+needPayAmount+"元");


        }



        super.onActivityResult(requestCode, resultCode, data);

    }
}
