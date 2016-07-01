

package com.facebook.imagepipeline.core;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Referenced classes of package com.facebook.imagepipeline.core:
//            ExecutorSupplier, PriorityThreadFactory

public class DefaultExecutorSupplier
    implements ExecutorSupplier
{

    public DefaultExecutorSupplier(int i)
    {
        PriorityThreadFactory prioritythreadfactory = new PriorityThreadFactory(10);
        mDecodeExecutor = Executors.newFixedThreadPool(i, prioritythreadfactory);
        mBackgroundExecutor = Executors.newFixedThreadPool(i, prioritythreadfactory);
        mLightWeightBackgroundExecutor = Executors.newFixedThreadPool(1, prioritythreadfactory);
    }

    public Executor forBackgroundTasks()
    {
        return mBackgroundExecutor;
    }

    public Executor forDecode()
    {
        return mDecodeExecutor;
    }

    public Executor forLightweightBackgroundTasks()
    {
        return mLightWeightBackgroundExecutor;
    }

    public Executor forLocalStorageRead()
    {
        return mIoBoundExecutor;
    }

    public Executor forLocalStorageWrite()
    {
        return mIoBoundExecutor;
    }

    private static final int NUM_IO_BOUND_THREADS = 2;
    private static final int NUM_LIGHTWEIGHT_BACKGROUND_THREADS = 1;
    private final Executor mBackgroundExecutor;
    private final Executor mDecodeExecutor;
    private final Executor mIoBoundExecutor = Executors.newFixedThreadPool(2);
    private final Executor mLightWeightBackgroundExecutor;
}
