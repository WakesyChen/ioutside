package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxiang.ioutside.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends Activity {
    private String TAG=getClass().getSimpleName();

    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;
    @Bind(R.id.tv_outdoor_aboutus)
    TextView tvAppVersion;
    @Bind(R.id.btn_scoreus_aboutus)
    Button btnScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_back, R.id.btn_scoreus_aboutus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_scoreus_aboutus:
                //这里开始执行一个应用市场跳转逻辑，默认this为Context上下文对象
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                Log.d(TAG,Uri.parse("market://details?id=" + getPackageName()).toString());
                //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
                if (intent.resolveActivity(getPackageManager()) != null) { //可以接收
                    startActivity(intent);
                } else { //没有应用市场，我们通过浏览器跳转到Google Play
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    //这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
                    if (intent.resolveActivity(getPackageManager()) != null) { //有浏览器
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "没有找到应用商店和浏览器", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
