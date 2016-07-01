

package com.facebook.imagepipeline.animated.base;

import android.graphics.Rect;
import com.facebook.common.references.CloseableReference;

// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedDrawableBackend

public interface AnimatedDrawableCachingBackend
    extends AnimatedDrawableBackend
{

    public abstract void appendDebugOptionString(StringBuilder stringbuilder);

    public abstract AnimatedDrawableCachingBackend forNewBounds(Rect rect);

    public abstract CloseableReference getBitmapForFrame(int i);

    public abstract CloseableReference getPreviewBitmap();
}
