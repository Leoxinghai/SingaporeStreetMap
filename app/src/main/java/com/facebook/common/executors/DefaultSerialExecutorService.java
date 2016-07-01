// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

// Referenced classes of package com.facebook.common.executors:
//            ConstrainedExecutorService, SerialExecutorService

public class DefaultSerialExecutorService extends ConstrainedExecutorService
    implements SerialExecutorService
{

    public DefaultSerialExecutorService(Executor executor)
    {
        super("SerialExecutor", 1, executor, new LinkedBlockingQueue());
    }

    public void execute(Runnable runnable)
    {
        super.execute(runnable);
        return;
    }
}
