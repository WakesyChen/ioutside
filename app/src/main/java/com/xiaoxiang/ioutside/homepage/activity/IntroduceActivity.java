package com.xiaoxiang.ioutside.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.adapter.IntroducePagerAdpater;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 15119 on 2016/6/28.
 */
public class IntroduceActivity extends AppCompatActivity {

    @Bind(R.id.vp_introduce)
    ViewPager vpIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        ButterKnife.bind(this);

        List<View> views = new ArrayList<>();
        views.add(View.inflate(this, R.layout.introduce_0, null));
        views.add(View.inflate(this, R.layout.introduce_1, null));
        views.add(View.inflate(this, R.layout.introduce_2, null));
        View lastView = View.inflate(this, R.layout.introduce_3, null);
        views.add(lastView);

        ImageView ivEnterApp = (ImageView)  lastView.findViewById(R.id.iv_enter_app);
        ivEnterApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });

        vpIntroduce.setAdapter(new IntroducePagerAdpater(views));
    }

    private void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
