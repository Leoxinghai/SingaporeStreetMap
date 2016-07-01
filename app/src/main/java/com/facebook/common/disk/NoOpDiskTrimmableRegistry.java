

package com.facebook.common.disk;


// Referenced classes of package com.facebook.common.disk:
//            DiskTrimmableRegistry, DiskTrimmable

public class NoOpDiskTrimmableRegistry
    implements DiskTrimmableRegistry
{

    private NoOpDiskTrimmableRegistry()
    {
    }

    public static NoOpDiskTrimmableRegistry getInstance()
    {
        NoOpDiskTrimmableRegistry noopdisktrimmableregistry;
        if(sInstance == null)
            sInstance = new NoOpDiskTrimmableRegistry();
        noopdisktrimmableregistry = sInstance;
        return noopdisktrimmableregistry;
    }

    public void registerDiskTrimmable(DiskTrimmable disktrimmable)
    {
    }

    public void unregisterDiskTrimmable(DiskTrimmable disktrimmable)
    {
    }

    private static NoOpDiskTrimmableRegistry sInstance = null;

}
