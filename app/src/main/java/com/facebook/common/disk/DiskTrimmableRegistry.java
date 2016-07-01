

package com.facebook.common.disk;


// Referenced classes of package com.facebook.common.disk:
//            DiskTrimmable

public interface DiskTrimmableRegistry
{

    public abstract void registerDiskTrimmable(DiskTrimmable disktrimmable);

    public abstract void unregisterDiskTrimmable(DiskTrimmable disktrimmable);
}
