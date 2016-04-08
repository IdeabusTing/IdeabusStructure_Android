package com.ideabus.ideabus_structure.activity;

import android.content.Intent;
import android.os.Bundle;

import com.ideabus.ideabuslibrary.BaseActivity;

/**
 * Created by Ting on 16/3/17.
 */
public abstract class MyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void goPage(Class cls, boolean isFinish){
        Intent it = new Intent(this, cls);
        startActivity(it);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if(isFinish)
            finish();
    }

}
