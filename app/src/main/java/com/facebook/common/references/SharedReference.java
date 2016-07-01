

package com.facebook.common.references;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import java.util.IdentityHashMap;
import java.util.Map;

// Referenced classes of package com.facebook.common.references:
//            ResourceReleaser

public class SharedReference
{
    public static class NullReferenceException extends RuntimeException
    {

        public NullReferenceException()
        {
            super("Null shared reference");
        }
    }


    public SharedReference(Object obj, ResourceReleaser resourcereleaser)
    {
        mValue = Preconditions.checkNotNull(obj);
        mResourceReleaser = (ResourceReleaser)Preconditions.checkNotNull(resourcereleaser);
        mRefCount = 1;
        addLiveReference(obj);
    }

    private static void addLiveReference(Object obj)
    {
        Map map = sLiveObjects;
        Integer integer = (Integer)sLiveObjects.get(obj);
        if(integer != null) {
            sLiveObjects.put(obj, Integer.valueOf(integer.intValue() + 1));
        } else
            sLiveObjects.put(obj, Integer.valueOf(1));
        return;
    }

    private int decreaseRefCount()
    {
        ensureValid();
        int i;
        boolean flag;
        if(mRefCount > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        mRefCount = mRefCount - 1;
        i = mRefCount;
        return i;
    }

    private void ensureValid()
    {
        if(!isValid(this))
            throw new NullReferenceException();
        else
            return;
    }

    public static boolean isValid(SharedReference sharedreference)
    {
        return sharedreference != null && sharedreference.isValid();
    }

    private static void removeLiveReference(Object obj)
    {
        Map map = sLiveObjects;
        Integer integer = (Integer)sLiveObjects.get(obj);
        if(integer != null) {
            if(integer.intValue() != 1) {
                sLiveObjects.put(obj, Integer.valueOf(integer.intValue() - 1));
            } else
                sLiveObjects.remove(obj);
        } else {
            FLog.wtf("SharedReference", "No entry in sLiveObjects for value of type %s", new Object[]{
                    obj.getClass()
            });
        }
        return;
    }

    public void addReference()
    {
        ensureValid();
        mRefCount = mRefCount + 1;
        return;
    }

    public void deleteReference()
    {
        if(decreaseRefCount() == 0) {
            Object obj;
            obj = mValue;
            mValue = null;
            mResourceReleaser.release(obj);
            removeLiveReference(obj);
            return;
        }
    }

    public Object get()
    {
        Object obj = mValue;
        return obj;
    }

    public int getRefCountTestOnly()
    {
        int i = mRefCount;
        return i;
    }

    public boolean isValid()
    {
        int i = mRefCount;
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static final Map sLiveObjects = new IdentityHashMap();
    private int mRefCount;
    private final ResourceReleaser mResourceReleaser;
    private Object mValue;

}
