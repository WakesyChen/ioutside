package com.xiaoxiang.ioutside.mine.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;

import com.xiaoxiang.ioutside.mine.dialog.ErrorMsgDialog;

/**
 * Created by 15119 on 2016/5/26.
 */

/**
 * 注册以及找回密码界面 fragment 的基类，继承这个类能够方便的显示错误信息弹窗，
 * 改变标题以及显示提醒用户等待的对话框，改变标题的前提是这个 fragment 附着在布局中
 * 带有自定义标题栏的 Activity 中，并且 Activity 的 setTitle() 方法应该重写如下：
 * TextView tvTitle; //布局中显示标题的 TextView
 * @Override
 * public void setTitle(CharSequence title) {
 *     super.setTitle(title);
 *     tvTitle(title);
 * }
 */
public abstract class BaseFragment extends Fragment {

    private ProgressDialog waitDialog;

    private final String  TAG = getClass().getSimpleName();

    /**
     * 这样用户返回上一个 fragment 的时候标题栏又会与当前的 fragment 保持一致
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (! hidden) {
            setupTitle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTitle();
    }

    /**
     * 设置自定义标题栏的标题
     */
    private void setupTitle() {
        getActivity().setTitle(getTitle());
    }

    /**
     * 显示提示用户等待的对话框
     * @param msg
     */
    protected void showWaitingDialog(String msg) {

        if (waitDialog == null) {
            waitDialog = new ProgressDialog(getActivity());
            waitDialog.setCancelable(false);
        }
        waitDialog.setMessage(msg);
        waitDialog.show();

    }

    /**
     * 隐藏提示等待的对话框
     */
    protected void hideWaitingDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.hide();
        }
    }

    /**
     * 显示错误信息
     * @param errorMsg 错误信息
     */
    protected void showErrorDialog(String errorMsg) {
        new ErrorMsgDialog(getActivity(), errorMsg).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }



    /**
     * 取回标题，留给子类实现
     * @return
     */
    protected abstract String getTitle();

}
