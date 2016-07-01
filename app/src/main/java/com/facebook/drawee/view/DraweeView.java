

package com.facebook.drawee.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.facebook.common.internal.Objects;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;

// Referenced classes of package com.facebook.drawee.view:
//            DraweeHolder

public class DraweeView extends ImageView
{

    public DraweeView(Context context)
    {
        super(context);
        mInitialised = false;
        init(context);
    }

    public DraweeView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mInitialised = false;
        init(context);
    }

    public DraweeView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mInitialised = false;
        init(context);
    }

    public DraweeView(Context context, AttributeSet attributeset, int i, int j)
    {
        super(context, attributeset, i, j);
        mInitialised = false;
        init(context);
    }

    private void init(Context context)
    {
        if(!mInitialised)
        {
            mInitialised = true;
            mDraweeHolder = DraweeHolder.create(null, context);
            if(android.os.Build.VERSION.SDK_INT >= 21)
            {
                ColorStateList colorStateList = getImageTintList();
                if(colorStateList != null)
                {
                    setColorFilter(colorStateList.getDefaultColor());
                    return;
                }
            }
        }
    }

    public DraweeController getController()
    {
        return mDraweeHolder.getController();
    }

    public DraweeHierarchy getHierarchy()
    {
        return mDraweeHolder.getHierarchy();
    }

    public Drawable getTopLevelDrawable()
    {
        return mDraweeHolder.getTopLevelDrawable();
    }

    public boolean hasController()
    {
        return mDraweeHolder.getController() != null;
    }

    public boolean hasHierarchy()
    {
        return mDraweeHolder.hasHierarchy();
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        mDraweeHolder.onAttach();
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mDraweeHolder.onDetach();
    }

    public void onFinishTemporaryDetach()
    {
        super.onFinishTemporaryDetach();
        mDraweeHolder.onAttach();
    }

    public void onStartTemporaryDetach()
    {
        super.onStartTemporaryDetach();
        mDraweeHolder.onDetach();
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(mDraweeHolder.onTouchEvent(motionevent))
            return true;
        else
            return super.onTouchEvent(motionevent);
    }

    public void setController(DraweeController draweecontroller)
    {
        mDraweeHolder.setController(draweecontroller);
        super.setImageDrawable(mDraweeHolder.getTopLevelDrawable());
    }

    public void setHierarchy(DraweeHierarchy draweehierarchy)
    {
        mDraweeHolder.setHierarchy(draweehierarchy);
        super.setImageDrawable(mDraweeHolder.getTopLevelDrawable());
    }

    public void setImageBitmap(Bitmap bitmap)
    {
        init(getContext());
        mDraweeHolder.setController(null);
        super.setImageBitmap(bitmap);
    }

    public void setImageDrawable(Drawable drawable)
    {
        init(getContext());
        mDraweeHolder.setController(null);
        super.setImageDrawable(drawable);
    }

    public void setImageResource(int i)
    {
        init(getContext());
        mDraweeHolder.setController(null);
        super.setImageResource(i);
    }

    public void setImageURI(Uri uri)
    {
        init(getContext());
        mDraweeHolder.setController(null);
        super.setImageURI(uri);
    }

    public String toString()
    {
        com.facebook.common.internal.Objects.ToStringHelper tostringhelper = Objects.toStringHelper(this);
        String s;
        if(mDraweeHolder != null)
            s = mDraweeHolder.toString();
        else
            s = "<no holder set>";
        return tostringhelper.add("holder", s).toString();
    }

    private DraweeHolder mDraweeHolder;
    private boolean mInitialised;
}
