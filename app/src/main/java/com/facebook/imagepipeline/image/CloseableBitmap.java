

package com.facebook.imagepipeline.image;

import android.graphics.Bitmap;

// Referenced classes of package com.facebook.imagepipeline.image:
//            CloseableImage

public abstract class CloseableBitmap extends CloseableImage
{

    public CloseableBitmap()
    {
    }

    public abstract Bitmap getUnderlyingBitmap();
}
