// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.datasource;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.producers.Producer;
import com.facebook.imagepipeline.producers.SettableProducerContext;

// Referenced classes of package com.facebook.imagepipeline.datasource:
//            AbstractProducerToDataSourceAdapter

public class CloseableProducerToDataSourceAdapter extends AbstractProducerToDataSourceAdapter
{

    private CloseableProducerToDataSourceAdapter(Producer producer, SettableProducerContext settableproducercontext, RequestListener requestlistener)
    {
        super(producer, settableproducercontext, requestlistener);
    }

    public static DataSource create(Producer producer, SettableProducerContext settableproducercontext, RequestListener requestlistener)
    {
        return new CloseableProducerToDataSourceAdapter(producer, settableproducercontext, requestlistener);
    }

    protected void closeResult(CloseableReference closeablereference)
    {
        CloseableReference.closeSafely(closeablereference);
    }

    protected void closeResult(Object obj)
    {
        closeResult((CloseableReference)obj);
    }

    public CloseableReference getResult()
    {
        return CloseableReference.cloneOrNull((CloseableReference)super.getResult());
    }



    protected void onNewResultImpl(CloseableReference closeablereference, boolean flag)
    {
        super.onNewResultImpl(CloseableReference.cloneOrNull(closeablereference), flag);
    }

    protected void onNewResultImpl(Object obj, boolean flag)
    {
        onNewResultImpl((CloseableReference)obj, flag);
    }
}
