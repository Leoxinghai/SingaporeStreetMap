// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.request;


// Referenced classes of package com.facebook.imagepipeline.request:
//            BasePostprocessor, RepeatedPostprocessor, RepeatedPostprocessorRunner

public abstract class BaseRepeatedPostProcessor extends BasePostprocessor
    implements RepeatedPostprocessor
{

    public BaseRepeatedPostProcessor()
    {
    }

    private RepeatedPostprocessorRunner getCallback()
    {
        RepeatedPostprocessorRunner repeatedpostprocessorrunner = mCallback;
        return repeatedpostprocessorrunner;
    }

    public void setCallback(RepeatedPostprocessorRunner repeatedpostprocessorrunner)
    {
        mCallback = repeatedpostprocessorrunner;
        return;
    }

    public void update()
    {
        RepeatedPostprocessorRunner repeatedpostprocessorrunner = getCallback();
        if(repeatedpostprocessorrunner != null)
            repeatedpostprocessorrunner.update();
    }

    private RepeatedPostprocessorRunner mCallback;
}
