// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.*;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.TriState;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.memory.PooledByteBufferOutputStream;
import com.facebook.imagepipeline.nativecode.JpegTranscoder;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Map;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, Consumer, ProducerContext, DelegatingConsumer,
//            JobScheduler, ProducerListener, BaseProducerContextCallbacks

public class ResizeAndRotateProducer
    implements Producer
{
    private class TransformingConsumer extends DelegatingConsumer
    {

        private void doTransform(EncodedImage encodedimage, boolean flag)
        {
            Object obj;
            Object obj1;
            Map map;
            Object obj2;
            Map map1;
            CloseableReference closeablereference;
            PooledByteBufferOutputStream pooledbytebufferoutputstream;
            ImageRequest imagerequest;

            try {
				mProducerContext.getListener().onProducerStart(mProducerContext.getId(), "ResizeAndRotateProducer");
				imagerequest = mProducerContext.getImageRequest();
				pooledbytebufferoutputstream = mPooledByteBufferFactory.newOutputStream();
				map1 = null;
				closeablereference = null;
				obj = null;
				map = map1;
				obj2 = obj;
				obj1 = closeablereference;
				int i = ResizeAndRotateProducer.getScaleNumerator(imagerequest, encodedimage);
				map = map1;
				obj2 = obj;
				obj1 = closeablereference;
				map1 = getExtraMap(encodedimage, imagerequest, i);
				map = map1;
				obj2 = obj;
				obj1 = closeablereference;
				obj = encodedimage.getInputStream();
				map = map1;
				obj2 = obj;
				obj1 = obj;
				JpegTranscoder.transcodeJpeg(((java.io.InputStream) (obj)), pooledbytebufferoutputstream, ResizeAndRotateProducer.getRotationAngle(imagerequest, encodedimage), i, 85);
				map = map1;
				obj2 = obj;
				obj1 = obj;
				closeablereference = CloseableReference.of(pooledbytebufferoutputstream.toByteBuffer());
				encodedimage = new EncodedImage(closeablereference);
				encodedimage.setImageFormat(ImageFormat.JPEG);
				encodedimage.parseMetaData();
				mProducerContext.getListener().onProducerFinishWithSuccess(mProducerContext.getId(), "ResizeAndRotateProducer", map1);
				getConsumer().onNewResult(encodedimage, flag);
				EncodedImage.closeSafely(encodedimage);
				CloseableReference.closeSafely(closeablereference);
				Closeables.closeQuietly(((java.io.InputStream) (obj)));
				pooledbytebufferoutputstream.close();
				return;

			} catch(Exception ex) {
                ex.printStackTrace();
				return;
			}

        }

        private Map getExtraMap(EncodedImage encodedimage, ImageRequest imagerequest, int i)
        {
            if(!mProducerContext.getListener().requiresExtraMap(mProducerContext.getId()))
                return null;
            String s = (new StringBuilder()).append(encodedimage.getWidth()).append("x").append(encodedimage.getHeight()).toString();
            String temp;
            String temp2;

            if(imagerequest.getResizeOptions() != null)
                temp = (new StringBuilder()).append(imagerequest.getResizeOptions().width).append("x").append(imagerequest.getResizeOptions().height).toString();
            else
                temp = "Unspecified";
            if(i > 0)
                temp2 = (new StringBuilder()).append(i).append("/8").toString();
            else
                temp2 = "";
            return ImmutableMap.of("Original size", s, "Requested size", temp, "Fraction", temp2, "queueTime", String.valueOf(mJobScheduler.getQueuedTime()));
        }

        protected void onNewResultImpl(EncodedImage encodedimage, boolean flag)
        {
            if(mIsCancelled)
	            return;

            if(encodedimage != null) {
				TriState tristate = ResizeAndRotateProducer.shouldTransform(mProducerContext.getImageRequest(), encodedimage);
				if(flag || tristate != TriState.UNSET)
				{
					if(tristate != TriState.YES)
					{
						getConsumer().onNewResult(encodedimage, flag);
						return;
					}
					if(mJobScheduler.updateJob(encodedimage, flag) && (flag || mProducerContext.isIntermediateResultExpected()))
					{
						mJobScheduler.scheduleJob();
						return;
					}
				}
			}
            if(flag)
            {
                getConsumer().onNewResult(null, true);
                return;
            }
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((EncodedImage)obj, flag);
        }

        private boolean mIsCancelled;
        private final JobScheduler mJobScheduler;
        private final ProducerContext mProducerContext;





/*
        static boolean access$402(TransformingConsumer transformingconsumer, boolean flag)
        {
            transformingconsumer.mIsCancelled = flag;
            return flag;
        }

*/

        public TransformingConsumer(Consumer consumer, ProducerContext producercontext)
        {
            super(consumer);
            mIsCancelled = false;
            mProducerContext = producercontext;
            _cls1 cls1 = new _cls1();
            mJobScheduler = new JobScheduler(mExecutor, cls1, 100);
            mProducerContext.addCallbacks( new _cls2());
        }

        class _cls1
        implements JobScheduler.JobRunnable
        {

            public void run(EncodedImage encodedimage, boolean flag)
            {
                doTransform(encodedimage, flag);
            }
        }

        class _cls2 extends BaseProducerContextCallbacks
        {

            public void onCancellationRequested()
            {
                mJobScheduler.clearJob();
                mIsCancelled = true;
                TransformingConsumer.this.onCancellation();
            }

        public void onIsIntermediateResultExpectedChanged()
        {
            if(mProducerContext.isIntermediateResultExpected())
                mJobScheduler.scheduleJob();
        }

    }


    }


    public ResizeAndRotateProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, Producer producer)
    {
        mExecutor = (Executor)Preconditions.checkNotNull(executor);
        mPooledByteBufferFactory = (PooledByteBufferFactory)Preconditions.checkNotNull(pooledbytebufferfactory);
        mInputProducer = (Producer)Preconditions.checkNotNull(producer);
    }

    static float determineResizeRatio(ResizeOptions resizeoptions, int i, int j)
    {
        float f1;
        if(resizeoptions == null)
        {
            f1 = 1.0F;
        } else
        {
            f1 = Math.max((float)resizeoptions.width / (float)i, (float)resizeoptions.height / (float)j);
            float f = f1;
            if((float)i * f1 > 2048F)
                f = 2048F / (float)i;
            f1 = f;
            if((float)j * f > 2048F)
                return 2048F / (float)j;
        }
        return f1;
    }

    private static int getRotationAngle(ImageRequest imagerequest, EncodedImage encodedimage)
    {
        boolean flag = false;
        if(!imagerequest.getAutoRotateEnabled())
            return 0;
        int i = encodedimage.getRotationAngle();
        if(i == 0 || i == 90 || i == 180 || i == 270)
            flag = true;
        Preconditions.checkArgument(flag);
        return i;
    }

    private static int getScaleNumerator(ImageRequest imagerequest, EncodedImage encodedimage)
    {
        ResizeOptions resizeoptions = imagerequest.getResizeOptions();
        int i;
        if(resizeoptions == null)
        {
            i = 8;
        } else
        {
            i = getRotationAngle(imagerequest, encodedimage);
            int j;
            if(i == 90 || i == 270)
                j = 1;
            else
                j = 0;
            if(j != 0)
                i = encodedimage.getHeight();
            else
                i = encodedimage.getWidth();
            if(j != 0)
                j = encodedimage.getWidth();
            else
                j = encodedimage.getHeight();
            j = roundNumerator(determineResizeRatio(resizeoptions, i, j));
            if(j > 8)
                return 8;
            i = j;
            if(j < 1)
                return 1;
        }
        return i;
    }

    static int roundNumerator(float f)
    {
        return (int)(0.6666667F + 8F * f);
    }

    private static boolean shouldResize(int i)
    {
        return i < 8;
    }

    private static TriState shouldTransform(ImageRequest imagerequest, EncodedImage encodedimage)
    {
        if(encodedimage == null || encodedimage.getImageFormat() == ImageFormat.UNKNOWN)
            return TriState.UNSET;
        if(encodedimage.getImageFormat() != ImageFormat.JPEG)
            return TriState.NO;
        boolean flag;
        if(getRotationAngle(imagerequest, encodedimage) != 0 || shouldResize(getScaleNumerator(imagerequest, encodedimage)))
            flag = true;
        else
            flag = false;
        return TriState.valueOf(flag);
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        mInputProducer.produceResults(new TransformingConsumer(consumer, producercontext), producercontext);
    }

    static final int DEFAULT_JPEG_QUALITY = 85;
    private static final String FRACTION_KEY = "Fraction";
    static final int MAX_JPEG_SCALE_NUMERATOR = 8;
    static final int MIN_TRANSFORM_INTERVAL_MS = 100;
    private static final String ORIGINAL_SIZE_KEY = "Original size";
    private static final String PRODUCER_NAME = "ResizeAndRotateProducer";
    private static final String REQUESTED_SIZE_KEY = "Requested size";
    private static final float ROUNDUP_FRACTION = 0.6666667F;
    private final Executor mExecutor;
    private final Producer mInputProducer;
    private final PooledByteBufferFactory mPooledByteBufferFactory;









}
