// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, Consumer, ProducerContext, DelegatingConsumer

public class RemoveImageTransformMetaDataProducer
    implements Producer
{
    private class RemoveImageTransformMetaDataConsumer extends DelegatingConsumer
    {

        protected void onNewResultImpl(EncodedImage encodedimage, boolean flag)
        {
            CloseableReference closeablereference;
            CloseableReference closeablereference1;
            Object obj;
            obj = null;
            closeablereference = null;
            closeablereference1 = null;
            if(!EncodedImage.isValid(encodedimage))
                return;
            closeablereference1 = null;
            closeablereference = encodedimage.getByteBufferRef();
            closeablereference1 = closeablereference;
            getConsumer().onNewResult(closeablereference, flag);
            CloseableReference.closeSafely(closeablereference);
            return;
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((EncodedImage)obj, flag);
        }


        private RemoveImageTransformMetaDataConsumer(Consumer consumer)
        {
            super(consumer);
        }

    }


    public RemoveImageTransformMetaDataProducer(Producer producer)
    {
        mInputProducer = producer;
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        mInputProducer.produceResults(new RemoveImageTransformMetaDataConsumer(consumer), producercontext);
    }

    private final Producer mInputProducer;
}
