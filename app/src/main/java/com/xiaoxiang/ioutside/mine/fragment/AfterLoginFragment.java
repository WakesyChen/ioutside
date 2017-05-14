package com.xiaoxiang.ioutside.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.mine.activity.InfoSetActivity;
import com.xiaoxiang.ioutside.mine.activity.MyCollectActivity;
import com.xiaoxiang.ioutside.mine.activity.MyDynamicActivity;
import com.xiaoxiang.ioutside.mine.activity.MyEssayActivity;
import com.xiaoxiang.ioutside.mine.activity.MyFansActivity;
import com.xiaoxiang.ioutside.mine.activity.MyFeedbackActivity;
import com.xiaoxiang.ioutside.mine.activity.MyNotificationActivity;
import com.xiaoxiang.ioutside.mine.activity.MyObserverActivity;
import com.xiaoxiang.ioutside.mine.activity.MyScoreActivity;
import com.xiaoxiang.ioutside.mine.activity.MySettingActivity;
import com.xiaoxiang.ioutside.mine.activity.OrderListActivity;
import com.xiaoxiang.ioutside.mine.adapter.OderAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.listener.OnMsgCountReceivedListener;
import com.xiaoxiang.ioutside.mine.model.MessageCount;
import com.xiaoxiang.ioutside.mine.model.Nums;
import com.xiaoxiang.ioutside.mine.model.OderEntry;
import com.xiaoxiang.ioutside.mine.model.PersonalInfo;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GNumsList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GPersonalInfo;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * Created by oubin6666 on 2016/4/15.
 */
