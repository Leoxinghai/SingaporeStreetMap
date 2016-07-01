

package com.facebook.imagepipeline.listener;

import com.facebook.imagepipeline.producers.ProducerListener;
import com.facebook.imagepipeline.request.ImageRequest;

public interface RequestListener
    extends ProducerListener
{

    public abstract void onRequestCancellation(String s);

    public abstract void onRequestFailure(ImageRequest imagerequest, String s, Throwable throwable, boolean flag);

    public abstract void onRequestStart(ImageRequest imagerequest, Object obj, String s, boolean flag);

    public abstract void onRequestSuccess(ImageRequest imagerequest, String s, boolean flag);
}
