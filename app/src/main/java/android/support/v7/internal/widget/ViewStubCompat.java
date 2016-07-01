// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.*;
import java.lang.ref.WeakReference;

public final class ViewStubCompat extends View
{
    public static interface OnInflateListener
    {

        public abstract void onInflate(ViewStubCompat viewstubcompat, View view);
    }


    public ViewStubCompat(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public ViewStubCompat(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mLayoutResource = 0;
        TypedArray typedArray  = context.obtainStyledAttributes(attributeset, android.support.v7.appcompat.R.styleable.ViewStubCompat, i, 0);
        mInflatedId = typedArray.getResourceId(android.support.v7.appcompat.R.styleable.ViewStubCompat_android_inflatedId, -1);
        mLayoutResource = typedArray.getResourceId(android.support.v7.appcompat.R.styleable.ViewStubCompat_android_layout, 0);
        setId(typedArray.getResourceId(android.support.v7.appcompat.R.styleable.ViewStubCompat_android_id, -1));
        typedArray.recycle();
        setVisibility(8);
        setWillNotDraw(true);
    }

    protected void dispatchDraw(Canvas canvas)
    {
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
    }

    public int getInflatedId()
    {
        return mInflatedId;
    }

    public LayoutInflater getLayoutInflater()
    {
        return mInflater;
    }

    public int getLayoutResource()
    {
        return mLayoutResource;
    }

    public View inflate()
    {
        Object obj = getParent();
        if(obj != null && (obj instanceof ViewGroup))
        {
            if(mLayoutResource != 0)
            {
                ViewGroup viewgroup = (ViewGroup)obj;
                android.view.ViewGroup.LayoutParams layoutparams;
                int i;
                if(mInflater != null)
                    obj = mInflater;
                else
                    obj = LayoutInflater.from(getContext());
                obj = ((LayoutInflater) (obj)).inflate(mLayoutResource, viewgroup, false);
                if(mInflatedId != -1)
                    ((View) (obj)).setId(mInflatedId);
                i = viewgroup.indexOfChild(this);
                viewgroup.removeViewInLayout(this);
                layoutparams = getLayoutParams();
                if(layoutparams != null)
                    viewgroup.addView(((View) (obj)), i, layoutparams);
                else
                    viewgroup.addView(((View) (obj)), i);
                mInflatedViewRef = new WeakReference(obj);
                if(mInflateListener != null)
                    mInflateListener.onInflate(this, ((View) (obj)));
                return ((View) (obj));
            } else
            {
                throw new IllegalArgumentException("ViewStub must have a valid layoutResource");
            }
        } else
        {
            throw new IllegalStateException("ViewStub must have a non-null ViewGroup viewParent");
        }
    }

    protected void onMeasure(int i, int j)
    {
        setMeasuredDimension(0, 0);
    }

    public void setInflatedId(int i)
    {
        mInflatedId = i;
    }

    public void setLayoutInflater(LayoutInflater layoutinflater)
    {
        mInflater = layoutinflater;
    }

    public void setLayoutResource(int i)
    {
        mLayoutResource = i;
    }

    public void setOnInflateListener(OnInflateListener oninflatelistener)
    {
        mInflateListener = oninflatelistener;
    }

    public void setVisibility(int i)
    {
        if(mInflatedViewRef == null) {
            super.setVisibility(i);
            if(i == 0 || i == 4)
            {
                inflate();
                return;
            }
        } else {
            View view = (View) mInflatedViewRef.get();
            if (view == null)
                throw new IllegalStateException("setVisibility called on un-referenced view");

            view.setVisibility(i);
        }
        return;
    }

    private OnInflateListener mInflateListener;
    private int mInflatedId;
    private WeakReference mInflatedViewRef;
    private LayoutInflater mInflater;
    private int mLayoutResource;
}
