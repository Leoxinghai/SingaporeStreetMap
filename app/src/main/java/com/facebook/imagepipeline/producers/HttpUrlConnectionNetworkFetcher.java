// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.net.Uri;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            BaseNetworkFetcher, FetchState, ProducerContext, Consumer,
//            BaseProducerContextCallbacks

public class HttpUrlConnectionNetworkFetcher extends BaseNetworkFetcher
{

    public HttpUrlConnectionNetworkFetcher()
    {
    }

    public FetchState createFetchState(Consumer consumer, ProducerContext producercontext)
    {
        return new FetchState(consumer, producercontext);
    }

    public void fetch(final FetchState fetchState, final NetworkFetcher.Callback callback)
    {
        final Future future = mExecutorService.submit(new Runnable() {

            public void run()
            {
                Object obj;
                Object obj3;
                Object obj4;
                Object obj2;
                obj = null;
                obj4 = fetchState.getUri().getScheme();
                obj3 = fetchState.getUri().toString();

				try {

				for(;;) {
					obj2 = obj;
					HttpURLConnection httpurlconnection = (HttpURLConnection)(new URL(((String) (obj3)))).openConnection();
					obj2 = httpurlconnection;
					obj = httpurlconnection;
					Object obj5 = httpurlconnection.getHeaderField("Location");
					if(obj5 != null) {
						obj2 = httpurlconnection;
						obj = httpurlconnection;
						obj3 = Uri.parse(((String) (obj5))).getScheme();
					} else {
						obj3 = null;
					}

					if(obj5 != null) {
						obj2 = httpurlconnection;
						obj = httpurlconnection;
						if(!((String) (obj3)).equals(obj4)) {
							obj2 = obj5;
							obj5 = obj3;
							obj = httpurlconnection;
							obj4 = obj5;
							obj3 = obj2;
							if(httpurlconnection != null)
							{
								httpurlconnection.disconnect();
								obj = httpurlconnection;
								obj4 = obj5;
								obj3 = obj2;
							}
							continue;
						}
					}
					break;
				}

                return;

			} catch(Exception ex) {
                ex.printStackTrace();
                return;
			}

            }

        });

        fetchState.getContext().addCallbacks(new BaseProducerContextCallbacks() {

            public void onCancellationRequested()
            {
                if(future.cancel(false))
                    callback.onCancellation();
            }

        });
    }

    private static final int NUM_NETWORK_THREADS = 3;
    private final ExecutorService mExecutorService = Executors.newFixedThreadPool(3);
}
