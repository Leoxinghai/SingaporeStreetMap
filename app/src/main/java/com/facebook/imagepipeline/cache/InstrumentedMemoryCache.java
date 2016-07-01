

package com.facebook.imagepipeline.cache;

import com.android.internal.util.Predicate;
import com.facebook.common.references.CloseableReference;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            MemoryCache, MemoryCacheTracker

public class InstrumentedMemoryCache
    implements MemoryCache
{

    public InstrumentedMemoryCache(MemoryCache memorycache, MemoryCacheTracker memorycachetracker)
    {
        mDelegate = memorycache;
        mTracker = memorycachetracker;
    }

    public CloseableReference cache(Object obj, CloseableReference closeablereference)
    {
        mTracker.onCachePut();
        return mDelegate.cache(obj, closeablereference);
    }

    public boolean contains(Predicate predicate)
    {
        return mDelegate.contains(predicate);
    }

    public CloseableReference get(Object obj)
    {
        obj = mDelegate.get(obj);
        if(obj == null)
        {
            mTracker.onCacheMiss();
            return ((CloseableReference) (obj));
        } else
        {
            mTracker.onCacheHit();
            return ((CloseableReference) (obj));
        }
    }

    public int removeAll(Predicate predicate)
    {
        return mDelegate.removeAll(predicate);
    }

    private final MemoryCache mDelegate;
    private final MemoryCacheTracker mTracker;
}
