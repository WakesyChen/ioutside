package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.fragment.EmailVerifyResultFragment;
import com.xiaoxiang.ioutside.mine.fragment.PhoneVerifyFragment;
import com.xiaoxiang.ioutside.mine.fragment.RetrievePwdFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgetPwActivity extends Activity {

    public static final String TAG_FRAGMENT_EMAIL_VERIFY_RESULT = "tag_fragment_email_verify_result";
    public static final String TAG_FRAGMENT_PHONE_VERIFY = "tag_fragment_phone_verify";
    public static final String TAG_FRAGMENT_RETRIEVE_PWD = "tag_fragment_retrieve_pwd";

    private static final String TAG = "ForgetPWActivity";

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_pwd);
        ButterKnife.bind(this);

        tvTitle.setTextColor(Color.parseColor("#eeeeee"));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    public static Fragment getFragment(FragmentManager fm, String tag) {

        Fragment f = fm.findFragmentByTag(tag);

        if (f == null) {
            Log.d(TAG, "getFragment() --> " + "fragment is null");
            switch (tag) {
                case ForgetPwActivity.TAG_FRAGMENT_EMAIL_VERIFY_RESULT:
                    f = new EmailVerifyResultFragment();
                    break;
                case ForgetPwActivity.TAG_FRAGMENT_PHONE_VERIFY:
                    f = new PhoneVerifyFragment();
                    break;
                case ForgetPwActivity.TAG_FRAGMENT_RETRIEVE_PWD:
                    f = new RetrievePwdFragment();
            }
        }

        return f;
    }

}

