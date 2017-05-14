package com.xiaoxiang.ioutside.common.alipaydemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.common.SystemInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;

public class PayDemoActivity extends FragmentActivity {

    // 商户PID
    public static final String PARTNER = "2088221674855824";
    // 商户收款账号
    public static final String SELLER = "3290725880@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL4XyurdagogCTdc\n" +
            "KW7tdjyn5+hbG1wAK+AuG2W9a7EazjNaqIEOl94Wws3E61epCqpAC2Ake46LdMoB\n" +
            "mId6cOvaAWwdffeXEEqi5eh68zKT1vPbV4tfRJ0VHHZSs4YBIYwdgHRJsHp6tdhs\n" +
            "U7JnbbgyRGc/VgIBCbWGehuHn0xVAgMBAAECgYB9PJ2ieLdI7mxD/cMKYwX31kMy\n" +
            "UPvIdBB0BMxmDhKqPtfE+6ByTlP9rI8xE+BdA3CUU9dqwv5oHjaWc9CyyRclGFIM\n" +
            "rr0nPONjih/CyRMgjT6y45bBORTZIhNYSlXr5pgGfSk/NtQiJiwI7bonsGwkX+xj\n" +
            "aSUSHj6OM2xhVafojQJBAOuPfe/zQs3xpX70aBppVVxCEJvyv8jVXBA+AEQMCyw6\n" +
            "+tNOoXOQcO5vXZCfyGP0P+QfK3+OchDVcSOwIN1kvesCQQDOllQ5fu5hZ4vqQGmD\n" +
            "+ihA3XPzfF1Ow28PDWTDlbiG0Ki59BZ2jV0g3/QgjQFII+QJStt8wRwfqBPHhs3Y\n" +
            "yE6/AkEAsypt2G8HFctBXEGbWNNbKJPbPDIzqxaVMoll/FvIt7iYhNYeuN5Y9xHf\n" +
            "cZwGd/gUDHekxx1aJvGhX0qchKgWRwJAB8OQkMSbct+wW3JmRMZ/AskP1YPEAfTI\n" +
            "VLl9Iviagb6PyMVKntP7PwQLqbmC5dIrfMnqa8raIrpvW91OGSqWAwJAUjWR46md\n" +
            "lqD0q20QIM7OJYAIOFOLBmoQFMcwCKW0NolnzRtcY/R9IVkRElCRBHyTeigXMfPb\n" +
            "vwyvYteGbqUGbQ==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQ" +
            "Cnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAopri" +
            "h3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private Button btn_finish;
    private PopupWindow popwindow;
    private TextView alipay_productName;//商品名称
    private TextView alipay_productDescri;//商品描述
    private TextView alipay_productPrice;//商品价格
    private String productName;
    private String productDescri;
    private double productPrice;
    private String orderNum;
    private View view;
    @SuppressLint("HandlerLeak")
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
                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        showPopwindow();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayDemoActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void showPopwindow() {
        popwindow=new PopupWindow(PayDemoActivity.this);
        popwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popwindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popwindow.setContentView(view);

      //  popwindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popwindow.setOutsideTouchable(false);
        popwindow.setBackgroundDrawable(null);//点击窗口外不消失
        popwindow.setFocusable(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        //退出弹出框，背景变为纯白
        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        popwindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_main);
        initData();
        initEven();

    }

    private void initEven() {
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.enroll_success_popwindow2, null);
        btn_finish = (Button) view.findViewById(R.id.order_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popwindow != null) {
                    popwindow.dismiss();
                }
            }
        });

    }

    private void initData() {
        alipay_productName = (TextView) findViewById(R.id.alipay_productName);
        alipay_productDescri = (TextView) findViewById(R.id.alipay_productDescri);
        alipay_productPrice = (TextView) findViewById(R.id.alipay_productPrice);
//        设置默认值:
//        OrderInfoBean orderInfoBean1=new OrderInfoBean("爱户外沙龙","沙龙活动的报名费用",0.01);
//        通过上个界面传过来订单数据
        OrderInfoBean orderInfoBean = (OrderInfoBean) getIntent().getSerializableExtra("orderInfoBean");
        productName = orderInfoBean.getProductName();
        productDescri = orderInfoBean.getProductDescri();
        productPrice=orderInfoBean.getProductPrice();
//        测试用0.01元
//        productPrice =0.01;

        orderNum = orderInfoBean.getOrderNo();//得到订单号
        //价钱需要保留小数点后两位
        productPrice = Math.round(productPrice * 100) / 100.0;
        alipay_productName.setText(productName);
        alipay_productDescri.setText(productDescri);
        alipay_productPrice.setText(productPrice + "元");

    }

    /**
     * call alipay sdk pay. 调用SDK支付,点击支付
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
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
        String orderInfo = getOrderInfo(productName, productDescri, productPrice + "");

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
                PayTask alipay = new PayTask(PayDemoActivity.this);
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
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        /**
         *         服务器异步通知页面路径，用于接收短信通知，必须重写

         */
//        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
        orderInfo += "&notify_url=" + "\"" + "http://ioutside.com/xiaoxiang-backend/alipay/salon-pay-notify" + "\"";

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
//    需要传入商户服务器返回的订单号。此地为随机
    private String getOutTradeNo() {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
        return orderNum;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