public class AfterLoginFragment extends Fragment implements View.OnClickListener,
        OnMsgCountReceivedListener, OnItemClickListener {
    String TAG = getClass().getSimpleName();

    @Bind(R.id.cirImg_head_user)
    CircleImageView cirImgHeadUser;
    @Bind(R.id.tv_name_user)
    TextView tvNameUser;
    @Bind(R.id.tv_sex_user)
    TextView tvSexUser;
    @Bind(R.id.tv_add_user)
    TextView tvAddUser;
    @Bind(R.id.tv_certificate_1)
    TextView tvCertificate1;
    @Bind(R.id.tv_certificate_2)
    TextView tvCertificate2;
    @Bind(R.id.tv_certificate_3)
    TextView tvCertificate3;
    @Bind(R.id.tv_experience)
    TextView tvExperUser;
    @Bind(R.id.tv_level_user)
    TextView tvLevelUser;
    @Bind(R.id.tv_recomNum)
    TextView tvRecomNum;
    @Bind(R.id.tv_dynaNum)
    TextView tvDynaNum;
    @Bind(R.id.tv_obserNum)
    TextView tvObserNum;
    @Bind(R.id.tv_fansNum)
    TextView tvFansNum;
    @Bind(R.id.tv_score_user)
    TextView tvScore;
    @Bind(R.id.pb_credit)
    ProgressBar pbCredit;
    @Bind(R.id.rv_oder)
    RecyclerView rvOder;

    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse personInfoRe;
    private String name;
    private String sex;
    private String address;
    private String experience;
    private int level;
    private String photo;
    private String phone;
    private String email;
    private String skills;
    private int score;

    private CachedInfo cachedInfo;
    private BaseResponse numsRe;
    private int fansCount;
    private int observeCount;
    private int recomCount;
    private int dynaCount;
    private String mToken;

    private MessageCount.DataBean msgCountInfo;

    private TextView[] tvCertificates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_login, container, false);
        ButterKnife.bind(this, view);
        tvCertificates = new TextView[]{tvCertificate1, tvCertificate2, tvCertificate3};
        mToken = getActivity().getIntent().getStringExtra("token");
        gson = new Gson();
        apiImpl = new ApiInterImpl();

        cachedInfo = MyApplication.getInstance().getCachedInfo();
        OderAdapter oderAdapter = new OderAdapter(getOderEntry());
        oderAdapter.setOnItemClickListener(this);
        rvOder.setAdapter(oderAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvOder.setLayoutManager(layoutManager);

        loadData();

        return view;
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_oder_entry : {
                if (position == 0) {
                    seeOderToBoUsed();
                } else if (position == 1) {
                    seeOderToBePaid();
                } else if (position == 2) {
                    seeOderCompleted();
                } else if (position == 3) {
                    seeAfterMarket();
                }
            }
        }
    }

    private void seeOderToBoUsed() {
        Intent i = new Intent(getActivity(), OrderListActivity.class);
        i.putExtra("orderType", OrderListActivity.ORDER_TO_BE_USED);
        i.putExtra("token", mToken);
        startActivity(i);
    }

    private void seeOderToBePaid() {
        Intent i = new Intent(getActivity(), OrderListActivity.class);
        i.putExtra("orderType", OrderListActivity.ORDER_TO_BE_PAID);
        i.putExtra("token", mToken);
        startActivity(i);
    }

    private void seeAllOrder() {
       Intent i = new Intent(getActivity(), OrderListActivity.class);
        i.putExtra("orderType", OrderListActivity.ORDER_ALL);
        i.putExtra("token", mToken);
        startActivity(i);
    }
    private void seeOderCompleted() {
        Intent i = new Intent(getActivity(), OrderListActivity.class);
        i.putExtra("orderType", OrderListActivity.ORDER_COMPLETED);
        i.putExtra("token", mToken);
        startActivity(i);
    }

    private void seeAfterMarket() {
        Intent i = new Intent(getActivity(), OrderListActivity.class);
        i.putExtra("orderType", OrderListActivity.ORDER_TO_BE_REFUNDED);
        i.putExtra("token", mToken);
        startActivity(i);
    }

    public void loadData(){
        if (mToken != null) {
            cachedInfo.setToken(mToken);
            Log.d(TAG, "token" + mToken);
        } else {
            mToken = cachedInfo.getToken();
            Log.d(TAG, "这个操作没有传token值");
        }
        if (mToken == null) return;
        //如果有网络的话就发送请求更新数据，否则使用已经存起来的数据
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            //下面这些代码还要修改一下。
            mOkHttpManager = OkHttpManager.getInstance();
            //获取个人信息
            mOkHttpManager.getStringAsyn(apiImpl.getPersonalInfoIn(mToken), new OkHttpManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Type objectType = new TypeToken<BaseResponse<GPersonalInfo>>() {
                    }.getType();
                    personInfoRe = gson.fromJson(response, objectType);
                    GPersonalInfo mPersonalInfo = (GPersonalInfo) personInfoRe.getData();
                    cachedInfo.setPersonalInfo(mPersonalInfo.getPersonalInfo());
//                    Log.d(TAG, "mpersonalinfo:" + mPersonalInfo.getPersonalInfo().getName());
                    initData(mPersonalInfo.getPersonalInfo());
                    showData();
                }
            });

            //获取个人的点赞数等等
            mOkHttpManager.getStringAsyn(apiImpl.getNumsIn(mToken), new OkHttpManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    Log.d(TAG, "error");
                }

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Type objectType = new TypeToken<BaseResponse<GNumsList>>() {
                    }.getType();


                    numsRe = gson.fromJson(response, objectType);
                    GNumsList numsList = (GNumsList) numsRe.getData();

                    if (numsList != null) {
                        if (cachedInfo == null || numsList.getList() == null) return;
                        cachedInfo.setNums(numsList.getList().get(0));
                        cachedInfo.save();
                        Log.d(TAG, cachedInfo.toString());
                        initNums(numsList.getList().get(0));
                        showNums();
                    }
                }
            });
        } else {
            initData(cachedInfo.getPersonalInfo());
            showData();
            initNums(cachedInfo.getNums());
            showNums();
        }
    }

    @Override
    public void onReceivedMsg(MessageCount.DataBean msgCountInfo) {

        this.msgCountInfo = msgCountInfo;

        if (msgCountInfo == null) return;

        View redPoint = getView().findViewById(R.id.iv_msg_notification);
        int totalCount = msgCountInfo.getMessageCount().getSum_count();
        if (totalCount == 0) {
            redPoint.setVisibility(View.INVISIBLE);
        } else {
            redPoint.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    public void initNums(Nums nums) {
        observeCount = nums.getObserveCount();
        recomCount = nums.getRecommendCount();
        fansCount = nums.getFansCount();
        dynaCount = nums.getDynamic_count();
    }

    public void showNums() {
        tvDynaNum.setText(dynaCount + "");
        tvFansNum.setText(fansCount + "");
        tvRecomNum.setText(recomCount + "");
        tvObserNum.setText(observeCount + "");
    }

    public void initData(PersonalInfo mInfo) {

        if (mInfo == null) {
            Log.d(TAG, "minfo is null");
        } else {
            name = mInfo.getName();
            level = mInfo.getLevel();
            phone = mInfo.getPhone();
            email = mInfo.getEmail();
            photo=mInfo.getPhoto();
            address = mInfo.getAddress();
            score = mInfo.getScore();
            skills = mInfo.getSkills();
            experience = mInfo.getExperiences();
            char sexs = mInfo.getSex();
            if (sexs == 'u') {
                sex = "不确定";
            } else if (sexs == 'w') {
                sex = "女";
            } else if (sexs == 'm') {
                sex = "男";
            }
        }
    }

    public void showData() {
        tvNameUser.setText(name);
        tvAddUser.setText(address);

        for (TextView tv : tvCertificates) {
            tv.setVisibility(View.GONE);
        }

        if (skills != null) {

            String certificates[] = skills.split(",");

            for (int i = 0; i < certificates.length; i ++) {

                if (! TextUtils.isEmpty(certificates[i])) {
                    tvCertificates[i].setVisibility(View.VISIBLE);
                    tvCertificates[i].setText(certificates[i]);
                }
            }
        }

        tvLevelUser.setText(getString(R.string.tv_level, level));
        tvExperUser.setText(experience);
        tvSexUser.setText(sex);
        tvScore.setText(getString(R.string.tv_credit, score, getCurLevelTotalScore(level)));
        pbCredit.setMax(getCurLevelTotalScore(level));
        pbCredit.setProgress(score);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        if (photo != null) {
            ImageLoader.getInstance().displayImage(photo, cirImgHeadUser, options);
        } else {
            cirImgHeadUser.setImageResource(R.drawable.defoulthead);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.lay_news, R.id.lay_collect, R.id.lay_feedback, R.id.lay_setting,
            R.id.img_pencil_editInfo, R.id.ll_order,
            R.id.lay_essaynum, R.id.lay_dynamicnum, R.id.lay_obsernum, R.id.lay_fansnum,R.id.tv_level_user, R.id.tv_score_user, R.id.cirImg_head_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_news:
                Intent newIntent = new Intent(getActivity(), MyNotificationActivity.class);
                newIntent.putExtra("token", mToken);
                newIntent.putExtra("msgCountInfo", msgCountInfo);
                startActivity(newIntent);
                break;
            case R.id.lay_collect:
                Intent colIntent = new Intent(getActivity(), MyCollectActivity.class);
                colIntent.putExtra("token", mToken);
                startActivity(colIntent);
                break;
            case R.id.lay_feedback:
                Intent fbIntent = new Intent(getActivity(), MyFeedbackActivity.class);
                fbIntent.putExtra("token", mToken);
                startActivity(fbIntent);
                break;
            case R.id.lay_setting:
                Intent setIntent = new Intent(getActivity(), MySettingActivity.class);
                setIntent.putExtra("token", mToken);
                startActivity(setIntent);
                break;
            case R.id.cirImg_head_user:
            case R.id.img_pencil_editInfo:
                Intent editIn = new Intent(getActivity(), InfoSetActivity.class);
                editIn.putExtra("name", name);
                editIn.putExtra("photo", photo);
                editIn.putExtra("sex", sex);
                editIn.putExtra("level", level);
                editIn.putExtra("phone", phone);
                editIn.putExtra("email", email);
                editIn.putExtra("address", address);
                editIn.putExtra("certi", skills);
                editIn.putExtra("experience", experience);
                editIn.putExtra("token", mToken);
                startActivity(editIn);
                break;
            case R.id.lay_essaynum:
                Intent essIn = new Intent(getActivity(), MyEssayActivity.class);
                essIn.putExtra("token", mToken);
                startActivity(essIn);
                break;
            case R.id.lay_dynamicnum:
                Intent dyIn = new Intent(getActivity(), MyDynamicActivity.class);
                dyIn.putExtra("token", mToken);
                startActivity(dyIn);
                break;
            case R.id.lay_obsernum:
                Intent obIntent = new Intent(getActivity(), MyObserverActivity.class);
                obIntent.putExtra("token", mToken);
                startActivity(obIntent);
                break;
            case R.id.lay_fansnum:
                Intent fanIn = new Intent(getActivity(), MyFansActivity.class);
                fanIn.putExtra("token", mToken);
                startActivity(fanIn);
                break;
            case R.id.tv_level_user:
            case R.id.tv_score_user:
                Intent scoreIntent = new Intent(getActivity(), MyScoreActivity.class);
                scoreIntent.putExtra("photo", photo);
                scoreIntent.putExtra("level", level);
                scoreIntent.putExtra("score", score);
                startActivity(scoreIntent);
                break;
            case R.id.ll_order:
                seeAllOrder();

        }
    }


    /**
     * 当前等级的总分数
     * @param level 当前等级
     * @return 当前等级总分数
     */
    private int getCurLevelTotalScore(int level) {

        switch (level) {
            case 0 :
                return 50;
            case 1 :
                return 150;
            case 2 :
                return 300;
            case 3 :
                return 500;
            case 4 :
                return 1000;
            case 5 :
                return 2000;
            case 6 :
                return 5000;
            case 7 :
                return 10000;
        }
        return 0;

    }

    private List<OderEntry> getOderEntry() {
        List<OderEntry> oderEntries = new ArrayList<>();
        oderEntries.add(new OderEntry(R.drawable.ic_oder_to_be_used, "待使用"));
        oderEntries.add(new OderEntry(R.drawable.ic_oder_to_be_paid, "待付款"));
        oderEntries.add(new OderEntry(R.drawable.ic_oder_finished, "已完成"));
        oderEntries.add(new OderEntry(R.drawable.ic_after_market, "退款售后"));
        return oderEntries;
    }



}
