package com.xiaoxiang.ioutside.homepage.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.activity.DetailActivity;
import com.xiaoxiang.ioutside.activities.model.PersonInfor;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.circle.view.CircleFragment;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.common.ProgressWebView;
import com.xiaoxiang.ioutside.common.alipaydemo.OrderInfoBean;
import com.xiaoxiang.ioutside.common.alipaydemo.PayDemoActivity;
import com.xiaoxiang.ioutside.common.methods.UmengShare;
import com.xiaoxiang.ioutside.homepage.PersonInfoAll;
import com.xiaoxiang.ioutside.homepage.model.GSalonInfoList;
import com.xiaoxiang.ioutside.homepage.model.SalonInfo;
import com.xiaoxiang.ioutside.homepage.model.SalonOrderNo;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.NetworkUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Wakesy on 2016/6/2.
 * <p/>
 * 处理首页轮播图对应的沙龙活动，完成支付
 */
public class ItemWebVIew extends Activity implements View.OnClickListener {
    private ProgressWebView webview;
    private ImageView imageView;
    private TextView tv_title;
    private Button enroll_btn;//报名的按钮
    private boolean isChoosed=false;
    private ImageView activity_banner_share;//分享
    private String url = "";
    private String title = "";
    private int index;
    private String token;
    private String filetoken;
    private String photo;
    private CachedInfo cachedInfo;
    private View convertView;//弹出框视图
    private PopupWindow popupWindow;
    private RadioGroup price_choices;//选择价格
    private RadioButton order_price, order_price1, order_price2;
    private Button btn_topay;//支付
    private ImageView btn_cancel;//取消
    private TextView price_amount;//总价
    private EditText order_name;//订单姓名，电话
    private EditText order_phone;
    public double needPayAmount;
    private String orderNo;//订单号
    private static final String TAG = "ItemWebVIew";
    private ArrayList<Double> pricelist;
    //   沙龙活动规格
    private List<SalonInfo> salonlist;
    private int salonPeriods;//沙龙期数.当前是第二期
    private int salonstandardID;//活动规格id

    private PersonInfoAll.UserBean personInfor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_banner_webview);
        //        防止软键盘将底部顶起
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();
        initData();
        initEvent();

    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        token = intent.getStringExtra("token");
        index = intent.getIntExtra("index", 1);
        photo=intent.getStringExtra("photo");
//        //仅在第一个界面有报名选项
//        if (index == 0) {
//            enroll_btn.setVisibility(View.VISIBLE);//轮播图第一张可以报名
//        } else {
//            enroll_btn.setVisibility(View.GONE);
//        }
        /**
         * //        撤销这个报名按键，暂时消失

         */
        enroll_btn.setVisibility(View.GONE);


        cachedInfo = MyApplication.getInstance().getCachedInfo();//从文件中取出token
        if (cachedInfo != null) {
            filetoken = cachedInfo.getToken();
        }

        if (token == null && filetoken != null) {
            token = filetoken;
        }

//        取出token，获取用户信息
        if (token != null) {
            ApiInterImpl api = new ApiInterImpl();
            OkHttpManager okHttpManager = OkHttpManager.getInstance();
            okHttpManager.getStringAsyn(api.getPersonalInfoIn(token), new OkHttpManager.ResultCallback<String>() {

                @Override
                public void onError(Request request, Exception e) {
                    super.onError(request, e);

                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Log.i(TAG, "onResponse: personInfo:" + response);
                    Gson gson = new Gson();
                    Type type = new TypeToken<BaseResponse<PersonInfoAll>>() {
                    }.getType();
                    BaseResponse<PersonInfoAll> baseResponse = gson.fromJson(response, type);
                    personInfor = new PersonInfoAll.UserBean();
                    if (baseResponse.isSuccess()) {
                        personInfor = baseResponse.getData().getUser();
                    } else {
                        ToastUtils.show("获取用户信息失败");
                    }

                }
            });
        } else {
//            noLogin();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initEvent() {
        imageView.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_topay.setOnClickListener(this);
        activity_banner_share.setOnClickListener(this);

        tv_title.setText(title);
//              覆盖WebView通过第三方浏览器访问网页的行为，使得网页在WebView中打开
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//              返回值是true的时候控制网页在webview中打开,false则通过第三方打开
                view.loadUrl(url);
                return true;
            }
        });
        //启用支持JavaScript
        WebSettings websetting = webview.getSettings();
        websetting.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        websetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        websetting.setLoadWithOverviewMode(true);
        websetting.setBuiltInZoomControls(true);
        websetting.setUseWideViewPort(true);
        websetting.setDomStorageEnabled(true);
        websetting.setDatabaseEnabled(true);
        websetting.setAppCacheEnabled(true);

