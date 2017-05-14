package com.xiaoxiang.ioutside.dynamic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.dynamic.adapter.DynamicActivityAdapter;
import com.xiaoxiang.ioutside.dynamic.adapter.DynamicEquipmentAdapter;
import com.xiaoxiang.ioutside.dynamic.adapter.DynamicHotAdapter;
import com.xiaoxiang.ioutside.dynamic.model.TypeList;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GTypeList;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Request;

public class DynamicTagActivity extends Activity implements Constants,OkHttpManager.ResultCallback.CommonErrorListener{
    private static final String TAG=DynamicTagActivity.class.getSimpleName();
    public static final int RESULT_CODE=44;
    private RecyclerView recycler_hot;
    private RecyclerView recycler_activity;
    private RecyclerView recycler_equipment;
    private RecyclerView recycler_multiple;//综合
    private DynamicActivityAdapter activityAdapter;
    private DynamicEquipmentAdapter equipmentAdapter;
    private DynamicHotAdapter hotAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }
    private void initView() {
        setContentView(R.layout.activity_dynamic_tag);
        recycler_hot=(RecyclerView)findViewById(R.id.recycler_hot);
        recycler_activity=(RecyclerView)findViewById(R.id.recycler_activity);
        recycler_equipment=(RecyclerView)findViewById(R.id.recycler_equipment);

        recycler_hot.setLayoutManager(new GridLayoutManager(this,4));
        recycler_hot.setHasFixedSize(true);
        hotAdapter=new DynamicHotAdapter();
        recycler_hot.setAdapter(hotAdapter);

        recycler_activity.setLayoutManager(new GridLayoutManager(this,4));
        recycler_activity.setHasFixedSize(true);
        activityAdapter=new DynamicActivityAdapter();
        recycler_activity.setAdapter(activityAdapter);

        recycler_equipment.setLayoutManager(new GridLayoutManager(this, 4));
        recycler_equipment.setHasFixedSize(true);
        equipmentAdapter=new DynamicEquipmentAdapter();
        recycler_equipment.setAdapter(equipmentAdapter);
    }
    private void initData() {
        if (NetworkUtil.isNetworkConnected(this)) {
            //活动的数据
            ApiInterImpl api=new ApiInterImpl();
            OkHttpManager okHttpManager=OkHttpManager.getInstance();
            okHttpManager.getStringAsyn(api.getHotTypeList(),new OkHttpManager.ResultCallback<String>(DynamicTagActivity.this){
                @Override
                public void onError(Request request, Exception e) {
                    super.onError(request, e);
                    Log.d(TAG, "onError");
                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Type objectType = new TypeToken<BaseResponse<GTypeList>>() {
                    }.getType();
                    Gson gson=new Gson();
                    BaseResponse typeResponse = gson.fromJson(response, objectType);
                    GTypeList gTypeList = (GTypeList) typeResponse.getData();
                    List<TypeList> list = gTypeList.getList();
                    if (list != null && list.size() != 0) {
                        hotAdapter.addItem(list);
                    }
                }
            });

            okHttpManager.getStringAsyn(api.getOutdoorTypeList(), new OkHttpManager.ResultCallback<String>(DynamicTagActivity.this) {
                @Override
                public void onError(Request request, Exception e) {
                    Log.d(TAG, "onError");
                }
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Type objectType = new TypeToken<BaseResponse<GTypeList>>() {
                    }.getType();
                    Gson gson=new Gson();
                    BaseResponse typeResponse = gson.fromJson(response, objectType);
                    GTypeList gTypeList = (GTypeList) typeResponse.getData();
                    List<TypeList> list = gTypeList.getList();
                    if (list != null && list.size() != 0) {
                        activityAdapter.addItem(list);

                    }
                }
            });
            //装备的数据
            okHttpManager.getStringAsyn(api.getEquipmentTypeList(), new OkHttpManager.ResultCallback<String>(DynamicTagActivity.this) {
                @Override
                public void onError(Request request, Exception e) {
                    Log.d(TAG, "onError");
                }
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Type objectType = new TypeToken<BaseResponse<GTypeList>>() {
                    }.getType();
                    Gson gson=new Gson();
                    BaseResponse typeResponse = gson.fromJson(response, objectType);
                    GTypeList gTypeList = (GTypeList) typeResponse.getData();
                    List<TypeList> list = gTypeList.getList();
                    if (list != null && list.size() != 0) {
                        equipmentAdapter.addItem(list);
                    }
                }
            });
        }else {
            Toast.makeText(this,"网络有点问题!",Toast.LENGTH_SHORT).show();
        }
    }
    private void initEvent(){
        hotAdapter.setOnItemClickListener(new DynamicHotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String typeName=hotAdapter.getDataSet().get(position).getTypeName();
                Intent intent=new Intent(DynamicTagActivity.this,DynamicActivity.class);
                intent.putExtra("tag",typeName);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });

        equipmentAdapter.setOnItemClickListener(new DynamicEquipmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String typeName=equipmentAdapter.getDataSet().get(position).getTypeName();
                Intent intent=new Intent(DynamicTagActivity.this,DynamicActivity.class);
                intent.putExtra("tag",typeName);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });

        activityAdapter.setOnItemClickListener(new DynamicActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String typeName=activityAdapter.getDataSet().get(position).getTypeName();
                Intent intent=new Intent(DynamicTagActivity.this,DynamicActivity.class);
                intent.putExtra("tag",typeName);
                setResult(RESULT_CODE,intent);
                finish();
            }
        });
    }
    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this, "你已在别的地方登录，你被迫下线，请重新登录！", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this, LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this, LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this, LoginActivity.class);
            startActivity(in);
        }
    }


}
