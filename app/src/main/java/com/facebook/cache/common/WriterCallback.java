

package com.facebook.cache.common;

import java.io.IOException;
import java.io.OutputStream;

public interface WriterCallback
{

    public abstract void write(OutputStream outputstream)
        throws IOException;
}
