package com.bachhuberdesign.deckbuildergwent.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bachhuberdesign.deckbuildergwent.R;

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
public class GradientOutlineTextView extends android.support.v7.widget.AppCompatTextView {

    public GradientOutlineTextView(Context context) {
        super(context, null, -1);
    }

    public GradientOutlineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, -1);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GradientOutlineTextView, 0, 0);

        try {
            gradientColor = array.getInteger(R.styleable.GradientOutlineTextView_gradientColor, 0);
        } finally {
            array.recycle();
        }
    }

    public GradientOutlineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GradientOutlineTextView, 0, 0);

        try {
            gradientColor = array.getInteger(R.styleable.GradientOutlineTextView_gradientColor, 0);
        } finally {
            array.recycle();
        }
    }

    private int gradientColor = 0;
    private Shader goldShader = new LinearGradient(0f, 0f, 0f, 100f, Color.parseColor("#f1c248"), Color.parseColor("#f08c0b"), Shader.TileMode.CLAMP);
    private Shader silverShader = new LinearGradient(0f, 0f, 0f, 100f, Color.WHITE, Color.DKGRAY, Shader.TileMode.CLAMP);
    private Shader outline = new LinearGradient(0, 0, 0, getHeight(), Color.DKGRAY, Color.DKGRAY, Shader.TileMode.CLAMP);

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw shadow
//        getPaint().setShadowLayer(3, 2.0f, 2.0f, Color.DKGRAY);
//        getPaint().setShader(null);
//        super.onDraw(canvas);

        // Draw stroke
        getPaint().clearShadowLayer();
        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setStrokeWidth(3);
        getPaint().setShader(outline);
        super.onDraw(canvas);

        // Draw gradient
        getPaint().setStyle(Paint.Style.FILL);

        if (gradientColor == 0) {
            getPaint().setShader(silverShader);
        } else if (gradientColor == 1) {
            getPaint().setShader(goldShader);
        }

        super.onDraw(canvas);
    }

}
