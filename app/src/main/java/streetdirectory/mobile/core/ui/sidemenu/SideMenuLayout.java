// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.ui.sidemenu;

import android.content.Context;
import android.content.res.Resources;
import android.util.*;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.Action;

public abstract class SideMenuLayout extends RelativeLayout
{

    public SideMenuLayout(Context context)
    {
        super(context);
        mOffset = 100;
        mOffsetMax = 90;
        mOffsetMid = 50;
        mSlideDuration = 200;
        mOpened = false;
        mOpening = false;
        mClosing = false;
        onSlideOpenAction = null;
        onSlideCloseAction = null;
        startX = 0.0F;
        translateX = 0.0F;
        initialize(context);
    }

    public SideMenuLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mOffset = 100;
        mOffsetMax = 90;
        mOffsetMid = 50;
        mSlideDuration = 200;
        mOpened = false;
        mOpening = false;
        mClosing = false;
        onSlideOpenAction = null;
        onSlideCloseAction = null;
        startX = 0.0F;
        translateX = 0.0F;
        initialize(context);
    }

    private void onDragEnd(int i)
    {
        android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)frontview.getLayoutParams();
        i = layoutparams.leftMargin;
        layoutparams.setMargins(i, 0, -i, 0);
        if(layoutparams.leftMargin < mOffsetMid)
        {
            slideClose();
            return;
        } else
        {
            slideOpen();
            return;
        }
    }

    private void onDragX(int i)
    {
        android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)frontview.getLayoutParams();
        int j = layoutparams.leftMargin + i;
        i = j;
        if(j < 0)
            i = layoutparams.leftMargin;
        layoutparams.setMargins(i, 0, -i, 0);
        if(layoutparams.leftMargin > 0 && layoutparams.leftMargin < mOffsetMax)
            frontview.setLayoutParams(layoutparams);
    }

    public boolean getIsMenuOpen()
    {
        return mOpened;
    }

    public int getSlideOffset()
    {
        return mOffset;
    }

    protected void initialize(Context context)
    {
        displayMetrics = getResources().getDisplayMetrics();
        setOffsetPercentage(77);
    }

    protected void initializeChild(Context context)
    {
        backview = new FrameLayout(context);
        backview.setBackgroundColor(0xff444444);
        Object obj = new android.widget.RelativeLayout.LayoutParams(-1, -1);
        backview.setLayoutParams(((android.view.ViewGroup.LayoutParams) (obj)));
        frontview = new FrameLayout(context);
        frontview.setClickable(true);
        frontview.setBackgroundColor(0xff888888);
        obj = new android.widget.RelativeLayout.LayoutParams(-1, -1);
        frontview.setLayoutParams(((android.view.ViewGroup.LayoutParams) (obj)));
        if(android.os.Build.VERSION.SDK_INT >= 14)
        {
            setClipChildren(false);
            setClipToPadding(false);
        }
        obj = new ImageView(context);
        int i = (int)TypedValue.applyDimension(1, 3F, displayMetrics);
        android.widget.RelativeLayout.LayoutParams layoutparams = new android.widget.RelativeLayout.LayoutParams(i + 1, -1);
        layoutparams.leftMargin = -i;
        ((ImageView) (obj)).setLayoutParams(layoutparams);
        ((ImageView) (obj)).setImageDrawable(getResources().getDrawable(R.drawable.menu_shadow));
        ((ImageView) (obj)).setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        frontview.addView(((android.view.View) (obj)));
        int k = getChildCount();
        for(int j = 0; j < k; j++)
        {
            android.view.View view = getChildAt(0);
            removeView(view);
            frontview.addView(view);
        }

        populateBackView(context);
        addView(backview);
        addView(frontview);
        frontview.setOnClickListener(null);
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if(!isInEditMode())
            initializeChild(getContext());
    }

    protected void onSlideClosed()
    {
        if(onSlideCloseAction != null)
            onSlideCloseAction.execute();
    }

    protected void onSlideOpened()
    {
        if(onSlideOpenAction != null)
            onSlideOpenAction.execute();
    }

    protected void populateBackView(Context context)
    {
    }

    public void setOffsetPercentage(int i)
    {
        mOffset = (int)((double)displayMetrics.widthPixels * ((double)(float)i / 100D));
        mOffsetMax = displayMetrics.widthPixels;
        mOffsetMid = (int)((double)displayMetrics.widthPixels * 0.5D);
    }

    public void setOnSlideClose(Action action)
    {
        onSlideCloseAction = action;
    }

    public void setOnSlideOpen(Action action)
    {
        onSlideOpenAction = action;
    }

    public void slideClose()
    {
        mClosing = true;
        final android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams)frontview.getLayoutParams();
        TranslateAnimation translateanimation = new TranslateAnimation(0.0F, -params.leftMargin, 0.0F, 0.0F);
        translateanimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

            public void onAnimationEnd(Animation animation)
            {
                frontview.clearAnimation();
                params.setMargins(0, 0, 0, 0);
                frontview.setLayoutParams(params);
                mOpened = false;
                mClosing = false;
                onSlideClosed();
            }

            public void onAnimationRepeat(Animation animation)
            {
            }

            public void onAnimationStart(Animation animation)
            {
            }

        });
        translateanimation.setDuration((long)(((float)params.leftMargin / (float)mOffset) * (float)mSlideDuration));
        frontview.startAnimation(translateanimation);
    }

    public void slideOpen()
    {
        slideOpenInternal(mOffset);
    }

    public void slideOpen(int i)
    {
        slideOpenInternal((int)((double)displayMetrics.widthPixels * ((double)(float)i / 100D)));
    }

    protected void slideOpenInternal(final int offsetFinal)
    {
        mOpening = true;
        final android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams)frontview.getLayoutParams();
        TranslateAnimation translateanimation = new TranslateAnimation(0.0F, offsetFinal - params.leftMargin, 0.0F, 0.0F);
        translateanimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

            public void onAnimationEnd(Animation animation)
            {
                frontview.clearAnimation();
                params.setMargins(offsetFinal, 0, -offsetFinal, 0);
                frontview.setLayoutParams(params);
                mOpened = true;
                mOpening = false;
                onSlideOpened();
            }

            public void onAnimationRepeat(Animation animation)
            {
            }

            public void onAnimationStart(Animation animation)
            {
            }

        });
        translateanimation.setDuration(mSlideDuration);
        frontview.startAnimation(translateanimation);
    }

    public void touchExecutor(MotionEvent motionevent)
    {
        switch(motionevent.getAction() & 0xff) {
		default:
				return;
		case 0:
				startX = motionevent.getX();
				return;
		case 2:
				translateX = motionevent.getX() - startX;
				if((int)translateX != 0)
				{
					onDragX((int)translateX);
					return;
				}
				return;
		case 1:
				onDragEnd((int)translateX);
				return;
			}
    }

    protected FrameLayout backview;
    private DisplayMetrics displayMetrics;
    protected FrameLayout frontview;
    private boolean mClosing;
    private int mOffset;
    private int mOffsetMax;
    private int mOffsetMid;
    private boolean mOpened;
    private boolean mOpening;
    private int mSlideDuration;
    private Action onSlideCloseAction;
    private Action onSlideOpenAction;
    private float startX;
    private float translateX;


/*
    static boolean access$002(SideMenuLayout sidemenulayout, boolean flag)
    {
        sidemenulayout.mOpened = flag;
        return flag;
    }

*/


/*
    static boolean access$102(SideMenuLayout sidemenulayout, boolean flag)
    {
        sidemenulayout.mOpening = flag;
        return flag;
    }

*/


/*
    static boolean access$202(SideMenuLayout sidemenulayout, boolean flag)
    {
        sidemenulayout.mClosing = flag;
        return flag;
    }

*/
}
