// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.File;
import java.util.Map;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, StatefulProducerRunnable,
//            ProducerListener, BaseProducerContextCallbacks

public class LocalVideoThumbnailProducer
    implements Producer
{

    public LocalVideoThumbnailProducer(Executor executor)
    {
        mExecutor = executor;
    }

    private static int calculateKind(ImageRequest imagerequest)
    {
        return imagerequest.getPreferredWidth() <= 96 && imagerequest.getPreferredHeight() <= 96 ? 3 : 1;
    }

    public void produceResults(final Consumer final_consumer, ProducerContext producercontext)
    {
        final ImageRequest imageRequest =  producercontext.getImageRequest();

        final StatefulProducerRunnable statefulProducerRunnable = new StatefulProducerRunnable(final_consumer, producercontext.getListener(), "VideoThumbnailProducer", producercontext.getId()) {

            protected void disposeResult(CloseableReference closeablereference)
            {
                CloseableReference.closeSafely(closeablereference);
            }

            protected void disposeResult(Object obj)
            {
                disposeResult((CloseableReference)obj);
            }

            protected Map getExtraMapOnSuccess(CloseableReference closeablereference)
            {
                boolean flag;
                if(closeablereference != null)
                    flag = true;
                else
                    flag = false;
                return ImmutableMap.of("createdThumbnail", String.valueOf(flag));
            }

            protected Map getExtraMapOnSuccess(Object obj)
            {
                return getExtraMapOnSuccess((CloseableReference)obj);
            }

            protected CloseableReference getResult()
                throws Exception
            {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(imageRequest.getSourceFile().getPath(), LocalVideoThumbnailProducer.calculateKind(imageRequest));
                if(bitmap == null)
                    return null;
                else
                    return CloseableReference.of(new CloseableStaticBitmap(bitmap, SimpleBitmapReleaser.getInstance(), ImmutableQualityInfo.FULL_QUALITY, 0));
            }


        }
;
        producercontext.addCallbacks(new BaseProducerContextCallbacks() {

            public void onCancellationRequested()
            {
                statefulProducerRunnable.cancel();
            }

        });
        mExecutor.execute(statefulProducerRunnable);
    }

    static final String CREATED_THUMBNAIL = "createdThumbnail";
    static final String PRODUCER_NAME = "VideoThumbnailProducer";
    private final Executor mExecutor;


    // Unreferenced inner class imagepipeline/producers/LocalVideoThumbnailProducer$1$1

/* anonymous class */
    class _cls1
        implements ResourceReleaser
    {

        public void release(Bitmap bitmap)
        {
            bitmap.recycle();
        }

        public void release(Object obj)
        {
            release((Bitmap)obj);
        }

    }

}
