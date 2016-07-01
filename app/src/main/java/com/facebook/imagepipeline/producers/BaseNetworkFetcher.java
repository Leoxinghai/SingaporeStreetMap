

package com.facebook.imagepipeline.producers;

import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            NetworkFetcher, FetchState

public abstract class BaseNetworkFetcher
    implements NetworkFetcher
{

    public BaseNetworkFetcher()
    {
    }

    public Map getExtraMap(FetchState fetchstate, int i)
    {
        return null;
    }

    public void onFetchCompletion(FetchState fetchstate, int i)
    {
    }

    public boolean shouldPropagate(FetchState fetchstate)
    {
        return true;
    }
}
