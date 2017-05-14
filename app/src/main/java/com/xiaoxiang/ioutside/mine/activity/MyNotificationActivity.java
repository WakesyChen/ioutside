package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.model.MessageCount;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15119 on 2016/7/22.
 */
public class MyNotificationActivity extends AppCompatActivity {

    @Bind(R.id.layout_collected)
    LinearLayout llCollected;
    @Bind(R.id.layout_liked)
    LinearLayout llLiked;
    @Bind(R.id.layout_commented)
    LinearLayout llCommented;
    @Bind(R.id.layout_official_assistant)
    LinearLayout llOfficialAssistant;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_notification_liked)
    TextView tvNotificationLiked;
    @Bind(R.id.tv_notification_collected)
    TextView tvNotificationCollected;
    @Bind(R.id.tv_notification_comment)
    TextView tvNotificationComment;
    @Bind(R.id.tv_notification_official)
    TextView tvNotificationOfficial;

    private String mToken;

    private final int TYPE_COMMENTED = 1;
    private final int TYPE_LIKED = 2;
    private final int TYPE_OFFICIAL = 3;
    private final int TYPE_COLLECTED = 4;

    private MessageCount.DataBean msgCountInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);
        ButterKnife.bind(this);

        initData();
        initView();
        msgCountInfo = (MessageCount.DataBean) getIntent().getSerializableExtra("msgCountInfo");
        initMsgNotification(msgCountInfo);
        llLiked.setClickable(true);
    }

    private void initMsgNotification(MessageCount.DataBean msgCountInfo) {

        if (msgCountInfo == null) return;

        int tempCount;

        tempCount = msgCountInfo.getMessageCount().getLike_count();
        if (tempCount != 0) {
            tvNotificationLiked.setVisibility(View.VISIBLE);
            tvNotificationLiked.setText(String.valueOf(tempCount));
        } else {
            tvNotificationLiked.setVisibility(View.INVISIBLE);
        }

        tempCount = msgCountInfo.getMessageCount().getCollect_count();
        if (tempCount != 0) {
            tvNotificationCollected.setVisibility(View.VISIBLE);
            tvNotificationCollected.setText(String.valueOf(tempCount));
        } else {
            tvNotificationCollected.setVisibility(View.INVISIBLE);
        }

        tempCount = msgCountInfo.getMessageCount().getComment_count();
        if(tempCount != 0) {
            tvNotificationComment.setVisibility(View.VISIBLE);
            tvNotificationComment.setText(String.valueOf(tempCount));
        } else {
            tvNotificationComment.setVisibility(View.INVISIBLE);
        }

        tempCount = msgCountInfo.getMessageCount().getOfficial_count();
        if (tempCount != 0) {
            tvNotificationOfficial.setVisibility(View.VISIBLE);
            tvNotificationOfficial.setText(String.valueOf(tempCount));
        } else {
            tvNotificationOfficial.setVisibility(View.INVISIBLE);
        }
    }

    private void initData() {
        mToken = getIntent().getStringExtra("token");
    }

    private void initView() {
        tvTitle.setText("消息中心");
    }

    @OnClick({R.id.layout_official_assistant, R.id.layout_liked, R.id.layout_commented, R.id.layout_collected})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_official_assistant:
                tvNotificationOfficial.setVisibility(View.INVISIBLE);
                cancelNotification(TYPE_OFFICIAL);
                enterOfficialAssistant();
                break;
            case R.id.layout_collected:
                tvNotificationCollected.setVisibility(View.INVISIBLE);
                cancelNotification(TYPE_COLLECTED);

                viewCollected();
                break;
            case R.id.layout_commented:
                cancelNotification(TYPE_COMMENTED);
                tvNotificationComment.setVisibility(View.INVISIBLE);
                viewCommented();
                break;
            case R.id.layout_liked:
                cancelNotification(TYPE_LIKED);
                tvNotificationLiked.setVisibility(View.INVISIBLE);
                viewLiked();
                break;
        }
    }


    private void cancelNotification(int type) {

        String url = "http://ioutside.com/xiaoxiang-backend/message/change-other-messgae-state";

        Request request = new Request.Builder()
                .url(url)
                .method(Request.METHOD_GET)
                .addParam("token", mToken)
                .addParam("type", String.valueOf(type))
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    public void enterOfficialAssistant() {
        Intent i = new Intent(this, OfficialAssistantActivity.class);
        i.putExtra("token", mToken);
        startActivity(i);
    }

    public void viewCollected() {
        Intent i = new Intent(this, CollectedNotificationActivity.class);
        i.putExtra("token", mToken);
        startActivity(i);
    }

    public void viewLiked() {
        Intent i = new Intent(this, LikedNotificationActivity.class);
        i.putExtra("token", mToken);
        startActivity(i);

    }

    public void viewCommented() {
        Intent i = new Intent(this, CommentNotificationActivity.class);
        i.putExtra("token", mToken);
        startActivity(i);

    }

}
