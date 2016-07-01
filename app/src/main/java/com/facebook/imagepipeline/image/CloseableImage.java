

package com.facebook.imagepipeline.image;

import com.facebook.common.logging.FLog;
import java.io.Closeable;

// Referenced classes of package com.facebook.imagepipeline.image:
//            ImageInfo, ImmutableQualityInfo, QualityInfo

public abstract class CloseableImage
    implements Closeable, ImageInfo
{

    public CloseableImage()
    {
    }

    public abstract void close();

    protected void finalize()
        throws Throwable
    {
        if(isClosed())
            return;
        FLog.w("CloseableImage", "finalize: %s %x still open.", new Object[] {
            getClass().getSimpleName(), Integer.valueOf(System.identityHashCode(this))
        });
        close();
        super.finalize();
        return;
    }

    public QualityInfo getQualityInfo()
    {
        return ImmutableQualityInfo.FULL_QUALITY;
    }

    public abstract int getSizeInBytes();

    public abstract boolean isClosed();

    public boolean isStateful()
    {
        return false;
    }

    private static final String TAG = "CloseableImage";
}
