

package com.facebook.drawee.drawable;

import android.graphics.Matrix;
import android.graphics.RectF;

public interface TransformCallback
{

    public abstract void getRootBounds(RectF rectf);

    public abstract void getTransform(Matrix matrix);
}
