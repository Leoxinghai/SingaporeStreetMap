

package com.facebook.cache.disk;

import com.facebook.cache.common.*;
import com.facebook.common.disk.DiskTrimmableRegistry;
import com.facebook.common.disk.NoOpDiskTrimmableRegistry;
import com.facebook.common.internal.*;
import java.io.File;

public class DiskCacheConfig
{
    public static class Builder
    {

        public DiskCacheConfig build()
        {
            return new DiskCacheConfig(this);
        }

        public Builder setBaseDirectoryName(String s)
        {
            mBaseDirectoryName = s;
            return this;
        }

        public Builder setBaseDirectoryPath(File file)
        {
            mBaseDirectoryPathSupplier = Suppliers.of(file);
            return this;
        }

        public Builder setBaseDirectoryPathSupplier(Supplier supplier)
        {
            mBaseDirectoryPathSupplier = supplier;
            return this;
        }

        public Builder setCacheErrorLogger(CacheErrorLogger cacheerrorlogger)
        {
            mCacheErrorLogger = cacheerrorlogger;
            return this;
        }

        public Builder setCacheEventListener(CacheEventListener cacheeventlistener)
        {
            mCacheEventListener = cacheeventlistener;
            return this;
        }

        public Builder setDiskTrimmableRegistry(DiskTrimmableRegistry disktrimmableregistry)
        {
            mDiskTrimmableRegistry = disktrimmableregistry;
            return this;
        }

        public Builder setMaxCacheSize(long l)
        {
            mMaxCacheSize = l;
            return this;
        }

        public Builder setMaxCacheSizeOnLowDiskSpace(long l)
        {
            mMaxCacheSizeOnLowDiskSpace = l;
            return this;
        }

        public Builder setMaxCacheSizeOnVeryLowDiskSpace(long l)
        {
            mMaxCacheSizeOnVeryLowDiskSpace = l;
            return this;
        }

        public Builder setVersion(int i)
        {
            mVersion = i;
            return this;
        }

        public String mBaseDirectoryName;
        public Supplier mBaseDirectoryPathSupplier;
        public CacheErrorLogger mCacheErrorLogger;
        public CacheEventListener mCacheEventListener;
        public DiskTrimmableRegistry mDiskTrimmableRegistry;
        public long mMaxCacheSize;
        public long mMaxCacheSizeOnLowDiskSpace;
        public long mMaxCacheSizeOnVeryLowDiskSpace;
        public int mVersion;

        private Builder()
        {
            mVersion = 1;
        }

    }


    private DiskCacheConfig(Builder builder)
    {
        mVersion = builder.mVersion;
        mBaseDirectoryName = (String)Preconditions.checkNotNull(builder.mBaseDirectoryName);
        mBaseDirectoryPathSupplier = (Supplier)Preconditions.checkNotNull(builder.mBaseDirectoryPathSupplier);
        mDefaultSizeLimit = builder.mMaxCacheSize;
        mLowDiskSpaceSizeLimit = builder.mMaxCacheSizeOnLowDiskSpace;
        mMinimumSizeLimit = builder.mMaxCacheSizeOnVeryLowDiskSpace;
        Object obj;
        if(builder.mCacheErrorLogger == null)
            obj = NoOpCacheErrorLogger.getInstance();
        else
            obj = builder.mCacheErrorLogger;
        mCacheErrorLogger = ((CacheErrorLogger) (obj));
        if(builder.mCacheEventListener == null)
            obj = NoOpCacheEventListener.getInstance();
        else
            obj = builder.mCacheEventListener;
        mCacheEventListener = ((CacheEventListener) (obj));
        if(builder.mDiskTrimmableRegistry == null)
            mDiskTrimmableRegistry = NoOpDiskTrimmableRegistry.getInstance();
        else
            mDiskTrimmableRegistry = builder.mDiskTrimmableRegistry;

    }


    public static Builder newBuilder()
    {
        return new Builder();
    }

    public String getBaseDirectoryName()
    {
        return mBaseDirectoryName;
    }

    public Supplier getBaseDirectoryPathSupplier()
    {
        return mBaseDirectoryPathSupplier;
    }

    public CacheErrorLogger getCacheErrorLogger()
    {
        return mCacheErrorLogger;
    }

    public CacheEventListener getCacheEventListener()
    {
        return mCacheEventListener;
    }

    public long getDefaultSizeLimit()
    {
        return mDefaultSizeLimit;
    }

    public DiskTrimmableRegistry getDiskTrimmableRegistry()
    {
        return mDiskTrimmableRegistry;
    }

    public long getLowDiskSpaceSizeLimit()
    {
        return mLowDiskSpaceSizeLimit;
    }

    public long getMinimumSizeLimit()
    {
        return mMinimumSizeLimit;
    }

    public int getVersion()
    {
        return mVersion;
    }

    private final String mBaseDirectoryName;
    private final Supplier mBaseDirectoryPathSupplier;
    private final CacheErrorLogger mCacheErrorLogger;
    private final CacheEventListener mCacheEventListener;
    private final long mDefaultSizeLimit;
    private final DiskTrimmableRegistry mDiskTrimmableRegistry;
    private final long mLowDiskSpaceSizeLimit;
    private final long mMinimumSizeLimit;
    private final int mVersion;
}
