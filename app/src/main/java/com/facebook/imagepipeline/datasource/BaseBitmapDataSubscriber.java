

package com.facebook.imagepipeline.datasource;

import android.graphics.Bitmap;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableBitmap;

public abstract class BaseBitmapDataSubscriber extends BaseDataSubscriber
{

    public BaseBitmapDataSubscriber()
    {
    }

    protected abstract void onNewResultImpl(Bitmap bitmap);

    public void onNewResultImpl(DataSource datasource)
    {
        CloseableReference closeablereference;
        if(!datasource.isFinished())
            return;
        closeablereference = (CloseableReference)datasource.getResult();
        Object obj = null;
        datasource = null;
        Bitmap temp = null;
        if(closeablereference != null)
        {
            datasource = null;
            if(closeablereference.get() instanceof CloseableBitmap)
                temp = ((CloseableBitmap)closeablereference.get()).getUnderlyingBitmap();
        }
        onNewResultImpl(((Bitmap) (temp)));
        CloseableReference.closeSafely(closeablereference);
        return;
    }
}
