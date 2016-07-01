

package com.facebook.imagepipeline.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.cache.*;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.memory.ByteArrayPool;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.producers.*;

// Referenced classes of package com.facebook.imagepipeline.core:
//            ExecutorSupplier

public class ProducerFactory
{

    public ProducerFactory(Context context, ByteArrayPool bytearraypool, ImageDecoder imagedecoder, ProgressiveJpegConfig progressivejpegconfig, boolean flag, boolean flag1, ExecutorSupplier executorsupplier,
            PooledByteBufferFactory pooledbytebufferfactory, MemoryCache memorycache, MemoryCache memorycache1, BufferedDiskCache buffereddiskcache, BufferedDiskCache buffereddiskcache1, CacheKeyFactory cachekeyfactory, PlatformBitmapFactory platformbitmapfactory,
            boolean flag2)
    {
        mContentResolver = context.getApplicationContext().getContentResolver();
        mResources = context.getApplicationContext().getResources();
        mAssetManager = context.getApplicationContext().getAssets();
        mByteArrayPool = bytearraypool;
        mImageDecoder = imagedecoder;
        mProgressiveJpegConfig = progressivejpegconfig;
        mDownsampleEnabled = flag;
        mResizeAndRotateEnabledForNetwork = flag1;
        mExecutorSupplier = executorsupplier;
        mPooledByteBufferFactory = pooledbytebufferfactory;
        mBitmapMemoryCache = memorycache;
        mEncodedMemoryCache = memorycache1;
        mDefaultBufferedDiskCache = buffereddiskcache;
        mSmallImageBufferedDiskCache = buffereddiskcache1;
        mCacheKeyFactory = cachekeyfactory;
        mPlatformBitmapFactory = platformbitmapfactory;
        mDecodeFileDescriptorEnabled = flag2;
    }

    public static AddImageTransformMetaDataProducer newAddImageTransformMetaDataProducer(Producer producer)
    {
        return new AddImageTransformMetaDataProducer(producer);
    }

    public static BranchOnSeparateImagesProducer newBranchOnSeparateImagesProducer(Producer producer, Producer producer1)
    {
        return new BranchOnSeparateImagesProducer(producer, producer1);
    }

    public static NullProducer newNullProducer()
    {
        return new NullProducer();
    }

    public static SwallowResultProducer newSwallowResultProducer(Producer producer)
    {
        return new SwallowResultProducer(producer);
    }

    public ThreadHandoffProducer newBackgroundThreadHandoffProducer(Producer producer)
    {
        return new ThreadHandoffProducer(mExecutorSupplier.forLightweightBackgroundTasks(), producer);
    }

    public BitmapMemoryCacheGetProducer newBitmapMemoryCacheGetProducer(Producer producer)
    {
        return new BitmapMemoryCacheGetProducer(mBitmapMemoryCache, mCacheKeyFactory, producer);
    }

    public BitmapMemoryCacheKeyMultiplexProducer newBitmapMemoryCacheKeyMultiplexProducer(Producer producer)
    {
        return new BitmapMemoryCacheKeyMultiplexProducer(mCacheKeyFactory, producer);
    }

    public BitmapMemoryCacheProducer newBitmapMemoryCacheProducer(Producer producer)
    {
        return new BitmapMemoryCacheProducer(mBitmapMemoryCache, mCacheKeyFactory, producer);
    }

    public LocalContentUriFetchProducer newContentUriFetchProducer()
    {
        return new LocalContentUriFetchProducer(mExecutorSupplier.forLocalStorageRead(), mPooledByteBufferFactory, mContentResolver, mDecodeFileDescriptorEnabled);
    }

    public DataFetchProducer newDataFetchProducer()
    {
        return new DataFetchProducer(mPooledByteBufferFactory, mDecodeFileDescriptorEnabled);
    }

    public DecodeProducer newDecodeProducer(Producer producer)
    {
        return new DecodeProducer(mByteArrayPool, mExecutorSupplier.forDecode(), mImageDecoder, mProgressiveJpegConfig, mDownsampleEnabled, mResizeAndRotateEnabledForNetwork, producer);
    }

