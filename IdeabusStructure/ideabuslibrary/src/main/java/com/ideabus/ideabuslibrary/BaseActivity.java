package com.ideabus.ideabuslibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

/**
 * Created by Ting on 16/3/17.
 */
public abstract class BaseActivity extends FragmentActivity {
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        BaseGlobal.density = metrics.density;
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        BaseGlobal.screenWidth = width;
        BaseGlobal.screenHeight = height;

        //計算平板是16:9還是4:3的螢幕,如果是橫式,寬高對調後再計算
        if(width > height){
            width = height;
            height = BaseGlobal.screenWidth;
            BaseGlobal.screenScale = (float) height / (float) width;
            //如果橫式然後是4:3會導致版面空隙太大,所以size要依比例放大
            BaseGlobal.isTabletScaleSize = BaseGlobal.screenScale <= 1.4f;
        }else{
            BaseGlobal.screenScale = (float) height / (float) width;
            //如果直式然後是16:9會導致版面空隙太大,所以size要依比例放大
            BaseGlobal.isTabletScaleSize = BaseGlobal.screenScale >= 1.4f;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        cancelAlert();
    }
    protected abstract void initFragment();

    /**
     * findView初始化元件
     */
    protected abstract void initView();

    /**
     * 初始化參數
     */
    protected abstract void initParam();

    /**
     * 處理觸控事件
     */
    protected abstract void initListener();

    public void showBaseAlert(int titleRid, int confirmRid, boolean cancelable, int messageRid, DialogInterface.OnClickListener onClick){
        showBaseAlert(titleRid, confirmRid, cancelable, getString(messageRid), onClick);
    }

    public void showBaseAlert(int titleRid, int confirmRid, boolean cancelable, String messageStr,
                              DialogInterface.OnClickListener onClick){
        showBaseAlert(getString(titleRid), getString(confirmRid), cancelable, messageStr, onClick);
    }

    public void showBaseAlert(String titleStr, String confirmStr, boolean cancelable, String messageStr, DialogInterface.OnClickListener onClick){
        if(isDestroyed())
            return;
        cancelAlert();

        dialog = new AlertDialog.Builder(this)
                .setTitle(titleStr)
                .setMessage(messageStr)
                .setCancelable(cancelable)
                .setNegativeButton(confirmStr, onClick)
                .create();
        dialog.show();
    }

    public void cancelAlert(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    protected void goPage(Class cls, boolean isFinish){
        Intent it = new Intent(this, cls);
        startActivity(it);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if(isFinish)
            finish();
    }

}
