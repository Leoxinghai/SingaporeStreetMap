

package com.facebook.android;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import com.facebook.internal.Utility;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.facebook.android:
//            FacebookError

public final class Util
{

    public Util()
    {
    }

    public static Bundle decodeUrl(String s)
    {
        Bundle bundle = new Bundle();
        if(s == null)
            return bundle;

        int i;
        int j;
        String ss[] = s.split("&");
        j = ss.length;
        i = 0;
        try {
            for(;i < j;i++) {
                String as[] = ss[i].split("=");
                if (as.length != 2) {
                        if (as.length == 1)
                            bundle.putString(URLDecoder.decode(as[0], "UTF-8"), "");
                } else {
                    bundle.putString(URLDecoder.decode(as[0], "UTF-8"), URLDecoder.decode(as[1], "UTF-8"));
                }
            }
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
        }

        return bundle;
    }

    public static String encodePostBody(Bundle bundle, String s)
    {
        if(bundle == null)
            return "";
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = bundle.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s1 = (String)iterator.next();
            Object obj = bundle.get(s1);
            if(obj instanceof String)
            {
                stringbuilder.append((new StringBuilder()).append("Content-Disposition: form-data; name=\"").append(s1).append("\"\r\n\r\n").append((String)obj).toString());
                stringbuilder.append((new StringBuilder()).append("\r\n--").append(s).append("\r\n").toString());
            }
        } while(true);
        return stringbuilder.toString();
    }

    public static String encodeUrl(Bundle bundle)
    {
        if(bundle == null)
            return "";
        StringBuilder stringbuilder = new StringBuilder();
        boolean flag = true;
        Iterator iterator = bundle.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            if(bundle.get(s) instanceof String)
            {
                if(flag)
                    flag = false;
                else
                    stringbuilder.append("&");
                stringbuilder.append((new StringBuilder()).append(URLEncoder.encode(s)).append("=").append(URLEncoder.encode(bundle.getString(s))).toString());
            }
        } while(true);
        return stringbuilder.toString();
    }

    public static String openUrl(String s, String s1, Bundle bundle)
        throws MalformedURLException, IOException
    {
        Object obj;
        obj = s;

        try
        {

            if(s1.equals("GET"))
            obj = (new StringBuilder()).append(s).append("?").append(encodeUrl(bundle)).toString();
            Utility.logd("Facebook-Util", (new StringBuilder()).append(s1).append(" URL: ").append(((String) (obj))).toString());
            HttpURLConnection conn = (HttpURLConnection)(new URL(((String) (obj)))).openConnection();

            conn.setRequestProperty("User-Agent", (new StringBuilder()).append(System.getProperties().getProperty("http.agent")).append(" FacebookAndroidSDK").toString());

            if(!s1.equals("GET")) {
                Bundle localbundle = new Bundle();

                Iterator iterator = bundle.keySet().iterator();
                for (; iterator.hasNext(); ) {
                    String s3 = (String) iterator.next();
                    Object obj1 = bundle.get(s3);
                    if (obj1 instanceof byte[])
                        ((Bundle) (localbundle)).putByteArray(s3, (byte[]) (byte[]) obj1);
                }
                if (!bundle.containsKey("method"))
                    bundle.putString("method", s1);
                if (bundle.containsKey("access_token"))
                    bundle.putString("access_token", URLDecoder.decode(bundle.getString("access_token")));
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", (new StringBuilder()).append("multipart/form-data;boundary=").append("3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f").toString());
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.connect();
                BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());
                bos.write((new StringBuilder()).append("--").append("3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f").append("\r\n").toString().getBytes());
                bos.write(encodePostBody(bundle, "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f").getBytes());
                bos.write((new StringBuilder()).append("\r\n").append("--").append("3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f").append("\r\n").toString().getBytes());

                Iterator iterator0 = localbundle.keySet().iterator();
                if (!((Bundle) (obj)).isEmpty())
                    for (; iterator0.hasNext(); bos.write((new StringBuilder()).append("\r\n").append("--").append("3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f").append("\r\n").toString().getBytes())) {
                        String s2 = (String) iterator0.next();
                        bos.write((new StringBuilder()).append("Content-Disposition: form-data; filename=\"").append(s2).append("\"").append("\r\n").toString().getBytes());
                        bos.write((new StringBuilder()).append("Content-Type: content/unknown").append("\r\n").append("\r\n").toString().getBytes());
                        bos.write(localbundle.getByteArray(s2));
                    }
                bos.flush();
                bos.close();
                //s1 = read(bos.getInputStream());
            }
        }
        catch(Exception ex)
        {
            return ex.toString();
        }
        return s1;
    }

    public static JSONObject parseJson(String s)
        throws JSONException, FacebookError
    {
        if(s.equals("false"))
            throw new FacebookError("request failed");
        String s1 = s;
        if(s.equals("true"))
            s1 = "{value : true}";
        JSONObject json = new JSONObject(s1);
        if(json.has("error"))
        {
            JSONObject error = json.getJSONObject("error");
            throw new FacebookError(error.getString("message"), error.getString("type"), 0);
        }
        if(json.has("error_code") && json.has("error_msg"))
            throw new FacebookError(json.getString("error_msg"), "", Integer.parseInt(json.getString("error_code")));
        if(json.has("error_code"))
            throw new FacebookError("request failed", "", Integer.parseInt(json.getString("error_code")));
        if(json.has("error_msg"))
            throw new FacebookError(json.getString("error_msg"));
        if(json.has("error_reason"))
            throw new FacebookError(json.getString("error_reason"));
        else
            return json;
    }

    public static Bundle parseUrl(String s)
    {
        s = s.replace("fbconnect", "http");
        Bundle bundle;
        try
        {
            URL url = new URL(s);
            bundle = decodeUrl(url.getQuery());
            bundle.putAll(decodeUrl(url.getRef()));
        }
        // Misplaced declaration of an exception variable
        catch(Exception error)
        {
            return new Bundle();
        }
        return bundle;
    }

    private static String read(InputStream inputstream)
        throws IOException
    {
        StringBuilder stringbuilder = new StringBuilder();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream), 1000);
        for(String s = bufferedreader.readLine(); s != null; s = bufferedreader.readLine())
            stringbuilder.append(s);

        inputstream.close();
        return stringbuilder.toString();
    }

    public static void showAlert(Context context, String s, String s1)
    {
        AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(context);
        dialog.setTitle(s);
        dialog.setMessage(s1);
        dialog.create().show();
    }

    private static final String UTF8 = "UTF-8";
}
