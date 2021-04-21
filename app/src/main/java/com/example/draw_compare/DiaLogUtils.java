package com.example.draw_compare;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class DiaLogUtils {

    public static Dialog showWaitDialog(Context context,String msg,boolean isTransBg,boolean isCancelable){
        LayoutInflater inflater = LayoutInflater.from(context);
        View loadingView=inflater.inflate(R.layout.loading_layout,null);
        ConstraintLayout constraintLayout=loadingView.findViewById(R.id.constrainlayout);

        //加载imageView
        ImageView loadingImage=loadingView.findViewById(R.id.imageView);
        TextView loadingText=loadingView.findViewById(R.id.textView);
        //加载动画
        Animation hyperspaceAnimation= AnimationUtils.loadAnimation(context,R.anim.rotate_loading);
        //使用imageView显示动画
        loadingImage.startAnimation(hyperspaceAnimation);
        //设置信息
        loadingText.setText(msg);

        Dialog loadingDialog=new Dialog(context,isTransBg ? R.style.TransDialogStyle:R.style.WhiteDialogStyle);
        loadingDialog.setContentView(constraintLayout);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(false);

        Window window=loadingDialog.getWindow();
        WindowManager.LayoutParams lp=window.getAttributes();
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;


    }
    public static Dialog showNormalDialog(Context context,String msg,boolean isCancelable){
        LayoutInflater inflater = LayoutInflater.from(context);
        View loadingView=inflater.inflate(R.layout.result_dialog_layout,null);
        ConstraintLayout constraintLayout=loadingView.findViewById(R.id.constrainlayout2);

        TextView loadingText=loadingView.findViewById(R.id.textView2);
        //设置信息
        loadingText.setText(msg);

        Dialog loadingDialog=new Dialog(context);
        loadingDialog.setContentView(constraintLayout);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(true);

        Window window=loadingDialog.getWindow();
        WindowManager.LayoutParams lp=window.getAttributes();
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;
    }
    public static void closeDialog(Dialog mDialogUtils) {

        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }
}
