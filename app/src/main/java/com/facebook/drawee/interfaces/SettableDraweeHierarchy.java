

package com.facebook.drawee.interfaces;

import android.graphics.drawable.Drawable;

// Referenced classes of package com.facebook.drawee.interfaces:
//            DraweeHierarchy

public interface SettableDraweeHierarchy
    extends DraweeHierarchy
{

    public abstract void reset();

    public abstract void setControllerOverlay(Drawable drawable);

    public abstract void setFailure(Throwable throwable);

    public abstract void setImage(Drawable drawable, float f, boolean flag);

    public abstract void setProgress(float f, boolean flag);

    public abstract void setRetry(Throwable throwable);
}
