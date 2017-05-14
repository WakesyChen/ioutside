package com.xiaoxiang.ioutside.circle.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.QaData;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.circle.adapter.QAofVAdapter;
import com.xiaoxiang.ioutside.circle.model.QAdataList;
import com.xiaoxiang.ioutside.circle.widge.MyItemDecoration;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/9/19.
 * 大V问答
 */

public class QAofVActivity extends Activity {
    @Bind(R.id.circle_bigV_recycleview)
    RecyclerView circle_bigV_recycleview;
    @Bind(R.id.circle_bigV_inputQ)
    TextView circle_bigV_inputQ;
    @Bind(R.id.circle_bigV_refresh)
    SwipeRefreshLayout circle_bigV_refresh;

    private QAofVAdapter mAdapter;
    public final static int STATE_NORMAL = 0;
    public final static int STATE_REFRESH = 1;
    public final static int STATE_LOAD = 2;
    private int current_state = STATE_NORMAL;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "QAofVActivity";
    private int lastVisibleItemPosition = 0;
    public Handler handler = new Handler();
    private String token = CircleFragment.token;
    private int circleID = 1;
    private int pageNo = 1;
    private int pageSize = 10;
    private List<QAdataList.QAdata> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_bigv_activity);
        ButterKnife.bind(this);


        initData();
        initEvent();


    }


    private void initEvent() {

        mAdapter.setOnClickListener(new QAofVAdapter.OnClickListener() {
            @Override
            public void backClick() {
                finish();
            }

            @Override
            public void shareClick() {

            }

            @Override
            public void focusClick(View v) {
                String content = ((ToggleButton) v).isChecked() ? "已关注" : "未关注";
                ToastUtils.show(content);
            }

            @Override
            public void ChooseTypeClick(View v) {
                String content = ((ToggleButton) v).isChecked() ? "最新" : "最热";
                ToastUtils.show(content);
            }

            @Override
            public void commentClick() {

            }

            @Override
            public void likeClick(View v) {
                String content = ((ToggleButton) v).isChecked() ? "喜欢" : "不喜欢";
                ToastUtils.show(content);
            }
        });


    }


    private void initData() {
//                首次进去加载数据
        current_state = STATE_NORMAL;
        mAdapter = new QAofVAdapter();

        setSwipeRefreshStyle();
        getData();
        updateData();


    }

    //  设置下拉进度条样式
    private void setSwipeRefreshStyle() {
        circle_bigV_refresh.setColorSchemeColors(0xfffb9b20);
        circle_bigV_refresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
//设置下拉出现的小圈圈是否缩放出现，出现的位置，最大的下拉位置
        circle_bigV_refresh.setProgressViewOffset(true, 50, 200);

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void updateData() {

//        下拉刷新
        circle_bigV_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                circle_bigV_refresh.setRefreshing(true);
                current_state = STATE_REFRESH;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        circle_bigV_refresh.setRefreshing(false);
                    }
                }, 2000);


            }
        });

//        上拉加载
        circle_bigV_recycleview.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
                    current_state = STATE_LOAD;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
//                            Log.i(TAG, "datasize: "+mAdapter.getItemCount());
                        }
                    }, 2000);


                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
//                得到最后一个可见项的位置,注意要强转
                    lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                }
            }
        });


    }


    public void getData() {

        OkHttpManager okHttpManager = OkHttpManager.getInstance();
        ApiInterImpl api = new ApiInterImpl();
        String url=api.getBigVQAList(token, circleID, pageNo, pageSize);
        Log.i(TAG, "getData: "+url);
        okHttpManager.getStringAsyn(url, new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.i(TAG, "onError: "+e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Type type = new TypeToken<BaseResponse<QAdataList>>() {
                }.getType();
                Gson gson = new Gson();
                //              添加临时数据
                Log.i(TAG, "onResponse: " + response);
                BaseResponse<QAdataList> baseRespone = gson.fromJson(response, type);
                if (baseRespone.isSuccess()) {
                    datalist = baseRespone.getData().getList();
                    if (datalist==null||datalist.size()==0) {
                        addTempData();
                    }
                    showData();
                } else {
                    ToastUtils.show("请求失败，" + baseRespone.getErrorMessage());
                }



            }
        });


    }

    private void showData() {

        switch (current_state) {
            case STATE_NORMAL:
                mAdapter.setData(datalist);
                ToastUtils.show("显示了"+mAdapter.getData().size()+"条");
//                Log.i(TAG, "showData: "+mAdapter.getData().size());
                layoutManager = new LinearLayoutManager(this);
                circle_bigV_recycleview.setLayoutManager(layoutManager);
                circle_bigV_recycleview.addItemDecoration(new MyItemDecoration());
                circle_bigV_recycleview.setItemAnimator(new DefaultItemAnimator());
                circle_bigV_recycleview.setAdapter(mAdapter);
                break;
            case STATE_REFRESH:
                mAdapter.clearData();
                mAdapter.setData(datalist);
                ToastUtils.show("显示了"+mAdapter.getData().size()+"条");
                break;
            case STATE_LOAD:
                int position = mAdapter.getData().size();
                mAdapter.addData(position, datalist);
                circle_bigV_recycleview.scrollToPosition(position);
                ToastUtils.show("显示了"+mAdapter.getData().size()+"条");
                break;

        }


    }

    public void addTempData(){
        datalist = new ArrayList<QAdataList.QAdata>();
//        List<QAdataList.QAdata>datas=new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            QAdataList.QAdata qAdata=new QAdataList.QAdata();
            qAdata.setQuestion("君不见，黄河之水天上来，奔流到海不复回；君不见，高堂明镜悲白发，朝如青丝暮成雪；" +
                    "人生得意须尽欢，莫使金樽空对月。天生我材必有用，千金散尽还复来。烹羊宰牛且为乐，会须一饮三百杯." +
                    "钟鼓馔玉不足贵，但愿长醉不愿醒。古来圣贤皆寂寞，惟有饮者留其名。主人为何言少钱，径须沽取对君酌。" +
                    "五花马，千金裘，呼儿将出换美酒，与尔同销万古愁");
            qAdata.setAnswer("一曲清歌酒满樽，人生何处不相逢");
            datalist.add(qAdata);
        }
//        datalist.addAll(datas);


    }


}
