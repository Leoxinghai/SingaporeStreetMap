// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.image.EncodedImage;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, Consumer, ProducerContext, DelegatingConsumer

public class AddImageTransformMetaDataProducer
    implements Producer
{
    private static class AddImageTransformMetaDataConsumer extends DelegatingConsumer
    {

        protected void onNewResultImpl(EncodedImage encodedimage, boolean flag)
        {
            if(encodedimage == null)
            {
                getConsumer().onNewResult(null, flag);
                return;
            }
            if(!EncodedImage.isMetaDataAvailable(encodedimage))
                encodedimage.parseMetaData();
            getConsumer().onNewResult(encodedimage, flag);
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((EncodedImage)obj, flag);
        }

        private AddImageTransformMetaDataConsumer(Consumer consumer)
        {
            super(consumer);
        }

    }


    public AddImageTransformMetaDataProducer(Producer producer)
    {
        mInputProducer = producer;
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        mInputProducer.produceResults(new AddImageTransformMetaDataConsumer(consumer), producercontext);
    }

    private final Producer mInputProducer;
}
