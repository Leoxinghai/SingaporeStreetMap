// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import android.os.SystemClock;
import com.android.internal.util.Predicate;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            MemoryCache, CountingLruMap, MemoryCacheParams, ValueDescriptor

public class CountingMemoryCache
    implements MemoryCache, MemoryTrimmable
{
    public static interface CacheTrimStrategy
    {

        public abstract double getTrimRatio(MemoryTrimType memorytrimtype);
    }

    static class Entry
    {

        static Entry of(Object obj, CloseableReference closeablereference, EntryStateObserver entrystateobserver)
        {
            return new Entry(obj, closeablereference, entrystateobserver);
        }

        public int clientCount;
        public boolean isOrphan;
        public final Object key;
        public final EntryStateObserver observer;
        public final CloseableReference valueRef;

        private Entry(Object obj, CloseableReference closeablereference, EntryStateObserver entrystateobserver)
        {
            key = Preconditions.checkNotNull(obj);
            valueRef = (CloseableReference)Preconditions.checkNotNull(CloseableReference.cloneOrNull(closeablereference));
            clientCount = 0;
            isOrphan = false;
            observer = entrystateobserver;
        }
    }

    public static interface EntryStateObserver
    {

        public abstract void onExclusivityChanged(Object obj, boolean flag);
    }


    public CountingMemoryCache(ValueDescriptor valuedescriptor, CacheTrimStrategy cachetrimstrategy, Supplier supplier)
    {
        mValueDescriptor = valuedescriptor;
        mExclusiveEntries = new CountingLruMap(wrapValueDescriptor(valuedescriptor));
        mCachedEntries = new CountingLruMap(wrapValueDescriptor(valuedescriptor));
        mCacheTrimStrategy = cachetrimstrategy;
        mMemoryCacheParamsSupplier = supplier;
        mMemoryCacheParams = (MemoryCacheParams)mMemoryCacheParamsSupplier.get();
        mLastCacheParamsCheck = SystemClock.elapsedRealtime();
    }

    private boolean canCacheNewValue(Object obj)
    {
        boolean flag = true;
        int i = mValueDescriptor.getSizeInBytes(obj);
        if(i > mMemoryCacheParams.maxCacheEntrySize || getInUseCount() > mMemoryCacheParams.maxCacheEntries - 1)
            flag = false;
        else {
            int j;
            int k;
            j = getInUseSizeInBytes();
            k = mMemoryCacheParams.maxCacheSize;
            if (j > k - i)
                flag = false;
        }
        return flag;
    }

    private void decreaseClientCount(Entry entry)
    {
        Preconditions.checkNotNull(entry);
        boolean flag;
        if(entry.clientCount > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        entry.clientCount = entry.clientCount - 1;
        return;
    }

    private void increaseClientCount(Entry entry)
    {
        Preconditions.checkNotNull(entry);
        boolean flag;
        if(!entry.isOrphan)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        entry.clientCount = entry.clientCount + 1;
        return;
    }

    private void makeOrphan(Entry entry)
    {
        boolean flag = true;
        Preconditions.checkNotNull(entry);
        if(entry.isOrphan)
            flag = false;
        Preconditions.checkState(flag);
        entry.isOrphan = true;
        return;
    }

    private void makeOrphans(ArrayList arraylist)
    {
        if(arraylist != null) {
            for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); makeOrphan((Entry) iterator.next()))
                ;
        }
    }

    private boolean maybeAddToExclusives(Entry entry)
    {
        boolean flag = true;
        if(entry.isOrphan || entry.clientCount != 0) {
            flag = false;
        } else {
            mExclusiveEntries.put(entry.key, entry);
        }
        return flag;
    }

    private void maybeClose(ArrayList arraylist)
    {
        if(arraylist != null)
            for(Iterator iterator = arraylist.iterator(); iterator.hasNext(); CloseableReference.closeSafely(referenceToClose((Entry)iterator.next())));
    }

    private void maybeEvictEntries()
    {
        ArrayList arraylist;
        arraylist = trimExclusivelyOwnedEntries(Math.min(mMemoryCacheParams.maxEvictionQueueEntries, mMemoryCacheParams.maxCacheEntries - getInUseCount()), Math.min(mMemoryCacheParams.maxEvictionQueueSize, mMemoryCacheParams.maxCacheSize - getInUseSizeInBytes()));
        makeOrphans(arraylist);
        maybeClose(arraylist);
        maybeNotifyExclusiveEntryRemoval(arraylist);
        return;
    }

    private static void maybeNotifyExclusiveEntryInsertion(Entry entry)
    {
        if(entry != null && entry.observer != null)
            entry.observer.onExclusivityChanged(entry.key, true);
    }

    private static void maybeNotifyExclusiveEntryRemoval(Entry entry)
    {
        if(entry != null && entry.observer != null)
            entry.observer.onExclusivityChanged(entry.key, false);
    }

    private void maybeNotifyExclusiveEntryRemoval(ArrayList arraylist)
    {
        if(arraylist != null)
            for(Iterator iterator = arraylist.iterator(); iterator.hasNext(); maybeNotifyExclusiveEntryRemoval((Entry)iterator.next()));
    }

    private void maybeUpdateCacheParams()
    {
        long l;
        long l1;
        long l2;
        l = mLastCacheParamsCheck;
        l1 = PARAMS_INTERCHECK_INTERVAL_MS;
        l2 = SystemClock.elapsedRealtime();
        if(l + l1 <= l2) {
            mLastCacheParamsCheck = SystemClock.elapsedRealtime();
            mMemoryCacheParams = (MemoryCacheParams)mMemoryCacheParamsSupplier.get();
        }
        return;
    }

    private CloseableReference newClientReference(final Entry entry)
    {
        increaseClientCount(entry);
        CloseableReference temp = CloseableReference.of(entry.valueRef.get(), new ResourceReleaser() {

            public void release(Object obj)
            {
                releaseClientReference(entry);
            }

        });
        return temp;
    }

    private CloseableReference referenceToClose(Entry entry)
    {
        Preconditions.checkNotNull(entry);
        CloseableReference temp;
        if(!entry.isOrphan || entry.clientCount != 0)
            temp = null;
        else
            temp = entry.valueRef;

        return temp;
    }

    private void releaseClientReference(Entry entry)
    {
        Preconditions.checkNotNull(entry);
        CloseableReference closeablereference;
        boolean flag;
        decreaseClientCount(entry);
        flag = maybeAddToExclusives(entry);
        closeablereference = referenceToClose(entry);
        CloseableReference.closeSafely(closeablereference);
        if(!flag)
            entry = null;
        maybeNotifyExclusiveEntryInsertion(entry);
        maybeUpdateCacheParams();
        maybeEvictEntries();
        return;
    }


