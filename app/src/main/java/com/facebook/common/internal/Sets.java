// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.internal;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public final class Sets
{

    private Sets()
    {
    }

    public static CopyOnWriteArraySet newCopyOnWriteArraySet()
    {
        return new CopyOnWriteArraySet();
    }

    public static HashSet newHashSet()
    {
        return new HashSet();
    }

    public static HashSet newHashSet(Iterable iterable)
    {
        if(iterable instanceof Collection)
            return new HashSet((Collection)iterable);
        else
            return newHashSet(iterable.iterator());
    }

    public static HashSet newHashSet(Iterator iterator)
    {
        HashSet hashset = newHashSet();
        for(; iterator.hasNext(); hashset.add(iterator.next()));
        return hashset;
    }

    public static HashSet newHashSet(Object aobj[])
    {
        HashSet hashset = newHashSetWithCapacity(aobj.length);
        Collections.addAll(hashset, aobj);
        return hashset;
    }

    public static HashSet newHashSetWithCapacity(int i)
    {
        return new HashSet(i);
    }

    public static Set newIdentityHashSet()
    {
        return newSetFromMap(new IdentityHashMap());
    }

    public static LinkedHashSet newLinkedHashSet()
    {
        return new LinkedHashSet();
    }

    public static Set newSetFromMap(Map map)
    {
        return Collections.newSetFromMap(map);
    }
}
