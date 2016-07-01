// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.os.SystemClock;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.*;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, FetchState, ProducerListener, NetworkFetcher,
//            Consumer, ProducerContext

public class NetworkFetchProducer
    implements Producer
{

    public NetworkFetchProducer(PooledByteBufferFactory pooledbytebufferfactory, ByteArrayPool bytearraypool, NetworkFetcher networkfetcher)
    {
        mPooledByteBufferFactory = pooledbytebufferfactory;
        mByteArrayPool = bytearraypool;
        mNetworkFetcher = networkfetcher;
    }

    private static float calculateProgress(int i, int j)
    {
        if(j > 0)
            return (float)i / (float)j;
        else
            return 1.0F - (float)Math.exp((double)(-i) / 50000D);
    }

    private Map getExtraMap(FetchState fetchstate, int i)
    {
        if(!fetchstate.getListener().requiresExtraMap(fetchstate.getId()))
            return null;
        else
            return mNetworkFetcher.getExtraMap(fetchstate, i);
    }

    private void handleFinalResult(PooledByteBufferOutputStream pooledbytebufferoutputstream, FetchState fetchstate)
    {
        Map map = getExtraMap(fetchstate, pooledbytebufferoutputstream.size());
        fetchstate.getListener().onProducerFinishWithSuccess(fetchstate.getId(), "NetworkFetchProducer", map);
        notifyConsumer(pooledbytebufferoutputstream, true, fetchstate.getConsumer());
    }

    private void maybeHandleIntermediateResult(PooledByteBufferOutputStream pooledbytebufferoutputstream, FetchState fetchstate)
    {
        long l = SystemClock.elapsedRealtime();
        if(shouldPropagateIntermediateResults(fetchstate) && l - fetchstate.getLastIntermediateResultTimeMs() >= 100L)
        {
            fetchstate.setLastIntermediateResultTimeMs(l);
            fetchstate.getListener().onProducerEvent(fetchstate.getId(), "NetworkFetchProducer", "intermediate_result");
            notifyConsumer(pooledbytebufferoutputstream, false, fetchstate.getConsumer());
        }
    }

    private void notifyConsumer(PooledByteBufferOutputStream pooledbytebufferoutputstream, boolean flag, Consumer consumer)
    {
        CloseableReference closeablereference;
        closeablereference = CloseableReference.of(pooledbytebufferoutputstream.toByteBuffer());
        pooledbytebufferoutputstream = null;
        EncodedImage encodedimage = new EncodedImage(closeablereference);
        encodedimage.parseMetaData();
        consumer.onNewResult(encodedimage, flag);
        EncodedImage.closeSafely(encodedimage);
        CloseableReference.closeSafely(closeablereference);
        return;
    }

    private void onCancellation(FetchState fetchstate)
    {
        fetchstate.getListener().onProducerFinishWithCancellation(fetchstate.getId(), "NetworkFetchProducer", null);
        fetchstate.getConsumer().onCancellation();
    }

    private void onFailure(FetchState fetchstate, Throwable throwable)
    {
        fetchstate.getListener().onProducerFinishWithFailure(fetchstate.getId(), "NetworkFetchProducer", throwable, null);
        fetchstate.getConsumer().onFailure(throwable);
    }

    private void onResponse(FetchState fetchstate, InputStream inputstream, int i)
        throws IOException
    {
        PooledByteBufferOutputStream pooledbytebufferoutputstream;
        byte abyte0[];
        float f;
        int j;
        if(i > 0)
            pooledbytebufferoutputstream = mPooledByteBufferFactory.newOutputStream(i);
        else
            pooledbytebufferoutputstream = mPooledByteBufferFactory.newOutputStream();
        abyte0 = (byte[])mByteArrayPool.get(16384);
        for(;;) {
            j = inputstream.read(abyte0);
            if (j < 0)
                break;
            if (j <= 0)
                continue;
            pooledbytebufferoutputstream.write(abyte0, 0, j);
            maybeHandleIntermediateResult(pooledbytebufferoutputstream, fetchstate);
            f = calculateProgress(pooledbytebufferoutputstream.size(), i);
            fetchstate.getConsumer().onProgressUpdate(f);
        }

        mNetworkFetcher.onFetchCompletion(fetchstate, pooledbytebufferoutputstream.size());
        handleFinalResult(pooledbytebufferoutputstream, fetchstate);
        mByteArrayPool.release(abyte0);
        pooledbytebufferoutputstream.close();
        return;

    }

    private boolean shouldPropagateIntermediateResults(FetchState fetchstate)
    {
        if(!fetchstate.getContext().getImageRequest().getProgressiveRenderingEnabled())
            return false;
        else
            return mNetworkFetcher.shouldPropagate(fetchstate);
    }

    public void produceResults(final Consumer fetchState, ProducerContext producercontext)
    {
        producercontext.getListener().onProducerStart(producercontext.getId(), "NetworkFetchProducer");
        final FetchState fetchstate = mNetworkFetcher.createFetchState(fetchState, producercontext);
        mNetworkFetcher.fetch(fetchstate, new NetworkFetcher.Callback() {

            public void onCancellation()
            {
                NetworkFetchProducer.this.onCancellation(fetchstate);
            }

            public void onFailure(Throwable throwable)
            {
                NetworkFetchProducer.this.onFailure(fetchstate, throwable);
            }

            public void onResponse(InputStream inputstream, int i)
                throws IOException
            {
                NetworkFetchProducer.this.onResponse(fetchstate, inputstream, i);
            }

        });
    }

    public static final String INTERMEDIATE_RESULT_PRODUCER_EVENT = "intermediate_result";
    static final String PRODUCER_NAME = "NetworkFetchProducer";
    private static final int READ_SIZE = 16384;
    static final long TIME_BETWEEN_PARTIAL_RESULTS_MS = 100L;
    private final ByteArrayPool mByteArrayPool;
    private final NetworkFetcher mNetworkFetcher;
    private final PooledByteBufferFactory mPooledByteBufferFactory;



}
