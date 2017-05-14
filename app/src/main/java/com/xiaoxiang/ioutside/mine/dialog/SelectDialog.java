package com.xiaoxiang.ioutside.mine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

/**
 * Created by 15119 on 2016/6/4.
 */
public class SelectDialog extends Dialog implements View.OnClickListener {

    private OnItemSelectListener onItemSelectListener;

    public static final int ITEM_MALE = 0;
    public static final int ITEM_FEMALE = 1;

    public interface OnItemSelectListener {
        void onItemSelect(int which);
    }

    private TextView tvMale;
    private TextView tvFemale;

    public SelectDialog(Context context, @NonNull OnItemSelectListener l) {
        super(context);
        onItemSelectListener = l;

        //小米 mimax 手机没有 title
        View title = getWindow().findViewById(android.R.id.title);
        if (title != null) {
            title.setVisibility(View.GONE);
        }
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_select, null);
        tvFemale = (TextView) contentView.findViewById(R.id.tv_sex_female);
        tvFemale.setOnClickListener(this);
        tvMale = (TextView) contentView.findViewById(R.id.tv_sex_male);
        tvMale.setOnClickListener(this);
        setContentView(contentView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_sex_male) {
            onItemSelectListener.onItemSelect(ITEM_MALE);
            dismiss();
        } else if (v.getId() == R.id.tv_sex_female) {
            onItemSelectListener.onItemSelect(ITEM_FEMALE);
            dismiss();
        }
    }

}
