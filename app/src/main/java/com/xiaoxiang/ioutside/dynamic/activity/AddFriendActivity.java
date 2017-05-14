package com.xiaoxiang.ioutside.dynamic.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.xiaoxiang.ioutside.dynamic.adapter.AddFriendAdapter;
import com.xiaoxiang.ioutside.dynamic.model.FriendList;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GFriendList;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class AddFriendActivity extends AppCompatActivity implements Constants, OkHttpManager.ResultCallback.CommonErrorListener {
    private static final String TAG = "AddFriendActivity";
    private RelativeLayout friend_relativeLayout2;
    private RelativeLayout friend_relativeLayout3;
    private RelativeLayout friend_relativeLayout4;
    private RelativeLayout friend_relativeLayout5;
    private SwipeRefreshLayout swipeRefresh_add;
    private RecyclerView recyclerView_add;
    private ImageView friend_back;
    private AddFriendAdapter mAdapter;
    private String token;
    private String fileToken;
    private int pageSize = 5;
    private int pageNo = 0;
    private int footprintNum = 3;
    private boolean isObserved = false;
    private List<Integer> zeroList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getStringExtra("token");
        CachedInfo cachedInfo = MyApplication.getInstance().getCachedInfo();
        if (cachedInfo != null) {
            fileToken = cachedInfo.getToken();
            Log.i(TAG, "fileToken=" + fileToken);
        }
        initView();
    }


    private void initView() {
        setContentView(R.layout.activity_add_friend);
        friend_back = (ImageView) findViewById(R.id.friend_back);
        friend_relativeLayout2 = (RelativeLayout) findViewById(R.id.friend_relativeLayout2);//龙虎榜
        friend_relativeLayout3 = (RelativeLayout) findViewById(R.id.friend_relativeLayout3);
        friend_relativeLayout4 = (RelativeLayout) findViewById(R.id.friend_relativeLayout4);
        friend_relativeLayout5 = (RelativeLayout) findViewById(R.id.friend_relativeLayout5);
        swipeRefresh_add = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_add);
        recyclerView_add = (RecyclerView) findViewById(R.id.recyclerView_add);
        recyclerView_add.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_add.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView_add.setHasFixedSize(true);
        mAdapter = new AddFriendAdapter();
        recyclerView_add.setAdapter(mAdapter);
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

    private void initData() {
        if (NetworkUtil.isNetworkConnected(this)) {
            final ApiInterImpl api = new ApiInterImpl();
            final OkHttpManager okHttpManager = OkHttpManager.getInstance();
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "开始刷新");
                    pageNo=1;
                    okHttpManager.getStringAsyn(api.getRecommendUserFootprint(pageSize, pageNo, footprintNum, token), new OkHttpManager.ResultCallback<String>(AddFriendActivity.this) {
                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Type objectType = new TypeToken<BaseResponse<GFriendList>>() {
                            }.getType();
                            Log.i(TAG, "onResponse: response:" + response);
                            Gson gson = new Gson();
                            BaseResponse addResponse = gson.fromJson(response, objectType);
                            GFriendList gFriendList = (GFriendList) addResponse.getData();
                            List<FriendList> list = gFriendList.getList();
                            //每次刷新前先清除所有数据，然后再加载
                            mAdapter.getDataSet().clear();
                            if (list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    FriendList friendList = list.get(i);
                                    zeroList.add(0);
                                    if (!mAdapter.getDataSet().contains(friendList)) {
                                        mAdapter.addItemToHead(i, friendList);
                                    }
                                }
                            }
                            Log.i(TAG, "friendlist.size():"+mAdapter.getDataSet().size());
                            Log.i("ApiUrls", "getRecommendUserFootprint: "+"http://"+"www.ioutside.com"+"/xiaoxiang-backend/footprint/get-recommend-user-footprint?pageSize="+pageSize+"&pageNo="
                                    +pageNo+"&footprintNum="+footprintNum+"&token="+token);
                            swipeRefresh_add.setRefreshing(false);
                        }


                        @Override
                        public void onError(Request request, Exception e) {
                            // super.onError(request, e);
                            Log.d(TAG, "onError");
                            Toast.makeText(AddFriendActivity.this, "网络有点问题!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };

            swipeRefresh_add.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh_add.setRefreshing(true);
                }
            });

            listener.onRefresh();
            swipeRefresh_add.setOnRefreshListener(listener);
            //上拉刷新
            recyclerView_add.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recyclerView_add.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recyclerView_add.getLayoutManager().getChildAt(recyclerView_add.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recyclerView_add.getLayoutManager().getPosition(lastChildView);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == recyclerView_add.getLayoutManager().getItemCount() - 1) {
                            pageNo++;
                            okHttpManager.getStringAsyn(api.getRecommendUserFootprint(pageSize, pageNo, footprintNum, token), new OkHttpManager.ResultCallback<String>(AddFriendActivity.this) {
                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);

                                    Type objectType = new TypeToken<BaseResponse<GFriendList>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    BaseResponse addResponse = gson.fromJson(response, objectType);
                                    GFriendList gFriendList = (GFriendList) addResponse.getData();
                                    List<FriendList> list = gFriendList.getList();
                                    if (list.size() != 0) {
                                        for (FriendList friendList : list) {
                                            zeroList.add(0);
                                            if (!mAdapter.getDataSet().contains(friendList)) {
                                                mAdapter.addItem(friendList);
                                            }
                                        }
                                    }
                                    Log.i(TAG, "friendlist.size()2:"+mAdapter.getDataSet().size());

                                    swipeRefresh_add.setRefreshing(false);
                                }

                                @Override
                                public void onError(Request request, Exception e) {
                                    // super.onError(request, e);
                                    Log.d(TAG, "onError");
                                    Toast.makeText(AddFriendActivity.this, "网络有点问题!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        } else {
            swipeRefresh_add.setVisibility(View.GONE);
            recyclerView_add.setVisibility(View.GONE);
            Toast.makeText(this, "网络有点问题!", Toast.LENGTH_LONG).show();
        }
    }

    private void initEvent() {
        friend_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        friend_relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(AddFriendActivity.this,LHBActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });

        friend_relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //邀请微博好友
            }
        });

        friend_relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "一起来玩吧";//要分享的标题
                String url = "http://android.myapp.com/myapp/detail.htm?apkName=com.xiaoxiang.ioutside";
                setShareContent(title, url);//分享
            }
        });

        friend_relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //邀请微信好友
                String title = "一起来玩吧";
                String url = "http://android.myapp.com/myapp/detail.htm?apkName=com.xiaoxiang.ioutside";
                setShareContentWeChat(title, url);
            }
        });

        mAdapter.setOnItemClickListener(new AddFriendAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(View view, int position) {
                Intent intent = new Intent();
                int userId = mAdapter.getDataSet().get(position).getUserId();
                intent.setClass(AddFriendActivity.this, OtherPersonActivity.class);
                intent.putExtra("observed", false);
                intent.putExtra("userID", userId);
                intent.putExtra("token", token);
                startActivity(intent);

            }

            @Override
            public void onFocusClick(final View view, final int position) {
                if (token == null) {
                    noLogin();
                } else {
                    int userId = mAdapter.getDataSet().get(position).getUserId();
                    ApiInterImpl api = new ApiInterImpl();
                    OkHttpManager okHttpManager = OkHttpManager.getInstance();
                    if (isObserved == false && zeroList.get(position) == 0) {
                        okHttpManager.getStringAsyn(api.observeUser(userId, token), new OkHttpManager.ResultCallback<String>(AddFriendActivity.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                //super.onError(request, e);
                                Toast.makeText(AddFriendActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "关注失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Toast.makeText(AddFriendActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, response);
                                ImageView imageView = (ImageView) view.findViewById(R.id.friend_focus);
                                imageView.setImageResource(R.drawable.has_focus);
                                zeroList.set(position, 1);
                            }
                        });
                    } else {
                        Toast.makeText(AddFriendActivity.this, "已经关注过了，请不要重复关注!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onItemClick(View view, int position) {
                //Intent intent=new Intent();
                // intent.putExtra("token",token);
                // intent.putExtra("id",);
            }
        });
    }

    private void setShareContentWeChat(String title, String url) {
        UMImage localImage = new UMImage(this, R.mipmap.head_ele);

        final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
        };

        new ShareAction(this).setDisplayList(displayList)
                .withText(title)
                .withTitle("来自爱户外的分享")
                .withTargetUrl(url)
                .withMedia(localImage)
                //.setListenerList(umShareListener)
                .open();
    }

    public void noLogin() {
        String[] items = new String[]{"登录", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    private void setShareContent(String title, String url) {

        UMImage localImage = new UMImage(this, R.mipmap.head_ele);

        final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
        };

        new ShareAction(this).setDisplayList(displayList)
                .withText(title)
                .withTitle("来自爱户外的分享")
                .withTargetUrl(url)
                .withMedia(localImage)
                //.setListenerList(umShareListener)
                .open();

    }


    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this, "你已在别的地方登录，你被迫下线，请重新登录！", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
        } else if (errorCode == TOKEN_OVERTIME) {
            Toast.makeText(this, "你的登录信息已过期，请重新登录", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
        } else if (errorCode == SERVER_ERROR) {
            Toast.makeText(this, "服务器内部错误，请重新登录", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
        }
    }

}
