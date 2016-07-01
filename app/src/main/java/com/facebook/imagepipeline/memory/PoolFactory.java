

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PoolConfig, BitmapPool, FlexByteArrayPool, PoolParams,
//            NativeMemoryChunkPool, NativePooledByteBufferFactory, PooledByteStreams, SharedByteArray,
//            GenericByteArrayPool, PooledByteBufferFactory, ByteArrayPool

public class PoolFactory
{

    public PoolFactory(PoolConfig poolconfig)
    {
        mConfig = (PoolConfig)Preconditions.checkNotNull(poolconfig);
    }

    public BitmapPool getBitmapPool()
    {
        if(mBitmapPool == null)
            mBitmapPool = new BitmapPool(mConfig.getMemoryTrimmableRegistry(), mConfig.getBitmapPoolParams(), mConfig.getBitmapPoolStatsTracker());
        return mBitmapPool;
    }

    public FlexByteArrayPool getFlexByteArrayPool()
    {
        if(mFlexByteArrayPool == null)
            mFlexByteArrayPool = new FlexByteArrayPool(mConfig.getMemoryTrimmableRegistry(), mConfig.getFlexByteArrayPoolParams());
        return mFlexByteArrayPool;
    }

    public int getFlexByteArrayPoolMaxNumThreads()
    {
        return mConfig.getFlexByteArrayPoolParams().maxNumThreads;
    }

    public NativeMemoryChunkPool getNativeMemoryChunkPool()
    {
        if(mNativeMemoryChunkPool == null)
            mNativeMemoryChunkPool = new NativeMemoryChunkPool(mConfig.getMemoryTrimmableRegistry(), mConfig.getNativeMemoryChunkPoolParams(), mConfig.getNativeMemoryChunkPoolStatsTracker());
        return mNativeMemoryChunkPool;
    }

    public PooledByteBufferFactory getPooledByteBufferFactory()
    {
        if(mPooledByteBufferFactory == null)
            mPooledByteBufferFactory = new NativePooledByteBufferFactory(getNativeMemoryChunkPool(), getPooledByteStreams());
        return mPooledByteBufferFactory;
    }

    public PooledByteStreams getPooledByteStreams()
    {
        if(mPooledByteStreams == null)
            mPooledByteStreams = new PooledByteStreams(getSmallByteArrayPool());
        return mPooledByteStreams;
    }

    public SharedByteArray getSharedByteArray()
    {
        if(mSharedByteArray == null)
            mSharedByteArray = new SharedByteArray(mConfig.getMemoryTrimmableRegistry(), mConfig.getFlexByteArrayPoolParams());
        return mSharedByteArray;
    }

    public ByteArrayPool getSmallByteArrayPool()
    {
        if(mSmallByteArrayPool == null)
            mSmallByteArrayPool = new GenericByteArrayPool(mConfig.getMemoryTrimmableRegistry(), mConfig.getSmallByteArrayPoolParams(), mConfig.getSmallByteArrayPoolStatsTracker());
        return mSmallByteArrayPool;
    }

    private BitmapPool mBitmapPool;
    private final PoolConfig mConfig;
    private FlexByteArrayPool mFlexByteArrayPool;
    private NativeMemoryChunkPool mNativeMemoryChunkPool;
    private PooledByteBufferFactory mPooledByteBufferFactory;
    private PooledByteStreams mPooledByteStreams;
    private SharedByteArray mSharedByteArray;
    private ByteArrayPool mSmallByteArrayPool;
}
