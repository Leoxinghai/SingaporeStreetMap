// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.core;

import android.app.ActivityManager;
import android.content.Context;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.imagepipeline.animated.factory.AnimatedImageFactory;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.cache.*;
import com.facebook.imagepipeline.decoder.*;
import com.facebook.imagepipeline.memory.PoolConfig;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher;
import com.facebook.imagepipeline.producers.NetworkFetcher;
import java.io.File;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.core:
//            DefaultExecutorSupplier, ExecutorSupplier

public class ImagePipelineConfig
{
    public static class Builder
    {

        public ImagePipelineConfig build()
        {
            return new ImagePipelineConfig(this);
        }

        public Builder setAnimatedImageFactory(AnimatedImageFactory animatedimagefactory)
        {
            mAnimatedImageFactory = animatedimagefactory;
            return this;
        }

        public Builder setBitmapMemoryCacheParamsSupplier(Supplier supplier)
        {
            mBitmapMemoryCacheParamsSupplier = (Supplier)Preconditions.checkNotNull(supplier);
            return this;
        }

        public Builder setBitmapsConfig(android.graphics.Bitmap.Config config)
        {
            mBitmapConfig = config;
            return this;
        }

        public Builder setCacheKeyFactory(CacheKeyFactory cachekeyfactory)
        {
            mCacheKeyFactory = cachekeyfactory;
            return this;
        }

        public Builder setDecodeFileDescriptorEnabled(boolean flag)
        {
            mDecodeFileDescriptorEnabled = flag;
            return this;
        }

        public Builder setDownsampleEnabled(boolean flag)
        {
            mDownsampleEnabled = flag;
            return this;
        }

        public Builder setEncodedMemoryCacheParamsSupplier(Supplier supplier)
        {
            mEncodedMemoryCacheParamsSupplier = (Supplier)Preconditions.checkNotNull(supplier);
            return this;
        }

        public Builder setExecutorSupplier(ExecutorSupplier executorsupplier)
        {
            mExecutorSupplier = executorsupplier;
            return this;
        }

        public Builder setImageCacheStatsTracker(ImageCacheStatsTracker imagecachestatstracker)
        {
            mImageCacheStatsTracker = imagecachestatstracker;
            return this;
        }

        public Builder setImageDecoder(ImageDecoder imagedecoder)
        {
            mImageDecoder = imagedecoder;
            return this;
        }

        public Builder setIsPrefetchEnabledSupplier(Supplier supplier)
        {
            mIsPrefetchEnabledSupplier = supplier;
            return this;
        }

        public Builder setMainDiskCacheConfig(DiskCacheConfig diskcacheconfig)
        {
            mMainDiskCacheConfig = diskcacheconfig;
            return this;
        }

        public Builder setMemoryTrimmableRegistry(MemoryTrimmableRegistry memorytrimmableregistry)
        {
            mMemoryTrimmableRegistry = memorytrimmableregistry;
            return this;
        }

        public Builder setNetworkFetcher(NetworkFetcher networkfetcher)
        {
            mNetworkFetcher = networkfetcher;
            return this;
        }

        public Builder setPlatformBitmapFactory(PlatformBitmapFactory platformbitmapfactory)
        {
            mPlatformBitmapFactory = platformbitmapfactory;
            return this;
        }

        public Builder setPoolFactory(PoolFactory poolfactory)
        {
            mPoolFactory = poolfactory;
            return this;
        }

        public Builder setProgressiveJpegConfig(ProgressiveJpegConfig progressivejpegconfig)
        {
            mProgressiveJpegConfig = progressivejpegconfig;
            return this;
        }

        public Builder setRequestListeners(Set set)
        {
            mRequestListeners = set;
            return this;
        }

        public Builder setResizeAndRotateEnabledForNetwork(boolean flag)
        {
            mResizeAndRotateEnabledForNetwork = flag;
            return this;
        }

        public Builder setSmallImageDiskCacheConfig(DiskCacheConfig diskcacheconfig)
        {
            mSmallImageDiskCacheConfig = diskcacheconfig;
            return this;
        }

        private AnimatedImageFactory mAnimatedImageFactory;
        private android.graphics.Bitmap.Config mBitmapConfig;
        private Supplier mBitmapMemoryCacheParamsSupplier;
        private CacheKeyFactory mCacheKeyFactory;
        private final Context mContext;
        private boolean mDecodeFileDescriptorEnabled;
        private boolean mDownsampleEnabled;
        private Supplier mEncodedMemoryCacheParamsSupplier;
        private ExecutorSupplier mExecutorSupplier;
        private ImageCacheStatsTracker mImageCacheStatsTracker;
        private ImageDecoder mImageDecoder;
        private Supplier mIsPrefetchEnabledSupplier;
        private DiskCacheConfig mMainDiskCacheConfig;
        private MemoryTrimmableRegistry mMemoryTrimmableRegistry;
        private NetworkFetcher mNetworkFetcher;
        private PlatformBitmapFactory mPlatformBitmapFactory;
        private PoolFactory mPoolFactory;
        private ProgressiveJpegConfig mProgressiveJpegConfig;
        private Set mRequestListeners;
        private boolean mResizeAndRotateEnabledForNetwork;
        private DiskCacheConfig mSmallImageDiskCacheConfig;


