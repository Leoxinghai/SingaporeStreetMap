

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            DefaultBitmapPoolParams, NoOpPoolStatsTracker, DefaultFlexByteArrayPoolParams, DefaultNativeMemoryChunkPoolParams,
//            DefaultByteArrayPoolParams, PoolParams, PoolStatsTracker

public class PoolConfig
{
    public static class Builder
    {

        public PoolConfig build()
        {
            return new PoolConfig(this);
        }

        public Builder setBitmapPoolParams(PoolParams poolparams)
        {
            mBitmapPoolParams = (PoolParams)Preconditions.checkNotNull(poolparams);
            return this;
        }

        public Builder setBitmapPoolStatsTracker(PoolStatsTracker poolstatstracker)
        {
            mBitmapPoolStatsTracker = (PoolStatsTracker)Preconditions.checkNotNull(poolstatstracker);
            return this;
        }

        public Builder setFlexByteArrayPoolParams(PoolParams poolparams)
        {
            mFlexByteArrayPoolParams = poolparams;
            return this;
        }

        public Builder setMemoryTrimmableRegistry(MemoryTrimmableRegistry memorytrimmableregistry)
        {
            mMemoryTrimmableRegistry = memorytrimmableregistry;
            return this;
        }

        public Builder setNativeMemoryChunkPoolParams(PoolParams poolparams)
        {
            mNativeMemoryChunkPoolParams = (PoolParams)Preconditions.checkNotNull(poolparams);
            return this;
        }

        public Builder setNativeMemoryChunkPoolStatsTracker(PoolStatsTracker poolstatstracker)
        {
            mNativeMemoryChunkPoolStatsTracker = (PoolStatsTracker)Preconditions.checkNotNull(poolstatstracker);
            return this;
        }

        public Builder setSmallByteArrayPoolParams(PoolParams poolparams)
        {
            mSmallByteArrayPoolParams = (PoolParams)Preconditions.checkNotNull(poolparams);
            return this;
        }

        public Builder setSmallByteArrayPoolStatsTracker(PoolStatsTracker poolstatstracker)
        {
            mSmallByteArrayPoolStatsTracker = (PoolStatsTracker)Preconditions.checkNotNull(poolstatstracker);
            return this;
        }

        private PoolParams mBitmapPoolParams;
        private PoolStatsTracker mBitmapPoolStatsTracker;
        private PoolParams mFlexByteArrayPoolParams;
        private MemoryTrimmableRegistry mMemoryTrimmableRegistry;
        private PoolParams mNativeMemoryChunkPoolParams;
        private PoolStatsTracker mNativeMemoryChunkPoolStatsTracker;
        private PoolParams mSmallByteArrayPoolParams;
        private PoolStatsTracker mSmallByteArrayPoolStatsTracker;









        private Builder()
        {
        }

    }


    private PoolConfig(Builder builder)
    {
        Object obj;
        if(builder.mBitmapPoolParams == null)
            obj = DefaultBitmapPoolParams.get();
        else
            obj = builder.mBitmapPoolParams;
        mBitmapPoolParams = ((PoolParams) (obj));
        if(builder.mBitmapPoolStatsTracker == null)
            obj = NoOpPoolStatsTracker.getInstance();
        else
            obj = builder.mBitmapPoolStatsTracker;
        mBitmapPoolStatsTracker = ((PoolStatsTracker) (obj));
        if(builder.mFlexByteArrayPoolParams == null)
            obj = DefaultFlexByteArrayPoolParams.get();
        else
            obj = builder.mFlexByteArrayPoolParams;
        mFlexByteArrayPoolParams = ((PoolParams) (obj));
        if(builder.mMemoryTrimmableRegistry == null)
            obj = NoOpMemoryTrimmableRegistry.getInstance();
        else
            obj = builder.mMemoryTrimmableRegistry;
        mMemoryTrimmableRegistry = ((MemoryTrimmableRegistry) (obj));
        if(builder.mNativeMemoryChunkPoolParams == null)
            obj = DefaultNativeMemoryChunkPoolParams.get();
        else
            obj = builder.mNativeMemoryChunkPoolParams;
        mNativeMemoryChunkPoolParams = ((PoolParams) (obj));
        if(builder.mNativeMemoryChunkPoolStatsTracker == null)
            obj = NoOpPoolStatsTracker.getInstance();
        else
            obj = builder.mNativeMemoryChunkPoolStatsTracker;
        mNativeMemoryChunkPoolStatsTracker = ((PoolStatsTracker) (obj));
        if(builder.mSmallByteArrayPoolParams == null)
            obj = DefaultByteArrayPoolParams.get();
        else
            obj = builder.mSmallByteArrayPoolParams;
        mSmallByteArrayPoolParams = ((PoolParams) (obj));
        PoolStatsTracker temp;
        if(builder.mSmallByteArrayPoolStatsTracker == null)
            temp = NoOpPoolStatsTracker.getInstance();
        else
            temp = builder.mSmallByteArrayPoolStatsTracker;
        mSmallByteArrayPoolStatsTracker = temp;
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }

    public PoolParams getBitmapPoolParams()
    {
        return mBitmapPoolParams;
    }

    public PoolStatsTracker getBitmapPoolStatsTracker()
    {
        return mBitmapPoolStatsTracker;
    }

    public PoolParams getFlexByteArrayPoolParams()
    {
        return mFlexByteArrayPoolParams;
    }

    public MemoryTrimmableRegistry getMemoryTrimmableRegistry()
    {
        return mMemoryTrimmableRegistry;
    }

    public PoolParams getNativeMemoryChunkPoolParams()
    {
        return mNativeMemoryChunkPoolParams;
    }

    public PoolStatsTracker getNativeMemoryChunkPoolStatsTracker()
    {
        return mNativeMemoryChunkPoolStatsTracker;
    }

    public PoolParams getSmallByteArrayPoolParams()
    {
        return mSmallByteArrayPoolParams;
    }

    public PoolStatsTracker getSmallByteArrayPoolStatsTracker()
    {
        return mSmallByteArrayPoolStatsTracker;
    }

    private final PoolParams mBitmapPoolParams;
    private final PoolStatsTracker mBitmapPoolStatsTracker;
    private final PoolParams mFlexByteArrayPoolParams;
    private final MemoryTrimmableRegistry mMemoryTrimmableRegistry;
    private final PoolParams mNativeMemoryChunkPoolParams;
    private final PoolStatsTracker mNativeMemoryChunkPoolStatsTracker;
    private final PoolParams mSmallByteArrayPoolParams;
    private final PoolStatsTracker mSmallByteArrayPoolStatsTracker;
}
