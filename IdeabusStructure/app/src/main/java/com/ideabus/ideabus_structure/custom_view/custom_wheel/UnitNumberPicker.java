package com.ideabus.ideabus_structure.custom_view.custom_wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ideabus.ideabus_structure.R;

/**
 * Created by devil on 15/5/18.
 * 自定义NumberPicker 绘制单位
 */
public class UnitNumberPicker extends NumberPicker {

    private int textX, textY;
    private String unitText;
    private Paint paint;

    public UnitNumberPicker(Context context) {
        super(context);
    }

    public UnitNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) throw new RuntimeException("DateNumberPicker只能从xml布局文件调用");
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.UnitNumberPicker);
        unitText = ta.getString(R.styleable.UnitNumberPicker_unitText);
        float unitTextSize = ta.getDimension(R.styleable.UnitNumberPicker_unitTextSize, 14);
        int unitTextColor = ta.getColor(R.styleable.UnitNumberPicker_unitTextColor, Color.GRAY);
        ta.recycle();
        paint = new Paint();
        paint.setColor(unitTextColor);
        paint.setTextSize(unitTextSize);
        paint.setAntiAlias(true);
        post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(unitText)) return;
                Rect rect = new Rect();
                paint.getTextBounds(unitText, 0, unitText.length(), rect);
                textX = getWidth() - rect.width() - 20;
                textY = getHeight() / 3 + rect.height() + 20;
            }
        });

    }

    public void setUnitText(int unitTextSize){
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(unitTextSize);
        paint.setAntiAlias(true);
        post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(unitText)) return;
                Rect rect = new Rect();
                paint.getTextBounds(unitText, 0, unitText.length(), rect);
                textX = getWidth() - rect.width() - 20;
                textY = getHeight() / 3 + rect.height() + 20;
            }
        });
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(unitText)) {
            canvas.drawText(unitText, textX, textY, paint);
        }
    }

}
