// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.datasource;

import com.facebook.common.internal.Preconditions;
import com.facebook.datasource.AbstractDataSource;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.producers.*;

public abstract class AbstractProducerToDataSourceAdapter extends AbstractDataSource
{

    protected AbstractProducerToDataSourceAdapter(Producer producer, SettableProducerContext settableproducercontext, RequestListener requestlistener)
    {
        mSettableProducerContext = settableproducercontext;
        mRequestListener = requestlistener;
        mRequestListener.onRequestStart(settableproducercontext.getImageRequest(), mSettableProducerContext.getCallerContext(), mSettableProducerContext.getId(), mSettableProducerContext.isPrefetch());
        producer.produceResults(createConsumer(), settableproducercontext);
    }

    private Consumer createConsumer()
    {
        return new BaseConsumer() {

            protected void onCancellationImpl()
            {
                AbstractProducerToDataSourceAdapter.this.onCancellationImpl();
            }

            protected void onFailureImpl(Throwable throwable)
            {
                AbstractProducerToDataSourceAdapter.this.onFailureImpl(throwable);
            }

            protected void onNewResultImpl(Object obj, boolean flag)
            {
                AbstractProducerToDataSourceAdapter.this.onNewResultImpl(obj, flag);
            }

            protected void onProgressUpdateImpl(float f)
            {
                setProgress(f);
            }

        };
    }

    private void onCancellationImpl()
    {
        Preconditions.checkState(isClosed());
        return;
    }

    private void onFailureImpl(Throwable throwable)
    {
        if(super.setFailure(throwable))
            mRequestListener.onRequestFailure(mSettableProducerContext.getImageRequest(), mSettableProducerContext.getId(), throwable, mSettableProducerContext.isPrefetch());
    }

    public boolean close()
    {
        if(!super.close())
            return false;
        if(!super.isFinished())
        {
            mRequestListener.onRequestCancellation(mSettableProducerContext.getId());
            mSettableProducerContext.cancel();
        }
        return true;
    }

    protected void onNewResultImpl(Object obj, boolean flag)
    {
        if(super.setResult(obj, flag) && flag)
            mRequestListener.onRequestSuccess(mSettableProducerContext.getImageRequest(), mSettableProducerContext.getId(), mSettableProducerContext.isPrefetch());
    }

    private final RequestListener mRequestListener;
    private final SettableProducerContext mSettableProducerContext;



}
