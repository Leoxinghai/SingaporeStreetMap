// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            CountingMemoryCache, CountingLruMap, MemoryCacheParams

public class CountingMemoryCacheInspector
{
    public static class DumpInfo
    {

        public void release()
        {
            for(Iterator iterator = lruEntries.iterator(); iterator.hasNext(); ((DumpInfoEntry)iterator.next()).release());
            for(Iterator iterator1 = sharedEntries.iterator(); iterator1.hasNext(); ((DumpInfoEntry)iterator1.next()).release());
        }

        public final List lruEntries = new ArrayList();
        public final int lruSize;
        public final int maxEntriesCount;
        public final int maxEntrySize;
        public final int maxSize;
        public final List sharedEntries = new ArrayList();
        public final int size;

        public DumpInfo(int i, int j, MemoryCacheParams memorycacheparams)
        {
            maxSize = memorycacheparams.maxCacheSize;
            maxEntriesCount = memorycacheparams.maxCacheEntries;
            maxEntrySize = memorycacheparams.maxCacheEntrySize;
            size = i;
            lruSize = j;
        }
    }

    public static class DumpInfoEntry
    {

        public void release()
        {
            CloseableReference.closeSafely(value);
        }

        public final Object key;
        public final CloseableReference value;

        public DumpInfoEntry(Object obj, CloseableReference closeablereference)
        {
            key = Preconditions.checkNotNull(obj);
            value = CloseableReference.cloneOrNull(closeablereference);
        }
    }


    public CountingMemoryCacheInspector(CountingMemoryCache countingmemorycache)
    {
        mCountingBitmapCache = countingmemorycache;
    }

    public DumpInfo dumpCacheContent()
    {
        CountingMemoryCache countingmemorycache = mCountingBitmapCache;
        Object obj;
        Iterator iterator;
        obj = new DumpInfo(mCountingBitmapCache.getSizeInBytes(), mCountingBitmapCache.getEvictionQueueSizeInBytes(), mCountingBitmapCache.mMemoryCacheParams);
        iterator = mCountingBitmapCache.mCachedEntries.getMatchingEntries(null).iterator();
        DumpInfoEntry dumpinfoentry;
        for(;iterator.hasNext();) {
            CountingMemoryCache.Entry entry = (CountingMemoryCache.Entry)((java.util.Map.Entry)iterator.next()).getValue();
            dumpinfoentry = new DumpInfoEntry(entry.key, entry.valueRef);
            if(entry.clientCount <= 0)
                ((DumpInfo) (obj)).lruEntries.add(dumpinfoentry);
            else
                ((DumpInfo) (obj)).sharedEntries.add(dumpinfoentry);
        }
        return ((DumpInfo) (obj));
    }

    private final CountingMemoryCache mCountingBitmapCache;
}
