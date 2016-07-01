// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LikeBoxCountView extends FrameLayout
{
    public static enum LikeBoxCountViewCaretPosition
    {

		LEFT("LEFT", 0),
		TOP("TOP", 1),
		RIGHT("RIGHT", 2),
		BOTTOM("BOTTOM", 3);

        String sType;
		int iType;

        private LikeBoxCountViewCaretPosition(String s, int i)
        {
		sType = s;
		iType = i;
        }
    }


    public LikeBoxCountView(Context context)
    {
        super(context);
        caretPosition = LikeBoxCountViewCaretPosition.LEFT;
        initialize(context);
    }

    private void drawBorder(Canvas canvas, float f, float f1, float f2, float f3)
    {
        Path path = new Path();
        float f4 = 2.0F * borderRadius;
        path.addArc(new RectF(f, f1, f + f4, f1 + f4), -180F, 90F);
        if(caretPosition == LikeBoxCountViewCaretPosition.TOP)
        {
            path.lineTo((f2 - f - caretWidth) / 2.0F + f, f1);
            path.lineTo((f2 - f) / 2.0F + f, f1 - caretHeight);
            path.lineTo(((f2 - f) + caretWidth) / 2.0F + f, f1);
        }
        path.lineTo(f2 - borderRadius, f1);
        path.addArc(new RectF(f2 - f4, f1, f2, f1 + f4), -90F, 90F);
        if(caretPosition == LikeBoxCountViewCaretPosition.RIGHT)
        {
            path.lineTo(f2, (f3 - f1 - caretWidth) / 2.0F + f1);
            path.lineTo(caretHeight + f2, (f3 - f1) / 2.0F + f1);
            path.lineTo(f2, ((f3 - f1) + caretWidth) / 2.0F + f1);
        }
        path.lineTo(f2, f3 - borderRadius);
        path.addArc(new RectF(f2 - f4, f3 - f4, f2, f3), 0.0F, 90F);
        if(caretPosition == LikeBoxCountViewCaretPosition.BOTTOM)
        {
            path.lineTo(((f2 - f) + caretWidth) / 2.0F + f, f3);
            path.lineTo((f2 - f) / 2.0F + f, caretHeight + f3);
            path.lineTo((f2 - f - caretWidth) / 2.0F + f, f3);
        }
        path.lineTo(borderRadius + f, f3);
        path.addArc(new RectF(f, f3 - f4, f + f4, f3), 90F, 90F);
        if(caretPosition == LikeBoxCountViewCaretPosition.LEFT)
        {
            path.lineTo(f, ((f3 - f1) + caretWidth) / 2.0F + f1);
            path.lineTo(f - caretHeight, (f3 - f1) / 2.0F + f1);
            path.lineTo(f, (f3 - f1 - caretWidth) / 2.0F + f1);
        }
        path.lineTo(f, borderRadius + f1);
        canvas.drawPath(path, borderPaint);
    }

    private void initialize(Context context)
    {
        setWillNotDraw(false);
        caretHeight = getResources().getDimension(com.facebook.android.R.dimen.com_facebook_likeboxcountview_caret_height);
        caretWidth = getResources().getDimension(com.facebook.android.R.dimen.com_facebook_likeboxcountview_caret_width);
        borderRadius = getResources().getDimension(com.facebook.android.R.dimen.com_facebook_likeboxcountview_border_radius);
        borderPaint = new Paint();
        borderPaint.setColor(getResources().getColor(com.facebook.android.R.color.com_facebook_likeboxcountview_border_color));
        borderPaint.setStrokeWidth(getResources().getDimension(com.facebook.android.R.dimen.com_facebook_likeboxcountview_border_width));
        borderPaint.setStyle(android.graphics.Paint.Style.STROKE);
        initializeLikeCountLabel(context);
        addView(likeCountLabel);
        setCaretPosition(caretPosition);
    }

    private void initializeLikeCountLabel(Context context)
    {
        likeCountLabel = new TextView(context);
        FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(-1, -1);
        likeCountLabel.setLayoutParams(layoutParams);
        likeCountLabel.setGravity(17);
        likeCountLabel.setTextSize(0, getResources().getDimension(com.facebook.android.R.dimen.com_facebook_likeboxcountview_text_size));
        likeCountLabel.setTextColor(getResources().getColor(com.facebook.android.R.color.com_facebook_likeboxcountview_text_color));
        textPadding = getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_likeboxcountview_text_padding);
        additionalTextPadding = getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_likeboxcountview_caret_height);
    }

    private void setAdditionalTextPadding(int i, int j, int k, int l)
    {
        likeCountLabel.setPadding(textPadding + i, textPadding + j, textPadding + k, textPadding + l);
    }

    protected void onDraw(Canvas canvas)
    {
        int i;
        int j;
        int k;
        int l;
        super.onDraw(canvas);
        l = getPaddingTop();
        j = getPaddingLeft();
        k = getWidth() - getPaddingRight();
        i = getHeight() - getPaddingBottom();
        switch(caretPosition.ordinal()) {
		default:
				break;
		case 4:
				i = (int)((float)i - caretHeight);
				break;
		case 1:
				j = (int)((float)j + caretHeight);
				break;
		case 2:
				l = (int)((float)l + caretHeight);
				break;
		case 3:
				k = (int)((float)k - caretHeight);
				break;
		}
        drawBorder(canvas, j, l, k, i);
        return;

    }

    public void setCaretPosition(LikeBoxCountViewCaretPosition likeboxcountviewcaretposition)
    {
        caretPosition = likeboxcountviewcaretposition;
        switch(likeboxcountviewcaretposition.ordinal())
        {
        default:
            return;

        case 1: // '\001'
            setAdditionalTextPadding(additionalTextPadding, 0, 0, 0);
            return;

        case 2: // '\002'
            setAdditionalTextPadding(0, additionalTextPadding, 0, 0);
            return;

        case 3: // '\003'
            setAdditionalTextPadding(0, 0, additionalTextPadding, 0);
            return;

        case 4: // '\004'
            setAdditionalTextPadding(0, 0, 0, additionalTextPadding);
            break;
        }
    }

    public void setText(String s)
    {
        likeCountLabel.setText(s);
    }

    private int additionalTextPadding;
    private Paint borderPaint;
    private float borderRadius;
    private float caretHeight;
    private LikeBoxCountViewCaretPosition caretPosition;
    private float caretWidth;
    private TextView likeCountLabel;
    private int textPadding;
}
