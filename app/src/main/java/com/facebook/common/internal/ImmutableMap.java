

package com.facebook.common.internal;

import java.util.*;

public class ImmutableMap extends HashMap
{

    private ImmutableMap(Map map)
    {
        super(map);
    }

    public static ImmutableMap copyOf(Map map)
    {
        return new ImmutableMap(map);
    }

    public static Map of()
    {
        return Collections.unmodifiableMap(new HashMap());
    }

    public static Map of(Object obj, Object obj1)
    {
        HashMap hashmap = new HashMap();
        hashmap.put(obj, obj1);
        return Collections.unmodifiableMap(hashmap);
    }

    public static Map of(Object obj, Object obj1, Object obj2, Object obj3)
    {
        HashMap hashmap = new HashMap();
        hashmap.put(obj, obj1);
        hashmap.put(obj2, obj3);
        return Collections.unmodifiableMap(hashmap);
    }

    public static Map of(Object obj, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5)
    {
        HashMap hashmap = new HashMap();
        hashmap.put(obj, obj1);
        hashmap.put(obj2, obj3);
        hashmap.put(obj4, obj5);
        return Collections.unmodifiableMap(hashmap);
    }

    public static Map of(Object obj, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7)
    {
        HashMap hashmap = new HashMap();
        hashmap.put(obj, obj1);
        hashmap.put(obj2, obj3);
        hashmap.put(obj4, obj5);
        hashmap.put(obj6, obj7);
        return Collections.unmodifiableMap(hashmap);
    }

    public static Map of(Object obj, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7,
            Object obj8, Object obj9)
    {
        HashMap hashmap = new HashMap();
        hashmap.put(obj, obj1);
        hashmap.put(obj2, obj3);
        hashmap.put(obj4, obj5);
        hashmap.put(obj6, obj7);
        hashmap.put(obj8, obj9);
        return Collections.unmodifiableMap(hashmap);
    }
}
