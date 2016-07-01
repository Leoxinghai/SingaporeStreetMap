

package com.facebook.drawee.components;

import android.os.Handler;
import android.os.Looper;
import java.util.*;

public class DeferredReleaser
{
    public static interface Releasable
    {

        public abstract void release();
    }


    public DeferredReleaser()
    {
    }

    public static DeferredReleaser getInstance()
    {
        DeferredReleaser deferredreleaser;
        if(sInstance == null)
            sInstance = new DeferredReleaser();
        deferredreleaser = sInstance;
        return deferredreleaser;
    }

    public void cancelDeferredRelease(Releasable releasable)
    {
        mPendingReleasables.remove(releasable);
    }

    public void scheduleDeferredRelease(Releasable releasable)
    {
        while(!mPendingReleasables.add(releasable) || mPendingReleasables.size() != 1)
            return;
        mUiHandler.post(releaseRunnable);
    }

    private static DeferredReleaser sInstance = null;
    private final Set mPendingReleasables = new HashSet();
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private final Runnable releaseRunnable = new Runnable() {

        public void run()
        {
            for(Iterator iterator = mPendingReleasables.iterator(); iterator.hasNext(); ((Releasable)iterator.next()).release());
            mPendingReleasables.clear();
        }
    };


}
