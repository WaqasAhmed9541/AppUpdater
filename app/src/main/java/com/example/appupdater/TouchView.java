package com.example.appupdater;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class TouchView extends View {
    private Paint paint = new Paint();
    private Map<Integer, Float> xMap = new HashMap<>();
    private Map<Integer, Float> yMap = new HashMap<>();

    public TouchView(Context context) {
        super(context);
        init();
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(40);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Map.Entry<Integer, Float> entry : xMap.entrySet()) {
            int pointerId = entry.getKey();
            Float x = entry.getValue();
            Float y = yMap.get(pointerId);
            if (x != null && y != null) {
                canvas.drawCircle(x, y, 30, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    xMap.put(id, event.getX(i));
                    yMap.put(id, event.getY(i));
                }
                invalidate(); // Redraw the view
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                xMap.remove(pointerId);
                yMap.remove(pointerId);
                invalidate(); // Redraw the view
                break;
        }
        return true;
    }
}
