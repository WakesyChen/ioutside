package com.xiaoxiang.ioutside.dynamic.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.xiaoxiang.ioutside.dynamic.activity.RecommendActivity;
import com.xiaoxiang.ioutside.dynamic.adapter.DynamicRecommendAdapter;
import com.xiaoxiang.ioutside.dynamic.model.ThumbList;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GThumbList;
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
public class DynamicRecommendFragment extends Fragment implements Constants,OkHttpManager.ResultCallback.CommonErrorListener {
    private static final String TAG=DynamicRecommendFragment.class.getSimpleName();
    private static final int REQUEST_CODE=55;
    private View view;
    private SwipeRefreshLayout swipeRefresh_recommend;
    private RecyclerView recyclerView_recommend;
    private DynamicRecommendAdapter mAdapter;
    private List<Integer> ids=new ArrayList<>();
    private int pageSize=5;
    private int pageNo=1;
    private String token;
    private String fileToken;
    private ACache mCache;
    private boolean add=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.dynamic_recommend_fragment, container, false);
        token=getActivity().getIntent().getStringExtra("token");
        CachedInfo cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo!=null){
            fileToken=cachedInfo.getToken();
            Log.d(TAG, "fileToken=" + fileToken);
        }
        mCache=ACache.get(getContext());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefresh_recommend=(SwipeRefreshLayout)view.findViewById(R.id.recommend_swipeRefresh);
        recyclerView_recommend=(RecyclerView)view.findViewById(R.id.recommend_recycleView);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView_recommend.setLayoutManager(mLayoutManager);
        recyclerView_recommend.setHasFixedSize(true);
        recyclerView_recommend.setItemAnimator(new NoAlphaItemAnimator());
        mAdapter=new DynamicRecommendAdapter(getContext());
        recyclerView_recommend.setAdapter(mAdapter);
        Log.d("token", token + "");
        if (token!=null){
            initData();
        }
        else if (token==null&&fileToken==null){
            initData();
        }
        else {
            token=fileToken;
            initData();
        }
        initEvent();
    }

    private void initData() {
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "开始刷新");
                    int pageNo = 1;
                    ApiInterImpl api=new ApiInterImpl();
                    OkHttpManager okHttpManager=OkHttpManager.getInstance();
                    okHttpManager.getStringAsyn(api.getThumbList(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(DynamicRecommendFragment.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, "推荐的数据得到了刷新");
                            mCache.put("recommend",response,1000*7200);
                            Type objectObject = new TypeToken<BaseResponse<GThumbList>>() {
                            }.getType();
                            Gson gson=new Gson();
                            BaseResponse recommendResponse = gson.fromJson(response, objectObject);
                            GThumbList gThumbList = (GThumbList) recommendResponse.getData();
                            List<ThumbList> list = gThumbList.getList();
                            if (list != null && list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    ThumbList thumbList = list.get(i);
                                    int id = thumbList.getId();
                                    ids.add(id);
                                    if (mAdapter.getDataSet().size() >= pageSize) {
                                        for (int a = 0; a < pageSize; a++) {
                                            if ((mAdapter.getDataSet().get(a).getId() == thumbList.getId())) {
                                                add = true;
                                            }
                                        }
                                    }
                                    if (add == false) {
                                        mAdapter.addItemToHead(i, thumbList);
                                    }
                                    else {
                                        add=false;
                                    }
                                }
                            }
                            swipeRefresh_recommend.setRefreshing(false);
                        }
                    });
                }
            };

            swipeRefresh_recommend.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh_recommend.setRefreshing(true);
                }
            });


            listener.onRefresh();
            swipeRefresh_recommend.setOnRefreshListener(listener);
            recyclerView_recommend.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recyclerView_recommend.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recyclerView_recommend.getLayoutManager().getChildAt(recyclerView_recommend.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recyclerView_recommend.getLayoutManager().getPosition(lastChildView);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == recyclerView_recommend.getLayoutManager().getItemCount() - 1) {
                            pageNo++;
                            ApiInterImpl api = new ApiInterImpl();
                            OkHttpManager okHttpManager = OkHttpManager.getInstance();
                            okHttpManager.getStringAsyn(api.getThumbList(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(DynamicRecommendFragment.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Type objectObject = new TypeToken<BaseResponse<GThumbList>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    BaseResponse recommendResponse = gson.fromJson(response, objectObject);
                                    GThumbList gThumb = (GThumbList) recommendResponse.getData();
                                    List<ThumbList> list = gThumb.getList();
                                    if (list != null && list.size() != 0) {
                                        for (ThumbList thumbList : list) {
                                            int id = thumbList.getId();
                                            ids.add(id);
                                            if (!mAdapter.getDataSet().contains(thumbList)) {
                                                mAdapter.addItem(thumbList);
                                            }
                                        }
                                    }
                                    swipeRefresh_recommend.setRefreshing(false);
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
            String response = mCache.getAsString("recommend");
            if (! TextUtils.isEmpty(response)) {
                Type objectObject = new TypeToken<BaseResponse<GThumbList>>() {
                }.getType();
                Gson gson=new Gson();
                BaseResponse recommendResponse = gson.fromJson(response, objectObject);
                GThumbList gThumbList = (GThumbList) recommendResponse.getData();
                List<ThumbList> list = gThumbList.getList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        ThumbList thumbList = list.get(i);
                        mAdapter.addItemToHead(i, thumbList);
                    }
                }
                swipeRefresh_recommend.setRefreshing(false);
            }
        }
    }

    private void initEvent(){
        final  ApiInterImpl api=new ApiInterImpl();
        mAdapter.setOnItemClickListener(new DynamicRecommendAdapter.ItemClickListener() {
            @Override
            public void onUserInfoClick(View view, int position) {
                //跳转到用户信息列表
                int userId = mAdapter.getDataSet().get(position).getUserID();
                boolean observed = mAdapter.getDataSet().get(position).isObserved();
                Intent intent = new Intent();
                intent.putExtra("userID", userId);
                intent.putExtra("observed", observed);
                intent.putExtra("token",token);
                intent.setClass(getActivity(), OtherPersonActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFocusClick(View view, final int position) {
                boolean isObserved = mAdapter.getDataSet().get(position).isObserved();
                int userId = mAdapter.getDataSet().get(position).getUserID();
                OkHttpManager okHttpManager=OkHttpManager.getInstance();
                if (token == null) {
                    noLogin();
                } else {
                    if (isObserved == false) {
                        okHttpManager.getStringAsyn(api.observeUser(userId, token), new OkHttpManager.ResultCallback<String>(DynamicRecommendFragment.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                //super.onError(request, e);
                                Toast.makeText(getActivity(), "网络有问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "关注失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Log.i(TAG, response);
                                mAdapter.getDataSet().get(position).setObserved(true);
                                mAdapter.notifyItemChanged(position);
                            }
                        });
                    } else {
                        okHttpManager.getStringAsyn(api.cancelObserveUser(userId, token), new OkHttpManager.ResultCallback<String>(DynamicRecommendFragment.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Toast.makeText(getActivity(), "网络有问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "取消关注失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Log.i(TAG, response);
                                mAdapter.getDataSet().get(position).setObserved(false);
                                mAdapter.notifyItemChanged(position);
                            }
                        });
                    }
                }

            }

            @Override
            public void onZanClick(View view, final int position) {
                boolean isLike = mAdapter.getDataSet().get(position).isLiked();
                int id = mAdapter.getDataSet().get(position).getId();
                OkHttpManager okHttpManager=OkHttpManager.getInstance();
                if (token == null) {
                    noLogin();
                } else {
                    if (isLike == false) {
                        okHttpManager.postAsyn(api.likeFootprint(id, token), new OkHttpManager.ResultCallback<String>(DynamicRecommendFragment.this) {
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
                        okHttpManager.getStringAsyn(api.cancelLikeFootprint(id, token), new OkHttpManager.ResultCallback<String>(DynamicRecommendFragment.this) {
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
            }

            @Override
            public void onItemClick(View view, int position) {
                String photo=null;
                Intent intent = new Intent(getActivity(), RecommendActivity.class);
                ThumbList thumbList=mAdapter.getDataSet().get(position);
                int id = thumbList.getId();
                if (thumbList.getPhotoList().size()>0) {
                    photo=thumbList.getPhotoList().get(0);
                }
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("token", token);
                intent.putExtra("photo",photo);
                startActivityForResult(intent,REQUEST_CODE);
            }

            @Override
            public void onShareClick(View view, int position) {
                ThumbList thumbList=mAdapter.getDataSet().get(position);
                String photo=null;
                if (thumbList.getPhotoList().size() > 0) {
                     photo = thumbList.getPhotoList().get(0);//得到第一张图片
                } else {

                }
                String title=thumbList.getThoughts();
                int id=thumbList.getId();
                String url="http://ioutside.com/xiaoxiang-backend/footprint-share.html?footprintID="+id;
                setShareContent(title, url,photo);
            }
        });
    }

    private void setShareContent(String title, String url,String photo) {
        UMImage localImage = new UMImage(getActivity(), R.mipmap.head_ele);
        if (!TextUtils.isEmpty(photo)) {
           localImage = new UMImage(getActivity(),photo);

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
        if (resultCode==RecommendActivity.RESULT_CODE){
            int id=data.getExtras().getInt("id");
            int likeCount=data.getExtras().getInt("likeCount");
            int  commentCount=data.getExtras().getInt("commentCount");
            boolean observed=data.getExtras().getBoolean("observed");
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
            if (observed==true&&mAdapter.getDataSet().get(position).isObserved()==false){
                mAdapter.getDataSet().get(position).setObserved(true);
                mAdapter.notifyItemChanged(position);
            }

            if (observed==false&&mAdapter.getDataSet().get(position).isObserved()==true){
                mAdapter.getDataSet().get(position).setObserved(false);
                mAdapter.notifyItemChanged(position);
            }

            if (commentCount!=mAdapter.getDataSet().get(position).getCommentCount()){
                //文章详情评论成功，文章列表的评论量加1
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
                break;
            }
        }
        return position;
    }

    public void noLogin(){
        String[] items=new String[]{"登录","取消"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
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
