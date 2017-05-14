package com.xiaoxiang.ioutside.mine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/6/5.
 */
public class MultiEditTextDialog extends Dialog implements View.OnClickListener{

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private TextView tvConfirm;
    private TextView tvCancel;

    private int id;

    private OnTextChangeListener onTextChangeListener;

    public interface OnTextChangeListener {
        /**
         * 在点击确定文本时调用
         * @param dialog 该对话框
         * @param id
         */
        void onSubmitText(MultiEditTextDialog dialog, int id);

        /**
         * 在编辑文本之前调用，用户可以对话框进行设置，比如 title 和文本框的设置
         * @param dialog
         * @param id
         */
        void onPreEdit(MultiEditTextDialog dialog, int id);
    }

    public MultiEditTextDialog(Context context, @NonNull OnTextChangeListener l) {

        super(context);
        onTextChangeListener = l;
        View title  = getWindow().getDecorView().findViewById(android.R.id.title);

        if (title != null) {
            title.setVisibility(View.GONE);
        }

        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_multi_edit_text,
                (ViewGroup)getWindow().getDecorView(), false);

        editText1 = (EditText) contentView.findViewById(R.id.et_input_box_1);
        editText2 = (EditText) contentView.findViewById(R.id.et_input_box_2);
        editText3 = (EditText) contentView.findViewById(R.id.et_input_box_3);

        tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        setContentView(contentView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            dismiss();
        } else if (v.getId() == R.id.tv_confirm) {
            onTextChangeListener.onSubmitText(this, id);
            dismiss();
        }
    }

    public EditText[] getEditTexts() {
        return new EditText[]{editText1, editText2, editText3};
    }

    public void show(int id) {
        this.id = id;
        onTextChangeListener.onPreEdit(this, id);
        super.show();
    }
}
