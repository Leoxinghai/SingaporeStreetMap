// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.model;

import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

class JsonUtil
{
    private static final class JSONObjectEntry
        implements java.util.Map.Entry
    {


        public String getKey()
        {
            return key;
        }

        public Object getValue()
        {
            return value;
        }

        public Object setValue(Object obj)
        {
            throw new UnsupportedOperationException("JSONObjectEntry is immutable");
        }

        private final String key;
        private final Object value;

        JSONObjectEntry(String s, Object obj)
        {
            key = s;
            value = obj;
        }
    }


    JsonUtil()
    {
    }

    static void jsonObjectClear(JSONObject jsonobject)
    {
        for(Iterator iterator = jsonobject.keys(); iterator.hasNext(); iterator.remove())
            iterator.next();

    }

    static boolean jsonObjectContainsValue(JSONObject jsonobject, Object obj)
    {
        for(Iterator iterator = jsonobject.keys(); iterator.hasNext();)
        {
            Object obj1 = jsonobject.opt((String)iterator.next());
            if(obj1 != null && obj1.equals(obj))
                return true;
        }

        return false;
    }

    static Set jsonObjectEntrySet(JSONObject jsonobject)
    {
        HashSet hashset = new HashSet();
        String s;
        for(Iterator iterator = jsonobject.keys(); iterator.hasNext(); hashset.add(new JSONObjectEntry(s, jsonobject.opt(s))))
            s = (String)iterator.next();

        return hashset;
    }

    static Set jsonObjectKeySet(JSONObject jsonobject)
    {
        HashSet hashset = new HashSet();
        for(Iterator iterator = jsonobject.keys(); iterator.hasNext(); hashset.add(iterator.next()));
        return hashset;
    }

    static void jsonObjectPutAll(JSONObject jsonobject, Map map)
    {
        for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            try
            {
                jsonobject.putOpt((String)entry.getKey(), entry.getValue());
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }

    }

    static Collection jsonObjectValues(JSONObject jsonobject)
    {
        ArrayList arraylist = new ArrayList();
        for(Iterator iterator = jsonobject.keys(); iterator.hasNext(); arraylist.add(jsonobject.opt((String)iterator.next())));
        return arraylist;
    }
}
