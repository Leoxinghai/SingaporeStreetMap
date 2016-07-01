

package com.facebook.imagepipeline.listener;

import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.listener:
//            RequestListener

public class BaseRequestListener
    implements RequestListener
{

    public BaseRequestListener()
    {
    }

    public void onProducerEvent(String s, String s1, String s2)
    {
    }

    public void onProducerFinishWithCancellation(String s, String s1, Map map)
    {
    }

    public void onProducerFinishWithFailure(String s, String s1, Throwable throwable, Map map)
    {
    }

    public void onProducerFinishWithSuccess(String s, String s1, Map map)
    {
    }

    public void onProducerStart(String s, String s1)
    {
    }

    public void onRequestCancellation(String s)
    {
    }

    public void onRequestFailure(ImageRequest imagerequest, String s, Throwable throwable, boolean flag)
    {
    }

    public void onRequestStart(ImageRequest imagerequest, Object obj, String s, boolean flag)
    {
    }

    public void onRequestSuccess(ImageRequest imagerequest, String s, boolean flag)
    {
    }

    public boolean requiresExtraMap(String s)
    {
        return false;
    }
}
