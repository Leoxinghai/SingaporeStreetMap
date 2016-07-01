// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

// Referenced classes of package streetdirectory.mobile.core:
//            ISDStoryProvider, SDStory, SDLogger

public class SDStorySDProvider
    implements ISDStoryProvider
{

    public SDStorySDProvider()
    {
    }

    public void post(String s, HashMap hashmap)
    {
        final StringBuilder urlString = new StringBuilder();
        urlString.append((new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=location_nearby&act=nearby&output=xml&label=").append(SDStory.tryEncode(s)).toString());
        Iterator iterator = hashmap.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s1 = (String)iterator.next();
            String s2 = (String)hashmap.get(s1);
            if(s2 != null)
            {
                urlString.append("&");
                urlString.append((new StringBuilder()).append(s1).append("=").append(SDStory.tryEncode(s2)).toString());
            }
        } while(true);

        (new AsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected Void doInBackground(Void avoid[])
            {
                try
                {
                    HttpURLConnection temp = (HttpURLConnection)(new URL(urlString.toString())).openConnection();
                    temp.setRequestMethod("GET");
                    int i = temp.getResponseCode();
                    SDLogger.info((new StringBuilder()).append("Response code ").append(i).append(" URL: ").append(urlString.toString()).toString());
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                return null;
            }


        }).execute(new Void[0]);

    }
}
