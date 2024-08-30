package com.example.appupdater;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AudioVisualizerView extends View {
    private static final int LINE_WIDTH = 5;
    private static final int LINE_SPACING = 10;

    private Paint paint;
    private List<Float> amplitudes;

    public AudioVisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(LINE_WIDTH);
        amplitudes = new ArrayList<>();
    }

    public void addAmplitude(float amplitude) {
        amplitudes.add(amplitude);
        if (amplitudes.size() * (LINE_WIDTH + LINE_SPACING) > getWidth()) {
            amplitudes.remove(0); // Remove oldest amplitude to make space for new one
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float centerY = height / 2f;

        for (int i = 0; i < amplitudes.size(); i++) {
            float scaledHeight = height * amplitudes.get(i);
            float startX = i * (LINE_WIDTH + LINE_SPACING);
            canvas.drawLine(startX, centerY - scaledHeight / 2, startX, centerY + scaledHeight / 2, paint);
        }
    }
}
