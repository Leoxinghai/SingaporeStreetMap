

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView
{

    public LockableScrollView(Context context)
    {
        super(context);
        mScrollable = true;
    }

    public LockableScrollView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mScrollable = true;
    }

    public LockableScrollView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mScrollable = true;
    }

    public boolean isScrollable()
    {
        return mScrollable;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionevent)
    {
        if(!mScrollable)
            return false;
        else
            return super.onInterceptTouchEvent(motionevent);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        switch(motionevent.getAction())
        {
        default:
            return super.onTouchEvent(motionevent);

        case 0: // '\0'
            break;
        }
        if(mScrollable)
            return super.onTouchEvent(motionevent);
        else
            return mScrollable;
    }

    public void setScrollingEnabled(boolean flag)
    {
        mScrollable = flag;
    }

    private boolean mScrollable;
}
