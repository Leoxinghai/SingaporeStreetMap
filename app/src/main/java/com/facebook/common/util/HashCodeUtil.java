

package com.facebook.common.util;


public class HashCodeUtil
{

    public HashCodeUtil()
    {
    }

    public static int hashCode(int i)
    {
        return i + 31;
    }

    public static int hashCode(int i, int j)
    {
        return (i + 31) * 31 + j;
    }

    public static int hashCode(int i, int j, int k)
    {
        return ((i + 31) * 31 + j) * 31 + k;
    }

    public static int hashCode(int i, int j, int k, int l)
    {
        return (((i + 31) * 31 + j) * 31 + k) * 31 + l;
    }

    public static int hashCode(int i, int j, int k, int l, int i1)
    {
        return ((((i + 31) * 31 + j) * 31 + k) * 31 + l) * 31 + i1;
    }

    public static int hashCode(int i, int j, int k, int l, int i1, int j1)
    {
        return (((((i + 31) * 31 + j) * 31 + k) * 31 + l) * 31 + i1) * 31 + j1;
    }

    public static int hashCode(Object obj)
    {
        int i;
        if(obj == null)
            i = 0;
        else
            i = obj.hashCode();
        return hashCode(i);
    }

    public static int hashCode(Object obj, Object obj1)
    {
        int j = 0;
        int i;
        if(obj == null)
            i = 0;
        else
            i = obj.hashCode();
        if(obj1 != null)
            j = obj1.hashCode();
        return hashCode(i, j);
    }

    public static int hashCode(Object obj, Object obj1, Object obj2)
    {
        int k = 0;
        int i;
        int j;
        if(obj == null)
            i = 0;
        else
            i = obj.hashCode();
        if(obj1 == null)
            j = 0;
        else
            j = obj1.hashCode();
        if(obj2 != null)
            k = obj2.hashCode();
        return hashCode(i, j, k);
    }

    public static int hashCode(Object obj, Object obj1, Object obj2, Object obj3)
    {
        int l = 0;
        int i;
        int j;
        int k;
        if(obj == null)
            i = 0;
        else
            i = obj.hashCode();
        if(obj1 == null)
            j = 0;
        else
            j = obj1.hashCode();
        if(obj2 == null)
            k = 0;
        else
            k = obj2.hashCode();
        if(obj3 != null)
            l = obj3.hashCode();
        return hashCode(i, j, k, l);
    }

    public static int hashCode(Object obj, Object obj1, Object obj2, Object obj3, Object obj4)
    {
        int i1 = 0;
        int i;
        int j;
        int k;
        int l;
        if(obj == null)
            i = 0;
        else
            i = obj.hashCode();
        if(obj1 == null)
            j = 0;
        else
            j = obj1.hashCode();
        if(obj2 == null)
            k = 0;
        else
            k = obj2.hashCode();
        if(obj3 == null)
            l = 0;
        else
            l = obj3.hashCode();
        if(obj4 != null)
            i1 = obj4.hashCode();
        return hashCode(i, j, k, l, i1);
    }

    public static int hashCode(Object obj, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5)
    {
        int j1 = 0;
        int i;
        int j;
        int k;
        int l;
        int i1;
        if(obj == null)
            i = 0;
        else
            i = obj.hashCode();
        if(obj1 == null)
            j = 0;
        else
            j = obj1.hashCode();
        if(obj2 == null)
            k = 0;
        else
            k = obj2.hashCode();
        if(obj3 == null)
            l = 0;
        else
            l = obj3.hashCode();
        if(obj4 == null)
            i1 = 0;
        else
            i1 = obj4.hashCode();
        if(obj5 != null)
            j1 = obj5.hashCode();
        return hashCode(i, j, k, l, i1, j1);
    }

    private static final int X = 31;
}
