// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.impl;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Objects;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class AnimatedFrameCache
{
    static class FrameKey
        implements CacheKey
    {

        public boolean equals(Object obj)
        {
            if(obj != this)
                if(obj instanceof FrameKey)
                {
                    if(mImageCacheKey != ((FrameKey) (obj = (FrameKey)obj)).mImageCacheKey || mFrameIndex != ((FrameKey) (obj)).mFrameIndex)
                        return false;
                } else
                {
                    return false;
                }
            return true;
        }

        public int hashCode()
        {
            return mImageCacheKey.hashCode() * 1013 + mFrameIndex;
        }

        public String toString()
        {
            return Objects.toStringHelper(this).add("imageCacheKey", mImageCacheKey).add("frameIndex", mFrameIndex).toString();
        }

        private final int mFrameIndex;
        private final CacheKey mImageCacheKey;

        public FrameKey(CacheKey cachekey, int i)
        {
            mImageCacheKey = cachekey;
            mFrameIndex = i;
        }
    }


    public AnimatedFrameCache(CacheKey cachekey, CountingMemoryCache countingmemorycache)
    {
        mImageCacheKey = cachekey;
        mBackingCache = countingmemorycache;
    }

    private FrameKey keyFor(int i)
    {
        return new FrameKey(mImageCacheKey, i);
    }

    private CacheKey popFirstFreeItemKey()
    {
        CacheKey cachekey = null;
        Iterator iterator = mFreeItemsPool.iterator();
        if(iterator.hasNext())
        {
            cachekey = (CacheKey)iterator.next();
            iterator.remove();
        }
        return cachekey;
    }

    public CloseableReference cache(int i, CloseableReference closeablereference)
    {
        return mBackingCache.cache(keyFor(i), closeablereference, mEntryStateObserver);
    }

    public CloseableReference get(int i)
    {
        return mBackingCache.get(keyFor(i));
    }

    public CloseableReference getForReuse()
    {
        Object obj;
        do
        {
            obj = popFirstFreeItemKey();
            if(obj == null)
                return null;
            obj = mBackingCache.reuse(obj);
        } while(obj == null);
        return ((CloseableReference) (obj));
    }

    public void onReusabilityChange(CacheKey cachekey, boolean flag)
    {
        if(!flag)
            mFreeItemsPool.remove(cachekey);
        else
            mFreeItemsPool.add(cachekey);
        return;
    }

    private final CountingMemoryCache mBackingCache;
    private final com.facebook.imagepipeline.cache.CountingMemoryCache.EntryStateObserver mEntryStateObserver = new com.facebook.imagepipeline.cache.CountingMemoryCache.EntryStateObserver() {

        public void onExclusivityChanged(CacheKey cachekey1, boolean flag)
        {
            onReusabilityChange(cachekey1, flag);
        }

        public void onExclusivityChanged(Object obj, boolean flag)
        {
            onExclusivityChanged((CacheKey)obj, flag);
        }

    };
    private final LinkedHashSet mFreeItemsPool = new LinkedHashSet();
    private final CacheKey mImageCacheKey;
}
