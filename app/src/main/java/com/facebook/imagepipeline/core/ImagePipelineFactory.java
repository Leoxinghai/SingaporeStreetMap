// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.core;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import com.facebook.cache.disk.DiskCacheFactory;
import com.facebook.cache.disk.DiskStorageCache;
import com.facebook.common.executors.*;
import com.facebook.common.internal.AndroidPredicates;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.time.MonotonicClock;
import com.facebook.common.time.RealtimeSinceBootClock;
import com.facebook.imagepipeline.animated.base.*;
import com.facebook.imagepipeline.animated.factory.AnimatedDrawableFactory;
import com.facebook.imagepipeline.animated.factory.AnimatedImageFactory;
import com.facebook.imagepipeline.animated.impl.*;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;
import com.facebook.imagepipeline.bitmaps.*;
import com.facebook.imagepipeline.cache.*;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.imagepipeline.platform.*;
import java.util.concurrent.ScheduledExecutorService;

// Referenced classes of package com.facebook.imagepipeline.core:
//            ImagePipelineConfig, ExecutorSupplier, ProducerFactory, ProducerSequenceFactory,
//            ImagePipeline

public class ImagePipelineFactory
{

    public ImagePipelineFactory(ImagePipelineConfig imagepipelineconfig)
    {
        mConfig = (ImagePipelineConfig)Preconditions.checkNotNull(imagepipelineconfig);
    }

    public static AnimatedDrawableFactory buildAnimatedDrawableFactory(SerialExecutorService serialexecutorservice, ActivityManager activitymanager, AnimatedDrawableUtil animateddrawableutil, AnimatedDrawableBackendProvider animateddrawablebackendprovider, ScheduledExecutorService scheduledexecutorservice, MonotonicClock monotonicclock, Resources resources)
    {
        final SerialExecutorService serialExecutorService = serialexecutorservice;
        final ActivityManager activityManager = activitymanager;
        final AnimatedDrawableUtil animatedDrawableUtil = animateddrawableutil;
        final MonotonicClock monotonicClock = monotonicclock;
        return new AnimatedDrawableFactory(animateddrawablebackendprovider, new AnimatedDrawableCachingBackendImplProvider() {

            public AnimatedDrawableCachingBackendImpl get(AnimatedDrawableBackend animateddrawablebackend, AnimatedDrawableOptions animateddrawableoptions)
            {
                return new AnimatedDrawableCachingBackendImpl(serialExecutorService, activityManager, animatedDrawableUtil, monotonicClock, animateddrawablebackend, animateddrawableoptions);
            }

        }
, animateddrawableutil, scheduledexecutorservice, resources);
    }

    public static AnimatedImageFactory buildAnimatedImageFactory(AnimatedDrawableUtil animateddrawableutil, PlatformBitmapFactory platformbitmapfactory)
    {
        final AnimatedDrawableUtil animatedDrawableUtil = animateddrawableutil;
        return new AnimatedImageFactory(new AnimatedDrawableBackendProvider() {

            public AnimatedDrawableBackend get(AnimatedImageResult animatedimageresult, Rect rect)
            {
                return new AnimatedDrawableBackendImpl(animatedDrawableUtil, animatedimageresult, rect);
            }
        }, platformbitmapfactory);
    }

