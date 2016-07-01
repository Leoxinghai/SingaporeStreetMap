

package com.facebook.imagepipeline.memory;

import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.references.ResourceReleaser;

public interface Pool
    extends ResourceReleaser, MemoryTrimmable
{

    public abstract Object get(int i);

    public abstract void release(Object obj);
}