        private Builder(Context context)
        {
            mDownsampleEnabled = false;
            mDecodeFileDescriptorEnabled = mDownsampleEnabled;
            mResizeAndRotateEnabledForNetwork = true;
            mContext = (Context)Preconditions.checkNotNull(context);
        }

    }


    private ImagePipelineConfig(Builder builder)
    {
        mAnimatedImageFactory = builder.mAnimatedImageFactory;
        Object obj;
        int i;
        boolean flag;
        if(builder.mBitmapMemoryCacheParamsSupplier == null)
            obj = new DefaultBitmapMemoryCacheParamsSupplier((ActivityManager)builder.mContext.getSystemService(Context.ACTIVITY_SERVICE));
        else
            obj = builder.mBitmapMemoryCacheParamsSupplier;
        mBitmapMemoryCacheParamsSupplier = ((Supplier) (obj));
        if(builder.mBitmapConfig == null)
            obj = android.graphics.Bitmap.Config.ARGB_8888;
        else
            obj = builder.mBitmapConfig;
        mBitmapConfig = ((android.graphics.Bitmap.Config) (obj));
        if(builder.mCacheKeyFactory == null)
            obj = DefaultCacheKeyFactory.getInstance();
        else
            obj = builder.mCacheKeyFactory;
        mCacheKeyFactory = ((CacheKeyFactory) (obj));
        mContext = (Context)Preconditions.checkNotNull(builder.mContext);
        if(builder.mDownsampleEnabled && builder.mDecodeFileDescriptorEnabled)
            flag = true;
        else
            flag = false;
        mDecodeFileDescriptorEnabled = flag;
        mDownsampleEnabled = builder.mDownsampleEnabled;
        if(builder.mEncodedMemoryCacheParamsSupplier == null)
            obj = new DefaultEncodedMemoryCacheParamsSupplier();
        else
            obj = builder.mEncodedMemoryCacheParamsSupplier;
        mEncodedMemoryCacheParamsSupplier = ((Supplier) (obj));
        if(builder.mImageCacheStatsTracker == null)
            obj = NoOpImageCacheStatsTracker.getInstance();
        else
            obj = builder.mImageCacheStatsTracker;
        mImageCacheStatsTracker = ((ImageCacheStatsTracker) (obj));
        mImageDecoder = builder.mImageDecoder;
        if(builder.mIsPrefetchEnabledSupplier == null)
            obj = new Supplier() {

                public Boolean get()
                {
                    return Boolean.valueOf(true);
                }

            };
        else
            obj = builder.mIsPrefetchEnabledSupplier;
        mIsPrefetchEnabledSupplier = ((Supplier) (obj));
        if(builder.mMainDiskCacheConfig == null)
            obj = getDefaultMainDiskCacheConfig(builder.mContext);
        else
            obj = builder.mMainDiskCacheConfig;
        mMainDiskCacheConfig = ((DiskCacheConfig) (obj));
        if(builder.mMemoryTrimmableRegistry == null)
            obj = NoOpMemoryTrimmableRegistry.getInstance();
        else
            obj = builder.mMemoryTrimmableRegistry;
        mMemoryTrimmableRegistry = ((MemoryTrimmableRegistry) (obj));
        if(builder.mNetworkFetcher == null)
            obj = new HttpUrlConnectionNetworkFetcher();
        else
            obj = builder.mNetworkFetcher;
        mNetworkFetcher = ((NetworkFetcher) (obj));
        mPlatformBitmapFactory = builder.mPlatformBitmapFactory;
        if(builder.mPoolFactory == null)
            obj = new PoolFactory(PoolConfig.newBuilder().build());
        else
            obj = builder.mPoolFactory;
        mPoolFactory = ((PoolFactory) (obj));
        if(builder.mProgressiveJpegConfig == null)
            obj = new SimpleProgressiveJpegConfig();
        else
            obj = builder.mProgressiveJpegConfig;
        mProgressiveJpegConfig = ((ProgressiveJpegConfig) (obj));
        if(builder.mRequestListeners == null)
            obj = new HashSet();
        else
            obj = builder.mRequestListeners;
        mRequestListeners = ((Set) (obj));
        mResizeAndRotateEnabledForNetwork = builder.mResizeAndRotateEnabledForNetwork;
        if(builder.mSmallImageDiskCacheConfig == null)
            obj = mMainDiskCacheConfig;
        else
            obj = builder.mSmallImageDiskCacheConfig;
        mSmallImageDiskCacheConfig = ((DiskCacheConfig) (obj));
        i = mPoolFactory.getFlexByteArrayPoolMaxNumThreads();

        if(builder.mExecutorSupplier == null)
            mExecutorSupplier = new DefaultExecutorSupplier(i);
        else
            mExecutorSupplier = builder.mExecutorSupplier;
    }


