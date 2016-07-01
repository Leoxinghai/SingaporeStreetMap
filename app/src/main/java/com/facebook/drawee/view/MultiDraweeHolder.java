

package com.facebook.drawee.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.facebook.common.internal.Preconditions;
import java.util.ArrayList;

// Referenced classes of package com.facebook.drawee.view:
//            DraweeHolder

public class MultiDraweeHolder
{

    public MultiDraweeHolder()
    {
        mIsAttached = false;
        mHolders = new ArrayList();
    }

    public void add(int i, DraweeHolder draweeholder)
    {
        Preconditions.checkNotNull(draweeholder);
        Preconditions.checkElementIndex(i, mHolders.size() + 1);
        mHolders.add(i, draweeholder);
        if(mIsAttached)
            draweeholder.onAttach();
    }

    public void add(DraweeHolder draweeholder)
    {
        add(mHolders.size(), draweeholder);
    }

    public void clear()
    {
        if(mIsAttached)
        {
            for(int i = 0; i < mHolders.size(); i++)
                ((DraweeHolder)mHolders.get(i)).onDetach();

        }
        mHolders.clear();
    }

    public void draw(Canvas canvas)
    {
        for(int i = 0; i < mHolders.size(); i++)
        {
            Drawable drawable = get(i).getTopLevelDrawable();
            if(drawable != null)
                drawable.draw(canvas);
        }

    }

    public DraweeHolder get(int i)
    {
        return (DraweeHolder)mHolders.get(i);
    }

    public void onAttach()
    {
        if(!mIsAttached)
        {
            mIsAttached = true;
            int i = 0;
            while(i < mHolders.size())
            {
                ((DraweeHolder)mHolders.get(i)).onAttach();
                i++;
            }
        }
    }

    public void onDetach()
    {
        if(mIsAttached)
        {
            mIsAttached = false;
            int i = 0;
            while(i < mHolders.size())
            {
                ((DraweeHolder)mHolders.get(i)).onDetach();
                i++;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        for(int i = 0; i < mHolders.size(); i++)
            if(((DraweeHolder)mHolders.get(i)).onTouchEvent(motionevent))
                return true;

        return false;
    }

    public void remove(int i)
    {
        DraweeHolder draweeholder = (DraweeHolder)mHolders.get(i);
        if(mIsAttached)
            draweeholder.onDetach();
        mHolders.remove(i);
    }

    public int size()
    {
        return mHolders.size();
    }

    public boolean verifyDrawable(Drawable drawable)
    {
        for(int i = 0; i < mHolders.size(); i++)
            if(drawable == get(i).getTopLevelDrawable())
                return true;

        return false;
    }

    ArrayList mHolders;
    boolean mIsAttached;
}
