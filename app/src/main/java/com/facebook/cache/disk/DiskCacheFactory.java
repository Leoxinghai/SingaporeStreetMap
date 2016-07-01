

package com.facebook.cache.disk;


// Referenced classes of package com.facebook.cache.disk:
//            DiskStorageCache, DiskCacheConfig, DefaultDiskStorageSupplier, DiskStorageSupplier

public class DiskCacheFactory
{

    public DiskCacheFactory()
    {
    }

    public static DiskStorageCache newDiskStorageCache(DiskCacheConfig diskcacheconfig)
    {
        return new DiskStorageCache(newDiskStorageSupplier(diskcacheconfig), new DiskStorageCache.Params(diskcacheconfig.getMinimumSizeLimit(), diskcacheconfig.getLowDiskSpaceSizeLimit(), diskcacheconfig.getDefaultSizeLimit()), diskcacheconfig.getCacheEventListener(), diskcacheconfig.getCacheErrorLogger(), diskcacheconfig.getDiskTrimmableRegistry());
    }

    private static DiskStorageSupplier newDiskStorageSupplier(DiskCacheConfig diskcacheconfig)
    {
        return new DefaultDiskStorageSupplier(diskcacheconfig.getVersion(), diskcacheconfig.getBaseDirectoryPathSupplier(), diskcacheconfig.getBaseDirectoryName(), diskcacheconfig.getCacheErrorLogger());
    }
}
