package com.ideabus.ideabus_structure.activity;

import android.os.Bundle;
import android.util.Log;

import com.ideabus.ideabus_structure.R;
import com.ideabus.ideabus_structure.custom_view.custom_wheel.NumberPicker;
import com.ideabus.ideabus_structure.handler.MyHandler;

/**
 * ＃所有功能模組化
 * ＃一個Function不要超過100行
 * ＃Layout xml name命名規則：
 *      activity_(class name)
 *      dialog_(class name)
 *      fragment_(class name)
 *      include_xxx
 *      item_xxx
 *      view_xxx
 *
 */
public class MainActivity extends MyActivity {

    private NumberPicker picker1, picker2, picker3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initParam();
        initListener();

//        goPage(MainActivity.class, true);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onRestart(){
        super.onRestart();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected void initView() {
        picker1 = (NumberPicker) findViewById(R.id.picker1);
        picker2 = (NumberPicker) findViewById(R.id.picker2);
        picker3 = (NumberPicker) findViewById(R.id.picker3);
    }

    @Override
    protected void initParam() {
        setPicker1();
        setPicker2();
        setPicker3();
        int value1 = picker1.getValue();
        int value2 = picker2.getValue();
        int value3 = picker3.getValue();
    }

    @Override
    protected void initListener() {

    }

    private void setPicker1(){
        picker1.setMaxValue(9);
        picker1.setMinValue(0);
        picker1.setValue(5);
        picker1.setWrapSelectorWheel(true);
        picker1.setFocusableInTouchMode(true);
        picker1.setFocusable(true);
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //value1 = newVal;
            }
        });
    }

    private void setPicker2(){
        picker2.setMaxValue(9);
        picker2.setMinValue(0);
        picker2.setValue(5);
        picker2.setWrapSelectorWheel(true);
        picker2.setFocusableInTouchMode(true);
        picker2.setFocusable(true);
        picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //value2 = newVal;
            }
        });
    }

    private void setPicker3(){
        picker3.setMaxValue(9);
        picker3.setMinValue(0);
        picker3.setValue(5);
        picker3.setWrapSelectorWheel(true);
        picker3.setFocusableInTouchMode(true);
        picker3.setFocusable(true);
        picker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //value3 = newVal;
            }
        });
    }

}
