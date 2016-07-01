// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.cache.disk;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheErrorLogger;
import com.facebook.cache.common.CacheEventListener;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.common.disk.DiskTrimmable;
import com.facebook.common.disk.DiskTrimmableRegistry;
import com.facebook.common.logging.FLog;
import com.facebook.common.statfs.StatFsHelper;
import com.facebook.common.time.Clock;
import com.facebook.common.time.SystemClock;
import com.facebook.common.util.SecureHashUtil;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.facebook.cache.disk:
//            FileCache, DiskStorageSupplier, DiskStorage

public class DiskStorageCache
    implements FileCache, DiskTrimmable
{
    static class CacheStats
    {

        public long getCount()
        {
            long l = mCount;
            return l;
        }

        public long getSize()
        {
            long l = mSize;
            return l;
        }

        public void increment(long l, long l1)
        {
            if(mInitialized)
            {
                mSize = mSize + l;
                mCount = mCount + l1;
            }
            return;
        }

        public boolean isInitialized()
        {
            boolean flag = mInitialized;
            return flag;
        }

        public void reset()
        {
            mInitialized = false;
            mCount = -1L;
            mSize = -1L;
            return;
        }

        public void set(long l, long l1)
        {
            mCount = l1;
            mSize = l;
            mInitialized = true;
            return;
        }

        private long mCount;
        private boolean mInitialized;
        private long mSize;

        CacheStats()
        {
            mInitialized = false;
            mSize = -1L;
            mCount = -1L;
        }
    }

    public static class Params
    {

        public final long mCacheSizeLimitMinimum;
        public final long mDefaultCacheSizeLimit;
        public final long mLowDiskSpaceCacheSizeLimit;

        public Params(long l, long l1, long l2)
        {
            mCacheSizeLimitMinimum = l;
            mLowDiskSpaceCacheSizeLimit = l1;
            mDefaultCacheSizeLimit = l2;
        }
    }

    private static class TimestampComparator
        implements Comparator
    {

        public int compare(DiskStorage.Entry entry, DiskStorage.Entry entry1)
        {
            long l;
            long l1;
            if(entry.getTimestamp() <= threshold)
                l = entry.getTimestamp();
            else
                l = 0L;
            if(entry1.getTimestamp() <= threshold)
                l1 = entry1.getTimestamp();
            else
                l1 = 0L;
            if(l < l1)
                return -1;
            return l1 <= l ? 0 : 1;
        }

        public int compare(Object obj, Object obj1)
        {
            return compare((DiskStorage.Entry)obj, (DiskStorage.Entry)obj1);
        }

        private final long threshold;

        public TimestampComparator(long l)
        {
            threshold = l;
        }
    }


    public DiskStorageCache(DiskStorageSupplier diskstoragesupplier, Params params, CacheEventListener cacheeventlistener, CacheErrorLogger cacheerrorlogger, DiskTrimmableRegistry disktrimmableregistry)
    {
        mLowDiskSpaceCacheSizeLimit = params.mLowDiskSpaceCacheSizeLimit;
        mDefaultCacheSizeLimit = params.mDefaultCacheSizeLimit;
        mCacheSizeLimit = params.mDefaultCacheSizeLimit;
        mStorageSupplier = diskstoragesupplier;
        mCacheSizeLastUpdateTime = -1L;
        mCacheEventListener = cacheeventlistener;
        mCacheSizeLimitMinimum = params.mCacheSizeLimitMinimum;
        mCacheErrorLogger = cacheerrorlogger;
        if(disktrimmableregistry != null)
            disktrimmableregistry.registerDiskTrimmable(this);
    }

    private void calcFileCacheSize()
    {
        int i;
        int j;
        int k;
        boolean flag;
        long l1;
        long l2;
        long l4;
        long l5;
        l1 = 0L;
        k = 0;
        flag = false;
        j = 0;
        i = 0;
        l2 = -1L;
        l4 = mClock.now();
        l5 = FUTURE_TIMESTAMP_THRESHOLD_MS;
        try {
            Iterator iterator = mStorageSupplier.get().getEntries().iterator();
            DiskStorage.Entry entry;

            for(;iterator.hasNext();) {
                entry = (DiskStorage.Entry) iterator.next();
                int l = k + 1;
                long l3;
                l3 = l1 + entry.getSize();
                k = l;
                l1 = l3;
                if (entry.getTimestamp() > l4 + l5) {
                    flag = true;
                    j++;
                    i = (int) ((long) i + entry.getSize());
                    l2 = Math.max(entry.getTimestamp() - l4, l2);
                    k = l;
                    l1 = l3;
                }
            }
        } catch (IOException ioexception) {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.GENERIC_IO, TAG, (new StringBuilder()).append("calcFileCacheSize: ").append(ioexception.getMessage()).toString(), ioexception);
            return;
        }



        if(flag) {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.READ_INVALID_ENTRY, TAG, (new StringBuilder()).append("Future timestamp found in ").append(j).append(" files , with a total size of ").append(i).append(" bytes, and a maximum time delta of ").append(l2).append("ms").toString(), null);
            mCacheStats.set(l1, k);
            return;
        }
    }

    private BinaryResource commitResource(String s, CacheKey cachekey, BinaryResource binaryresource)
        throws IOException
    {
        synchronized(mLock)
        {
            BinaryResource result = mStorageSupplier.get().commit(s, binaryresource, cachekey);
            mCacheStats.increment(result.size(), 1L);
            return result;
        }
    }

    private BinaryResource createTemporaryResource(String s, CacheKey cachekey)
        throws IOException
    {
        maybeEvictFilesInCacheDir();
        return mStorageSupplier.get().createTemporary(s, cachekey);
    }

    private void deleteTemporaryResource(BinaryResource binaryresource)
    {
        File file  = ((FileBinaryResource)binaryresource).getFile();
        if(binaryresource instanceof FileBinaryResource)
            if(file.exists())
            {
                FLog.e(TAG, "Temp file still on disk: %s ", new Object[] {
                    file
                });
                if(!file.delete())
                {
                    FLog.e(TAG, "Failed to delete temp file: %s", new Object[] {
                        file
                    });
                    return;
                }
            }
    }

    private void evictAboveSize(long l, com.facebook.cache.common.CacheEventListener.EvictionReason evictionreason)
        throws IOException
    {
        DiskStorage diskstorage = mStorageSupplier.get();
        Object obj;
        int i;
        long l1;
        long l2;
        try
        {
            obj = getSortedEntries(diskstorage.getEntries());
        }
        // Misplaced declaration of an exception variable
        catch(Exception reason)
        {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.EVICTION, TAG, (new StringBuilder()).append("evictAboveSize: ").append(reason.getMessage()).toString(), reason);
            throw reason;
        }
        l2 = mCacheStats.getSize();
        i = 0;
        l1 = 0L;
        obj = ((Collection) (obj)).iterator();
        DiskStorage.Entry entry;
        for(;((Iterator) (obj)).hasNext();)
        {
            entry = (DiskStorage.Entry)((Iterator) (obj)).next();
            if(l1 <= l2 - l) {
                long l3 = diskstorage.remove(entry);
                if (l3 > 0L) {
                    i++;
                    l1 += l3;
                }
            }
        }
        mCacheStats.increment(-l1, -i);
        diskstorage.purgeUnexpectedResources();
        reportEviction(evictionreason, i, l1);
        return;
    }

    private Collection getSortedEntries(Collection collection)
    {
        ArrayList temp = new ArrayList(collection);
        Collections.sort(temp, new TimestampComparator(mClock.now() + FUTURE_TIMESTAMP_THRESHOLD_MS));
        return collection;
    }

    private void maybeEvictFilesInCacheDir()
        throws IOException
    {
        Object obj = mLock;
        long l;
        boolean flag;
        flag = maybeUpdateFileCacheSize();
        updateFileCacheSizeLimit();
        l = mCacheStats.getSize();
        if(l <= mCacheSizeLimit || flag)
            return;
        mCacheStats.reset();
        maybeUpdateFileCacheSize();
        if(l > mCacheSizeLimit)
            evictAboveSize((mCacheSizeLimit * 9L) / 10L, com.facebook.cache.common.CacheEventListener.EvictionReason.CACHE_FULL);
    }

    private boolean maybeUpdateFileCacheSize()
    {
        boolean flag = false;
        long l = android.os.SystemClock.elapsedRealtime();
        if(!mCacheStats.isInitialized() || mCacheSizeLastUpdateTime == -1L || l - mCacheSizeLastUpdateTime > FILECACHE_SIZE_UPDATE_PERIOD_MS)
        {
            calcFileCacheSize();
            mCacheSizeLastUpdateTime = l;
            flag = true;
        }
        return flag;
    }

    private void reportEviction(com.facebook.cache.common.CacheEventListener.EvictionReason evictionreason, int i, long l)
    {
        mCacheEventListener.onEviction(evictionreason, i, l);
    }

    private void trimBy(double d) {
        Object obj = mLock;
        mCacheStats.reset();
        maybeUpdateFileCacheSize();
        long l = mCacheStats.getSize();
        try {
            evictAboveSize(l - (long) ((double) l * d), com.facebook.cache.common.CacheEventListener.EvictionReason.CACHE_MANAGER_TRIMMED);
            return;
        } catch (IOException ioex) {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.EVICTION, TAG, (new StringBuilder()).append("trimBy: ").append(((IOException) (ioex)).getMessage()).toString(), ((Throwable) (ioex)));
        }
    }

    private void updateFileCacheSizeLimit()
    {
        if(mStatFsHelper.testLowDiskSpace(com.facebook.common.statfs.StatFsHelper.StorageType.INTERNAL, mDefaultCacheSizeLimit - mCacheStats.getSize()))
        {
            mCacheSizeLimit = mLowDiskSpaceCacheSizeLimit;
            return;
        } else
        {
            mCacheSizeLimit = mDefaultCacheSizeLimit;
            return;
        }
    }

    public void clearAll()
    {
        Object obj = mLock;
        try {
            mStorageSupplier.get().clearAll();
        }  catch(IOException ioex) {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.EVICTION, TAG, (new StringBuilder()).append("clearAll: ").append(((IOException) (ioex)).getMessage()).toString(), ((Throwable) (ioex)));
        }
        mCacheStats.reset();
        return;
    }

    public long clearOldEntries(long l) {
        long l1 = 0L;
        Object obj = mLock;
        long l2 = l1;
        long l4 = mClock.now();
        l2 = l1;
        l2 = l1;
        int i;
        long l3;
        i = 0;
        l3 = 0L;
        l2 = l1;
        l2 = l1;


        try {
            DiskStorage diskstorage = mStorageSupplier.get();
            Object obj2 = diskstorage.getEntries();
            obj2 = ((Collection) (obj2)).iterator();
            for (; ((Iterator) (obj2)).hasNext(); ) {
                l2 = l1;
                DiskStorage.Entry entry = (DiskStorage.Entry) ((Iterator) (obj2)).next();
                l2 = l1;
                long l5 = Math.max(1L, Math.abs(l4 - entry.getTimestamp()));
                if (l5 < l) {
                    l2 = l1;
                    l1 = Math.max(l1, l5);
                } else {
                    l2 = l1;
                    l5 = diskstorage.remove(entry);
                    if (l5 > 0L) {
                        i++;
                        l3 += l5;
                    }
                }
            }

            l2 = l1;
            diskstorage.purgeUnexpectedResources();
            l2 = l1;
            Object obj1;
            if (i <= 0) {
//                mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.EVICTION, TAG, (new StringBuilder()).append("clearOldEntries: ").append(((IOException) (obj1)).getMessage()).toString(), ((Throwable) (obj1)));
            } else {
                l2 = l1;
                maybeUpdateFileCacheSize();
                l2 = l1;
                mCacheStats.increment(-l3, -i);
                l2 = l1;
                reportEviction(com.facebook.cache.common.CacheEventListener.EvictionReason.CONTENT_STALE, i, l3);
                l2 = l1;
            }
        } catch(IOException ioex) {
            ioex.printStackTrace();
        }
        return l2;
    }

    public DiskStorage.DiskDumpInfo getDumpInfo()
        throws IOException
    {
        return mStorageSupplier.get().getDumpInfo();
    }

    public BinaryResource getResource(CacheKey cachekey)
    {
        Object obj = mLock;
        try {
            BinaryResource result = mStorageSupplier.get().getResource(getResourceId(cachekey), cachekey);
            if(result != null)
                mCacheEventListener.onHit();
            else
                mCacheEventListener.onMiss();

            return result;
        }catch(IOException ioEx)
        {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.GENERIC_IO, TAG, "getResource", ioEx);
        }
        mCacheEventListener.onReadException();
        return null;
    }

    String getResourceId(CacheKey cachekey)
    {
        try
        {
            String result = SecureHashUtil.makeSHA1HashBase64(cachekey.toString().getBytes("UTF-8"));
            return result;
        } catch(IOException ioex)
        {
            throw new RuntimeException(ioex);
        }
    }

    public long getSize()
    {
        return mCacheStats.getSize();
    }

    public boolean hasKey(CacheKey cachekey)
    {
        boolean flag;
        try
        {
            flag = mStorageSupplier.get().contains(getResourceId(cachekey), cachekey);
        }
        // Misplaced declaration of an exception variable
        catch(IOException ioEx)
        {
            return false;
        }
        return flag;
    }

    public BinaryResource insert(CacheKey cachekey, WriterCallback writercallback)
        throws IOException
    {
        try
        {
            String s;
            mCacheEventListener.onWriteAttempt();
            s = getResourceId(cachekey);
            BinaryResource binaryresource = createTemporaryResource(s, cachekey);
            mStorageSupplier.get().updateResource(s, binaryresource, writercallback, cachekey);
            BinaryResource temp = commitResource(s, cachekey, binaryresource);
            deleteTemporaryResource(binaryresource);
            return temp;
        }
        catch(IOException ioEx)
        {
            mCacheEventListener.onWriteException();
            FLog.d(TAG, "Failed inserting a file into the cache", cachekey);
            throw ioEx;
        }
    }

    public boolean isEnabled()
    {
        boolean flag;
        try
        {
            flag = mStorageSupplier.get().isEnabled();
        }
        catch(IOException ioexception)
        {
            return false;
        }
        return flag;
    }

    public boolean probe(CacheKey cachekey)
    {
        boolean flag;
        try
        {
            synchronized(mLock)
            {
                flag = mStorageSupplier.get().touch(getResourceId(cachekey), cachekey);
            }
            return flag;
        }
        catch(IOException ioEx)
        {
            mCacheEventListener.onReadException();
        }
        return false;
    }

    public void remove(CacheKey cachekey)
    {
        Object obj = mLock;
        try {
            mStorageSupplier.get().remove(getResourceId(cachekey));
            return;
        }catch(IOException iex) {
            iex.printStackTrace();
        }
    }

    public void trimToMinimum()
    {
        Object obj = mLock;
        long l;
        maybeUpdateFileCacheSize();
        l = mCacheStats.getSize();
        if(mCacheSizeLimitMinimum <= 0L || l <= 0L)
            return;
        if(l < mCacheSizeLimitMinimum)
            return;
        double d = 1.0D - (double)mCacheSizeLimitMinimum / (double)l;
        if(d > 0.02D)
            trimBy(d);
        return;
    }

    public void trimToNothing()
    {
        clearAll();
    }

    private static final long FILECACHE_SIZE_UPDATE_PERIOD_MS;
    private static final long FUTURE_TIMESTAMP_THRESHOLD_MS;
    public static final int START_OF_VERSIONING = 1;
    private static final Class TAG = DiskStorageCache.class;
    private static final double TRIMMING_LOWER_BOUND = 0.02D;
    private static final long UNINITIALIZED = -1L;
    private final CacheErrorLogger mCacheErrorLogger;
    private final CacheEventListener mCacheEventListener;
    private long mCacheSizeLastUpdateTime;
    private long mCacheSizeLimit;
    private final long mCacheSizeLimitMinimum;
    private final CacheStats mCacheStats = new CacheStats();
    private final Clock mClock = SystemClock.get();
    private final long mDefaultCacheSizeLimit;
    private final Object mLock = new Object();
    private final long mLowDiskSpaceCacheSizeLimit;
    private final StatFsHelper mStatFsHelper = StatFsHelper.getInstance();
    private final DiskStorageSupplier mStorageSupplier;

    static
    {
        FUTURE_TIMESTAMP_THRESHOLD_MS = TimeUnit.HOURS.toMillis(2L);
        FILECACHE_SIZE_UPDATE_PERIOD_MS = TimeUnit.MINUTES.toMillis(30L);
    }
}
