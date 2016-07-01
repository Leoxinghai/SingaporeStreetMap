

package com.facebook.imagepipeline.cache;

import com.android.internal.util.Predicate;
import com.facebook.common.references.CloseableReference;

public interface MemoryCache
{

    public abstract CloseableReference cache(Object obj, CloseableReference closeablereference);

    public abstract boolean contains(Predicate predicate);

    public abstract CloseableReference get(Object obj);

    public abstract int removeAll(Predicate predicate);
}
