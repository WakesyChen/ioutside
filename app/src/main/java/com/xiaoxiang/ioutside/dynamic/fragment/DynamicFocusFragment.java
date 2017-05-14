package com.xiaoxiang.ioutside.dynamic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.dynamic.activity.FocusActivity;
import com.xiaoxiang.ioutside.dynamic.adapter.DynamicFocusAdapter;
import com.xiaoxiang.ioutside.dynamic.model.ThumbListOfObservedUser;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GThumbListOfObservedUser;
import com.xiaoxiang.ioutside.util.ACache;
import com.xiaoxiang.ioutside.util.NetworkUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


/**
 * Created by zhang on 2016/4/25,0025.
 */
public class DynamicFocusFragment extends Fragment implements Constants,OkHttpManager.ResultCallback.CommonErrorListener{
    private static final String TAG=DynamicFocusFragment.class.getSimpleName();
    private static final int REQUEST_CODE=33;
    private View view;
    private SwipeRefreshLayout focus_swipeRefresh;
    private RecyclerView mRecyclerView;
    private DynamicFocusAdapter mAdapter;
    private List<Integer> ids=new ArrayList<>();
    private int pageSize=5;
    private int pageNo=1;
    private String token;
    private String fileToken;
    private boolean add=false;
    private ACache mCache;
    private LinearLayout tv_focus_notify;//没有订阅提醒订阅
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.dynamic_focus_fragment, container, false);
        token=getActivity().getIntent().getStringExtra("token");
        CachedInfo cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo!=null){
            fileToken=cachedInfo.getToken();
            Log.d(TAG,"fileToken="+fileToken);
        }
        mCache=ACache.get(getContext());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        focus_swipeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.focus_swipeRefresh);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.focus_recyclerView);
        tv_focus_notify= (LinearLayout) view.findViewById(R.id.tv_focus_notify);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());//创建默认的线性LayoutManager
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new NoAlphaItemAnimator());
        mAdapter=new DynamicFocusAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        if (token!=null){
            initData();
        }else if(token==null&&fileToken==null){
            tv_focus_notify.setVisibility(View.VISIBLE);
            focus_swipeRefresh.setRefreshing(false);
        }
        else {
            token=fileToken;
            initData();
        }
        initEvent();
    }

    private void initData() {
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "开始刷新");
                    int pageNo = 1;
                    OkHttpManager okHttpManager=OkHttpManager.getInstance();
                    ApiInterImpl api=new ApiInterImpl();
                    okHttpManager.getStringAsyn(api.getThumbListOfObservedUser(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(DynamicFocusFragment.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "onError");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, "关注的数据得到了刷新");
                            mCache.put("focus",response,1000*7200);
                            Type objectType = new TypeToken<BaseResponse<GThumbListOfObservedUser>>() {
                            }.getType();
                            Gson gson=new Gson();
                            BaseResponse focusResponse = gson.fromJson(response, objectType);
                            GThumbListOfObservedUser gThumbListOfObservedUser = (GThumbListOfObservedUser) focusResponse.getData();
                            List<ThumbListOfObservedUser> list = gThumbListOfObservedUser.getList();
                            if (list != null && list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    ThumbListOfObservedUser thumbListOfObservedUser = list.get(i);
                                    int id = thumbListOfObservedUser.getId();
                                    ids.add(id);

                                    if (mAdapter.getDataSet().size() >= pageSize) {
                                        for (int a = 0; a < pageSize; a++) {
                                            if ((mAdapter.getDataSet().get(a).getId() == thumbListOfObservedUser.getId())) {
                                                add = true;
                                            }
                                        }
                                    }
                                    if (add == false)
                                        mAdapter.addItemToHead(i, thumbListOfObservedUser);

                                    else
                                        add = false;

                                }
                                focus_swipeRefresh.setRefreshing(false);
                            } else {
                                tv_focus_notify.setVisibility(View.VISIBLE);
                                focus_swipeRefresh.setRefreshing(false);
                            }
                        }
                    });
                }
            };

            focus_swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    focus_swipeRefresh.setRefreshing(true);
                }
            });

            listener.onRefresh();
            focus_swipeRefresh.setOnRefreshListener(listener);
            //上拉刷新
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mRecyclerView.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = mRecyclerView.getLayoutManager().getChildAt(mRecyclerView.getLayoutManager().getChildCount() - 1);
                        int lastPosition = mRecyclerView.getLayoutManager().getPosition(lastChildView);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == mRecyclerView.getLayoutManager().getItemCount() - 1) {
                            pageNo++;
                            OkHttpManager okHttpManager = OkHttpManager.getInstance();
                            ApiInterImpl api = new ApiInterImpl();
                            okHttpManager.getStringAsyn(api.getThumbListOfObservedUser(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(DynamicFocusFragment.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "onError");
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Type objectType = new TypeToken<BaseResponse<GThumbListOfObservedUser>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    BaseResponse focusResponse = gson.fromJson(response, objectType);
                                    GThumbListOfObservedUser gThumbListOfObservedUser = (GThumbListOfObservedUser) focusResponse.getData();
                                    List<ThumbListOfObservedUser> list = gThumbListOfObservedUser.getList();
                                    if (list != null && list.size() != 0) {
                                        for (ThumbListOfObservedUser thumbListOfObservedUser : list) {
                                            int id = thumbListOfObservedUser.getId();
                                            ids.add(id);

                                            if (!mAdapter.getDataSet().contains(thumbListOfObservedUser)) {
                                                mAdapter.addItem(thumbListOfObservedUser);
                                            }
                                        }
                                        focus_swipeRefresh.setRefreshing(false);
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
        else {
            focus_swipeRefresh.setRefreshing(false);
            String response = mCache.getAsString("focus");
            if (response.length()!=0&&response!=null){
                Type objectType = new TypeToken<BaseResponse<GThumbListOfObservedUser>>(){}.getType();
                Gson gson=new Gson();
                BaseResponse focusResponse = gson.fromJson(response, objectType);
                GThumbListOfObservedUser gThumbListOfObservedUser = (GThumbListOfObservedUser) focusResponse.getData();
                List<ThumbListOfObservedUser> list = gThumbListOfObservedUser.getList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        ThumbListOfObservedUser thumbListOfObservedUser = list.get(i);
                        mAdapter.addItemToHead(i, thumbListOfObservedUser);
                    }
                }
            }

        }
    }

    //处理点击事件
    private void initEvent(){
        mAdapter.setOnItemClickListener(new DynamicFocusAdapter.OnItemClickListener() {
            @Override
            public void onUserInfoClick(View view, int position) {
                //跳转到用户信息页
                Intent intent = new Intent();
                int userId = mAdapter.getDataSet().get(position).getUserID();
                boolean observed = mAdapter.getDataSet().get(position).isObserved();
                intent.setClass(getActivity(), OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", observed);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onZanClick(View view, final int position) {
                boolean isLike = mAdapter.getDataSet().get(position).isLiked();
                OkHttpManager okHttpManager=OkHttpManager.getInstance();
                ApiInterImpl api=new ApiInterImpl();
                int id = mAdapter.getDataSet().get(position).getId();
                if (isLike == false) {
                    okHttpManager.getStringAsyn(api.likeFootprint(id, token), new OkHttpManager.ResultCallback<String>(DynamicFocusFragment.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(getActivity(), "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "点赞失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, response);
                            mAdapter.getDataSet().get(position).setLiked(true);
                            int likeCount=mAdapter.getDataSet().get(position).getLikedCount();
                            likeCount++;
                            mAdapter.getDataSet().get(position).setLikedCount(likeCount);
                            mAdapter.notifyItemChanged(position);
                        }
                    });
                } else {
                    okHttpManager.getStringAsyn(api.cancelLikeFootprint(id, token), new OkHttpManager.ResultCallback<String>(DynamicFocusFragment.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(getActivity(), "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "取消点赞失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, response);
                            mAdapter.getDataSet().get(position).setLiked(false);
                            int likeCount=mAdapter.getDataSet().get(position).getLikedCount();
                            likeCount--;
                            mAdapter.getDataSet().get(position).setLikedCount(likeCount);
                            mAdapter.notifyItemChanged(position);
                        }
                    });
                }

            }


            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                int id = mAdapter.getDataSet().get(position).getId();
                intent.setClass(getActivity(), FocusActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("token", token);
                startActivityForResult(intent,REQUEST_CODE);
            }

            @Override
            public void onShareClick(View view, int position) {
                String photo=null;
                ThumbListOfObservedUser thumbListOfObservedUser=mAdapter.getDataSet().get(position);
                //分享
                String title = thumbListOfObservedUser.getThoughts();
                if (thumbListOfObservedUser.getPhotoList().size()>0) {
                    photo=thumbListOfObservedUser.getPhotoList().get(0);
                }
                int id=thumbListOfObservedUser.getId();
                String url="http://ioutside.com/xiaoxiang-backend/footprint-share.html?footprintID="+id;
                setShareContent(title, url,photo);
            }
        });
    }

    private void setShareContent(String title, String url,String photo) {
        UMImage localImage = new UMImage(getActivity(), R.mipmap.head_ele);
        if (!TextUtils.isEmpty(photo)) {
            localImage=new UMImage(getActivity(),photo);
        }

        final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };

        new ShareAction(getActivity()).setDisplayList( displayList )
                .withText(title)
                .withTitle("来自爱户外的分享")
                .withTargetUrl(url)
                .withMedia(localImage)
                        //.setListenerList(umShareListener)
                .open();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==FocusActivity.RESULT_CODE){
            Log.e(TAG,"onActivityResult");
            int id=data.getExtras().getInt("id");
            int likeCount=data.getExtras().getInt("likeCount");
            int commentCount=data.getExtras().getInt("commentCount");
            int position=findPositionById(id);
            if (likeCount>mAdapter.getDataSet().get(position).getLikedCount()){
                //文章详情点赞，文章列表没有被点赞，文章列表要加1
                mAdapter.getDataSet().get(position).setLiked(true);
                mAdapter.getDataSet().get(position).setLikedCount(likeCount);
                mAdapter.notifyItemChanged(position);
            }
            else if (likeCount<mAdapter.getDataSet().get(position).getLikedCount()){
                //文章详情取消点赞，文章列表被点赞，文章列表要减1
                mAdapter.getDataSet().get(position).setLiked(false);
                mAdapter.getDataSet().get(position).setLikedCount(likeCount);
                mAdapter.notifyItemChanged(position);
            }
            if (commentCount!=mAdapter.getDataSet().get(position).getCommentCount()){
                //文章详情评论数量不一致
                mAdapter.getDataSet().get(position).setCommentCount(commentCount);
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    private int findPositionById(int id){
        int position=0;
        for (int i=0;i<ids.size();i++){
            if (id==ids.get(i)){
                position=i;
            }
        }
        return position;
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(getActivity(),"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(getActivity(),LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(getActivity(),"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(getActivity(),LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(getActivity(),"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(getActivity(),LoginActivity.class);
            startActivity(in);
        }
    }

}