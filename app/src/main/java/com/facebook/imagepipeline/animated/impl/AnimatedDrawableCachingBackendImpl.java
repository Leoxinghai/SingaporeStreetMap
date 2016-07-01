// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.impl;

import android.app.ActivityManager;
import android.graphics.*;
import android.support.v4.util.SparseArrayCompat;
import bolts.Continuation;
import bolts.Task;
import com.facebook.common.executors.SerialExecutorService;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import com.facebook.common.time.MonotonicClock;
import com.facebook.imagepipeline.animated.base.*;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// Referenced classes of package com.facebook.imagepipeline.animated.impl:
//            AnimatedImageCompositor, WhatToKeepCachedArray

public class AnimatedDrawableCachingBackendImpl extends DelegatingAnimatedDrawableBackend
    implements AnimatedDrawableCachingBackend
{

    public AnimatedDrawableCachingBackendImpl(SerialExecutorService serialexecutorservice, ActivityManager activitymanager, AnimatedDrawableUtil animateddrawableutil, MonotonicClock monotonicclock, AnimatedDrawableBackend animateddrawablebackend, AnimatedDrawableOptions animateddrawableoptions)
    {
        super(animateddrawablebackend);
        mExecutorService = serialexecutorservice;
        mActivityManager = activitymanager;
        mAnimatedDrawableUtil = animateddrawableutil;
        mMonotonicClock = monotonicclock;
        mAnimatedDrawableBackend = animateddrawablebackend;
        mAnimatedDrawableOptions = animateddrawableoptions;
        double d;
        if(animateddrawableoptions.maximumBytes >= 0)
            d = animateddrawableoptions.maximumBytes / 1024;
        else
            d = getDefaultMaxBytes(activitymanager) / 1024;
        mMaximumKiloBytes = d;
        mAnimatedImageCompositor = new AnimatedImageCompositor(animateddrawablebackend, new AnimatedImageCompositor.Callback() {

            public CloseableReference getCachedBitmap(int i)
            {
                return getCachedOrPredecodedFrame(i);
            }

            public void onIntermediateResult(int i, Bitmap bitmap)
            {
                maybeCacheBitmapDuringRender(i, bitmap);
            }

        });

        mBitmapsToKeepCached = new WhatToKeepCachedArray(mAnimatedDrawableBackend.getFrameCount());
        mApproxKiloBytesToHoldAllFrames = ((mAnimatedDrawableBackend.getRenderedWidth() * mAnimatedDrawableBackend.getRenderedHeight()) / 1024) * mAnimatedDrawableBackend.getFrameCount() * 4;
    }

    private void cancelFuturesOutsideOfRange(int i, int j)
    {
        int k = 0;

        for(;k < mDecodesInFlight.size();) {
            if (AnimatedDrawableUtil.isOutsideRange(i, j, mDecodesInFlight.keyAt(k))) {
                Task task = (Task) mDecodesInFlight.valueAt(k);
                mDecodesInFlight.removeAt(k);
            }
            k++;
        }
    }

    private void copyAndCacheBitmapDuringRendering(int i, Bitmap bitmap)
    {
        CloseableReference closeablereference = obtainBitmapInternal();
        Canvas canvas = new Canvas((Bitmap)closeablereference.get());
        canvas.drawColor(0, android.graphics.PorterDuff.Mode.SRC);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
        maybeCacheRenderedBitmap(i, closeablereference);
        closeablereference.close();
        return;
    }

    private Bitmap createNewBitmap()
    {
        FLog.v(TAG, "Creating new bitmap");
        sTotalBitmaps.incrementAndGet();
        FLog.v(TAG, "Total bitmaps: %d", Integer.valueOf(sTotalBitmaps.get()));
        return Bitmap.createBitmap(mAnimatedDrawableBackend.getRenderedWidth(), mAnimatedDrawableBackend.getRenderedHeight(), android.graphics.Bitmap.Config.ARGB_8888);
    }

    private void doPrefetch(int i, int j)
    {
        int k = 0;
        for(;k < j;) {
            Task task;
            final int frameNumber;
            boolean flag;
            frameNumber = (i + k) % mAnimatedDrawableBackend.getFrameCount();
            flag = hasCachedOrPredecodedFrame(frameNumber);
            task = (Task) mDecodesInFlight.get(frameNumber);
            if (flag || task != null)
                continue; /* Loop/switch isn't completed */
            final Task newFuture = Task.call(new Callable() {

                public Object call() {
                    runPrefetch(frameNumber);
                    return null;
                }

            }, mExecutorService);

            mDecodesInFlight.put(frameNumber, newFuture);
            newFuture.continueWith(new Continuation() {

                public Object then(Task task1)
                        throws Exception {
                    onFutureFinished(newFuture, frameNumber);
                    return null;
                }
            });

            k++;
        }
        return;
    }

    private void dropBitmapsThatShouldNotBeCached()
    {
        int i = 0;
        for(;i < mCachedBitmaps.size();) {
            int j = mCachedBitmaps.keyAt(i);
            if (!mBitmapsToKeepCached.get(j)) {
                CloseableReference closeablereference = (CloseableReference) mCachedBitmaps.valueAt(i);
                mCachedBitmaps.removeAt(i);
                closeablereference.close();
            }
            i++;
        }
    }

    private CloseableReference getBitmapForFrameInternal(int i, boolean flag)
    {
        boolean flag2;
        long l;
        flag2 = false;
        l = mMonotonicClock.now();
        boolean flag1 = flag2;
        CloseableReference closeablereference;
        mBitmapsToKeepCached.set(i, true);
        closeablereference = getCachedOrPredecodedFrame(i);
        Object obj;
        try {
            if (closeablereference == null) {
                if (!flag) {
                    obj = null;
                    l = mMonotonicClock.now() - l;
                    if (l > 10L) {
                        if (false)
                            obj = "renderedOnCallingThread";
                        else if (true)
                            obj = "deferred";
                        else
                            obj = "ok";
                        FLog.v(TAG, "obtainBitmap for frame %d took %d ms (%s)", Integer.valueOf(i), Long.valueOf(l), obj);
                        return null;
                    }
                } else {
                    flag2 = true;
                    flag1 = flag2;
                    obj = obtainBitmapInternal();
                    mAnimatedImageCompositor.renderFrame(i, (Bitmap) ((CloseableReference) (obj)).get());
                    maybeCacheRenderedBitmap(i, ((CloseableReference) (obj)));
                    closeablereference = ((CloseableReference) (obj)).clone();
                    flag1 = flag2;
                    ((CloseableReference) (obj)).close();
                    l = mMonotonicClock.now() - l;
                    if (l > 10L) {
                        Exception exception;
                        if (true)
                            obj = "renderedOnCallingThread";
                        else if (false)
                            obj = "deferred";
                        else
                            obj = "ok";
                        FLog.v(TAG, "obtainBitmap for frame %d took %d ms (%s)", Integer.valueOf(i), Long.valueOf(l), obj);
                    }
                    return closeablereference;
                }

            } else {
                l = mMonotonicClock.now() - l;
                obj = closeablereference;
                if (l > 10L) {
                    if (false)
                        obj = "renderedOnCallingThread";
                    else if (false)
                        obj = "deferred";
                    else
                        obj = "ok";
                    FLog.v(TAG, "obtainBitmap for frame %d took %d ms (%s)", Integer.valueOf(i), Long.valueOf(l), obj);
                    obj = closeablereference;
                }
            }

            return ((CloseableReference) (obj));

        } catch(Exception exception) {
            l = mMonotonicClock.now() - l;
            if (l > 10L) {
                if (flag1)
                    obj = "renderedOnCallingThread";
                else if (false)
                    obj = "deferred";
                else
                    obj = "ok";
                FLog.v(TAG, "obtainBitmap for frame %d took %d ms (%s)", Integer.valueOf(i), Long.valueOf(l), obj);
            }
            throw exception;
        }

    }

    private CloseableReference getCachedOrPredecodedFrame(int i)
    {
        CloseableReference closeablereference1 = CloseableReference.cloneOrNull((CloseableReference)mCachedBitmaps.get(i));
        CloseableReference closeablereference;
        closeablereference = closeablereference1;
        if(closeablereference1 == null)
            closeablereference = mAnimatedDrawableBackend.getPreDecodedFrame(i);
        return closeablereference;
    }

    private static int getDefaultMaxBytes(ActivityManager activitymanager)
    {
        return activitymanager.getMemoryClass() <= 32 ? 0x300000 : 0x500000;
    }

    private boolean hasCachedOrPredecodedFrame(int i)
    {
        boolean flag = true;
        if(mCachedBitmaps.get(i) == null) {
            flag = mAnimatedDrawableBackend.hasPreDecodedFrame(i);
            if (!flag)
                flag = false;
        }
        return flag;
    }

    private void maybeCacheBitmapDuringRender(int i, Bitmap bitmap)
    {
        boolean flag = false;
        if(mBitmapsToKeepCached.get(i))
            if(mCachedBitmaps.get(i) == null)
                flag = true;
            else
                flag = false;
        if(flag)
            copyAndCacheBitmapDuringRendering(i, bitmap);
        return;
    }

    private void maybeCacheRenderedBitmap(int i, CloseableReference closeablereference)
    {
        boolean flag = mBitmapsToKeepCached.get(i);
        if(!flag)
            return;

        int j = mCachedBitmaps.indexOfKey(i);
        if(j >= 0) {
            ((CloseableReference) mCachedBitmaps.valueAt(j)).close();
            mCachedBitmaps.removeAt(j);
            mCachedBitmaps.put(i, closeablereference.clone());
            return;
        }
    }

    private CloseableReference obtainBitmapInternal()
    {
        long l;
        long l1;
        l = System.nanoTime();
        l1 = l + TimeUnit.NANOSECONDS.convert(20L, TimeUnit.MILLISECONDS);

        try {
            for (; ; ) {
                boolean flag = mFreeBitmaps.isEmpty();
                if (!flag || l >= l1) {
                    Bitmap bitmap;
                    if (!mFreeBitmaps.isEmpty())
                        bitmap = (Bitmap) mFreeBitmaps.remove(mFreeBitmaps.size() - 1);
                    else
                        bitmap = createNewBitmap();
                    return CloseableReference.of(bitmap, mResourceReleaserForBitmaps);

                }
                TimeUnit.NANOSECONDS.timedWait(this, l1 - l);
                l = System.nanoTime();
            }
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(((Throwable) (ex)));
        }

    }

    private void onFutureFinished(Task task, int i)
    {
        int j = mDecodesInFlight.indexOfKey(i);
        if(j >= 0) {
            if ((Task) mDecodesInFlight.valueAt(j) == task) {
                mDecodesInFlight.removeAt(j);
                if (task.getError() != null)
                    FLog.v(TAG, task.getError(), "Failed to render frame %d", new Object[]{
                            Integer.valueOf(i)
                    });
            }
        }
        return;
    }

    private void runPrefetch(int i)
    {
        if(!mBitmapsToKeepCached.get(i))
            return;

        if(hasCachedOrPredecodedFrame(i))
            return;
        CloseableReference closeablereference = mAnimatedDrawableBackend.getPreDecodedFrame(i);
        if(closeablereference == null) {
            Object obj = obtainBitmapInternal();
            mAnimatedImageCompositor.renderFrame(i, (Bitmap)((CloseableReference) (obj)).get());
            maybeCacheRenderedBitmap(i, ((CloseableReference) (obj)));
            FLog.v(TAG, "Prefetch rendered frame %d", Integer.valueOf(i));
            ((CloseableReference) (obj)).close();
        } else {
            maybeCacheRenderedBitmap(i, closeablereference);
        }

        CloseableReference.closeSafely(closeablereference);
        return;
    }

    private void schedulePrefetches()
    {
        boolean flag = true;
        int i;
        int j;
        int k;
        if(mAnimatedDrawableBackend.getFrameInfo(mCurrentFrameIndex).disposalMethod == com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_PREVIOUS)
            i = 1;
        else
            i = 0;
        k = mCurrentFrameIndex;
        if(i != 0)
            j = 1;
        else
            j = 0;
        k = Math.max(0, k - j);
        if(mAnimatedDrawableOptions.allowPrefetching)
            j = 3;
        else
            j = 0;

        if(i == 0)
            i = 0;
        else {
            i = ((flag) ? 1 : 0);
        }

        j = Math.max(j, i);
        i = (k + j) % mAnimatedDrawableBackend.getFrameCount();
        cancelFuturesOutsideOfRange(k, i);
        if(!shouldKeepAllFramesInMemory()) {
            mBitmapsToKeepCached.setAll(true);
            mBitmapsToKeepCached.removeOutsideRange(k, i);
            i = k;
            for (; i >= 0; ) {
                if (mCachedBitmaps.get(i) != null) {
                    mBitmapsToKeepCached.set(i, true);
                    break;
                }
                i--;
            }
            dropBitmapsThatShouldNotBeCached();
        }

        if(!mAnimatedDrawableOptions.allowPrefetching)
            cancelFuturesOutsideOfRange(mCurrentFrameIndex, mCurrentFrameIndex);
        else
            doPrefetch(k, j);

        return;

    }

    private boolean shouldKeepAllFramesInMemory()
    {
        while(mAnimatedDrawableOptions.forceKeepAllFramesInMemory || mApproxKiloBytesToHoldAllFrames < mMaximumKiloBytes)
            return true;
        return false;
    }

    public void appendDebugOptionString(StringBuilder stringbuilder)
    {
        if(mAnimatedDrawableOptions.forceKeepAllFramesInMemory)
        {
            stringbuilder.append("Pinned To Memory");
        } else
        {
            if(mApproxKiloBytesToHoldAllFrames < mMaximumKiloBytes)
                stringbuilder.append("within ");
            else
                stringbuilder.append("exceeds ");
            mAnimatedDrawableUtil.appendMemoryString(stringbuilder, (int)mMaximumKiloBytes);
        }
        if(shouldKeepAllFramesInMemory() && mAnimatedDrawableOptions.allowPrefetching)
            stringbuilder.append(" MT");
    }

    public void dropCaches()
    {
        mBitmapsToKeepCached.setAll(false);
        dropBitmapsThatShouldNotBeCached();
        for(Iterator iterator = mFreeBitmaps.iterator(); iterator.hasNext(); sTotalBitmaps.decrementAndGet())
            ((Bitmap)iterator.next()).recycle();

        mFreeBitmaps.clear();
        mAnimatedDrawableBackend.dropCaches();
        FLog.v(TAG, "Total bitmaps: %d", Integer.valueOf(sTotalBitmaps.get()));
    }

    protected void finalize()
        throws Throwable
    {
        super.finalize();
        if(mCachedBitmaps.size() > 0)
            FLog.d(TAG, "Finalizing with rendered bitmaps");
        sTotalBitmaps.addAndGet(-mFreeBitmaps.size());
        mFreeBitmaps.clear();
        return;
    }


    public AnimatedDrawableCachingBackend forNewBounds(Rect rect)
    {
        AnimatedDrawableBackend temp = mAnimatedDrawableBackend.forNewBounds(rect);
        if(temp == mAnimatedDrawableBackend)
            return this;
        else
            return new AnimatedDrawableCachingBackendImpl(mExecutorService, mActivityManager, mAnimatedDrawableUtil, mMonotonicClock, temp, mAnimatedDrawableOptions);
    }

    public CloseableReference getBitmapForFrame(int i)
    {
        mCurrentFrameIndex = i;
        CloseableReference closeablereference = getBitmapForFrameInternal(i, false);
        schedulePrefetches();
        return closeablereference;
    }

    CloseableReference getBitmapForFrameBlocking(int i)
    {
        mCurrentFrameIndex = i;
        CloseableReference closeablereference = getBitmapForFrameInternal(i, true);
        schedulePrefetches();
        return closeablereference;
    }

    Map getDecodesInFlight()
    {
        HashMap hashmap = new HashMap();
        int i = 0;
        for(;i < mDecodesInFlight.size();) {
            hashmap.put(Integer.valueOf(mDecodesInFlight.keyAt(i)), mDecodesInFlight.valueAt(i));
            i++;
        }
        return hashmap;
    }

    Set getFramesCached()
    {
        HashSet hashset = new HashSet();
        int i = 0;

        for(;i < mCachedBitmaps.size();) {
            hashset.add(Integer.valueOf(mCachedBitmaps.keyAt(i)));
            i++;
        }
        return hashset;
    }

    public int getMemoryUsage()
    {
        int i = 0;
        for(Iterator iterator = mFreeBitmaps.iterator(); iterator.hasNext();)
        {
            Bitmap bitmap = (Bitmap)iterator.next();
            i += mAnimatedDrawableUtil.getSizeOfBitmap(bitmap);
        }

        int j;
        j = 0;

        for(;j < mCachedBitmaps.size();) {
            CloseableReference closeablereference = (CloseableReference) mCachedBitmaps.valueAt(j);
            i += mAnimatedDrawableUtil.getSizeOfBitmap((Bitmap) closeablereference.get());
            j++;
        }

        return i + mAnimatedDrawableBackend.getMemoryUsage();
    }

    public CloseableReference getPreviewBitmap()
    {
        return getAnimatedImageResult().getPreviewBitmap();
    }

    void releaseBitmapInternal(Bitmap bitmap)
    {
        mFreeBitmaps.add(bitmap);
        return;
    }

    public void renderFrame(int i, Canvas canvas)
    {
        throw new IllegalStateException();
    }

    private static final int PREFETCH_FRAMES = 3;
    private static final Class TAG = AnimatedDrawableCachingBackendImpl.class;
    private static final AtomicInteger sTotalBitmaps = new AtomicInteger();
    private final ActivityManager mActivityManager;
    private final AnimatedDrawableBackend mAnimatedDrawableBackend;
    private final AnimatedDrawableOptions mAnimatedDrawableOptions;
    private final AnimatedDrawableUtil mAnimatedDrawableUtil;
    private final AnimatedImageCompositor mAnimatedImageCompositor;
    private final double mApproxKiloBytesToHoldAllFrames;
    private final WhatToKeepCachedArray mBitmapsToKeepCached;
    private final SparseArrayCompat mCachedBitmaps = new SparseArrayCompat(10);
    private int mCurrentFrameIndex;
    private final SparseArrayCompat mDecodesInFlight = new SparseArrayCompat(10);
    private final SerialExecutorService mExecutorService;
    private final List mFreeBitmaps = new ArrayList();
    private final double mMaximumKiloBytes;
    private final MonotonicClock mMonotonicClock;
    private final ResourceReleaser mResourceReleaserForBitmaps = new ResourceReleaser() {

        public void release(Bitmap bitmap)
        {
            releaseBitmapInternal(bitmap);
        }

        public void release(Object obj)
        {
            release((Bitmap)obj);
        }

    };

}
