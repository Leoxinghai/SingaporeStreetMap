

package com.facebook.imagepipeline.listener;

import com.facebook.common.logging.FLog;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.listener:
//            RequestListener

public class ForwardingRequestListener
    implements RequestListener
{

    public ForwardingRequestListener(Set set)
    {
        mRequestListeners = new ArrayList(set.size());
        RequestListener requestlistener;
        for(Iterator iterator = set.iterator(); iterator.hasNext(); mRequestListeners.add(requestlistener))
            requestlistener = (RequestListener)iterator.next();

    }

    private void onException(String s, Throwable throwable)
    {
        FLog.e("ForwardingRequestListener", s, throwable);
    }

    public void onProducerEvent(String s, String s1, String s2)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onProducerEvent(s, s1, s2);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onIntermediateChunkStart", exception);
            }
            i++;
        }
    }

    public void onProducerFinishWithCancellation(String s, String s1, Map map)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onProducerFinishWithCancellation(s, s1, map);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onProducerFinishWithCancellation", exception);
            }
            i++;
        }
    }

    public void onProducerFinishWithFailure(String s, String s1, Throwable throwable, Map map)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onProducerFinishWithFailure(s, s1, throwable, map);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onProducerFinishWithFailure", exception);
            }
            i++;
        }
    }

    public void onProducerFinishWithSuccess(String s, String s1, Map map)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onProducerFinishWithSuccess(s, s1, map);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onProducerFinishWithSuccess", exception);
            }
            i++;
        }
    }

    public void onProducerStart(String s, String s1)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onProducerStart(s, s1);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onProducerStart", exception);
            }
            i++;
        }
    }

    public void onRequestCancellation(String s)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onRequestCancellation(s);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onRequestCancellation", exception);
            }
            i++;
        }
    }

    public void onRequestFailure(ImageRequest imagerequest, String s, Throwable throwable, boolean flag)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onRequestFailure(imagerequest, s, throwable, flag);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onRequestFailure", exception);
            }
            i++;
        }
    }

    public void onRequestStart(ImageRequest imagerequest, Object obj, String s, boolean flag)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onRequestStart(imagerequest, obj, s, flag);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onRequestStart", exception);
            }
            i++;
        }
    }

    public void onRequestSuccess(ImageRequest imagerequest, String s, boolean flag)
    {
        int j = mRequestListeners.size();
        int i = 0;
        while(i < j)
        {
            RequestListener requestlistener = (RequestListener)mRequestListeners.get(i);
            try
            {
                requestlistener.onRequestSuccess(imagerequest, s, flag);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onRequestSuccess", exception);
            }
            i++;
        }
    }

    public boolean requiresExtraMap(String s)
    {
        int j = mRequestListeners.size();
        for(int i = 0; i < j; i++)
            if(((RequestListener)mRequestListeners.get(i)).requiresExtraMap(s))
                return true;

        return false;
    }

    private static final String TAG = "ForwardingRequestListener";
    private final List mRequestListeners;
}
