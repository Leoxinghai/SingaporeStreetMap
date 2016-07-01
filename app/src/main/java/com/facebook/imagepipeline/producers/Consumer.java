

package com.facebook.imagepipeline.producers;


public interface Consumer
{

    public abstract void onCancellation();

    public abstract void onFailure(Throwable throwable);

    public abstract void onNewResult(Object obj, boolean flag);

    public abstract void onProgressUpdate(float f);
}
