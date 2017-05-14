package com.xiaoxiang.ioutside.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15119 on 2016/5/25.
 */
public class EmailVerifyResultFragment extends BaseFragment {

    private String email;

    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.btn_finish)
    Button btnFinish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getArguments().getString("email");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_email_verify, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        ButterKnife.bind(this, v);
        tvEmail.setText(email);
    }

    @OnClick({R.id.btn_finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected String getTitle() {
        return "邮箱验证";
    }
}
