

package com.facebook.cache.disk;

import java.io.IOException;

// Referenced classes of package com.facebook.cache.disk:
//            DiskStorage

public interface DiskStorageSupplier
{

    public abstract DiskStorage get()
        throws IOException;
}
