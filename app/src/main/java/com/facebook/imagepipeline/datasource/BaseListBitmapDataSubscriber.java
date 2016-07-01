

package com.facebook.imagepipeline.datasource;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableBitmap;
import java.util.*;

public abstract class BaseListBitmapDataSubscriber extends BaseDataSubscriber
{

    public BaseListBitmapDataSubscriber()
    {
    }

    public void onNewResultImpl(DataSource datasource)
    {
        if(!datasource.isFinished())
            return;

        List temp = (List)datasource.getResult();
        if(datasource == null)
        {
            onNewResultListImpl(null);
            return;
        }
        Object obj;
        Iterator iterator;
        obj = new ArrayList(temp.size());
        iterator = temp.iterator();

        CloseableReference closeablereference;
        for(;iterator.hasNext();) {
            CloseableReference temp2 = (CloseableReference) iterator.next();
            if (temp2 != null &&(temp2.get() instanceof CloseableBitmap))
                ((List) (obj)).add(((CloseableBitmap) temp2.get()).getUnderlyingBitmap());
            else
                ((List) (obj)).add(null);
        }

        for(Iterator iterator2 = temp.iterator(); iterator2.hasNext(); CloseableReference.closeSafely((CloseableReference)iterator2.next()));
        onNewResultListImpl(((List) (obj)));
        Iterator iterator3 = temp.iterator();
        for(;iterator3.hasNext();)
            CloseableReference.closeSafely((CloseableReference)iterator3.next());
    }

    protected abstract void onNewResultListImpl(List list);
}
