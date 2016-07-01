// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.*;
import java.lang.ref.WeakReference;

public class ToolTipPopup
{
    private class PopupContentView extends FrameLayout
    {

        private void init()
        {
            LayoutInflater.from(getContext()).inflate(com.facebook.android.R.layout.com_facebook_tooltip_bubble, this);
            topArrow = (ImageView)findViewById(com.facebook.android.R.id.com_facebook_tooltip_bubble_view_top_pointer);
            bottomArrow = (ImageView)findViewById(com.facebook.android.R.id.com_facebook_tooltip_bubble_view_bottom_pointer);
            bodyFrame = findViewById(com.facebook.android.R.id.com_facebook_body_frame);
            xOut = (ImageView)findViewById(com.facebook.android.R.id.com_facebook_button_xout);
        }

        public void onMeasure(int i, int j)
        {
            super.onMeasure(i, j);
        }

        public void showBottomArrow()
        {
            topArrow.setVisibility(4);
            bottomArrow.setVisibility(View.VISIBLE);
        }

        public void showTopArrow()
        {
            topArrow.setVisibility(View.VISIBLE);
            bottomArrow.setVisibility(4);
        }

        private View bodyFrame;
        private ImageView bottomArrow;
        private ImageView topArrow;
        private ImageView xOut;





        public PopupContentView(Context context)
        {
            super(context);
            init();
        }
    }

    public static enum Style
    {

		BLUE("BLUE", 0),
		BLACK("BLACK", 1);
		String sType;
		int iType;

        private Style(String s, int i)
        {
		sType = s;
		iType = i;
        }
    }


    public ToolTipPopup(String s, View view)
    {
        mStyle = Style.BLUE;
        mNuxDisplayTime = 6000L;
        mText = s;
        mAnchorViewRef = new WeakReference(view);
        mContext = view.getContext();
    }

    private void registerObserver()
    {
        unregisterObserver();
        if(mAnchorViewRef.get() != null)
            ((View)mAnchorViewRef.get()).getViewTreeObserver().addOnScrollChangedListener(mScrollListener);
    }

    private void unregisterObserver()
    {
        if(mAnchorViewRef.get() != null)
            ((View)mAnchorViewRef.get()).getViewTreeObserver().removeOnScrollChangedListener(mScrollListener);
    }

    private void updateArrows()
    {
label0:
        {
            if(mPopupWindow != null && mPopupWindow.isShowing())
            {
                if(!mPopupWindow.isAboveAnchor())
                    break label0;
                mPopupContent.showBottomArrow();
            }
            return;
        }
        mPopupContent.showTopArrow();
    }

    public void dismiss()
    {
        unregisterObserver();
        if(mPopupWindow != null)
            mPopupWindow.dismiss();
    }

    public void setNuxDisplayTime(long l)
    {
        mNuxDisplayTime = l;
    }

    public void setStyle(Style style)
    {
        mStyle = style;
    }

    public void show()
    {
        if(mAnchorViewRef.get() != null)
        {
            mPopupContent = new PopupContentView(mContext);
            ((TextView)mPopupContent.findViewById(com.facebook.android.R.id.com_facebook_tooltip_bubble_view_text_body)).setText(mText);
            View view;
            int i;
            int j;
            if(mStyle == Style.BLUE)
            {
                mPopupContent.bodyFrame.setBackgroundResource(com.facebook.android.R.drawable.com_facebook_tooltip_blue_background);
                mPopupContent.bottomArrow.setImageResource(com.facebook.android.R.drawable.com_facebook_tooltip_blue_bottomnub);
                mPopupContent.topArrow.setImageResource(com.facebook.android.R.drawable.com_facebook_tooltip_blue_topnub);
                mPopupContent.xOut.setImageResource(com.facebook.android.R.drawable.com_facebook_tooltip_blue_xout);
            } else
            {
                mPopupContent.bodyFrame.setBackgroundResource(com.facebook.android.R.drawable.com_facebook_tooltip_black_background);
                mPopupContent.bottomArrow.setImageResource(com.facebook.android.R.drawable.com_facebook_tooltip_black_bottomnub);
                mPopupContent.topArrow.setImageResource(com.facebook.android.R.drawable.com_facebook_tooltip_black_topnub);
                mPopupContent.xOut.setImageResource(com.facebook.android.R.drawable.com_facebook_tooltip_black_xout);
            }
            view = ((Activity)mContext).getWindow().getDecorView();
            i = view.getWidth();
            j = view.getHeight();
            registerObserver();
            mPopupContent.onMeasure(android.view.View.MeasureSpec.makeMeasureSpec(i, 0x80000000), android.view.View.MeasureSpec.makeMeasureSpec(j, 0x80000000));
            mPopupWindow = new PopupWindow(mPopupContent, mPopupContent.getMeasuredWidth(), mPopupContent.getMeasuredHeight());
            mPopupWindow.showAsDropDown((View)mAnchorViewRef.get());
            updateArrows();
            if(mNuxDisplayTime > 0L)
                mPopupContent.postDelayed(new Runnable() {

                    public void run()
                    {
                        dismiss();
                    }

                }
, mNuxDisplayTime);
            mPopupWindow.setTouchable(true);
            mPopupContent.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view1)
                {
                    dismiss();
                }

            }
);
        }
    }

    public static final long DEFAULT_POPUP_DISPLAY_TIME = 6000L;
    private final WeakReference mAnchorViewRef;
    private final Context mContext;
    private long mNuxDisplayTime;
    private PopupContentView mPopupContent;
    private PopupWindow mPopupWindow;
    private final android.view.ViewTreeObserver.OnScrollChangedListener mScrollListener = new android.view.ViewTreeObserver.OnScrollChangedListener() {

        public void onScrollChanged()
        {
label0:
            {
                if(mAnchorViewRef.get() != null && mPopupWindow != null && mPopupWindow.isShowing())
                {
                    if(!mPopupWindow.isAboveAnchor())
                        break label0;
                    mPopupContent.showBottomArrow();
                }
                return;
            }
            mPopupContent.showTopArrow();
        }

    };
    private Style mStyle;
    private final String mText;



}
