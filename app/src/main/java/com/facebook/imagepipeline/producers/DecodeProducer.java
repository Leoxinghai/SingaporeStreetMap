// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.graphics.Bitmap;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.decoder.*;
import com.facebook.imagepipeline.image.*;
import com.facebook.imagepipeline.memory.ByteArrayPool;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Map;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, DelegatingConsumer,
//            JobScheduler, ProducerListener, DownsampleUtil, BaseProducerContextCallbacks

public class DecodeProducer
    implements Producer
{
    private class LocalImagesProgressiveDecoder extends ProgressiveDecoder
    {

        protected int getIntermediateImageEndOffset(EncodedImage encodedimage)
        {
            return encodedimage.getSize();
        }

        protected QualityInfo getQualityInfo()
        {
            return ImmutableQualityInfo.of(0, false, false);
        }

        protected boolean updateDecodeJob(EncodedImage encodedimage, boolean flag)
        {
            if(flag) {
				flag = super.updateDecodeJob(encodedimage, flag);
			} else {
            	flag = false;
			}

            return flag;
        }


        public LocalImagesProgressiveDecoder(Consumer consumer, ProducerContext producercontext)
        {
            super(consumer, producercontext);
        }
    }

    private class NetworkImagesProgressiveDecoder extends ProgressiveDecoder
    {

        protected int getIntermediateImageEndOffset(EncodedImage encodedimage)
        {
            return mProgressiveJpegParser.getBestScanEndOffset();
        }

        protected QualityInfo getQualityInfo()
        {
            return mProgressiveJpegConfig.getQualityInfo(mProgressiveJpegParser.getBestScanNumber());
        }

        protected boolean updateDecodeJob(EncodedImage encodedimage, boolean flag)
        {
            boolean flag2 = super.updateDecodeJob(encodedimage, flag);
            boolean flag1 = flag2;
            if(!flag) {
				flag1 = flag2;
				if(EncodedImage.isValid(encodedimage)) {
					flag = mProgressiveJpegParser.parseMoreData(encodedimage);
					if(flag) {
						int i;
						i = mProgressiveJpegParser.getBestScanNumber();
						if(i <= mLastScheduledScanNumber || i < mProgressiveJpegConfig.getNextScanNumberToDecode(mLastScheduledScanNumber)) {
							flag1 = false;
						} else {
							mLastScheduledScanNumber = i;
							flag1 = flag2;
						}
					} else
						flag1 = false;
				}
			}

            return flag1;
        }

        private int mLastScheduledScanNumber;
        private final ProgressiveJpegConfig mProgressiveJpegConfig;
        private final ProgressiveJpegParser mProgressiveJpegParser;

        public NetworkImagesProgressiveDecoder(Consumer consumer, ProducerContext producercontext, ProgressiveJpegParser progressivejpegparser, ProgressiveJpegConfig progressivejpegconfig)
        {
            super(consumer, producercontext);
            mProgressiveJpegParser = (ProgressiveJpegParser)Preconditions.checkNotNull(progressivejpegparser);
            mProgressiveJpegConfig = (ProgressiveJpegConfig)Preconditions.checkNotNull(progressivejpegconfig);
            mLastScheduledScanNumber = 0;
        }
    }

    private abstract class ProgressiveDecoder extends DelegatingConsumer
    {

        private void doDecode(EncodedImage encodedimage, boolean flag)
        {
            if(isFinished() || !EncodedImage.isValid(encodedimage))
                return;
            long l = mJobScheduler.getQueuedTime();
            int i = encodedimage.getSize();
            if(!flag)
            	i = getIntermediateImageEndOffset(encodedimage);

            Object obj = ImmutableQualityInfo.FULL_QUALITY;
            if(!flag)
				obj = getQualityInfo();

            mProducerListener.onProducerStart(mProducerContext.getId(), "DecodeProducer");
            CloseableImage closeableimage = mImageDecoder.decodeImage(encodedimage, i, ((QualityInfo) (obj)), mImageDecodeOptions);
            obj = getExtraMap(closeableimage, l, ((QualityInfo) (obj)), flag);
            mProducerListener.onProducerFinishWithSuccess(mProducerContext.getId(), "DecodeProducer", ((Map) (obj)));
            handleResult(closeableimage, flag);
            EncodedImage.closeSafely(encodedimage);
            return;
        }

        private Map getExtraMap(CloseableImage closeableimage, long l, QualityInfo qualityinfo, boolean flag)
        {
            if(!mProducerListener.requiresExtraMap(mProducerContext.getId()))
                return null;
            String s = String.valueOf(l);
            String hasGoodQuality = String.valueOf(qualityinfo.isOfGoodEnoughQuality());
            String s1 = String.valueOf(flag);
            if(closeableimage instanceof CloseableStaticBitmap)
            {
                Bitmap bitmap = ((CloseableStaticBitmap)closeableimage).getUnderlyingBitmap();
                return ImmutableMap.of("bitmapSize", (new StringBuilder()).append(bitmap.getWidth()).append("x").append(bitmap.getHeight()).toString(), "queueTime", s, "hasGoodQuality", hasGoodQuality, "isFinal", s1);
            } else
            {
                return ImmutableMap.of("queueTime", s, "hasGoodQuality", qualityinfo, "isFinal", s1);
            }
        }

        private void handleCancellation()
        {
            maybeFinish(true);
            getConsumer().onCancellation();
        }

        private void handleError(Throwable throwable)
        {
            maybeFinish(true);
            getConsumer().onFailure(throwable);
        }

        private void handleResult(CloseableImage closeableimage, boolean flag)
        {
            CloseableReference closeableReference = CloseableReference.of(closeableimage);
            maybeFinish(flag);
            getConsumer().onNewResult(closeableimage, flag);
            CloseableReference.closeSafely(closeableReference);
            return;
        }

        private boolean isFinished()
        {
            boolean flag = mIsFinished;

            return flag;
        }

        private void maybeFinish(boolean flag)
        {
            if(!flag)
                return;
            if(!mIsFinished) {
                mIsFinished = true;
                mJobScheduler.clearJob();
                return;

            }
            return;
        }

        protected abstract int getIntermediateImageEndOffset(EncodedImage encodedimage);

        protected abstract QualityInfo getQualityInfo();

        public void onCancellationImpl()
        {
            handleCancellation();
        }

        public void onFailureImpl(Throwable throwable)
        {
            handleError(throwable);
        }

        public void onNewResultImpl(EncodedImage encodedimage, boolean flag)
        {
            if(flag && !EncodedImage.isValid(encodedimage))
                handleError(new NullPointerException("Encoded image is not valid."));
            else
            if(updateDecodeJob(encodedimage, flag) && (flag || mProducerContext.isIntermediateResultExpected()))
            {
                mJobScheduler.scheduleJob();
                return;
            }
        }

        public void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((EncodedImage)obj, flag);
        }

        protected boolean updateDecodeJob(EncodedImage encodedimage, boolean flag)
        {
            return mJobScheduler.updateJob(encodedimage, flag);
        }

        private final ImageDecodeOptions mImageDecodeOptions;
        private boolean mIsFinished;
        private final JobScheduler mJobScheduler;
        private final ProducerContext mProducerContext;
        private final ProducerListener mProducerListener;

        public ProgressiveDecoder(Consumer consumer, ProducerContext producercontext)
        {
            super(consumer);
            mProducerContext = producercontext;
            mProducerListener = producercontext.getListener();
            mImageDecodeOptions = producercontext.getImageRequest().getImageDecodeOptions();
            mIsFinished = false;
            _cls1 cls1 = new _cls1();
            mJobScheduler = new JobScheduler(mExecutor, cls1, mImageDecodeOptions.minDecodeIntervalMs);
            mProducerContext.addCallbacks(new _cls2());
        }

        class _cls1
        implements JobScheduler.JobRunnable
        {

            public void run(EncodedImage encodedimage, boolean flag)
            {
                if(encodedimage != null)
                {
                    if(mDownsampleEnabled)
                    {
                        ImageRequest imagerequest = mProducerContext.getImageRequest();
                        if(mDownsampleEnabledForNetwork || !UriUtil.isNetworkUri(imagerequest.getSourceUri()))
                            encodedimage.setSampleSize(DownsampleUtil.determineSampleSize(imagerequest, encodedimage));
                    }
                    doDecode(encodedimage, flag);
                }
            }

        }


        class _cls2 extends BaseProducerContextCallbacks
        {

            public void onIsIntermediateResultExpectedChanged()
            {
                if(mProducerContext.isIntermediateResultExpected())
                    mJobScheduler.scheduleJob();
            }

        }

    }


    public DecodeProducer(ByteArrayPool bytearraypool, Executor executor, ImageDecoder imagedecoder, ProgressiveJpegConfig progressivejpegconfig, boolean flag, boolean flag1, Producer producer)
    {
        mByteArrayPool = (ByteArrayPool)Preconditions.checkNotNull(bytearraypool);
        mExecutor = (Executor)Preconditions.checkNotNull(executor);
        mImageDecoder = (ImageDecoder)Preconditions.checkNotNull(imagedecoder);
        mProgressiveJpegConfig = (ProgressiveJpegConfig)Preconditions.checkNotNull(progressivejpegconfig);
        mDownsampleEnabled = flag;
        mDownsampleEnabledForNetwork = flag1;
        mInputProducer = (Producer)Preconditions.checkNotNull(producer);
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        if(!UriUtil.isNetworkUri(producercontext.getImageRequest().getSourceUri()))
            consumer = new LocalImagesProgressiveDecoder(consumer, producercontext);
        else
            consumer = new NetworkImagesProgressiveDecoder(consumer, producercontext, new ProgressiveJpegParser(mByteArrayPool), mProgressiveJpegConfig);
        mInputProducer.produceResults(consumer, producercontext);
    }

    private static final String BITMAP_SIZE_KEY = "bitmapSize";
    private static final String HAS_GOOD_QUALITY_KEY = "hasGoodQuality";
    private static final String IS_FINAL_KEY = "isFinal";
    public static final String PRODUCER_NAME = "DecodeProducer";
    private final ByteArrayPool mByteArrayPool;
    private final boolean mDownsampleEnabled;
    private final boolean mDownsampleEnabledForNetwork;
    private final Executor mExecutor;
    private final ImageDecoder mImageDecoder;
    private final Producer mInputProducer;
    private final ProgressiveJpegConfig mProgressiveJpegConfig;





}
