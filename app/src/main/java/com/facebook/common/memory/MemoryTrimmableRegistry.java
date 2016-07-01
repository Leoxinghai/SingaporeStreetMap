

package com.facebook.common.memory;


// Referenced classes of package com.facebook.common.memory:
//            MemoryTrimmable

public interface MemoryTrimmableRegistry
{

    public abstract void registerMemoryTrimmable(MemoryTrimmable memorytrimmable);

    public abstract void unregisterMemoryTrimmable(MemoryTrimmable memorytrimmable);
}
