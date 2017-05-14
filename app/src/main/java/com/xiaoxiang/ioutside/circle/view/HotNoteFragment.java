package com.xiaoxiang.ioutside.circle.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.circle.adapter.HotNoteAdapter;
import com.xiaoxiang.ioutside.circle.model.HotNotes;
import com.xiaoxiang.ioutside.circle.widge.MyItemDecoration;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/9/17.
 */
public class HotNoteFragment extends Fragment {
    @Bind(R.id.hotNote_refresh)
    SwipeRefreshLayout hotNote_refresh;
    @Bind(R.id.hotNote_recycler)
    RecyclerView hotNote_recycler;
    private List<HotNotes.Notes> datalist;
    private HotNoteAdapter mAdapter;
    public final static int STATE_NORMAL = 0;
    public final static int STATE_REFRESH = 1;
    public final static int STATE_lOAD = 2;
    private int current_state;
    private int pageNo = 1;
    private int pageSize = 5;
    private String token = CircleFragment.token;
    private static final String TAG = "HotNoteFragment";
    private int lastVisibleItemPosition;
    private LinearLayoutManager layoutManager;
    private OkHttpManager okHttpManager = OkHttpManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.circle_hotnote, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();

    }

    private void initEvent() {

//        下拉刷新
        hotNote_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                current_state = STATE_REFRESH;
                pageNo = 1;
                getData();
                hotNote_refresh.setRefreshing(false);


            }
        });

//          上拉加载更多

        hotNote_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
                    current_state = STATE_lOAD;
//                    pageNo++;
                    getData();


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    lastVisibleItemPosition = ((LinearLayoutManager) hotNote_recycler.getLayoutManager()).findLastVisibleItemPosition();
                }
            }
        });

    }

    private void initData() {
        setSwipeRefreshStyle();

//      首次加载
        current_state = STATE_NORMAL;
        mAdapter = new HotNoteAdapter();
        getData();


    }

    private void getData() {
        okHttpManager.getStringAsyn(new ApiInterImpl().getHotNote(token, pageNo, pageSize), new OkHttpManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "onResponse: " + response);
                Gson gson = new Gson();
                datalist = new ArrayList<HotNotes.Notes>();
                Type type = new TypeToken<BaseResponse<HotNotes>>() {
                }.getType();
                BaseResponse<HotNotes> baseResponse = gson.fromJson(response, type);
                if (baseResponse.isSuccess()) {
                        datalist = baseResponse.getData().getList();
                    Log.i(TAG, "onResponse: datalist.size()"+datalist.size());
                   datalist.addAll(addTempData()) ;//增加临时数据
                    showData();
                } else {
                    ToastUtils.show("请求失败，" + baseResponse.getErrorMessage());
                }

            }
        });


    }

    private void showData() {
        switch (current_state) {

            case STATE_NORMAL:
                layoutManager = new LinearLayoutManager(getContext());
                hotNote_recycler.setLayoutManager(layoutManager);
                hotNote_recycler.addItemDecoration(new MyItemDecoration());
                mAdapter.setData(datalist);
                hotNote_recycler.setAdapter(mAdapter);
                break;

            case STATE_REFRESH:
                mAdapter.clearData();
                mAdapter.setData(datalist);
                ToastUtils.show("已刷新"+mAdapter.getData().size()+"条");

                break;
            case STATE_lOAD:
                int position = mAdapter.getData().size();
                mAdapter.addData(position, datalist);
                hotNote_recycler.scrollToPosition(position);
                ToastUtils.show("已加载"+mAdapter.getData().size()+ "条");

                break;
        }


    }


    //    增加临时数据
    public  List<HotNotes.Notes> addTempData() {
        List<HotNotes.Notes> tempData=new ArrayList<>();
        List<String> photolist = new ArrayList<>();
        photolist.add("http://img1.imgtn.bdimg.com/it/u=2903919484,2781795919&fm=21&gp=0.jpg");
        photolist.add("http://img5.imgtn.bdimg.com/it/u=891966115,192832720&fm=21&gp=0.jpg");
        photolist.add("http://img3.imgtn.bdimg.com/it/u=3507726384,830956393&fm=21&gp=0.jpg");
        photolist.add("http://hi.csdn.net/attachment/201110/30/0_1319976939pkgr.gif");
        photolist.add("http://img3.imgtn.bdimg.com/it/u=3507726384,830956393&fm=21&gp=0.jpg");
        photolist.add("http://img5.imgtn.bdimg.com/it/u=1664922004,3162313055&fm=21&gp=0.jpg");
        photolist.add("http://img5.imgtn.bdimg.com/it/u=1664922004,3162313055&fm=21&gp=0.jpg");

        for (int i = 0; i < 10; i++) {
            HotNotes.Notes note = new HotNotes.Notes();
            note.setPublishUserPhoto("http://img5.imgtn.bdimg.com/it/u=891966115,192832720&fm=21&gp=0.jpg");
            note.setPublishUserName("生于忧患");
            note.setPublishDate("6小时前");
            note.setTitle("登山攻略");
            note.setContent("当我发现所谓醒来其实是另一个梦，梦的出口散不开的雾太浓重");
            note.setPhotoList(photolist);
            note.setViewCount(12);
            tempData.add(note);
        }


//        datalist.addAll(tempData);

        return tempData;

    }

    //  设置下拉进度条样式
    private void setSwipeRefreshStyle() {
        hotNote_refresh.setColorSchemeColors(0xfffb9b20);
        hotNote_refresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
//设置下拉出现的小圈圈是否缩放出现，出现的位置，最大的下拉位置
        hotNote_refresh.setProgressViewOffset(true, 50, 200);

    }
}
