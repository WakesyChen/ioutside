package com.xiaoxiang.ioutside.mine.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyScoreActivity extends AppCompatActivity {

    @Bind(R.id.img_userhead_score)
    CircleImageView imgUserheadScore;
    @Bind(R.id.tv_currscore_score)
    TextView tvCurrscoreScore;
    @Bind(R.id.tv_allscore_score)
    TextView tvAllscoreScore;
    @Bind(R.id.tv_level_score)
    TextView tvLevelScore;
    @Bind(R.id.img_backbtn_score)
    ImageView imgBackbtn;
    private String userphoto;
    private String level;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        ButterKnife.bind(this);
        level = getIntent().getStringExtra("level");
        userphoto = getIntent().getStringExtra("photo");
        score=getIntent().getIntExtra("score",20);
        tvLevelScore.setText("LV"+level);
        tvCurrscoreScore.setText(score+"");
        tvAllscoreScore.setText(score+"");

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        if(userphoto!=null){
            ImageLoader.getInstance().displayImage(userphoto, imgUserheadScore, options);
        }else{
            imgUserheadScore.setImageResource(R.drawable.defoulthead);
        }
    }

    @OnClick(R.id.img_backbtn_score)
    public void onClick() {
        finish();
    }
}
