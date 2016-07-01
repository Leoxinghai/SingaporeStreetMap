// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.internal;

import java.util.*;

public class ImmutableSet extends HashSet
{

    private ImmutableSet(Set set)
    {
        super(set);
    }

    public static ImmutableSet copyOf(Set set)
    {
        return new ImmutableSet(set);
    }

    public static ImmutableSet of(Object aobj[])
    {
        HashSet hashset = new HashSet();
        Collections.addAll(hashset, aobj);
        return new ImmutableSet(hashset);
    }
}
