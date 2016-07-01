

package com.facebook.cache.common;

import com.facebook.common.internal.Preconditions;

// Referenced classes of package com.facebook.cache.common:
//            CacheKey

public class SimpleCacheKey
    implements CacheKey
{

    public SimpleCacheKey(String s)
    {
        mKey = (String)Preconditions.checkNotNull(s);
    }

    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        if(obj instanceof SimpleCacheKey)
        {
            obj = (SimpleCacheKey)obj;
            return mKey.equals(((SimpleCacheKey) (obj)).mKey);
        } else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return mKey.hashCode();
    }

    public String toString()
    {
        return mKey;
    }

    final String mKey;
}
