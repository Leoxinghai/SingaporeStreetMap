// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.*;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, StatefulProducerRunnable,
//            ProducerListener, BaseProducerContextCallbacks

public abstract class LocalFetchProducer
    implements Producer
{

    protected LocalFetchProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, boolean flag)
    {
        mExecutor = executor;
        mPooledByteBufferFactory = pooledbytebufferfactory;
        if(flag && android.os.Build.VERSION.SDK_INT == 19)
            flag = true;
        else
            flag = false;
        mDecodeFileDescriptorEnabledForKitKat = flag;
    }

    protected EncodedImage getByteBufferBackedEncodedImage(InputStream inputstream, int i)
        throws IOException
    {
        CloseableReference closeablereference1 = null;
        CloseableReference closeablereference = null;
        if(i >= 0)
			closeablereference = CloseableReference.of(mPooledByteBufferFactory.newByteBuffer(inputstream, i));
		else
        	closeablereference = CloseableReference.of(mPooledByteBufferFactory.newByteBuffer(inputstream));

        closeablereference1 = closeablereference;
        EncodedImage encodedimage = new EncodedImage(closeablereference);
        Closeables.closeQuietly(inputstream);
        CloseableReference.closeSafely(closeablereference);
        return encodedimage;
    }

    protected abstract EncodedImage getEncodedImage(ImageRequest imagerequest)
        throws IOException;

    protected EncodedImage getEncodedImage(InputStream inputstream, int i)
        throws IOException
    {
        Runtime runtime = Runtime.getRuntime();
        long l = runtime.maxMemory();
        long l1 = Math.min(l - (runtime.totalMemory() - runtime.freeMemory()), 0x800000L);
        if(mDecodeFileDescriptorEnabledForKitKat && (inputstream instanceof FileInputStream) && l >= 64L * l1)
            return getInputStreamBackedEncodedImage(new File(inputstream.toString()), i);
        else
            return getByteBufferBackedEncodedImage(inputstream, i);
    }

    protected EncodedImage getInputStreamBackedEncodedImage(final File file, int i)
        throws IOException
    {
        return new EncodedImage(new Supplier() {

            public FileInputStream get()
            {
                FileInputStream fileinputstream;
                try
                {
                    fileinputstream = new FileInputStream(file);
                }
                catch(IOException ioexception)
                {
                    throw new RuntimeException(ioexception);
                }
                return fileinputstream;
            }

        }, i);
    }

    protected abstract String getProducerName();

    public void produceResults(final Consumer final_consumer, ProducerContext producercontext)
    {
        ProducerListener producerlistener = producercontext.getListener();
        String s = producercontext.getId();
        final ImageRequest imagerequest = producercontext.getImageRequest();
        final StatefulProducerRunnable statefulproducerrunnable = new StatefulProducerRunnable(final_consumer, producerlistener, this.getProducerName(), s) {

            protected void disposeResult(EncodedImage encodedimage)
            {
                EncodedImage.closeSafely(encodedimage);
            }

            protected void disposeResult(Object obj)
            {
                disposeResult((EncodedImage)obj);
            }

            protected EncodedImage getResult()
                throws Exception
            {
                EncodedImage encodedimage = getEncodedImage(imagerequest);
                if(encodedimage == null)
                {
                    return null;
                } else
                {
                    encodedimage.parseMetaData();
                    return encodedimage;
                }
            }


        };
        producercontext.addCallbacks(new BaseProducerContextCallbacks() {

            public void onCancellationRequested()
            {
                statefulproducerrunnable.cancel();
            }

        });
        mExecutor.execute(statefulproducerrunnable);
    }

    private final boolean mDecodeFileDescriptorEnabledForKitKat;
    private final Executor mExecutor;
    private final PooledByteBufferFactory mPooledByteBufferFactory;
}
