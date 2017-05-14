package com.xiaoxiang.ioutside.activities.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.adapter.PersonListAdapter;
import com.xiaoxiang.ioutside.activities.model.GPersonListInfor;
import com.xiaoxiang.ioutside.activities.model.PersonInfor;
import com.xiaoxiang.ioutside.activities.model.TravelerInfor;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.DividerItemDecoration;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/8/18.
 */
public class
PersonListActivity  extends Activity implements View.OnClickListener{
    @Bind(R.id.personlist_addNewPerson)
    ImageView personlist_addNewPerson;
    @Bind(R.id.common_back)
    ImageView common_back;
    @Bind(R.id.recycler_personList)
    RecyclerView recycler_personList;
    @Bind(R.id.personlist_addChoosen)
    TextView personlist_addChoosen;
    private String token;
    private int pageNo=1;
    private int pageSize=15;
    private List<PersonInfor>personInforList;
    private PersonListAdapter personListAdapter;
    private static final String TAG = "PersonListActivity";
    private int personIndex;//选择的游客position
    private int touristId;//游客id
    public final int RESULT_CODE=200;
    private boolean isChoosed=false;//标识是否已经选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personlist);

        ButterKnife.bind(this);
        token=getIntent().getStringExtra("token");

    }



    @Override
    protected void onStart() {
        super.onStart();
        initData();
        initEvent();






    }

    private void initEvent() {

        personlist_addNewPerson.setOnClickListener(this);
        common_back.setOnClickListener(this);
        personlist_addChoosen.setOnClickListener(this);
        isChoosed=false;
         personListAdapter.setOnItemClickListener(new PersonListAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(View v,int position) {
                isChoosed=true;
                personIndex=position;
                touristId=personInforList.get(position).getId();
//                选择时其他项为未选中状态
                RadioButton personlistRbtn;
                for (int i=0;i<personInforList.size();i++) {
                    View view =recycler_personList.getChildAt(i);
                    personlistRbtn= (RadioButton) view.findViewById(R.id.personlist_rbtn);
                    personlistRbtn.setSelected(false);

                }
                personlistRbtn= (RadioButton) v.findViewById(R.id.personlist_rbtn);
                personlistRbtn.setSelected(true);


            }

            @Override
            public void onModifyClick(int position) {
                Intent intent=new Intent(PersonListActivity.this,EditPersonActivity.class);
                String name=personInforList.get(position).getName();
                String phone=personInforList.get(position).getPhone();
                String personId=personInforList.get(position).getIdCard();
                String passport=personInforList.get(position).getPassport();
                int id=personInforList.get(position).getId();
                TravelerInfor travelerInfor=new TravelerInfor(name,phone,personId,passport);
                intent.putExtra("travelerInfor",travelerInfor);
                intent.putExtra("id",id);
                intent.putExtra("token",token);
                startActivity(intent);

            }
        });

    }

    private void initData() {
            personIndex=0;
            touristId=0;

        personListAdapter=new PersonListAdapter();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_personList.setLayoutManager(layoutManager);
        recycler_personList.setItemAnimator(new DefaultItemAnimator());
        recycler_personList.setHasFixedSize(true);
        recycler_personList.addItemDecoration(new DividerItemDecoration(PersonListActivity.this, DividerItemDecoration.VERTICAL_LIST));
        recycler_personList.setAdapter(personListAdapter);

        //        获取常用游客列表
        ApiInterImpl api=new ApiInterImpl();
        OkHttpManager okHttpManager=OkHttpManager.getInstance();
        okHttpManager.getStringAsyn(api.getOffenUsedTravelers(token,pageNo,pageSize),new OkHttpManager.ResultCallback<String>(){
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "onResponse: "+response);
                Gson gson=new Gson();
                Type type=new TypeToken<BaseResponse<GPersonListInfor>>(){}.getType();
                BaseResponse<GPersonListInfor> baseResponse=gson.fromJson(response,type);
                if ( baseResponse.isSuccess()) {
                    personInforList=new ArrayList<>();
                    personInforList=baseResponse.getData().getList();
                    if (personInforList!=null) {
                        personListAdapter.setTravelerInforList(personInforList);
                        recycler_personList.setAdapter(personListAdapter);

                    }
                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back:
                finish();
                break;

            case R.id.personlist_addNewPerson:
                Intent intent=new Intent(this,AddPersonActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                break;
            case R.id.personlist_addChoosen:
                if (personListAdapter.getTravelerInforList() != null) {
                    if (isChoosed) {
                        if (personListAdapter.getTravelerInforList().get(personIndex) != null) {
                            PersonInfor personInfor = personListAdapter.getTravelerInforList().get(personIndex);
                            TravelerInfor travelerInfor = new TravelerInfor(personInfor.getName(), personInfor.getPhone(),
                                    personInfor.getIdCard(), personInfor.getPassport(), personInfor.getId());
                            Intent intent1 = new Intent();
                            intent1.putExtra("travelerInfor", travelerInfor);
                            setResult(RESULT_CODE, intent1);
                            finish();
                        } else {
                            ToastUtils.show("什么鬼？添加失败！");
                        }
                    } else {
                        ToastUtils.show("请先选择一位出行人");
                    }


                } else {
                    ToastUtils.show("请先添加一些常用游客！");
                }


                break;


        }
    }
}
