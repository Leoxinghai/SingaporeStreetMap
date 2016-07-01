// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.references;

import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;

// Referenced classes of package com.facebook.common.references:
//            SharedReference, ResourceReleaser

public final class CloseableReference
    implements Cloneable, Closeable
{

    private CloseableReference(SharedReference sharedreference)
    {
        mIsClosed = false;
        mSharedReference = (SharedReference)Preconditions.checkNotNull(sharedreference);
        sharedreference.addReference();
    }

    private CloseableReference(Object obj, ResourceReleaser resourcereleaser)
    {
        mIsClosed = false;
        mSharedReference = new SharedReference(obj, resourcereleaser);
    }

    public static CloseableReference cloneOrNull(CloseableReference closeablereference)
    {
        if(closeablereference != null)
            return closeablereference.cloneOrNull();
        else
            return null;
    }

    public static List cloneOrNull(Collection collection)
    {
        if(collection != null) {
            ArrayList arraylist = new ArrayList(collection.size());
            Iterator iterator = collection.iterator();
            //collection = arraylist;
            for(;iterator.hasNext();) {
                arraylist.add(cloneOrNull((CloseableReference)iterator.next()));
            }
            return arraylist;
        }

        return null;
    }

    public static void closeSafely(CloseableReference closeablereference)
    {
        if(closeablereference != null)
            closeablereference.close();
    }

    public static void closeSafely(Iterable iterable)
    {
        if(iterable != null)
            for(Iterator iterator = iterable.iterator(); iterator.hasNext(); closeSafely((CloseableReference)iterator.next()));
    }

    public static boolean isValid(CloseableReference closeablereference)
    {
        return closeablereference != null && closeablereference.isValid();
    }

    public static CloseableReference of(Closeable closeable)
    {
        if(closeable == null)
            return null;
        else
            return new CloseableReference(closeable, DEFAULT_CLOSEABLE_RELEASER);
    }

    public static CloseableReference of(Object obj, ResourceReleaser resourcereleaser)
    {
        if(obj == null)
            return null;
        else
            return new CloseableReference(obj, resourcereleaser);
    }

    public CloseableReference clone()
    {
        CloseableReference closeablereference;
        Preconditions.checkState(isValid());
        closeablereference = new CloseableReference(mSharedReference);
        return closeablereference;
    }

    public CloseableReference cloneOrNull()
    {
        CloseableReference closeablereference;
        if(!isValid())
            closeablereference = null;
        else
            closeablereference = new CloseableReference(mSharedReference);

        return closeablereference;
    }

    public void close()
    {
        if(!mIsClosed) {
            mIsClosed = true;
            mSharedReference.deleteReference();
            return;
        }
        return;
    }

    protected void finalize()
        throws Throwable
    {
        if(!mIsClosed) {
            FLog.w(TAG, "Finalized without closing: %x %x (type = %s)", new Object[] {
                    Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(mSharedReference)), mSharedReference.get().getClass().getSimpleName()
            });
            close();
            super.finalize();
            return;

        }
        super.finalize();
        return;
    }

    public Object get()
    {
        Object obj;
        boolean flag;
        if(!mIsClosed)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        obj = mSharedReference.get();
        return obj;
    }

    public SharedReference getUnderlyingReferenceTestOnly()
    {
        SharedReference sharedreference = mSharedReference;
        return sharedreference;
    }

    public int getValueHash()
    {
        int i;
        if(!isValid())
            i = 0;
        else
            i = System.identityHashCode(mSharedReference.get());
        return i;
    }

    public boolean isValid()
    {
        boolean flag = mIsClosed;
        if(!flag)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static final ResourceReleaser DEFAULT_CLOSEABLE_RELEASER = new ResourceReleaser() {

        public void release(Closeable closeable)
        {
            try
            {
                Closeables.close(closeable, true);
                return;
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                return;
            }
        }

        public void release(Object obj)
        {
            release((Closeable)obj);
        }

    };

    private static Class TAG = CloseableReference.class;
    private boolean mIsClosed;
    private final SharedReference mSharedReference;

}