    public static PlatformBitmapFactory buildPlatformBitmapFactory(PoolFactory poolfactory, PlatformDecoder platformdecoder)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            return new ArtBitmapFactory(poolfactory.getBitmapPool());
        if(android.os.Build.VERSION.SDK_INT >= 11)
            return new HoneycombBitmapFactory(new EmptyJpegGenerator(poolfactory.getPooledByteBufferFactory()), platformdecoder);
        else
            return new GingerbreadBitmapFactory();
    }

    public static PlatformDecoder buildPlatformDecoder(PoolFactory poolfactory)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            return new ArtDecoder(poolfactory.getBitmapPool(), poolfactory.getFlexByteArrayPoolMaxNumThreads());
        else
            return new KitKatPurgeableDecoder(poolfactory.getFlexByteArrayPool());
    }

    private AnimatedDrawableUtil getAnimatedDrawableUtil()
    {
        if(mAnimatedDrawableUtil == null)
            mAnimatedDrawableUtil = new AnimatedDrawableUtil();
        return mAnimatedDrawableUtil;
    }

    private AnimatedImageFactory getAnimatedImageFactory()
    {
        if(mAnimatedImageFactory == null)
            if(mConfig.getAnimatedImageFactory() != null)
                mAnimatedImageFactory = mConfig.getAnimatedImageFactory();
            else
                mAnimatedImageFactory = buildAnimatedImageFactory(getAnimatedDrawableUtil(), getPlatformBitmapFactory());
        return mAnimatedImageFactory;
    }

    private ImageDecoder getImageDecoder()
    {
        if(mImageDecoder == null)
            if(mConfig.getImageDecoder() != null)
                mImageDecoder = mConfig.getImageDecoder();
            else
                mImageDecoder = new ImageDecoder(getAnimatedImageFactory(), getPlatformDecoder(), mConfig.getBitmapConfig());
        return mImageDecoder;
    }

    public static ImagePipelineFactory getInstance()
    {
        return (ImagePipelineFactory)Preconditions.checkNotNull(sInstance, "ImagePipelineFactory was not initialized!");
    }

    private BufferedDiskCache getMainBufferedDiskCache()
    {
        if(mMainBufferedDiskCache == null)
            mMainBufferedDiskCache = new BufferedDiskCache(getMainDiskStorageCache(), mConfig.getPoolFactory().getPooledByteBufferFactory(), mConfig.getPoolFactory().getPooledByteStreams(), mConfig.getExecutorSupplier().forLocalStorageRead(), mConfig.getExecutorSupplier().forLocalStorageWrite(), mConfig.getImageCacheStatsTracker());
        return mMainBufferedDiskCache;
    }

    private ProducerFactory getProducerFactory()
    {
        if(mProducerFactory == null)
            mProducerFactory = new ProducerFactory(mConfig.getContext(), mConfig.getPoolFactory().getSmallByteArrayPool(), getImageDecoder(), mConfig.getProgressiveJpegConfig(), mConfig.isDownsampleEnabled(), mConfig.isResizeAndRotateEnabledForNetwork(), mConfig.getExecutorSupplier(), mConfig.getPoolFactory().getPooledByteBufferFactory(), getBitmapMemoryCache(), getEncodedMemoryCache(), getMainBufferedDiskCache(), getSmallImageBufferedDiskCache(), mConfig.getCacheKeyFactory(), getPlatformBitmapFactory(), mConfig.isDecodeFileDescriptorEnabled());
        return mProducerFactory;
    }

    private ProducerSequenceFactory getProducerSequenceFactory()
    {
        if(mProducerSequenceFactory == null)
            mProducerSequenceFactory = new ProducerSequenceFactory(getProducerFactory(), mConfig.getNetworkFetcher(), mConfig.isResizeAndRotateEnabledForNetwork(), mConfig.isDownsampleEnabled());
        return mProducerSequenceFactory;
    }

    private BufferedDiskCache getSmallImageBufferedDiskCache()
    {
        if(mSmallImageBufferedDiskCache == null)
            mSmallImageBufferedDiskCache = new BufferedDiskCache(getSmallImageDiskStorageCache(), mConfig.getPoolFactory().getPooledByteBufferFactory(), mConfig.getPoolFactory().getPooledByteStreams(), mConfig.getExecutorSupplier().forLocalStorageRead(), mConfig.getExecutorSupplier().forLocalStorageWrite(), mConfig.getImageCacheStatsTracker());
        return mSmallImageBufferedDiskCache;
    }

    public static void initialize(Context context)
    {
        initialize(ImagePipelineConfig.newBuilder(context).build());
    }

    public static void initialize(ImagePipelineConfig imagepipelineconfig)
    {
        sInstance = new ImagePipelineFactory(imagepipelineconfig);
    }

    public static void shutDown()
    {
        if(sInstance != null)
        {
            sInstance.getBitmapMemoryCache().removeAll(AndroidPredicates.True());
            sInstance.getEncodedMemoryCache().removeAll(AndroidPredicates.True());
            sInstance = null;
        }
    }

    public AnimatedDrawableBackendProvider getAnimatedDrawableBackendProvider()
    {
        if(mAnimatedDrawableBackendProvider == null)
            mAnimatedDrawableBackendProvider = new AnimatedDrawableBackendProvider() {

                public AnimatedDrawableBackend get(AnimatedImageResult animatedimageresult, Rect rect)
                {
                    return new AnimatedDrawableBackendImpl(getAnimatedDrawableUtil(), animatedimageresult, rect);
                }

            };
        return mAnimatedDrawableBackendProvider;
    }

    public AnimatedDrawableFactory getAnimatedDrawableFactory()
    {
        if(mAnimatedDrawableFactory == null)
            mAnimatedDrawableFactory = buildAnimatedDrawableFactory(new DefaultSerialExecutorService(mConfig.getExecutorSupplier().forDecode()), (ActivityManager)mConfig.getContext().getSystemService(Context.ACTIVITY_SERVICE), getAnimatedDrawableUtil(), getAnimatedDrawableBackendProvider(), UiThreadImmediateExecutorService.getInstance(), RealtimeSinceBootClock.get(), mConfig.getContext().getResources());
        return mAnimatedDrawableFactory;
    }

    public CountingMemoryCache getBitmapCountingMemoryCache()
    {
        if(mBitmapCountingMemoryCache == null)
            mBitmapCountingMemoryCache = BitmapCountingMemoryCacheFactory.get(mConfig.getBitmapMemoryCacheParamsSupplier(), mConfig.getMemoryTrimmableRegistry());
        return mBitmapCountingMemoryCache;
    }

    public MemoryCache getBitmapMemoryCache()
    {
        if(mBitmapMemoryCache == null)
            mBitmapMemoryCache = BitmapMemoryCacheFactory.get(getBitmapCountingMemoryCache(), mConfig.getImageCacheStatsTracker());
        return mBitmapMemoryCache;
    }

    public CountingMemoryCache getEncodedCountingMemoryCache()
    {
        if(mEncodedCountingMemoryCache == null)
            mEncodedCountingMemoryCache = EncodedCountingMemoryCacheFactory.get(mConfig.getEncodedMemoryCacheParamsSupplier(), mConfig.getMemoryTrimmableRegistry());
        return mEncodedCountingMemoryCache;
    }

    public MemoryCache getEncodedMemoryCache()
    {
        if(mEncodedMemoryCache == null)
            mEncodedMemoryCache = EncodedMemoryCacheFactory.get(getEncodedCountingMemoryCache(), mConfig.getImageCacheStatsTracker());
        return mEncodedMemoryCache;
    }

    public ImagePipeline getImagePipeline()
    {
        if(mImagePipeline == null)
            mImagePipeline = new ImagePipeline(getProducerSequenceFactory(), mConfig.getRequestListeners(), mConfig.getIsPrefetchEnabledSupplier(), getBitmapMemoryCache(), getEncodedMemoryCache(), getMainBufferedDiskCache(), getSmallImageBufferedDiskCache(), mConfig.getCacheKeyFactory());
        return mImagePipeline;
    }

    public DiskStorageCache getMainDiskStorageCache()
    {
        if(mMainDiskStorageCache == null)
            mMainDiskStorageCache = DiskCacheFactory.newDiskStorageCache(mConfig.getMainDiskCacheConfig());
        return mMainDiskStorageCache;
    }

    public PlatformBitmapFactory getPlatformBitmapFactory()
    {
        if(mPlatformBitmapFactory == null)
            mPlatformBitmapFactory = buildPlatformBitmapFactory(mConfig.getPoolFactory(), getPlatformDecoder());
        return mPlatformBitmapFactory;
    }

    public PlatformDecoder getPlatformDecoder()
    {
        if(mPlatformDecoder == null)
            mPlatformDecoder = buildPlatformDecoder(mConfig.getPoolFactory());
        return mPlatformDecoder;
    }

    public DiskStorageCache getSmallImageDiskStorageCache()
    {
        if(mSmallImageDiskStorageCache == null)
            mSmallImageDiskStorageCache = DiskCacheFactory.newDiskStorageCache(mConfig.getSmallImageDiskCacheConfig());
        return mSmallImageDiskStorageCache;
    }

    private static ImagePipelineFactory sInstance = null;
    private AnimatedDrawableBackendProvider mAnimatedDrawableBackendProvider;
    private AnimatedDrawableFactory mAnimatedDrawableFactory;
    private AnimatedDrawableUtil mAnimatedDrawableUtil;
    private AnimatedImageFactory mAnimatedImageFactory;
    private CountingMemoryCache mBitmapCountingMemoryCache;
    private MemoryCache mBitmapMemoryCache;
    private final ImagePipelineConfig mConfig;
    private CountingMemoryCache mEncodedCountingMemoryCache;
    private MemoryCache mEncodedMemoryCache;
    private ImageDecoder mImageDecoder;
    private ImagePipeline mImagePipeline;
    private BufferedDiskCache mMainBufferedDiskCache;
    private DiskStorageCache mMainDiskStorageCache;
    private PlatformBitmapFactory mPlatformBitmapFactory;
    private PlatformDecoder mPlatformDecoder;
    private ProducerFactory mProducerFactory;
    private ProducerSequenceFactory mProducerSequenceFactory;
    private BufferedDiskCache mSmallImageBufferedDiskCache;
    private DiskStorageCache mSmallImageDiskStorageCache;


}
