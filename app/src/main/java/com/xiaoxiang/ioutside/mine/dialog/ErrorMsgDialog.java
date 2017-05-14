package com.xiaoxiang.ioutside.mine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/5/24.
 */
public class ErrorMsgDialog extends Dialog {
    public ErrorMsgDialog(Context context, String errorMsg) {
        super(context, R.style.DialogTheme);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_error_login, null);
        TextView msgText = (TextView) contentView.findViewById(R.id.error_text);
        msgText.setText(errorMsg);
        setContentView(contentView);
    }
}
