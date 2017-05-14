package com.xiaoxiang.ioutside.dynamic.activity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.xiaoxiang.ioutside.dynamic.adapter.FocusDetailAdapter;
import com.xiaoxiang.ioutside.dynamic.model.ThumbListDetail;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.homepage.activity.LightPlayActivity;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.model.CommentList;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GCommentList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GThumbListDetail;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Request;

public class FocusActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,Constants,OkHttpManager.ResultCallback.CommonErrorListener{
    private static final String TAG=FocusActivity.class.getSimpleName();
    public static final int RESULT_CODE=31;
    private RelativeLayout focus_relativeLayout;
    private SwipeRefreshLayout focus_swipeRefreshLayout;
    public RecyclerView focus_recycleView;
    public FocusDetailAdapter mAdapter;
    public EditText focus_footer_comment;
    public ImageView focus_function_comment;
    public TextView focus_footer_commentCount;
    public ImageView focus_function_zan;
    public TextView focus_footer_likedCount;
    public ImageView focus_function_collect;
    private int pageNo=1;
    private int pageSize=5;
    private int id;
    private String token;
    private String fileToken;
    private PopupWindow popupWindow;
    private CachedInfo cachedInfo;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        id=Integer.parseInt(intent.getStringExtra("id"));
        token=intent.getStringExtra("token");
        cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo!=null){
            fileToken=cachedInfo.getToken();
            Log.i(TAG, "fileToken=" + fileToken);
        }
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_focus);
        focus_relativeLayout=(RelativeLayout)findViewById(R.id.focus_relativeLayout);
        focus_footer_comment=(EditText)findViewById(R.id.focus_footer_comment);
        focus_function_comment=(ImageView)findViewById(R.id.focus_function_comment);
        focus_footer_commentCount=(TextView)findViewById(R.id.focus_footer_commentCount);
        focus_function_zan=(ImageView)findViewById(R.id.focus_function_zan);
        focus_footer_likedCount=(TextView)findViewById(R.id.focus_footer_likedCount);
        focus_function_collect=(ImageView)findViewById(R.id.focus_function_collect);
        focus_swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.focus_swipeRefreshLayout);
        focus_swipeRefreshLayout.setOnRefreshListener(this);
        focus_recycleView=(RecyclerView)findViewById(R.id.focus_recycleView);
        focus_recycleView.addOnScrollListener(listener);
        focus_recycleView.setItemAnimator(new NoAlphaItemAnimator());
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        focus_recycleView.setLayoutManager(layoutManager);
        mAdapter=new FocusDetailAdapter(this);
        focus_recycleView.setAdapter(mAdapter);
        if (token!=null){
            initData();
            onRefresh();
        }else if (token==null&fileToken==null){
            initData();
            onRefresh();
        }else {
            token=fileToken;
            initData();
            onRefresh();
        }
        initEvent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    private void initData() {
        OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
        ApiInterImpl api = new ApiInterImpl();
        mOkHttpManager.getStringAsyn(api.getDetail(id, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
            @Override
            public void onError(Request request, Exception e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Type objectType = new TypeToken<BaseResponse<GThumbListDetail>>() {
                }.getType();
                Gson gson = new Gson();
                BaseResponse ChoiceBaseResponse = gson.fromJson(response, objectType);
                GThumbListDetail gThumbListDetail = (GThumbListDetail) ChoiceBaseResponse.getData();//返回的是data类对象
                ThumbListDetail thumbListDetail = gThumbListDetail.getDetail();//返回的是下一层的list
                mAdapter.addThumbItem(thumbListDetail);
                if (thumbListDetail != null) {
                    int likeCount = mAdapter.getThumbListDetail().getLikedCount();
                    int commentCount = mAdapter.getThumbListDetail().getCommentCount();
                    focus_footer_commentCount.setText(commentCount + "");
                    focus_footer_likedCount.setText(likeCount + "");

                    boolean isLiked = thumbListDetail.isLiked();
                    if (isLiked == false)
                        focus_function_zan.setImageResource(R.drawable.zan_normal);
                    else
                        focus_function_zan.setImageResource(R.drawable.zan_pressed);

                }
            }
        });

    }

    @Override
    public void onRefresh() {
        int pageNo = 1;
        OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
        ApiInterImpl api = new ApiInterImpl();
        mOkHttpManager.getStringAsyn(api.getCommentListForFootprint(id, pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
            @Override
            public void onError(Request request, Exception e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "关注详情的数据得到了刷新");
                Type objectType = new TypeToken<BaseResponse<GCommentList>>() {
                }.getType();
                Gson gson = new Gson();
                BaseResponse CommentResponse = gson.fromJson(response, objectType);
                GCommentList gCommentList = (GCommentList) CommentResponse.getData();//返回的是data类对象
                List<CommentList> list = gCommentList.getList();//返回的是下一层的list
                if (list != null && list.size() != 0) {
                    mAdapter.addCommentItemToHead(pageSize, list);
                }
            }
        });
    }

    private RecyclerView.OnScrollListener listener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (focus_recycleView.getLayoutManager().getChildCount() > 0) {
                View lastChildView = focus_recycleView.getLayoutManager().getChildAt(focus_recycleView.getLayoutManager().getChildCount() - 1);
                int lastPosition = focus_recycleView.getLayoutManager().getPosition(lastChildView);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == focus_recycleView.getLayoutManager().getItemCount() - 1) {
                    pageNo++;
                    OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                    ApiInterImpl api = new ApiInterImpl();
                    mOkHttpManager.getStringAsyn(api.getCommentListForFootprint(id, pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Type objectType = new TypeToken<BaseResponse<GCommentList>>() {
                            }.getType();
                            Gson gson = new Gson();
                            BaseResponse CommentResponse = gson.fromJson(response, objectType);//一直提示错误，是网址的地址写错了
                            GCommentList gCommentList = (GCommentList) CommentResponse.getData();//返回的是data类对象
                            List<CommentList> list = gCommentList.getList();//返回的是下一层的list
                            if (list != null && list.size() != 0) {
                                for (CommentList commentList : list) {
                                    mAdapter.addCommentItem(commentList);
                                }
                            }
                            focus_swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        }
    };

    private void initEvent() {
        mAdapter.setOnItemClickListener(new FocusDetailAdapter.OnItemClickListener() {
            @Override
            public void onBackClick(View view, int position) {
                onBackPressed();
            }

            @Override
            public void onShareClick(View view, int position) {
                String photo=null;
                ThumbListDetail thumbListDetail=mAdapter.getThumbListDetail();
                String title=thumbListDetail.getThoughts();
                if (thumbListDetail.getPhotoList().size()>0) {
                    photo=thumbListDetail.getPhotoList().get(0);
                }
                String url="http://ioutside.com/xiaoxiang-backend/footprint-share.html?footprintID="+id;
                setShareContent(title, url,photo);
            }

            @Override
            public void onPlayClick(View view, int position) {
                Intent intent=new Intent(FocusActivity.this, LightPlayActivity.class);
                String video=mAdapter.getThumbListDetail().getVideo();
                intent.putExtra("url",video);
                startActivity(intent);
            }

            @Override
            public void onUserInfoClick(View view, int position) {
                int userId = mAdapter.getThumbListDetail().getUserID();
                boolean isObserved=mAdapter.getThumbListDetail().isObserved();
                Intent intent = new Intent();
                intent.setClass(FocusActivity.this, OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", isObserved);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onFocusClick(final View view, final int position) {
                int userId = mAdapter.getThumbListDetail().getUserID();
                boolean isObserved=mAdapter.getThumbListDetail().isObserved();
                ApiInterImpl api = new ApiInterImpl();
                OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                if (isObserved == false) {
                    mOkHttpManager.postAsyn(api.observeUser(userId, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(FocusActivity.this, "网络有点问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "关注失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.d(TAG, response);
                            mAdapter.getThumbListDetail().setObserved(true);
                            mAdapter.notifyItemChanged(0);
                        }
                    });
                } else {
                    mOkHttpManager.postAsyn(api.cancelObserveUser(userId, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(FocusActivity.this, "网络有点问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "取消关注失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.d(TAG, response);
                            mAdapter.getThumbListDetail().setObserved(false);
                            mAdapter.notifyItemChanged(0);
                        }
                    });
                }
            }

            @Override
            public void onCommentUserInfoClick(View view, int position) {
                int userId = mAdapter.getCommentList().get(position-1).getUserID();
                boolean isObserved=false;
                Intent intent = new Intent();
                intent.setClass(FocusActivity.this, OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", isObserved);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onCommentOtherUserNameClick(View view, int position) {
                int userId = mAdapter.getCommentList().get(position-1).getReceiverID();//不知道是不是
                boolean isObserved=false;
                Intent intent = new Intent();
                intent.setClass(FocusActivity.this, OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", isObserved);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onCommentItemClick(View view,final int position) {
                String userName=mAdapter.getCommentList().get(position-1).getUserName();
                String name=cachedInfo.getPersonalInfo().getName();
                if (userName.equals(name)){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(FocusActivity.this);
                    builder.setTitle("是否删除评论？");
                    builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int commentId = mAdapter.getCommentList().get(position - 1).getId();
                            ApiInterImpl api = new ApiInterImpl();
                            OkHttpManager okHttpManager = OkHttpManager.getInstance();
                            okHttpManager.getStringAsyn(api.deleteFootprintComment(commentId, id, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    super.onError(request, e);
                                    Toast.makeText(FocusActivity.this, "网络有点问题!", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onError");
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Toast.makeText(FocusActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                                    int commentCount=mAdapter.getThumbListDetail().getCommentCount();
                                    commentCount--;
                                    mAdapter.getThumbListDetail().setCommentCount(commentCount);
                                    focus_footer_commentCount.setText("" + commentCount);
                                    mAdapter.removeCommentItem(position - 1);
                                }
                            });
                        }
                    });
                    builder.show();
                }
                else {
                    //还剩下回复他人
                    int otherId=mAdapter.getCommentList().get(position-1).getId();
                    int otherUserId=mAdapter.getCommentList().get(position-1).getUserID();
                    showPopupComment(otherId,otherUserId);
                }
            }

            @Override
            public void onCommentDialogClick(View view, int position) {
                int userId=mAdapter.getCommentList().get(position-1).getUserID();//评论人id
                int receiverId=mAdapter.getCommentList().get(position-1).getReceiverID();//被评论人的id
                int referCommentId=mAdapter.getCommentList().get(position-1).getReferCommentID();//一级评论的id
                Intent intent=new Intent(FocusActivity.this,FootprintDialogActivity.class);
                intent.putExtra("id", id+"");
                intent.putExtra("userId", userId+"");
                intent.putExtra("receiverId",receiverId+"");
                intent.putExtra("referCommentId", referCommentId+"");
                intent.putExtra("token", token);
                startActivity(intent);

            }

        });


        focus_function_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterImpl api = new ApiInterImpl();
                OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                boolean isLiked = mAdapter.getThumbListDetail().isLiked();
                if (isLiked == false) {
                    mOkHttpManager.postAsyn(api.likeFootprint(id, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(FocusActivity.this, "网络有点问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "点赞失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, response);
                            focus_function_zan.setImageResource(R.drawable.zan_pressed);
                            int likeCount = mAdapter.getThumbListDetail().getLikedCount();
                            likeCount++;
                            mAdapter.getThumbListDetail().setLiked(true);
                            mAdapter.getThumbListDetail().setLikedCount(likeCount);
                            focus_footer_likedCount.setText(likeCount + "");
                        }
                    });
                } else {
                    mOkHttpManager.postAsyn(api.cancelLikeFootprint(id, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(FocusActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "取消点赞失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, response);
                            focus_function_zan.setImageResource(R.drawable.zan_normal);
                            int likeCount = mAdapter.getThumbListDetail().getLikedCount();
                            likeCount--;
                            mAdapter.getThumbListDetail().setLiked(false);
                            mAdapter.getThumbListDetail().setLikedCount(likeCount);
                            focus_footer_likedCount.setText(likeCount + "");
                        }
                    });
                }
            }
        });

        focus_function_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int userId = mAdapter.getThumbListDetail().getUserID();
                final AlertDialog.Builder builder = new AlertDialog.Builder(FocusActivity.this);
                final ApiInterImpl api=new ApiInterImpl();
                builder.setItems(new String[]{"收藏", "举报"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                boolean isCollected = mAdapter.getThumbListDetail().isCollected();
                                if (isCollected == false) {
                                    OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                                    mOkHttpManager.postAsyn(api.collectFootprint(id, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                            Toast.makeText(FocusActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "收藏失败");
                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            super.onResponse(response);
                                            Toast.makeText(FocusActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                            mAdapter.getThumbListDetail().setCollected(true);
                                            Log.i(TAG, response);
                                        }
                                    });
                                } else {
                                    Toast.makeText(FocusActivity.this, "已经收藏过了,请不要重复收藏!", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                //举报
                                String[] items1 = new String[]{"广告推销", "色情暴力", "其他"};
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(FocusActivity.this);
                                builder1.setTitle("举报类型");
                                builder1.setSingleChoiceItems(items1, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        index = which;
                                    }
                                });
                                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int postType = 2;//表示是足迹
                                        int reportUserID = userId;//用户的id
                                        int reportType;
                                        OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                                        switch (index) {
                                            case 0:
                                                reportType = 1;
                                                mOkHttpManager.postAsyn(api.addReport(id, postType, reportUserID, reportType, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                                                    @Override
                                                    public void onError(Request request, Exception e) {
                                                        Toast.makeText(FocusActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "举报失败");
                                                    }

                                                    @Override
                                                    public void onResponse(String response) {
                                                        super.onResponse(response);
                                                        Toast.makeText(FocusActivity.this, "举报动态广告推销成功", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, response);
                                                    }
                                                });
                                                break;
                                            case 1:
                                                reportType = 2;
                                                mOkHttpManager.postAsyn(api.addReport(id, postType, reportUserID, reportType, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                                                    @Override
                                                    public void onError(Request request, Exception e) {
                                                        Toast.makeText(FocusActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "举报失败");
                                                    }

                                                    @Override
                                                    public void onResponse(String response) {
                                                        super.onResponse(response);
                                                        Toast.makeText(FocusActivity.this, "举报动态色情暴力成功", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, response);
                                                    }
                                                });
                                                break;
                                            case 2:
                                                reportType = 3;
                                                mOkHttpManager.postAsyn(api.addReport(id, postType, reportUserID, reportType, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                                                    @Override
                                                    public void onError(Request request, Exception e) {
                                                        Toast.makeText(FocusActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "举报失败");
                                                    }

                                                    @Override
                                                    public void onResponse(String response) {
                                                        super.onResponse(response);
                                                        Toast.makeText(FocusActivity.this, "举报动态其他成功", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, response);
                                                    }
                                                });
                                                break;
                                            case 3:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                });
                                builder1.show();
                                break;
                        }
                    }
                });
                AlertDialog dialog=builder.create();
                Window window=dialog.getWindow();
                WindowManager.LayoutParams params=new WindowManager.LayoutParams();
                params.x=500;
                params.y=500;
                window.setAttributes(params);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });

        focus_footer_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(FocusActivity.this).inflate(R.layout.comment_popupwindow, null);
                final EditText inputComment = (EditText) view.findViewById(R.id.comment);
                ImageView submit_comment = (ImageView) view.findViewById(R.id.submit_comment);
                TextView quit_comment = (TextView) view.findViewById(R.id.quit_comment);
                popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());//不设置无法退出
                popupWindow.setOutsideTouchable(true);//设置点击窗口外边窗口消失
                popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                ColorDrawable cd = new ColorDrawable(0x000000);
                popupWindow.setBackgroundDrawable(cd);
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.4f;
                getWindow().setAttributes(params);
                popupWindow.update();
                popupInputMethodWindow();
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1f;
                        getWindow().setAttributes(params);
                    }
                });
                quit_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                submit_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiInterImpl api = new ApiInterImpl();
                        OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                        String comment = inputComment.getText().toString().trim();
                        if (TextUtils.isEmpty(comment)) {
                            Toast.makeText(FocusActivity.this, "评论不能为空!请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            //直接评论足迹
                            mOkHttpManager.postAsyn(api.addCommentForFootprint(id, comment, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Toast.makeText(FocusActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "评论失败");
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Toast.makeText(FocusActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, response);
                                    int commentCount=mAdapter.getThumbListDetail().getCommentCount();
                                    commentCount++;
                                    mAdapter.getThumbListDetail().setCommentCount(commentCount);
                                    focus_footer_commentCount.setText("" +commentCount);
                                    popupWindow.dismiss();
                                    onRefresh();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void setShareContent(String title, String url,String photo) {
        UMImage localImage = new UMImage(this, R.mipmap.head_ele);
        if (!TextUtils.isEmpty(photo)) {
           localImage = new UMImage(this, photo);

        }

        final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };

        new ShareAction(this).setDisplayList( displayList )
                .withText(title)
                .withTitle("来自爱户外的分享")
                .withTargetUrl(url)
                .withMedia(localImage)
                        //.setListenerList(umShareListener)
                .open();
    }

    private void showPopupComment(final int otherId, final int otherUserId) {
        View view = LayoutInflater.from(FocusActivity.this).inflate(R.layout.comment_popupwindow, null);
        final EditText inputComment=(EditText)view.findViewById(R.id.comment);
        ImageView submit_comment=(ImageView)view.findViewById(R.id.submit_comment);
        TextView quit_comment=(TextView)view.findViewById(R.id.quit_comment);
        popupWindow=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);//设置点击窗口外边窗口消失
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        popupWindow.update();
        popupInputMethodWindow();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        quit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向服务器发送post请求
                String comment = inputComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(FocusActivity.this, "评论不能为空!请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                    ApiInterImpl api = new ApiInterImpl();
                    mOkHttpManager.postAsyn(api.addCommentForFootprint(id,otherUserId,otherId, comment, token), new OkHttpManager.ResultCallback<String>(FocusActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(FocusActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "评论失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Toast.makeText(FocusActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            int commentCount=mAdapter.getThumbListDetail().getCommentCount();
                            commentCount++;
                            mAdapter.getThumbListDetail().setCommentCount(commentCount);
                            focus_footer_commentCount.setText("" + commentCount);
                            popupWindow.dismiss();
                            onRefresh();
                        }
                    });
                }
            }
        });
    }

    private void popupInputMethodWindow() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }.start();

    }

    @Override
    public void onBackPressed() {

        int likeCount=mAdapter.getThumbListDetail().getLikedCount();
        int commentCount=mAdapter.getThumbListDetail().getCommentCount();
        Intent mIntent=new Intent();
        mIntent.putExtra("id", id);
        mIntent.putExtra("likeCount", likeCount);
        mIntent.putExtra("commentCount", commentCount);
        setResult(RESULT_CODE, mIntent);
        finish();
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this, "你已在别的地方登录，你被迫下线，请重新登录！", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }
    }

}
