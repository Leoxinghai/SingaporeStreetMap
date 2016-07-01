// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.TriState;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.memory.PooledByteBufferOutputStream;
import com.facebook.imagepipeline.nativecode.WebpTranscoder;

import java.io.InputStream;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, DelegatingConsumer,
//            StatefulProducerRunnable, ProducerListener

public class WebpTranscodeProducer
    implements Producer
{
    private class WebpTranscodeConsumer extends DelegatingConsumer
    {

        protected void onNewResultImpl(EncodedImage encodedimage, boolean flag)
        {
            if(mShouldTranscodeWhenFinished == TriState.UNSET && encodedimage != null)
                mShouldTranscodeWhenFinished = WebpTranscodeProducer.shouldTranscode(encodedimage);
            if(mShouldTranscodeWhenFinished == TriState.NO)
                getConsumer().onNewResult(encodedimage, flag);
            else
            if(flag)
                if(mShouldTranscodeWhenFinished == TriState.YES && encodedimage != null)
                {
                    transcodeLastResult(encodedimage, getConsumer(), mContext);
                    return;
                } else
                {
                    getConsumer().onNewResult(encodedimage, flag);
                    return;
                }
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((EncodedImage)obj, flag);
        }

        private final ProducerContext mContext;
        private TriState mShouldTranscodeWhenFinished;

        public WebpTranscodeConsumer(Consumer consumer, ProducerContext producercontext)
        {
            super(consumer);
            mContext = producercontext;
            mShouldTranscodeWhenFinished = TriState.UNSET;
        }
    }


    public WebpTranscodeProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, Producer producer)
    {
        mExecutor = (Executor)Preconditions.checkNotNull(executor);
        mPooledByteBufferFactory = (PooledByteBufferFactory)Preconditions.checkNotNull(pooledbytebufferfactory);
        mInputProducer = (Producer)Preconditions.checkNotNull(producer);
    }

    private static void doTranscode(EncodedImage encodedimage, PooledByteBufferOutputStream pooledbytebufferoutputstream)
        throws Exception
    {
        InputStream inputStream = encodedimage.getInputStream();
        ImageFormat imageformat = ImageFormatChecker.getImageFormat_WrapIOException(inputStream);

        switch(imageformat.ordinal())
        {
        default:
            throw new IllegalArgumentException("Wrong image format");

        case 1: // '\001'
        case 3: // '\003'
            WebpTranscoder.transcodeWebpToJpeg(inputStream, pooledbytebufferoutputstream, 80);
            return;

        case 2: // '\002'
        case 4: // '\004'
            WebpTranscoder.transcodeWebpToPng(inputStream, pooledbytebufferoutputstream);
            break;
        }
    }

    private static TriState shouldTranscode(EncodedImage encodedimage)
    {
        Preconditions.checkNotNull(encodedimage);
        ImageFormat imageFormat = ImageFormatChecker.getImageFormat_WrapIOException(encodedimage.getInputStream());
        switch(imageFormat.ordinal())
        {
        default:
            return TriState.NO;

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            boolean flag;
            if(!WebpTranscoder.isWebpNativelySupported(imageFormat))
                flag = true;
            else
                flag = false;
            return TriState.valueOf(flag);

        case 5: // '\005'
            return TriState.UNSET;
        }
    }

    private void transcodeLastResult(final EncodedImage encodedimage, final Consumer final_consumer, ProducerContext producercontext)
    {
        Preconditions.checkNotNull(encodedimage);
        EncodedImage encodedImage1 = EncodedImage.cloneOrNull(encodedimage);
        StatefulProducerRunnable statefulProducerRunnable = new StatefulProducerRunnable(final_consumer, producercontext.getListener(), "WebpTranscodeProducer", producercontext.getId()) {

            protected void disposeResult(EncodedImage encodedimage1)
            {
                EncodedImage.closeSafely(encodedimage1);
            }

            protected void disposeResult(Object obj)
            {
                disposeResult((EncodedImage) obj);
            }

            protected EncodedImage getResult()
                throws Exception
            {
                PooledByteBufferOutputStream pooledbytebufferoutputstream = mPooledByteBufferFactory.newOutputStream();
                CloseableReference closeablereference;
                WebpTranscodeProducer.doTranscode(encodedimage, pooledbytebufferoutputstream);
                closeablereference = CloseableReference.of(pooledbytebufferoutputstream.toByteBuffer());
                EncodedImage encodedimage1;
                encodedimage1 = new EncodedImage(closeablereference);
                encodedimage1.copyMetaDataFrom(encodedimage);
                CloseableReference.closeSafely(closeablereference);
                pooledbytebufferoutputstream.close();
                return encodedimage1;
            }


            protected void onCancellation()
            {
                EncodedImage.closeSafely(encodedimage);
                super.onCancellation();
            }

            protected void onFailure(Exception exception)
            {
                EncodedImage.closeSafely(encodedimage);
                super.onFailure(exception);
            }

            protected void onSuccess(EncodedImage encodedimage1)
            {
                EncodedImage.closeSafely(encodedimage);
                super.onSuccess(encodedimage1);
            }

            protected void onSuccess(Object obj)
            {
                onSuccess((EncodedImage)obj);
            }

        };

        mExecutor.execute(statefulProducerRunnable);
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        mInputProducer.produceResults(new WebpTranscodeConsumer(consumer, producercontext), producercontext);
    }

    private static final int DEFAULT_JPEG_QUALITY = 80;
    private static final String PRODUCER_NAME = "WebpTranscodeProducer";
    private final Executor mExecutor;
    private final Producer mInputProducer;
    private final PooledByteBufferFactory mPooledByteBufferFactory;




}
