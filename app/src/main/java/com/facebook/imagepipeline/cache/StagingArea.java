// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import java.util.*;

public class StagingArea
{

    private StagingArea()
    {
        mMap = new HashMap();
    }

    public static StagingArea getInstance()
    {
        return new StagingArea();
    }

    private void logStats()
    {
        FLog.v(TAG, "Count = %d", Integer.valueOf(mMap.size()));
        return;
    }

    public void clearAll() {
        ArrayList arraylist;
        arraylist = new ArrayList(mMap.values());
        mMap.clear();
        for (int i = 0; i < arraylist.size(); i++) {
            EncodedImage encodedimage = (EncodedImage) arraylist.get(i);
            if (encodedimage != null)
                encodedimage.close();
        }
    }
    public EncodedImage get(CacheKey cachekey)
    {
        EncodedImage encodedimage1;
        Preconditions.checkNotNull(cachekey);
        encodedimage1 = (EncodedImage)mMap.get(cachekey);
        EncodedImage encodedimage = encodedimage1;
        EncodedImage temp;
        if(encodedimage1 == null) {
            temp = encodedimage;
        } else {
            if (!EncodedImage.isValid(encodedimage1)) {
                mMap.remove(cachekey);
                FLog.w(TAG, "Found closed reference %d for key %s (%d)", new Object[]{
                        Integer.valueOf(System.identityHashCode(encodedimage1)), cachekey.toString(), Integer.valueOf(System.identityHashCode(cachekey))
                });
                temp = null;
            } else {
                encodedimage = EncodedImage.cloneOrNull(encodedimage1);
                temp = encodedimage;
            }
        }
        return temp;
    }

    public void put(CacheKey cachekey, EncodedImage encodedimage)
    {
        Preconditions.checkNotNull(cachekey);
        Preconditions.checkArgument(EncodedImage.isValid(encodedimage));
        EncodedImage.closeSafely((EncodedImage)mMap.put(cachekey, EncodedImage.cloneOrNull(encodedimage)));
        logStats();
        return;
    }

    public boolean remove(CacheKey cachekey)
    {
        Preconditions.checkNotNull(cachekey);
        EncodedImage temp = (EncodedImage)mMap.remove(cachekey);
        if(temp == null)
            return false;
        boolean flag = temp.isValid();
        temp.close();
        return flag;
    }

    public boolean remove(CacheKey cachekey, EncodedImage encodedimage)
    {
        boolean flag = false;
        EncodedImage encodedimage1;
        Preconditions.checkNotNull(cachekey);
        Preconditions.checkNotNull(encodedimage);
        Preconditions.checkArgument(EncodedImage.isValid(encodedimage));
        encodedimage1 = (EncodedImage)mMap.get(cachekey);
        if(encodedimage1 == null)
            return flag;

        CloseableReference closeablereference;
        CloseableReference temp = encodedimage1.getByteBufferRef();
        CloseableReference temp2 = encodedimage.getByteBufferRef();
        if(temp == null || temp2 == null)
            return false;
        Object obj;
        Object obj1;
        obj = temp.get();
        obj1 = temp2.get();
        if(obj == obj1) {
            mMap.remove(cachekey);
            CloseableReference.closeSafely(temp2);
            CloseableReference.closeSafely(temp);
            EncodedImage.closeSafely(encodedimage1);
            logStats();
            flag = true;
            return flag;

        }
        CloseableReference.closeSafely(temp2);
        CloseableReference.closeSafely(temp);
        EncodedImage.closeSafely(encodedimage1);
        return false;
    }

    private static final Class TAG = StagingArea.class;
    private Map mMap;

}
