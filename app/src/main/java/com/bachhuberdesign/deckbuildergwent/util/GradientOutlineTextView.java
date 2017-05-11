package com.bachhuberdesign.deckbuildergwent.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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
    }

    public GradientOutlineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Shader goldShader = new LinearGradient(0f, 0f, 0f, 100f, Color.parseColor("#f1c248"), Color.parseColor("#f08c0b"), Shader.TileMode.CLAMP);
    Shader silverShader = new LinearGradient(0f, 0f, 0f, 100f, Color.WHITE, Color.DKGRAY, Shader.TileMode.CLAMP);
    Shader outline = new LinearGradient(0, 0, 0, getHeight(), Color.DKGRAY, Color.DKGRAY, Shader.TileMode.CLAMP);

    @Override
    protected void onDraw(Canvas canvas) {
//        getPaint().setShadowLayer(3, 2.0f, 2.0f, Color.DKGRAY);
//        getPaint().setShader(null);
//        super.onDraw(canvas);
//        getPaint().clearShadowLayer();

        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setStrokeWidth(6);
        getPaint().setShader(outline);
        super.onDraw(canvas);

        getPaint().setStyle(Paint.Style.FILL);
        getPaint().setShader(goldShader);
        super.onDraw(canvas);
    }
}
