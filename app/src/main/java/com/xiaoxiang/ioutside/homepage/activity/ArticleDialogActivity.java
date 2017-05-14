package com.xiaoxiang.ioutside.homepage.activity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.homepage.adapter.CommentDialogAdapter;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.model.CommentDialogList;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GCommentDialogList;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Request;

public class ArticleDialogActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,Constants,OkHttpManager.ResultCallback.CommonErrorListener {
    private static final String TAG=ArticleDialogActivity.class.getSimpleName();
    public static final int RESULT_CODE=22;
    private ImageView  comment_dialog_back;
    private SwipeRefreshLayout swipeRefresh_dialog;
    private RecyclerView recyclerView_dialog;
    private CommentDialogAdapter mAdapter;
    private int pageSize=5;
    private int pageNo=1;
    private String token;
    private String fileToken;
    private int id;
    private int userId;
    private int receiverId;
    private int referCommentId;
    private int commentCount;
    private CachedInfo cachedInfo;//为了得到登录人的userName
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        id=Integer.parseInt(intent.getStringExtra("id"));
        userId=Integer.parseInt(intent.getStringExtra("userId"));
        receiverId=Integer.parseInt(intent.getStringExtra("receiverId"));
        referCommentId=Integer.parseInt(intent.getStringExtra("referCommentId"));
        token=intent.getStringExtra("token");
        commentCount=Integer.parseInt(intent.getStringExtra("commentCount"));
        cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo != null) {
            fileToken = cachedInfo.getToken();
            Log.d(TAG, "fileToken=" + fileToken);
        }
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_article_dialog);
        comment_dialog_back=(ImageView)findViewById(R.id.comment_dialog_back);
        swipeRefresh_dialog=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh_dialog);
        swipeRefresh_dialog.setOnRefreshListener(this);
        recyclerView_dialog=(RecyclerView)findViewById(R.id.recyclerView_dialog);
        recyclerView_dialog.addOnScrollListener(listener);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView_dialog.setLayoutManager(manager);
        recyclerView_dialog.setHasFixedSize(true);
        recyclerView_dialog.setItemAnimator(new NoAlphaItemAnimator());
        mAdapter=new CommentDialogAdapter();
        recyclerView_dialog.setAdapter(mAdapter);
        if (token!=null){
            onRefresh();
        }
        else if (token==null&&fileToken==null){
            onRefresh();
        }
        else {
            token=fileToken;
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


    @Override
    public void onRefresh() {
        int pageNo = 1;
        OkHttpManager okHttpManager = OkHttpManager.getInstance();
        ApiInterImpl api = new ApiInterImpl();
        okHttpManager.getStringAsyn(api.getCommentDialogForArticle(pageNo,pageSize,id,receiverId,userId,referCommentId),new OkHttpManager.ResultCallback<String>(ArticleDialogActivity.this){
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.e(TAG, "onError");
                Toast.makeText(ArticleDialogActivity.this,"网络有点问题!",Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Type objectType=new TypeToken<BaseResponse<GCommentDialogList>>(){}.getType();
                Gson gson=new Gson();
                BaseResponse commentDialogResponse=gson.fromJson(response,objectType);
                GCommentDialogList gCommentDialogList=(GCommentDialogList)commentDialogResponse.getData();
                List<CommentDialogList> list=gCommentDialogList.getList();
                if (list!=null&&list.size()!=0){
                    mAdapter.addItemToHead(pageSize,list);
                }
                swipeRefresh_dialog.setRefreshing(false);
            }
        });
    }

    private RecyclerView.OnScrollListener listener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView_dialog.getLayoutManager().getChildCount() > 0) {
                View lastChildView = recyclerView_dialog.getLayoutManager().getChildAt(recyclerView_dialog.getLayoutManager().getChildCount() - 1);
                int lastPosition = recyclerView_dialog.getLayoutManager().getPosition(lastChildView);
                pageNo++;
                //进行判断
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == recyclerView_dialog.getLayoutManager().getItemCount() - 1) {
                    OkHttpManager okHttpManager = OkHttpManager.getInstance();
                    ApiInterImpl api = new ApiInterImpl();
                    okHttpManager.getStringAsyn(api.getCommentDialogForArticle(pageNo, pageSize, id, receiverId, userId, referCommentId), new OkHttpManager.ResultCallback<String>(ArticleDialogActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.i(TAG, "onError");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Type objectType = new TypeToken<BaseResponse<GCommentDialogList>>() {
                            }.getType();
                            Gson gson = new Gson();
                            BaseResponse commentDialogResponse = gson.fromJson(response, objectType);
                            GCommentDialogList gCommentList = (GCommentDialogList) commentDialogResponse.getData();
                            List<CommentDialogList> list = gCommentList.getList();
                            if (list != null && list.size() != 0) {
                                for (CommentDialogList commentDialogList : list) {
                                        mAdapter.addItem(commentDialogList);
                                }
                            }
                            swipeRefresh_dialog.setRefreshing(false);
                        }
                    });
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void initEvent() {
        comment_dialog_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdapter.setOnItemClickListener(new CommentDialogAdapter.OnItemClickListener() {
            @Override
            public void onUserInfoClick(View view, int position) {
                int userId = mAdapter.getDataSet().get(position).getUserID();
                boolean isObserved = false;
                Intent intent = new Intent(ArticleDialogActivity.this, OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", isObserved);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onItemClick(View view,final int position) {
                if (token==null){
                    noLogin();
                }else {
                    String userName = mAdapter.getDataSet().get(position).getUserName();
                    String name = cachedInfo.getPersonalInfo().getName();
                    if (userName.equals(name)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleDialogActivity.this);
                        builder.setTitle("是否删除评论？");
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int commentId = mAdapter.getDataSet().get(position).getId();
                                OkHttpManager okHttpManager = OkHttpManager.getInstance();
                                ApiInterImpl api = new ApiInterImpl();
                                okHttpManager.getStringAsyn(api.deleteArticleComment(commentId, id, token), new OkHttpManager.ResultCallback<String>(ArticleDialogActivity.this) {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        super.onError(request, e);
                                        Toast.makeText(ArticleDialogActivity.this, "网络有点问题!", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "onError");
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        super.onResponse(response);
                                        mAdapter.removeItem(position);
                                        commentCount--;
                                        Toast.makeText(ArticleDialogActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        builder.show();
                    } else {
                        int otherId = mAdapter.getDataSet().get(position).getId();
                        int otherUserId = mAdapter.getDataSet().get(position).getUserID();
                        showPopupComment(otherId, otherUserId);
                    }
                }
            }

            @Override
            public void onOtherUserNameClick(View view, int position) {
                int userId = mAdapter.getDataSet().get(position).getReceiverID();
                boolean isObserved = false;
                Intent intent = new Intent(ArticleDialogActivity.this, OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", isObserved);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

    }

    private void noLogin() {
        String[] items=new String[]{"登录","取消"};
        AlertDialog.Builder builder=new AlertDialog.Builder(ArticleDialogActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent intent=new Intent(ArticleDialogActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    private void showPopupComment(final int referCommentID, final int receiverID){
        View view = LayoutInflater.from(ArticleDialogActivity.this).inflate(R.layout.comment_popupwindow, null);
        final EditText inputComment=(EditText)view.findViewById(R.id.comment);

        ImageView submit_comment=(ImageView) view.findViewById(R.id.submit_comment);
        TextView quit_comment= (TextView) view.findViewById(R.id.quit_comment);
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
                    Toast.makeText(ArticleDialogActivity.this, "评论不能为空!请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                    ApiInterImpl api = new ApiInterImpl();
                    mOkHttpManager.postAsyn(api.addCommentForArticle(id,receiverID,referCommentID, comment, token), new OkHttpManager.ResultCallback<String>(ArticleDialogActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(ArticleDialogActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "评论失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Toast.makeText(ArticleDialogActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            commentCount++;
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
        Bundle bundle=new Bundle();
        bundle.putInt("commentCount",commentCount);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_CODE,intent);
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