    private static DiskCacheConfig getDefaultMainDiskCacheConfig(final Context context)
    {
        return DiskCacheConfig.newBuilder().setBaseDirectoryPathSupplier(new Supplier() {

            public File get()
            {
                return context.getApplicationContext().getCacheDir();
            }

        }
).setBaseDirectoryName("image_cache").setMaxCacheSize(0x2800000L).setMaxCacheSizeOnLowDiskSpace(0xa00000L).setMaxCacheSizeOnVeryLowDiskSpace(0x200000L).build();
    }

    public static Builder newBuilder(Context context)
    {
        return new Builder(context);
    }

    public AnimatedImageFactory getAnimatedImageFactory()
    {
        return mAnimatedImageFactory;
    }

    public android.graphics.Bitmap.Config getBitmapConfig()
    {
        return mBitmapConfig;
    }

    public Supplier getBitmapMemoryCacheParamsSupplier()
    {
        return mBitmapMemoryCacheParamsSupplier;
    }

    public CacheKeyFactory getCacheKeyFactory()
    {
        return mCacheKeyFactory;
    }

    public Context getContext()
    {
        return mContext;
    }

    public Supplier getEncodedMemoryCacheParamsSupplier()
    {
        return mEncodedMemoryCacheParamsSupplier;
    }

    public ExecutorSupplier getExecutorSupplier()
    {
        return mExecutorSupplier;
    }

    public ImageCacheStatsTracker getImageCacheStatsTracker()
    {
        return mImageCacheStatsTracker;
    }

    public ImageDecoder getImageDecoder()
    {
        return mImageDecoder;
    }

    public Supplier getIsPrefetchEnabledSupplier()
    {
        return mIsPrefetchEnabledSupplier;
    }

    public DiskCacheConfig getMainDiskCacheConfig()
    {
        return mMainDiskCacheConfig;
    }

    public MemoryTrimmableRegistry getMemoryTrimmableRegistry()
    {
        return mMemoryTrimmableRegistry;
    }

    public NetworkFetcher getNetworkFetcher()
    {
        return mNetworkFetcher;
    }

    public PlatformBitmapFactory getPlatformBitmapFactory()
    {
        return mPlatformBitmapFactory;
    }

    public PoolFactory getPoolFactory()
    {
        return mPoolFactory;
    }

    public ProgressiveJpegConfig getProgressiveJpegConfig()
    {
        return mProgressiveJpegConfig;
    }

    public Set getRequestListeners()
    {
        return Collections.unmodifiableSet(mRequestListeners);
    }

    public DiskCacheConfig getSmallImageDiskCacheConfig()
    {
        return mSmallImageDiskCacheConfig;
    }

    public boolean isDecodeFileDescriptorEnabled()
    {
        return mDecodeFileDescriptorEnabled;
    }

    public boolean isDownsampleEnabled()
    {
        return mDownsampleEnabled;
    }

    public boolean isResizeAndRotateEnabledForNetwork()
    {
        return mResizeAndRotateEnabledForNetwork;
    }

    private final AnimatedImageFactory mAnimatedImageFactory;
    private final android.graphics.Bitmap.Config mBitmapConfig;
    private final Supplier mBitmapMemoryCacheParamsSupplier;
    private final CacheKeyFactory mCacheKeyFactory;
    private final Context mContext;
    private final boolean mDecodeFileDescriptorEnabled;
    private final boolean mDownsampleEnabled;
    private final Supplier mEncodedMemoryCacheParamsSupplier;
    private final ExecutorSupplier mExecutorSupplier;
    private final ImageCacheStatsTracker mImageCacheStatsTracker;
    private final ImageDecoder mImageDecoder;
    private final Supplier mIsPrefetchEnabledSupplier;
    private final DiskCacheConfig mMainDiskCacheConfig;
    private final MemoryTrimmableRegistry mMemoryTrimmableRegistry;
    private final NetworkFetcher mNetworkFetcher;
    private final PlatformBitmapFactory mPlatformBitmapFactory;
    private final PoolFactory mPoolFactory;
    private final ProgressiveJpegConfig mProgressiveJpegConfig;
    private final Set mRequestListeners;
    private final boolean mResizeAndRotateEnabledForNetwork;
    private final DiskCacheConfig mSmallImageDiskCacheConfig;
}
