// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, Consumer, ProducerContext, DelegatingConsumer

public class BranchOnSeparateImagesProducer
    implements Producer
{
    private class OnFirstImageConsumer extends DelegatingConsumer
    {

        private boolean isResultGoodEnough(EncodedImage encodedimage, ImageRequest imagerequest)
        {
            while(encodedimage == null || encodedimage.getWidth() < imagerequest.getPreferredWidth() || encodedimage.getHeight() < imagerequest.getPreferredHeight())
                return false;
            return true;
        }

        protected void onFailureImpl(Throwable throwable)
        {
            mInputProducer2.produceResults(getConsumer(), mProducerContext);
        }

        protected void onNewResultImpl(EncodedImage encodedimage, boolean flag)
        {
            ImageRequest imagerequest = mProducerContext.getImageRequest();
            boolean flag2 = isResultGoodEnough(encodedimage, imagerequest);
            if(encodedimage != null && (flag2 || imagerequest.getLocalThumbnailPreviewsEnabled()))
            {
                Consumer consumer = getConsumer();
                boolean flag1;
                if(flag && flag2)
                    flag1 = true;
                else
                    flag1 = false;
                consumer.onNewResult(encodedimage, flag1);
            }
            if(flag && !flag2)
                mInputProducer2.produceResults(getConsumer(), mProducerContext);
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((EncodedImage)obj, flag);
        }

        private ProducerContext mProducerContext;

        private OnFirstImageConsumer(Consumer consumer, ProducerContext producercontext)
        {
            super(consumer);
            mProducerContext = producercontext;
        }

    }


    public BranchOnSeparateImagesProducer(Producer producer, Producer producer1)
    {
        mInputProducer1 = producer;
        mInputProducer2 = producer1;
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        consumer = new OnFirstImageConsumer(consumer, producercontext);
        mInputProducer1.produceResults(consumer, producercontext);
    }

    private final Producer mInputProducer1;
    private final Producer mInputProducer2;

}
