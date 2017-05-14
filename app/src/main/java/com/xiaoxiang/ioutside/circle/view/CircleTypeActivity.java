package com.xiaoxiang.ioutside.circle.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.circle.adapter.CircleTypeAdapter;
import com.xiaoxiang.ioutside.circle.model.CircleType;
import com.xiaoxiang.ioutside.circle.widge.MyItemDecoration;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/10/9.
 */
public class CircleTypeActivity extends Activity {
    @Bind(R.id.circle_typeBack)
    ImageView circle_typeBack;
    @Bind(R.id.circle_typeName)
    TextView circle_typeName;
    @Bind(R.id.circle_type_recycler)
    RecyclerView circle_type_recycler;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_REFRESH = 1;
    public final static int STATE_LOAD = 2;
    private CircleTypeAdapter mAdapter;
    private List<CircleType.ListBean>datalist;
    private int pageNo=1;
    private int pageSize=10;
    private int groupType=1;
    private String token;
    private OkHttpManager okHttpManager;
    private ApiInterImpl api;
    private static final String TAG = "CircleTypeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_type_activity);
        ButterKnife.bind(this);

        initData();
        initEvent();


    }

    private void initEvent() {


        mAdapter.setOnItemClickListener(new CircleTypeAdapter.OnItemClickListener() {
            @Override
            public void onJoinClick(int circleId) {
                ToastUtils.show(circleId+"号");
                Intent intent=new Intent(CircleTypeActivity.this,PostNoteActivity.class);
                startActivity(intent);
            }

        });
    }

    private void initData() {
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        groupType=intent.getIntExtra("groupType",1);
        okHttpManager=OkHttpManager.getInstance();
        api=new ApiInterImpl();

        mAdapter=new CircleTypeAdapter();
        circle_type_recycler.setLayoutManager(new LinearLayoutManager(this));
        circle_type_recycler.addItemDecoration(new MyItemDecoration());
        circle_type_recycler.setAdapter(mAdapter);

//        根据圈子id获取圈子列表
        getCircleData();




    }

    private void getCircleData() {

        okHttpManager.getStringAsyn(api.getCircleListByGroupID(token,groupType,pageNo,pageSize),new OkHttpManager.ResultCallback<String>(){


            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);

            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "onResponse: "+response);
                Gson gson=new Gson();
                Type type=new TypeToken<BaseResponse<CircleType>>(){}.getType();
                BaseResponse<CircleType> baseResponse=gson.fromJson(response,type);
                if (baseResponse.isSuccess()) {
                    datalist=baseResponse.getData().getList();
                    if (datalist!=null&&datalist.size()>0) {
                        showData();
                    }


                } else {
                    ToastUtils.show("获取数据失败，"+baseResponse.getErrorMessage());
                }

            }
        });




    }

    private void showData() {

        mAdapter.setData(datalist);





    }


}
