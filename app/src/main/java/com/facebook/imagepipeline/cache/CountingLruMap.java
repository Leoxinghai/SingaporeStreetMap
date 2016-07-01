// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import com.android.internal.util.Predicate;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            ValueDescriptor

public class CountingLruMap
{

    public CountingLruMap(ValueDescriptor valuedescriptor)
    {
        mSizeInBytes = 0;
        mValueDescriptor = valuedescriptor;
    }

    private int getValueSizeInBytes(Object obj)
    {
        if(obj == null)
            return 0;
        else
            return mValueDescriptor.getSizeInBytes(obj);
    }

    public ArrayList clear()
    {
        ArrayList arraylist;
        arraylist = new ArrayList(mMap.values());
        mMap.clear();
        mSizeInBytes = 0;
        return arraylist;
    }

    public boolean contains(Object obj)
    {
        boolean flag = mMap.containsKey(obj);
        return flag;
    }

    public Object get(Object obj)
    {
        obj = mMap.get(obj);
        return obj;
    }

    public int getCount()
    {
        int i = mMap.size();
        return i;
    }

    public Object getFirstKey()
    {
        boolean flag = mMap.isEmpty();
        Object obj = null;
        if(!flag)
            obj = mMap.keySet().iterator().next();

        return obj;
    }

    ArrayList getKeys()
    {
        ArrayList arraylist = new ArrayList(mMap.keySet());
        return arraylist;
    }

    public ArrayList getMatchingEntries(Predicate predicate)
    {
        ArrayList arraylist;
        Iterator iterator;
        arraylist = new ArrayList();
        iterator = mMap.entrySet().iterator();
        java.util.Map.Entry entry;

        for(;iterator.hasNext();) {
            entry = (java.util.Map.Entry) iterator.next();
            if (predicate != null) {
                if (!predicate.apply(entry.getKey()))
                    continue;
            }
            arraylist.add(entry);
        }
        return arraylist;
    }

    public int getSizeInBytes()
    {
        int i = mSizeInBytes;
        return i;
    }

    ArrayList getValues()
    {
        ArrayList arraylist = new ArrayList(mMap.values());
        return arraylist;
    }

    public Object put(Object obj, Object obj1)
    {
        Object obj2;
        obj2 = mMap.remove(obj);
        mSizeInBytes = mSizeInBytes - getValueSizeInBytes(obj2);
        mMap.put(obj, obj1);
        mSizeInBytes = mSizeInBytes + getValueSizeInBytes(obj1);
        return obj2;
    }

    public Object remove(Object obj)
    {
        obj = mMap.remove(obj);
        mSizeInBytes = mSizeInBytes - getValueSizeInBytes(obj);
        return obj;
    }

    public ArrayList removeAll(Predicate predicate)
    {
        ArrayList arraylist;
        Iterator iterator;
        arraylist = new ArrayList();
        iterator = mMap.entrySet().iterator();

        java.util.Map.Entry entry;
        for(;iterator.hasNext();) {
            entry = (java.util.Map.Entry) iterator.next();
            if (predicate != null) {
                if (!predicate.apply(entry.getKey()))
                    continue;
            }
            arraylist.add(entry.getValue());
            mSizeInBytes = mSizeInBytes - getValueSizeInBytes(entry.getValue());
            iterator.remove();
        }
        return arraylist;
    }

    private final LinkedHashMap mMap = new LinkedHashMap();
    private int mSizeInBytes;
    private final ValueDescriptor mValueDescriptor;
}
