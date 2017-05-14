package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.homepage.activity.SubjectDetailActivity;
import com.xiaoxiang.ioutside.mine.model.ChildSubjectTypeList;
import com.xiaoxiang.ioutside.mine.model.Subjects;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 15119 on 2016/8/8.
 */
public class ExpandableMoreSubjectsActivity extends Activity
        implements ExpandableListView.OnChildClickListener {

    @Bind(R.id.elv_subjects)
    ExpandableListView elvSubjects;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    private Subjects.DataBean.ListBean mFocusedSubject;

    private ExpandableListViewAdapter mAdapter;

    private List<ChildSubjectTypeList.DataBean.ListBean> mChildSubjectTypeList;

    private String mUrl;

    private Map<String, List<Subjects.DataBean.ListBean>> mListDataChildren;

    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_more_subjects);
        ButterKnife.bind(this);

        initData();
        initView();
        loadData();
    }

    private void initData() {
        String title = getIntent().getStringExtra("title");
        mToken = getIntent().getStringExtra("token");
        tvTitle.setText(title);
        mUrl = getIntent().getStringExtra("url");
        mChildSubjectTypeList = new ArrayList<>();
        mListDataChildren = new HashMap<>();
        mAdapter = new ExpandableListViewAdapter();
    }

    private void initView() {
        elvSubjects.setAdapter(mAdapter);
        elvSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void loadData() {
        loadChildSubjectTypeList();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mFocusedSubject = getItem(groupPosition, childPosition);
        viewSubjectDetail(mFocusedSubject);
        return true;
    }

    private final int REQUEST_SUBJECT_DETAIL = 0x01;

    private void viewSubjectDetail(Subjects.DataBean.ListBean subject) {
        Intent viewSubjectDetail = new Intent(this, SubjectDetailActivity.class);
        viewSubjectDetail.putExtra("subjectID", subject.getId());
        viewSubjectDetail.putExtra("token", mToken);
        startActivityForResult(viewSubjectDetail, REQUEST_SUBJECT_DETAIL);
    }

    private String TAG = "ExpandableMoreSubjectsActivity";

    private void loadChildSubjectTypeList() {

        final String url = mUrl;

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, response);
                Gson gson = new Gson();
                ChildSubjectTypeList childSubjectTypeList =
                        gson.fromJson(response, ChildSubjectTypeList.class);

                if (!childSubjectTypeList.isSuccess()) {
                    return;
                }

                List<ChildSubjectTypeList.DataBean.ListBean> childSubjectTypes =
                        childSubjectTypeList.getData().getList();

                if (childSubjectTypes == null) return;

                mChildSubjectTypeList = childSubjectTypes;

                loadListDataChildren();

            }

            @Override
            public void onError(Request request) {

            }
        };

        Request request = new Request.Builder()
                .url(url)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private Subjects.DataBean.ListBean getItem(int groupPosition, int childPosition) {
        return mListDataChildren.get(mChildSubjectTypeList.get(groupPosition)
                .getTypeName()).get(childPosition);
    }

    private void loadListDataChildren() {
        for (ChildSubjectTypeList.DataBean.ListBean list : mChildSubjectTypeList) {
            loadListDataChild(list.getId(), list.getTypeName());
        }
    }

    private void loadListDataChild(final int typeId, final String typeName) {

        String url = "http://ioutside.com/xiaoxiang-backend/article/get-subject-list-by-child-type?type="
                + typeId;

        if (mToken != null) {
            url += "&token=" + mToken;
        }

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "loadListDataChild --> " + response);
                Gson gson = new Gson();
                Subjects subjects = gson.fromJson(response, Subjects.class);

                if (!subjects.isSuccess()) {
                    return;
                }

                List<Subjects.DataBean.ListBean> subjectList = subjects.getData().getList();

                if (subjectList == null) return;

                mListDataChildren.put(typeName, subjectList);

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Request request) {
            }

        };

        Request request = new Request.Builder()
                .url(url)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    class ExpandableListViewAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mChildSubjectTypeList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            List<Subjects.DataBean.ListBean> subjects =
                    mListDataChildren.get(mChildSubjectTypeList.get(groupPosition).getTypeName());
            return subjects == null ? 0 : subjects.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mChildSubjectTypeList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return getItem(groupPosition, childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            GroupViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_view,
                        parent, false);
                viewHolder = new GroupViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GroupViewHolder) convertView.getTag();
            }

            bindGroupViewHolder(groupPosition, viewHolder);

            return convertView;
        }

        private void bindGroupViewHolder(int groupPosition, GroupViewHolder holder) {
            ChildSubjectTypeList.DataBean.ListBean childSubjectType = mChildSubjectTypeList.get(groupPosition);
            holder.tvGroupName.setText(childSubjectType.getTypeName());
            holder.position = groupPosition;
            holder.tvCount.setText(getString(R.string.tv_child_subjects_num, getChildrenCount(groupPosition)));
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ChildViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(ExpandableMoreSubjectsActivity.this)
                        .inflate(R.layout.item_subject, parent, false);
                viewHolder = new ChildViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ChildViewHolder) convertView.getTag();
            }

            bindChildViewHolder(groupPosition, childPosition, viewHolder);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void bindChildViewHolder(int groupPosition, int childPosition, ChildViewHolder holder) {

            Subjects.DataBean.ListBean subject = getItem(groupPosition, childPosition);
            holder.groupPosition = groupPosition;
            holder.childPosition = childPosition;
            holder.tvName.setText(subject.getTitle());
            holder.introduction.setText(subject.getIntroduction());
            holder.ivEnter.setVisibility(View.GONE);
            Glide.with(holder.itemView.getContext()).load(subject.getPhoto()).into(holder.ivPhoto);
            holder.ivSubscribe.setVisibility(View.VISIBLE);
            holder.ivSubscribe.setSelected(subject.isObserved());
        }
    }

    class GroupViewHolder {

        public TextView tvGroupName;
        public TextView tvCount;

        private int position;

        public GroupViewHolder(View item) {
            tvGroupName =  (TextView) item.findViewById(R.id.tv_group_name);
            tvCount = (TextView) item.findViewById(R.id.tv_count);
        }

    }

    private void subscribe(final Subjects.DataBean.ListBean subject) {

        if (mToken == null) {
            ToastUtils.show("请先登录");
        }

        ApiInterImpl api = new ApiInterImpl();

        String url;
        if (subject.isObserved()) {
            url = api.getUnObserSubIn(subject.getId(), mToken);
        } else {
            url = api.getObserSubIn(subject.getId(), mToken);
        }

        Log.d(TAG, "subscribe url --> " + url);

        HttpUtil.Callback callback = new HttpUtil.Callback() {

            @Override
            public void onSuccess(String response) {

                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, new TypeToken<BaseResponse>() {
                }.getType());
                if (!baseResponse.isSuccess()) return;

                if (subject.isObserved()) {
                    subject.setObserved(false);
                    mAdapter.notifyDataSetChanged();
                } else {
                    subject.setObserved(true);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Request request) {
                if (subject.isObserved()) {
                    ToastUtils.show("取消订阅失败，请检查网络");
                } else {
                    ToastUtils.show("订阅失败，请检查网络");
                }

            }
        };

        Request request = new Request.Builder()
                .url(url)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    public class ChildViewHolder implements View.OnClickListener {

        public TextView tvName;
        public ImageView ivPhoto;
        public ImageView ivSubscribe;
        public ImageView ivEnter;

        public TextView introduction;

        public int groupPosition;
        public int childPosition;

        public View itemView;

        public ChildViewHolder(View view) {

            itemView = view;

            view.findViewById(R.id.iv_divider).setVisibility(View.GONE);
            tvName = (TextView) view.findViewById(R.id.tv_title_mysub);
            ivSubscribe = (ImageView) view.findViewById(R.id.iv_subscribe);
            ivEnter = (ImageView) view.findViewById(R.id.iv_enter);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
            introduction = (TextView) view.findViewById(R.id.tv_intro_sub);

            ivSubscribe.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mFocusedSubject = getItem(groupPosition, childPosition);
            switch (v.getId()) {
                case R.id.item_subject:
                    viewSubjectDetail(mFocusedSubject);
                    break;
                case R.id.iv_subscribe:
                    subscribe(mFocusedSubject);
                    break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFocusedSubject.setObserved(data.getBooleanExtra("subscribeState", false));
        mAdapter.notifyDataSetChanged();
    }
}