/**
 * 撤销此类形式活动
 */

        //点击进行报名
//        enroll_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (token != null) {//MainActivity的token不为空
//                    //                填写订单信息，暂时不用了
//                    writeOrderInfo();
//
//                } else {
//                    noLogin();
//                }
//
//
//            }
//
//
//        });


//        选择的价格
        price_choices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                isChoosed=false;
                switch (checkedId) {
                    case R.id.order_price:
                        isChoosed=true;
                        salonstandardID=6;//选择周六

//                        if (pricelist.size() >= 1) {
//                            needPayAmount = pricelist.get(0);
//                            salonstandardID = salonlist.get(0).getId();
//
//                        }
                        break;
                    case R.id.order_price1:
                        isChoosed=true;
                        salonstandardID=7;//选择周日

//                        if (pricelist.size() >= 2) {
//                            needPayAmount = pricelist.get(1);
//                            salonstandardID=salonlist.get(1).getId();
//
//                        }
                        break;
                    case R.id.order_price2:
//                        if (pricelist.size() >= 3) {
//                            needPayAmount = pricelist.get(2);
//                            salonstandardID = salonlist.get(2).getId();
//
//                        }
                        break;

                }
//                showPrice(needPayAmount);

            }
        });

    }

    private void showPrice(double price) {
        price_amount.setText(price + "元");
    }

    private void initView() {
        webview = (ProgressWebView) findViewById(R.id.banner_webview);
        tv_title = (TextView) findViewById(R.id.webview_title);
        imageView = (ImageView) findViewById(R.id.banner_detail_back);
        enroll_btn = (Button) findViewById(R.id.enroll_btn);
        activity_banner_share= (ImageView) findViewById(R.id.activity_banner_share);
        //弹出框
        convertView = LayoutInflater.from(ItemWebVIew.this).inflate(R.layout.enroll_order_popwindow, null);
        btn_cancel = (ImageView) convertView.findViewById(R.id.btn_cancel);
        btn_topay = (Button) convertView.findViewById(R.id.order_topay);
        price_choices = (RadioGroup) convertView.findViewById(R.id.price_choices);
        price_amount = (TextView) convertView.findViewById(R.id.price_amount);
        order_name = (EditText) convertView.findViewById(R.id.order_name);
        order_phone = (EditText) convertView.findViewById(R.id.order_phone);
        order_price = (RadioButton) convertView.findViewById(R.id.order_price);
        order_price1 = (RadioButton) convertView.findViewById(R.id.order_price1);
        order_price2 = (RadioButton) convertView.findViewById(R.id.order_price2);


    }


    //        改写物理按键一次返回就退出应用的逻辑
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {//判断前面是否能返回
                webview.goBack();

                return true;
            } else {
                if (popupWindow != null && !popupWindow.isShowing()) {

                    finish();//退出当前Activity
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    //在弹出框填写订单信息
    public void writeOrderInfo() {
        if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
//       获取数据
//            OkHttpManager okHttpManager = OkHttpManager.getInstance();
//            ApiInterImpl api = new ApiInterImpl();
//            okHttpManager.getStringAsyn(api.getSalonForm(token, salonPeriods), new OkHttpManager.ResultCallback<String>() {
//
//                @Override
//                public void onError(Request request, Exception e) {
//                    super.onError(request, e);
////                    ToastUtils.show("sorry,未访问到数据");
//
//                }
//
//                @Override
//                public void onResponse(String response) {
//                    super.onResponse(response);
//                    Type objectType = new TypeToken<BaseResponse<GSalonInfoList>>() {
//                    }.getType();
//                    Gson gson = new Gson();
//                    BaseResponse SalonResponse = gson.fromJson(response, objectType);
//                    GSalonInfoList gSalonInfoList = (GSalonInfoList) SalonResponse.getData();
//                    salonlist = gSalonInfoList.getList();//得到沙龙活动规格列表
//                    Log.i(TAG, "onResponse: salonlist" + salonlist);
//                    if (salonlist != null && salonlist.size() > 0) {
//                        pricelist = new ArrayList<>();//必须每次重置
//                        for (SalonInfo saloninfo : salonlist) {
//                            pricelist.add(saloninfo.getPrice());
//                        }
//                        //                如果没有3条价钱，隐藏对应radiobutton
//                        if (pricelist.size()==1) {
//                            order_price1.setVisibility(View.GONE);
//                            order_price2.setVisibility(View.GONE);
//                        }
//                        if (pricelist.size()==2) {
//                            order_price2.setVisibility(View.GONE);
//                        }
////                        根据价格种类设置文字
//                        if (pricelist.size()>=1) {
//                            order_price.setText("早鸟票 " + pricelist.get(0) + "元");
//                            salonPeriods=salonlist.get(0).getSalonPeriods();//得到活动期档期
//
//                        }
//                        if (pricelist.size() >= 2) {
//
//                            order_price1.setText("一般票 " + pricelist.get(1) + "元");
//                        }
//                        if (pricelist.size()>=3)
//                        order_price2.setText("支持票 " + pricelist.get(2) + "元");
//
//                    }
//
////            剩余票数处理
//                    if (salonlist!=null&&salonlist.size()>=1&&salonlist.get(0).getRemainNum() < 1) {
//                            order_price.setEnabled(false); //早鸟票没了
//                    }
//                    if (salonlist!=null&&salonlist.size()>=2&&salonlist.get(1).getRemainNum() < 1) {
//                        order_price1.setEnabled(false); //一般票没了
//                    }
//                    if (salonlist!=null&&salonlist.size()>=3&&salonlist.get(2).getRemainNum() < 1) {
//                        order_price2.setEnabled(false); //支持票没了
//                    }
//
//
//
//                }
//            });

        popupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.showAtLocation(convertView, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.6f;//设置透明度
        getWindow().setAttributes(params);
        popupWindow.update();
        //退出弹出框，背景变为纯白
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

        } else {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            ToastUtils.show("请检查网络");
        }

    }


    public void noLogin() {
        String[] items = new String[]{"登录", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemWebVIew.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(ItemWebVIew.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.banner_detail_back:
                finish();
//                圈子详情入口
                Intent intent=new Intent(ItemWebVIew.this, CircleFragment.class);
                intent.putExtra("token",token);
                startActivity(intent);
                break;
                //分享
            case R.id.activity_banner_share:


                String content = "爱户外，玩出精彩，享受生活，贴近自然！";

                UmengShare.setShareContent(ItemWebVIew.this, title, url, photo, content);
                break;
            case R.id.btn_cancel:
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                break;
            case R.id.order_topay:
//                ToastUtils.show("点击了下单");
                String name = order_name.getText().toString().trim();
                String phone = order_phone.getText().toString().trim();

                if (name.length() < 2) {
                    ToastUtils.show("姓名不能少于两个字！");
                } else {
                    if ( !FormatUtil.isPhoneNum(phone)) {

                        ToastUtils.show("请输入11位正确电话号码，便于接收短信凭证");
                    } else {
                        if (!isChoosed) {
                            ToastUtils.show("请先选择一种票类型");
                        } else {
                            //上传订单，得到订单号,需要参数token,salonPeriods,salonstandardID,name,phone,needPayAmount;
                            getOrderNum(token,  salonstandardID, name, phone);//上传订单，获取订单号
                        }
                    }
                }

                break;


        }


    }

    //    发送订单到服务器，返回订单号
    public void getOrderNum(String token, int salonstandardID, String name, String phone) {
        if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
            ApiInterImpl api = new ApiInterImpl();
            OkHttpManager mokHttpManager = OkHttpManager.getInstance();
            mokHttpManager.postAsyn(api.addSalonOrder(token, salonstandardID, name, phone), new OkHttpManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    super.onError(request, e);
                    ToastUtils.show("下单失败，网络故障");
                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Log.i(TAG, "onResponse:Json "+response);
                    Gson gson=new Gson();
                    Type type=new TypeToken<BaseResponse>(){}.getType();
                    BaseResponse baseResponse=gson.fromJson(response,type);

                    if (baseResponse.isSuccess()) {
                        ToastUtils.show("参与成功！");
                        Intent intent=new Intent(ItemWebVIew.this,ItemWebView2.class);
                        if (personInfor!=null) {
                            intent.putExtra("personInfor",personInfor);
                            startActivity(intent);
                        }


                    } else {
                        ToastUtils.show("参与失败");
                    }

//                    Gson gson = new Gson();
//                    Type type = new TypeToken<SalonOrderNo>() {
//                    }.getType();
//                    SalonOrderNo salonOrder = gson.fromJson(response, type);
//                    orderNo = salonOrder.getData().getOrderNo();//订单号
//                    Log.i(TAG, "订单号: " + orderNo);
//                    if (!TextUtils.isEmpty(orderNo)) {
//                        ToastUtils.show("下单成功，请完成支付");
//                        Intent intent1 = new Intent(ItemWebVIew.this, PayDemoActivity.class);
//                        OrderInfoBean orderInfoBean = new OrderInfoBean("爱户外沙龙", "沙龙活动报名费用", needPayAmount, orderNo);
//                        intent1.putExtra("orderInfoBean", orderInfoBean);
//                        startActivity(intent1);
//                    }
                }
            });


        } else {
            ToastUtils.show("下单失败，请检查网络！");
        }


    }


}

