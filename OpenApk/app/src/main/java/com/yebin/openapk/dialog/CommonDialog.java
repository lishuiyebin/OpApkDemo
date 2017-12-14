package com.yebin.openapk.dialog;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yebin.openapk.R;

/**
 * Created by PVer on 2017/12/13.
 */

public class CommonDialog extends BaseDialog {
    private TextView common_dialog_title_tv;
    private TextView common_dialog_msg_tv;
    private TextView common_dialog_confirm_tv;
    private TextView common_dialog_cancel_tv;

    public CommonDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected View getContentView() {
        View view = View.inflate(mContext, R.layout.dialog_common, null);
        LinearLayout common_dialog_root_ll = view.findViewById(R.id.common_dialog_root_ll);
        common_dialog_title_tv = view.findViewById(R.id.common_dialog_title_tv);
        common_dialog_msg_tv = view.findViewById(R.id.common_dialog_msg_tv);
        common_dialog_confirm_tv = view.findViewById(R.id.common_dialog_confirm_tv);
        common_dialog_cancel_tv = view.findViewById(R.id.common_dialog_cancel_tv);

        common_dialog_root_ll.getLayoutParams().width= (int) (Resources.getSystem().getDisplayMetrics().widthPixels*0.7f);

        common_dialog_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnConfirmClickListener!=null){
                    mOnConfirmClickListener.onConfirm();
                }
                dismiss();
            }
        });

        common_dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnCancelClickListener!=null){
                    mOnCancelClickListener.onCancel();
                }
                dismiss();
            }
        });

        return view;
    }

    public CommonDialog setTitle(String title) {
        common_dialog_title_tv.setText(title);
        return this;
    }

    public CommonDialog setMsg(String msg) {
        common_dialog_msg_tv.setText(msg);
        return this;
    }

    public CommonDialog setConfirmText(String text) {
        common_dialog_confirm_tv.setText(text);
        return this;
    }

    public CommonDialog setCancelText(String text) {
        common_dialog_cancel_tv.setText(text);
        return this;
    }

    public CommonDialog singleButton() {
        common_dialog_cancel_tv.setVisibility(View.GONE);
        return this;
    }

}