    public DiskCacheProducer newDiskCacheProducer(Producer producer)
    {
        return new DiskCacheProducer(mDefaultBufferedDiskCache, mSmallImageBufferedDiskCache, mCacheKeyFactory, producer);
    }

    public EncodedCacheKeyMultiplexProducer newEncodedCacheKeyMultiplexProducer(Producer producer)
    {
        return new EncodedCacheKeyMultiplexProducer(mCacheKeyFactory, producer);
    }

    public EncodedMemoryCacheProducer newEncodedMemoryCacheProducer(Producer producer)
    {
        return new EncodedMemoryCacheProducer(mEncodedMemoryCache, mCacheKeyFactory, producer);
    }

    public LocalAssetFetchProducer newLocalAssetFetchProducer()
    {
        return new LocalAssetFetchProducer(mExecutorSupplier.forLocalStorageRead(), mPooledByteBufferFactory, mAssetManager, mDecodeFileDescriptorEnabled);
    }

    public LocalExifThumbnailProducer newLocalExifThumbnailProducer()
    {
        return new LocalExifThumbnailProducer(mExecutorSupplier.forLocalStorageRead(), mPooledByteBufferFactory, mContentResolver);
    }

    public LocalFileFetchProducer newLocalFileFetchProducer()
    {
        return new LocalFileFetchProducer(mExecutorSupplier.forLocalStorageRead(), mPooledByteBufferFactory, mDecodeFileDescriptorEnabled);
    }

    public LocalResourceFetchProducer newLocalResourceFetchProducer()
    {
        return new LocalResourceFetchProducer(mExecutorSupplier.forLocalStorageRead(), mPooledByteBufferFactory, mResources, mDecodeFileDescriptorEnabled);
    }

    public LocalVideoThumbnailProducer newLocalVideoThumbnailProducer()
    {
        return new LocalVideoThumbnailProducer(mExecutorSupplier.forLocalStorageRead());
    }

    public NetworkFetchProducer newNetworkFetchProducer(NetworkFetcher networkfetcher)
    {
        return new NetworkFetchProducer(mPooledByteBufferFactory, mByteArrayPool, networkfetcher);
    }

    public PostprocessedBitmapMemoryCacheProducer newPostprocessorBitmapMemoryCacheProducer(Producer producer)
    {
        return new PostprocessedBitmapMemoryCacheProducer(mBitmapMemoryCache, mCacheKeyFactory, producer);
    }

    public PostprocessorProducer newPostprocessorProducer(Producer producer)
    {
        return new PostprocessorProducer(producer, mPlatformBitmapFactory, mExecutorSupplier.forBackgroundTasks());
    }

    public ResizeAndRotateProducer newResizeAndRotateProducer(Producer producer)
    {
        return new ResizeAndRotateProducer(mExecutorSupplier.forBackgroundTasks(), mPooledByteBufferFactory, producer);
    }

    public ThrottlingProducer newThrottlingProducer(int i, Producer producer)
    {
        return new ThrottlingProducer(i, mExecutorSupplier.forLightweightBackgroundTasks(), producer);
    }

    public WebpTranscodeProducer newWebpTranscodeProducer(Producer producer)
    {
        return new WebpTranscodeProducer(mExecutorSupplier.forBackgroundTasks(), mPooledByteBufferFactory, producer);
    }

    private AssetManager mAssetManager;
    private final MemoryCache mBitmapMemoryCache;
    private final ByteArrayPool mByteArrayPool;
    private final CacheKeyFactory mCacheKeyFactory;
    private ContentResolver mContentResolver;
    private final boolean mDecodeFileDescriptorEnabled;
    private final BufferedDiskCache mDefaultBufferedDiskCache;
    private final boolean mDownsampleEnabled;
    private final MemoryCache mEncodedMemoryCache;
    private final ExecutorSupplier mExecutorSupplier;
    private final ImageDecoder mImageDecoder;
    private final PlatformBitmapFactory mPlatformBitmapFactory;
    private final PooledByteBufferFactory mPooledByteBufferFactory;
    private final ProgressiveJpegConfig mProgressiveJpegConfig;
    private final boolean mResizeAndRotateEnabledForNetwork;
    private Resources mResources;
    private final BufferedDiskCache mSmallImageBufferedDiskCache;
}
