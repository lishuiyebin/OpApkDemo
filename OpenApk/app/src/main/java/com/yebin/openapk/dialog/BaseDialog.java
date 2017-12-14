package com.yebin.openapk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yebin.openapk.R;

/**
 * Created by PVer on 2017/12/13.
 */

public abstract class BaseDialog{
    private Dialog mDialog;
    protected Activity mContext;
    protected OnConfirmClickListener mOnConfirmClickListener;
    protected OnCancelClickListener mOnCancelClickListener;

    BaseDialog(Activity activity){
        this(activity,false);
    }

    BaseDialog(Activity activity, boolean isBottom){
        this.mContext=activity;
        builder(isBottom);
    }

    private void builder(boolean bottom) {
        mDialog=new Dialog(mContext,R.style.CommonDialogStyle);
        Window window = mDialog.getWindow();
        if(window!=null){
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width= Resources.getSystem().getDisplayMetrics().widthPixels;
            params.height= Resources.getSystem().getDisplayMetrics().heightPixels;
            if(bottom){
                window.setGravity(Gravity.BOTTOM);
                params.width= Resources.getSystem().getDisplayMetrics().widthPixels;
            }else {
                window.setGravity(Gravity.CENTER);
            }
            params.x=0;
            params.y=0;
            window.setAttributes(params);
        }
        window.setContentView(getContentView());
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void show() {
        if (mDialog != null && !mDialog.isShowing() && !mContext.isFinishing() && !mContext.isDestroyed()) {
            mDialog.show();
        }
    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    protected BaseDialog showSoftInput() {
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        return this;
    }

    public BaseDialog setCancelable(boolean cancel) {
        if (mDialog != null) {
            mDialog.setCancelable(cancel);
        }
        return this;
    }

    public BaseDialog setCanceledOnTouchOutside(boolean cancel) {
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(cancel);
        }
        return this;
    }

    public BaseDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        if (mDialog != null) {
            mDialog.setOnDismissListener(onDismissListener);
        }
        return this;
    }


    public BaseDialog setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.mOnConfirmClickListener = onConfirmClickListener;
        return this;
    }

    public BaseDialog setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.mOnCancelClickListener = onCancelClickListener;
        return this;
    }


    protected abstract View getContentView();

    public interface OnConfirmClickListener {
        void onConfirm();
    }

    public interface OnCancelClickListener {
        void onCancel();
    }
}
