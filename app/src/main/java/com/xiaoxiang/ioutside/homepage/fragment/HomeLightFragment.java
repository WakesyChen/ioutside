package com.xiaoxiang.ioutside.homepage.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.homepage.activity.ArticleCommentActivity;
import com.xiaoxiang.ioutside.homepage.activity.LightPlayActivity;
import com.xiaoxiang.ioutside.homepage.adapter.HomeLightAdapter;
import com.xiaoxiang.ioutside.homepage.model.Video;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GVideo;
import com.xiaoxiang.ioutside.util.ACache;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 10648 on 2016/4/17 0017.
 */
public class HomeLightFragment extends Fragment implements Constants, OkHttpManager.ResultCallback.CommonErrorListener {
    private final static String TAG = HomeLightFragment.class.getSimpleName();
    private final static int REQUEST_CODE=22;
    private View view;
    private RecyclerView recyclerView_light;
    private HomeLightAdapter mAdapter;
    private String token;
    private String fileToken;
    private ACache mCache;
    private List<Integer> ids=new ArrayList<>();
    public static final String shareUrl= "http://ioutside.com/xiaoxiang-backend/video-share.html?videoID=";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_light_fragment, container, false);
        token = getActivity().getIntent().getStringExtra("token");
        CachedInfo cachedInfo = MyApplication.getInstance().getCachedInfo();
        if (cachedInfo != null) {
            fileToken = cachedInfo.getToken();
            Log.i(TAG, "fileToken=" + fileToken);
        }
        mCache= ACache.get(getContext());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView_light = (RecyclerView) view.findViewById(R.id.recycler_light);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_light.setLayoutManager(mLayoutManager);
        recyclerView_light.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView_light.setHasFixedSize(true);
        mAdapter = new HomeLightAdapter();
        recyclerView_light.setAdapter(mAdapter);
        if (token != null) {
            initData();
        } else if (token == null && fileToken == null) {
            initData();
        } else {
            token = fileToken;
            initData();
        }
        initEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            int pageSize = 10;//每页显示文章数量
            int pageNo = 1;//第几页
            ApiInterImpl api=new ApiInterImpl();
            OkHttpManager okHttpManager=OkHttpManager.getInstance();
            okHttpManager.getStringAsyn(api.getVideoDetailList(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(HomeLightFragment.this) {
                @Override
                public void onError(Request request, Exception e) {
                    Log.d(TAG, "error");
                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    mCache.put("light", response, 1000 * 7200);
                    Type objectType = new TypeToken<BaseResponse<GVideo>>() {
                    }.getType();
                    Gson gson=new Gson();
                    BaseResponse lightResponse = gson.fromJson(response, objectType);
                    GVideo gVideo = (GVideo) lightResponse.getData();
                    List<Video> videoList = gVideo.getList();
                    if (videoList != null && videoList.size() != 0) {
                        for (int i = 0; i < videoList.size(); i++) {
                            Video video = videoList.get(i);
                            int id=video.getId();
                            ids.add(i,id);
                            mAdapter.addItemToHead(i, video);
                        }
                    }
                }
            });
        } else {
            String response = mCache.getAsString("light");
            if (!TextUtils.isEmpty(response) && response != null) {
                Type objectType = new TypeToken<BaseResponse<GVideo>>() {
                }.getType();
                Gson gson=new Gson();
                BaseResponse lightResponse = gson.fromJson(response, objectType);
                GVideo gVideo = (GVideo) lightResponse.getData();
                List<Video> videoList = gVideo.getList();
                if (videoList != null && videoList.size() != 0) {
                    for (int i = 0; i < videoList.size(); i++) {
                        Video video = videoList.get(i);
                        int id=video.getId();
                        ids.add(i,id);

                        mAdapter.addItemToHead(i, video);
                    }
                }
            }
        }
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new HomeLightAdapter.OnItemClickListener() {
            @Override
            public void onZanClick(View view, final int position) {
                boolean isLiked = mAdapter.getDataSet().get(position).isLiked();
                ApiInterImpl api=new ApiInterImpl();
                OkHttpManager okHttpManager=OkHttpManager.getInstance();
                if (token == null) {
                    noLogin();
                } else {
                    if (isLiked == false) {
                        OkHttpManager.Param tokenParam = new OkHttpManager.Param("token", token);
                        OkHttpManager.Param idParam = new OkHttpManager.Param("id", String.valueOf(mAdapter.getDataSet().get(position).getId()));
                        //同步数据的方法
                        okHttpManager.postAsyn(api.likeArticle(mAdapter.getDataSet().get(position).getId(), token), new OkHttpManager.ResultCallback<String>(HomeLightFragment.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Toast.makeText(getActivity(), "网络有问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "点赞失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                mAdapter.getDataSet().get(position).setLiked(true);
                                mAdapter.getDataSet().get(position).setLikedCount(mAdapter.getDataSet().get(position).getLikedCount() + 1);
                                mAdapter.notifyItemChanged(position);
                            }
                        }, new OkHttpManager.Param[]{idParam, tokenParam});
                    } else {
                        OkHttpManager.Param tokenParam = new OkHttpManager.Param("token", token);
                        OkHttpManager.Param idParam = new OkHttpManager.Param("id", String.valueOf(mAdapter.getDataSet().get(position).getId()));
                        okHttpManager.postAsyn(api.cancelLikeArticle(mAdapter.getDataSet().get(position).getId(), token), new OkHttpManager.ResultCallback<String>(HomeLightFragment.this) {
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
                                mAdapter.getDataSet().get(position).setLikedCount(mAdapter.getDataSet().get(position).getLikedCount() - 1);
                                mAdapter.notifyItemChanged(position);
                            }
                        }, new OkHttpManager.Param[]{idParam, tokenParam});
                    }
                }
            }

            @Override
            public void onCommentClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id", "" + mAdapter.getDataSet().get(position).getId());
                intent.putExtra("token", token);
                intent.putExtra("commentCount", "" + mAdapter.getDataSet().get(position).getCommentCount());
                intent.setClass(getActivity(), ArticleCommentActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), LightPlayActivity.class);
                intent.putExtra("url", mAdapter.getDataSet().get(position).getUrl());
                startActivity(intent);
            }

            @Override
            public void onShareClick(View view, int position) {
                String image=mAdapter.getDataSet().get(position).getPhoto();
                String content=mAdapter.getDataSet().get(position).getRecommendReason();
                String title = mAdapter.getDataSet().get(position).getTitle();
                int videoId=mAdapter.getDataSet().get(position).getId();
//                String url = mAdapter.getDataSet().get(position).getUrl();
                String url =shareUrl+videoId;
                setShareContent(image,content,title, url);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //处理删除评论之后的评论数量不一致
        if (resultCode==ArticleCommentActivity.RESULT_CODE){
            Bundle bundle=data.getExtras();
            int id=bundle.getInt("id");
            int commentCount=bundle.getInt("commentCount");
            int position=findPositionById(id);

            if (commentCount!=mAdapter.getDataSet().get(position).getCommentCount()){
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

    public void noLogin() {
        String[] items = new String[]{"登录", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    private void setShareContent(String image,String content,String title, String url) {
        UMImage umImage = new UMImage(getActivity(), image);

        final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };

        new ShareAction(getActivity()).setDisplayList( displayList )
                .withText(content)
                .withTitle(title)
                .withTargetUrl(url)
                .withMedia(umImage)
                .open();
//        http://ioutside.com/xiaoxiang-backend/video-share.html?videoID=3560
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(getActivity(), "你已在别的地方登录，你被迫下线，请重新登录！", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getActivity(), LoginActivity.class);
            startActivity(in);
        } else if (errorCode == TOKEN_OVERTIME) {
            Toast.makeText(getActivity(), "你的登录信息已过期，请重新登录", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getActivity(), LoginActivity.class);
            startActivity(in);
        } else if (errorCode == SERVER_ERROR) {
            Toast.makeText(getActivity(), "服务器内部错误，请重新登录", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getActivity(), LoginActivity.class);
            startActivity(in);
        }
    }
}
