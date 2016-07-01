// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.core;

import android.os.Process;
import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory
    implements ThreadFactory
{

    public PriorityThreadFactory(int i)
    {
        mThreadPriority = i;
    }

    public Thread newThread(final Runnable runnable)
    {
        return new Thread(new Runnable() {

            public void run()
            {
                try
                {
                    Process.setThreadPriority(mThreadPriority);
                }
                catch(Throwable throwable) { }
                runnable.run();
            }

        });
    }

    private final int mThreadPriority;

}
