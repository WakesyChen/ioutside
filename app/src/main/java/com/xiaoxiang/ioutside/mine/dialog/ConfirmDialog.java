package com.xiaoxiang.ioutside.mine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/5/27.
 */

/**
 * 确认消息的弹框，
 * 弹框上半部分显示消息，
 * 下半部分是可点击的确认文本，
 * 点击确认文本后弹框消失
 */
public class ConfirmDialog extends Dialog {

    public ConfirmDialog(Context context, String msg, String confirmText) {

        super(context, R.style.DialogTheme);

        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);

        TextView tvMsg = (TextView) contentView.findViewById(R.id.tv_msg);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        tvMsg.setText(msg);
        tvConfirm.setText(confirmText);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(contentView);
    }
}
