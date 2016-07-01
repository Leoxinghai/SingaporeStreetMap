// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.factory;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.SystemClock;
import com.facebook.common.time.MonotonicClock;
import com.facebook.imagepipeline.animated.base.*;
import com.facebook.imagepipeline.animated.impl.*;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;
import java.util.concurrent.ScheduledExecutorService;

public class AnimatedDrawableFactory
{

    public AnimatedDrawableFactory(AnimatedDrawableBackendProvider animateddrawablebackendprovider, AnimatedDrawableCachingBackendImplProvider animateddrawablecachingbackendimplprovider, AnimatedDrawableUtil animateddrawableutil, ScheduledExecutorService scheduledexecutorservice, Resources resources)
    {
        mAnimatedDrawableBackendProvider = animateddrawablebackendprovider;
        mAnimatedDrawableCachingBackendProvider = animateddrawablecachingbackendimplprovider;
        mAnimatedDrawableUtil = animateddrawableutil;
        mScheduledExecutorServiceForUiThread = scheduledexecutorservice;
        mResources = resources;
    }

    private AnimatedDrawable createAnimatedDrawable(AnimatedDrawableOptions animateddrawableoptions, AnimatedDrawableBackend animateddrawablebackend)
    {
        android.util.DisplayMetrics displaymetrics = mResources.getDisplayMetrics();
        //animateddrawablebackend = mAnimatedDrawableCachingBackendProvider.get(animateddrawablebackend, animateddrawableoptions);
        AnimatedDrawableDiagnostics temp;
        if(animateddrawableoptions.enableDebugging)
            temp = new AnimatedDrawableDiagnosticsImpl(mAnimatedDrawableUtil, displaymetrics);
        else
            temp = AnimatedDrawableDiagnosticsNoop.getInstance();
        return new AnimatedDrawable(mScheduledExecutorServiceForUiThread, mAnimatedDrawableCachingBackendProvider.get(animateddrawablebackend, animateddrawableoptions), temp, mMonotonicClock);
    }

    public AnimatedDrawable create(AnimatedImageResult animatedimageresult)
    {
        return create(animatedimageresult, AnimatedDrawableOptions.DEFAULTS);
    }

    public AnimatedDrawable create(AnimatedImageResult animatedimageresult, AnimatedDrawableOptions animateddrawableoptions)
    {
        Object obj = animatedimageresult.getImage();
        obj = new Rect(0, 0, ((AnimatedImage) (obj)).getWidth(), ((AnimatedImage) (obj)).getHeight());
        return createAnimatedDrawable(animateddrawableoptions, mAnimatedDrawableBackendProvider.get(animatedimageresult, ((Rect) (obj))));
    }

    private final AnimatedDrawableBackendProvider mAnimatedDrawableBackendProvider;
    private final AnimatedDrawableCachingBackendImplProvider mAnimatedDrawableCachingBackendProvider;
    private final AnimatedDrawableUtil mAnimatedDrawableUtil;
    private final MonotonicClock mMonotonicClock = new MonotonicClock() {

        public long now()
        {
            return SystemClock.uptimeMillis();
        }

    };
    private final Resources mResources;
    private final ScheduledExecutorService mScheduledExecutorServiceForUiThread;
}
