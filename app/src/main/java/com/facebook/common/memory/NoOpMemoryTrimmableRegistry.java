

package com.facebook.common.memory;


// Referenced classes of package com.facebook.common.memory:
//            MemoryTrimmableRegistry, MemoryTrimmable

public class NoOpMemoryTrimmableRegistry
    implements MemoryTrimmableRegistry
{

    public NoOpMemoryTrimmableRegistry()
    {
    }

    public static NoOpMemoryTrimmableRegistry getInstance()
    {
        NoOpMemoryTrimmableRegistry noopmemorytrimmableregistry;
        if(sInstance == null)
            sInstance = new NoOpMemoryTrimmableRegistry();
        noopmemorytrimmableregistry = sInstance;
        return noopmemorytrimmableregistry;
    }

    public void registerMemoryTrimmable(MemoryTrimmable memorytrimmable)
    {
    }

    public void unregisterMemoryTrimmable(MemoryTrimmable memorytrimmable)
    {
    }

    private static NoOpMemoryTrimmableRegistry sInstance = null;

}
