

package com.facebook.imagepipeline.producers;


// Referenced classes of package com.facebook.imagepipeline.producers:
//            BaseConsumer, Consumer

public abstract class DelegatingConsumer extends BaseConsumer
{

    public DelegatingConsumer(Consumer consumer)
    {
        mConsumer = consumer;
    }

    public Consumer getConsumer()
    {
        return mConsumer;
    }

    protected void onCancellationImpl()
    {
        mConsumer.onCancellation();
    }

    protected void onFailureImpl(Throwable throwable)
    {
        mConsumer.onFailure(throwable);
    }

    protected void onProgressUpdateImpl(float f)
    {
        mConsumer.onProgressUpdate(f);
    }

    private final Consumer mConsumer;
}
