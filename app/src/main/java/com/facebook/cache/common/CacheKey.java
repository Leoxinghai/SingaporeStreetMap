

package com.facebook.cache.common;


public interface CacheKey
{

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract String toString();
}
