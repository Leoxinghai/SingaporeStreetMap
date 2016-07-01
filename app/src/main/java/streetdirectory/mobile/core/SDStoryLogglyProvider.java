// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

// Referenced classes of package streetdirectory.mobile.core:
//            ISDStoryProvider, SDStory, SDLogger

public class SDStoryLogglyProvider
    implements ISDStoryProvider
{

    public SDStoryLogglyProvider()
    {
    }

    public void post(String s, HashMap hashmap)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append((new StringBuilder()).append("label=").append(SDStory.tryEncode(s)).toString());
        Iterator iterator = hashmap.keySet().iterator();
        for(;iterator.hasNext();) {
            String s1 = (String)iterator.next();
            String s2 = (String)hashmap.get(s1);
            if(s2 != null)
            {
                sb.append("&");
                sb.append((new StringBuilder()).append(s1).append("=").append(SDStory.tryEncode(s2)).toString());
            }
        }

        (new AsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected Void doInBackground(Void avoid[])
            {
                try
                {
                    Object obj = new URL("http://logs-01.loggly.com/inputs/29158abf-3e8c-4ebb-9a31-ac09b24c0b4b");
                    byte byte0[] = sb.toString().getBytes();
                    obj = (HttpURLConnection)((URL) (obj)).openConnection();
                    ((HttpURLConnection) (obj)).setDoOutput(true);
                    ((HttpURLConnection) (obj)).setRequestMethod("POST");
                    ((HttpURLConnection) (obj)).setRequestProperty("Content-Length", Integer.toString(avoid.length));
                    ((HttpURLConnection) (obj)).setUseCaches(false);
                    OutputStream outputstream = ((HttpURLConnection) (obj)).getOutputStream();
                    outputstream.write(byte0);
                    outputstream.close();
                    int i = ((HttpURLConnection) (obj)).getResponseCode();
                    SDLogger.debug((new StringBuilder()).append("RC ").append(i).append(" Logly POST data ").append(sb.toString()).toString());
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                return null;
            }

        }
).execute(new Void[0]);
    }
}