// JavaClassFileOutputException: Prev chain is broken

    private ValueDescriptor wrapValueDescriptor(final ValueDescriptor evictableValueDescriptor)
    {
        return new ValueDescriptor() {

            public int getSizeInBytes(Entry entry)
            {
                return evictableValueDescriptor.getSizeInBytes(entry.valueRef.get());
            }

            public int getSizeInBytes(Object obj)
            {
                return getSizeInBytes((Entry)obj);
            }

        };
    }

    public CloseableReference cache(Object obj, CloseableReference closeablereference)
    {
        return cache(obj, closeablereference, null);
    }

    public CloseableReference cache(Object obj, CloseableReference closeablereference, EntryStateObserver entrystateobserver)
    {
        CloseableReference closeablereference1;
        Preconditions.checkNotNull(obj);
        Preconditions.checkNotNull(closeablereference);
        maybeUpdateCacheParams();
        closeablereference1 = null;
        CloseableReference closeablereference2 = null;
        Entry entry;
        Entry entry1;
        entry = (Entry)mExclusiveEntries.remove(obj);
        entry1 = (Entry)mCachedEntries.remove(obj);
        if(entry1 != null) {
            makeOrphan(entry1);
            closeablereference1 = referenceToClose(entry1);
        }
        if(canCacheNewValue(closeablereference.get()))
        {
            entry1 = Entry.of(obj, closeablereference, entrystateobserver);
            mCachedEntries.put(obj, entry1);
            closeablereference2 = newClientReference(entry1);
        }
        CloseableReference.closeSafely(closeablereference1);
        maybeNotifyExclusiveEntryRemoval(entry);
        maybeEvictEntries();
        return closeablereference2;
    }

    public void clear()
    {
        ArrayList arraylist;
        ArrayList arraylist1;
        arraylist = mExclusiveEntries.clear();
        arraylist1 = mCachedEntries.clear();
        makeOrphans(arraylist1);
        maybeClose(arraylist1);
        maybeNotifyExclusiveEntryRemoval(arraylist);
        maybeUpdateCacheParams();
        return;
    }

    public boolean contains(Predicate predicate)
    {
        boolean flag = mCachedEntries.getMatchingEntries(predicate).isEmpty();
        if(!flag)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public CloseableReference get(Object obj)
    {
        Preconditions.checkNotNull(obj);
        Object obj1 = null;
        Entry entry;
        Entry entry1;
        entry = (Entry)mExclusiveEntries.remove(obj);
        entry1 = (Entry)mCachedEntries.get(obj);
        obj = obj1;
        if(entry1 != null) {
            obj = newClientReference(entry1);
        }
        maybeNotifyExclusiveEntryRemoval(entry);
        maybeUpdateCacheParams();
        maybeEvictEntries();
        return ((CloseableReference) (obj));
    }

    public int getCount()
    {
        int i = mCachedEntries.getCount();
        return i;
    }

    public int getEvictionQueueCount()
    {
        int i = mExclusiveEntries.getCount();
        return i;
    }

    public int getEvictionQueueSizeInBytes()
    {
        int i = mExclusiveEntries.getSizeInBytes();
        return i;
    }

    public int getInUseCount()
    {
        int i;
        int j;
        i = mCachedEntries.getCount();
        j = mExclusiveEntries.getCount();
        return i - j;
    }

    public int getInUseSizeInBytes()
    {
        int i;
        int j;
        i = mCachedEntries.getSizeInBytes();
        j = mExclusiveEntries.getSizeInBytes();
        return i - j;
    }

    public int getSizeInBytes()
    {
        int i = mCachedEntries.getSizeInBytes();
        return i;
    }

    public int removeAll(Predicate predicate)
    {
        ArrayList arraylist;
        arraylist = mExclusiveEntries.removeAll(predicate);
        ArrayList temp  = mCachedEntries.removeAll(predicate);
        makeOrphans(temp);
        maybeClose(temp);
        maybeNotifyExclusiveEntryRemoval(arraylist);
        maybeUpdateCacheParams();
        maybeEvictEntries();
        return temp.size();
    }

    public CloseableReference reuse(Object obj)
    {
        CloseableReference closeablereference;
        Preconditions.checkNotNull(obj);
        closeablereference = null;
        boolean flag = false;
        Entry entry = (Entry)mExclusiveEntries.remove(obj);
        if(entry != null)
            obj = (Entry)mCachedEntries.remove(obj);
        Preconditions.checkNotNull(obj);
        boolean flag1;
        if(((Entry) (obj)).clientCount == 0)
            flag1 = true;
        else
            flag1 = false;
        Preconditions.checkState(flag1);
        closeablereference = ((Entry) (obj)).valueRef;
        flag = true;
        if(flag)
            maybeNotifyExclusiveEntryRemoval(entry);
        return closeablereference;
    }

    public void trim(MemoryTrimType memorytrimtype)
    {
        double d = mCacheTrimStrategy.getTrimRatio(memorytrimtype);
        ArrayList arraylist = trimExclusivelyOwnedEntries(0x7fffffff, Math.max(0, (int) ((double) mCachedEntries.getSizeInBytes() * (1.0D - d)) - getInUseSizeInBytes()));
        makeOrphans(arraylist);
        maybeClose(arraylist);
        maybeNotifyExclusiveEntryRemoval(arraylist);
        maybeUpdateCacheParams();
        maybeEvictEntries();
        return;
    }

    private synchronized ArrayList trimExclusivelyOwnedEntries(int count, int size) {
        count = Math.max(count, 0);
        size = Math.max(size, 0);
        // fast path without array allocation if no eviction is necessary
        if (mExclusiveEntries.getCount() <= count && mExclusiveEntries.getSizeInBytes() <= size) {
            return null;
        }
        ArrayList oldEntries = new ArrayList();
        while (mExclusiveEntries.getCount() > count || mExclusiveEntries.getSizeInBytes() > size) {
            Object key = mExclusiveEntries.getFirstKey();
            mExclusiveEntries.remove(key);
            oldEntries.add(mCachedEntries.remove(key));
        }
        return oldEntries;
    }

    static final long PARAMS_INTERCHECK_INTERVAL_MS;
    private final CacheTrimStrategy mCacheTrimStrategy;
    final CountingLruMap mCachedEntries;
    final CountingLruMap mExclusiveEntries;
    private long mLastCacheParamsCheck;
    protected MemoryCacheParams mMemoryCacheParams;
    private final Supplier mMemoryCacheParamsSupplier;
    private final ValueDescriptor mValueDescriptor;

    static
    {
        PARAMS_INTERCHECK_INTERVAL_MS = TimeUnit.MINUTES.toMillis(5L);
    }

}
