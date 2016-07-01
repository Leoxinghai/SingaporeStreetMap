

package com.facebook.drawee.interfaces;

import android.graphics.drawable.Animatable;
import android.view.MotionEvent;

// Referenced classes of package com.facebook.drawee.interfaces:
//            DraweeHierarchy

public interface DraweeController
{

    public abstract Animatable getAnimatable();

    public abstract DraweeHierarchy getHierarchy();

    public abstract void onAttach();

    public abstract void onDetach();

    public abstract boolean onTouchEvent(MotionEvent motionevent);

    public abstract void setHierarchy(DraweeHierarchy draweehierarchy);
}
