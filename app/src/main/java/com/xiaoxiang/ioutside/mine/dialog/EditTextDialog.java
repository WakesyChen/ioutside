package com.xiaoxiang.ioutside.mine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/4/21.
 */
public class EditTextDialog extends Dialog implements View.OnClickListener {

    private EditText editText;
    private TextView tvTitle;
    private TextView tvCancel;
    private TextView tvConfirm;
    /**
     * 用以区分不同的编辑事件
     */
    private int id;


    private OnTextChangeListener mOnTextChangeListener;

    public interface OnTextChangeListener {
        /**
         * 在点击确定文本时调用
         * @param dialog 该对话框
         * @param id
         */
        void onSubmitText(EditTextDialog dialog, int id);

        /**
         * 在编辑文本之前调用，用户可以对话框进行设置，比如 title 和文本框的设置
         * @param dialog
         * @param id
         */
        void onPreEdit(EditTextDialog dialog, int id);
    }

    public EditTextDialog(Context context, @NonNull OnTextChangeListener l) {
        super(context);
        //去除标题占用的空间

        View title = getWindow().findViewById(android.R.id.title);

        if (title != null) {
            title.setVisibility(View.GONE);
        }
  //      getWindow().getDecorView().findViewById(android.R.id.title).setVisibility(View.GONE);
        mOnTextChangeListener = l;

        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_view, null);
        tvCancel = (TextView) editView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        tvConfirm = (TextView) editView.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(this);
        tvTitle = (TextView) editView.findViewById(R.id.tv_title);
        editText = (EditText) editView.findViewById(R.id.et_input_box);
        setContentView(editView);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_confirm) {

            mOnTextChangeListener.onSubmitText(this, id);

            if (TextUtils.isEmpty(editText.getError())) {
                dismiss();
            }

        } else if (v.getId() == R.id.tv_cancel) {

            dismiss();

        }
    }

    /**
     * 设置对话框的额标题
     * @param title 标题
     */
    public void setDialogTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    /**
     * 获得该对话框的 EditText 对象
     * @return
     */
    public EditText getEditText() {
        return editText;
    }

    /**
     * 显示对话框
     * @param id 区分不同的编辑事件
     */
    public void show(int id) {
        this.id = id;
        mOnTextChangeListener.onPreEdit(this, id);
        //使 EditTextDialog 一弹出就拉起输入法
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                //使 text 处于全选状态，方便用户编辑
                editText.selectAll();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 50);
        super.show();
    }

}
