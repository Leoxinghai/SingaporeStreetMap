

package com.facebook.cache.disk;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.common.disk.DiskTrimmable;
import java.io.IOException;

public interface FileCache
    extends DiskTrimmable
{

    public abstract void clearAll();

    public abstract long clearOldEntries(long l);

    public abstract DiskStorage.DiskDumpInfo getDumpInfo()
        throws IOException;

    public abstract BinaryResource getResource(CacheKey cachekey);

    public abstract long getSize();

    public abstract boolean hasKey(CacheKey cachekey);

    public abstract BinaryResource insert(CacheKey cachekey, WriterCallback writercallback)
        throws IOException;

    public abstract boolean isEnabled();

    public abstract boolean probe(CacheKey cachekey);

    public abstract void remove(CacheKey cachekey);
}
