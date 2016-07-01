

package com.facebook.internal;

import android.os.Bundle;
import java.util.*;
import org.json.*;

public class BundleJSONConverter
{
    public static interface Setter
    {

        public abstract void setOnBundle(Bundle bundle, String s, Object obj)
            throws JSONException;

        public abstract void setOnJSON(JSONObject jsonobject, String s, Object obj)
            throws JSONException;
    }


    public BundleJSONConverter()
    {
    }

    public static Bundle convertToBundle(JSONObject jsonobject)
        throws JSONException
    {
        Bundle bundle = new Bundle();
        Iterator iterator = jsonobject.keys();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            Object obj = jsonobject.get(s);
            if(obj != null && obj != JSONObject.NULL)
                if(obj instanceof JSONObject)
                {
                    bundle.putBundle(s, convertToBundle((JSONObject)obj));
                } else
                {
                    Setter setter = (Setter)SETTERS.get(obj.getClass());
                    if(setter == null)
                        throw new IllegalArgumentException((new StringBuilder()).append("Unsupported type: ").append(obj.getClass()).toString());
                    setter.setOnBundle(bundle, s, obj);
                }
        } while(true);
        return bundle;
    }

    public static JSONObject convertToJSON(Bundle bundle)
        throws JSONException
    {
        JSONObject jsonobject = new JSONObject();
        Iterator iterator = bundle.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            Object obj = bundle.get(s);
            if(obj != null)
                if(obj instanceof List)
                {
                    JSONArray jsonarray = new JSONArray();
                    for(obj = ((List)obj).iterator(); ((Iterator) (obj)).hasNext(); jsonarray.put((String)((Iterator) (obj)).next()));
                    jsonobject.put(s, jsonarray);
                } else
                if(obj instanceof Bundle)
                {
                    jsonobject.put(s, convertToJSON((Bundle)obj));
                } else
                {
                    Setter setter = (Setter)SETTERS.get(obj.getClass());
                    if(setter == null)
                        throw new IllegalArgumentException((new StringBuilder()).append("Unsupported type: ").append(obj.getClass()).toString());
                    setter.setOnJSON(jsonobject, s, obj);
                }
        } while(true);
        return jsonobject;
    }

    private static final Map SETTERS;

    static
    {
        SETTERS = new HashMap();
        SETTERS.put(Boolean.class, new Setter() {

            public void setOnBundle(Bundle bundle, String s, Object obj)
                throws JSONException
            {
                bundle.putBoolean(s, ((Boolean) obj).booleanValue());
            }

            public void setOnJSON(JSONObject jsonobject, String s, Object obj)
                throws JSONException
            {
                jsonobject.put(s, obj);
            }

        });
        SETTERS.put(Integer.class, new Setter() {

            public void setOnBundle(Bundle bundle, String s, Object obj)
                throws JSONException
            {
                bundle.putInt(s, ((Integer) obj).intValue());
            }

            public void setOnJSON(JSONObject jsonobject, String s, Object obj)
                throws JSONException
            {
                jsonobject.put(s, obj);
            }

        }
);
        SETTERS.put(Long.class, new Setter() {

            public void setOnBundle(Bundle bundle, String s, Object obj)
                throws JSONException
            {
                bundle.putLong(s, ((Long) obj).longValue());
            }

            public void setOnJSON(JSONObject jsonobject, String s, Object obj)
                throws JSONException
            {
                jsonobject.put(s, obj);
            }

        });
        SETTERS.put(Double.class, new Setter() {

            public void setOnBundle(Bundle bundle, String s, Object obj)
                throws JSONException
            {
                bundle.putDouble(s, ((Double)obj).doubleValue());
            }

            public void setOnJSON(JSONObject jsonobject, String s, Object obj)
                throws JSONException
            {
                jsonobject.put(s, obj);
            }

        }
);
        SETTERS.put(String.class, new Setter() {

            public void setOnBundle(Bundle bundle, String s, Object obj)
                throws JSONException
            {
                bundle.putString(s, (String)obj);
            }

            public void setOnJSON(JSONObject jsonobject, String s, Object obj)
                throws JSONException
            {
                jsonobject.put(s, obj);
            }

        });
        SETTERS.put(String.class, new Setter() {

            public void setOnBundle(Bundle bundle, String s, Object obj)
                throws JSONException
            {
                throw new IllegalArgumentException("Unexpected type from JSON");
            }

            public void setOnJSON(JSONObject jsonobject, String s, Object obj)
                throws JSONException
            {
                JSONArray jsonarray = new JSONArray();
                obj = (String[])(String[])obj;
                int j = ((String[])obj).length;
                for(int i = 0; i < j; i++)
                    jsonarray.put(((String[])obj)[i]);

                jsonobject.put(s, jsonarray);
            }

        });
        SETTERS.put(JSONArray.class, new Setter() {

            public void setOnBundle(Bundle bundle, String s, Object obj)
                throws JSONException
            {
                obj = (JSONArray)obj;
                ArrayList arraylist = new ArrayList();
                if(((JSONArray) (obj)).length() == 0)
                {
                    bundle.putStringArrayList(s, arraylist);
                    return;
                }
                for(int i = 0; i < ((JSONArray) (obj)).length();)
                {
                    Object obj1 = ((JSONArray) (obj)).get(i);
                    if(obj1 instanceof String)
                    {
                        arraylist.add((String)obj1);
                        i++;
                    } else
                    {
                        throw new IllegalArgumentException((new StringBuilder()).append("Unexpected type in an array: ").append(obj1.getClass()).toString());
                    }
                }

                bundle.putStringArrayList(s, arraylist);
            }

            public void setOnJSON(JSONObject jsonobject, String s, Object obj)
                throws JSONException
            {
                throw new IllegalArgumentException("JSONArray's are not supported in bundles.");
            }

        });
    }
}
