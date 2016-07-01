// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.os.SystemClock;
import com.facebook.imagepipeline.image.EncodedImage;
import java.util.concurrent.*;

public class JobScheduler
{
    public static interface JobRunnable
    {

        public abstract void run(EncodedImage encodedimage, boolean flag);
    }

    static class JobStartExecutorSupplier
    {

        static ScheduledExecutorService get()
        {
            if(sJobStarterExecutor == null)
                sJobStarterExecutor = Executors.newSingleThreadScheduledExecutor();
            return sJobStarterExecutor;
        }

        private static ScheduledExecutorService sJobStarterExecutor;

        JobStartExecutorSupplier()
        {
        }
    }

    static enum JobState
    {

		IDLE("IDLE", 0),
		QUEUED("QUEUED", 1),
		RUNNING("RUNNING", 2),
		RUNNING_AND_PENDING("RUNNING_AND_PENDING", 3);

		String sType;
		int iType;

        private JobState(String s, int i)
        {
		sType = s;
		iType = i;
        }
    }


    public JobScheduler(Executor executor, JobRunnable jobrunnable, int i)
    {
        mExecutor = executor;
        mJobRunnable = jobrunnable;
        mMinimumJobIntervalMs = i;
        mEncodedImage = null;
        mIsLast = false;
        mJobState = JobState.IDLE;
        mJobSubmitTime = 0L;
        mJobStartTime = 0L;
    }

    private void doJob()
    {
        long l = SystemClock.uptimeMillis();
        Object obj;
        boolean flag;
        obj = mEncodedImage;
        flag = mIsLast;
        mEncodedImage = null;
        mIsLast = false;
        mJobState = JobState.RUNNING;
        mJobStartTime = l;
        if(shouldProcess(((EncodedImage) (obj)), flag))
            mJobRunnable.run(((EncodedImage) (obj)), flag);
        EncodedImage.closeSafely(((EncodedImage) (obj)));
        onJobFinished();
        return;
    }

    private void enqueueJob(long l)
    {
        if(l > 0L)
        {
            JobStartExecutorSupplier.get().schedule(mSubmitJobRunnable, l, TimeUnit.MILLISECONDS);
            return;
        } else
        {
            mSubmitJobRunnable.run();
            return;
        }
    }

    private void onJobFinished()
    {
        long l;
        long l1;
        l1 = SystemClock.uptimeMillis();
        l = 0L;
        boolean flag = false;
        if(mJobState != JobState.RUNNING_AND_PENDING) {
			mJobState = JobState.IDLE;
		} else {
			l = Math.max(mJobStartTime + (long)mMinimumJobIntervalMs, l1);
			flag = true;
			mJobSubmitTime = l1;
			mJobState = JobState.QUEUED;
		}

        if(flag)
            enqueueJob(l - l1);
        return;
    }

    private static boolean shouldProcess(EncodedImage encodedimage, boolean flag)
    {
        return flag || EncodedImage.isValid(encodedimage);
    }

    private void submitJob()
    {
        mExecutor.execute(mDoJobRunnable);
    }

    public void clearJob()
    {
        EncodedImage encodedimage;
        encodedimage = mEncodedImage;
        mEncodedImage = null;
        mIsLast = false;
        EncodedImage.closeSafely(encodedimage);
        return;
    }

    public long getQueuedTime()
    {
        long l;
        long l1;
        l = mJobStartTime;
        l1 = mJobSubmitTime;
        return l - l1;
    }

    public boolean scheduleJob()
    {
        long l1;
        long l2;
        l2 = SystemClock.uptimeMillis();
        l1 = 0L;
        boolean flag1 = false;
        if(shouldProcess(mEncodedImage, mIsLast)) {
            boolean flag;
            long l;
            flag = flag1;
            l = l1;

            switch(mJobState.ordinal()) {
                case 2:
                    break;
                case 1:
                    l = Math.max(mJobStartTime + (long)mMinimumJobIntervalMs, l2);
                    flag = true;
                    mJobSubmitTime = l2;
                    mJobState = JobState.QUEUED;
                    break;
                case 3:
                    mJobState = JobState.RUNNING_AND_PENDING;
                    flag = flag1;
                    l = l1;
                    break;
                default:
                    flag = flag1;
                    l = l1;
                    break;
            }
            if(flag)
                enqueueJob(l - l2);
            return true;

        }
        return false;


    }

    public boolean updateJob(EncodedImage encodedimage, boolean flag)
    {
        if(!shouldProcess(encodedimage, flag))
            return false;
        EncodedImage encodedimage1;
        encodedimage1 = mEncodedImage;
        mEncodedImage = EncodedImage.cloneOrNull(encodedimage);
        mIsLast = flag;
        EncodedImage.closeSafely(encodedimage1);
        return true;
    }

    static final String QUEUE_TIME_KEY = "queueTime";
    private final Runnable mDoJobRunnable = new Runnable() {

        public void run()
        {
            doJob();
        }

    };
    EncodedImage mEncodedImage;
    private final Executor mExecutor;
    boolean mIsLast;
    private final JobRunnable mJobRunnable;
    long mJobStartTime;
    JobState mJobState;
    long mJobSubmitTime;
    private final int mMinimumJobIntervalMs;
    private final Runnable mSubmitJobRunnable = new Runnable() {

        public void run()
        {
            submitJob();
        }

    };


}
