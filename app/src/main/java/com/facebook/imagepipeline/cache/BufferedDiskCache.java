// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import bolts.Task;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.cache.disk.FileCache;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            StagingArea, ImageCacheStatsTracker

public class BufferedDiskCache
{

    public BufferedDiskCache(FileCache filecache, PooledByteBufferFactory pooledbytebufferfactory, PooledByteStreams pooledbytestreams, Executor executor, Executor executor1, ImageCacheStatsTracker imagecachestatstracker)
    {
        mFileCache = filecache;
        mPooledByteBufferFactory = pooledbytebufferfactory;
        mPooledByteStreams = pooledbytestreams;
        mReadExecutor = executor;
        mWriteExecutor = executor1;
        mImageCacheStatsTracker = imagecachestatstracker;
    }

    private PooledByteBuffer readFromDiskCache(CacheKey cachekey)
        throws IOException
    {
        Object obj;
        Object obj1;
        try
        {
            FLog.v(TAG, "Disk cache read for %s", cachekey.toString());
            obj1 = mFileCache.getResource(cachekey);
            if(obj1 != null) {
                FLog.v(TAG, "Found entry in disk cache for %s", cachekey.toString());
                mImageCacheStatsTracker.onDiskCacheHit();
                obj = ((BinaryResource) (obj1)).openStream();
                obj1 = mPooledByteBufferFactory.newByteBuffer(((InputStream) (obj)), (int)((BinaryResource) (obj1)).size());
                ((InputStream) (obj)).close();
                FLog.v(TAG, "Successful read from disk cache for %s", cachekey.toString());
                return ((PooledByteBuffer) (obj1));

            }
            FLog.v(TAG, "Disk cache miss for %s", cachekey.toString());
            mImageCacheStatsTracker.onDiskCacheMiss();
            return null;
        }
        catch(Exception ex)
        {
            FLog.w(TAG, ((Throwable) (ex)), "Exception reading from cache for %s", new Object[] {
                    cachekey.toString()
            });
            mImageCacheStatsTracker.onDiskCacheGetFail();
            throw ex;
        }


    }

    private void writeToDiskCache(CacheKey cachekey, final EncodedImage encodedImage)
    {
        FLog.v(TAG, "About to write to disk-cache for key %s", cachekey.toString());
        try
        {
            mFileCache.insert(cachekey, new WriterCallback() {

                public void write(OutputStream outputstream)
                    throws IOException
                {
                    mPooledByteStreams.copy(encodedImage.getInputStream(), outputstream);
                }

            });
            FLog.v(TAG, "Successful disk-cache write for key %s", cachekey.toString());
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            FLog.w(TAG, ex, "Failed to write to disk-cache for key %s", new Object[] {
                cachekey.toString()
            });
        }
    }

    public Task clearAll()
    {
        mStagingArea.clearAll();
        Task task;
        try
        {
            task = Task.call(new Callable() {

                public Void call()
                    throws Exception
                {
                    mStagingArea.clearAll();
                    mFileCache.clearAll();
                    return null;
                }

            }, mWriteExecutor);
        }
        catch(Exception exception)
        {
            FLog.w(TAG, exception, "Failed to schedule disk-cache clear", new Object[0]);
            return Task.forError(exception);
        }
        return task;
    }

    public Task contains(final CacheKey key)
    {
        Preconditions.checkNotNull(key);
        Object obj = mStagingArea.get(key);
        if(obj != null)
        {
            ((EncodedImage) (obj)).close();
            FLog.v(TAG, "Found image for %s in staging area", key.toString());
            mImageCacheStatsTracker.onStagingAreaHit();
            return Task.forResult(Boolean.valueOf(true));
        }
        try
        {
            obj = Task.call(new Callable() {

                public Boolean call()
                    throws Exception
                {
                    EncodedImage encodedimage = mStagingArea.get(key);
                    if(encodedimage != null)
                    {
                        encodedimage.close();
                        FLog.v(BufferedDiskCache.TAG, "Found image for %s in staging area", key.toString());
                        mImageCacheStatsTracker.onStagingAreaHit();
                        return Boolean.valueOf(true);
                    }
                    FLog.v(BufferedDiskCache.TAG, "Did not find image for %s in staging area", key.toString());
                    mImageCacheStatsTracker.onStagingAreaMiss();
                    boolean flag;
                    try
                    {
                        flag = mFileCache.hasKey(key);
                    }
                    catch(Exception exception1)
                    {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(flag);
                }


            }
, mReadExecutor);
        }
        catch(Exception exception)
        {
            FLog.w(TAG, exception, "Failed to schedule disk-cache read for %s", new Object[] {
                key.toString()
            });
            return Task.forError(exception);
        }
        return ((Task) (obj));
    }

    public Task get(final CacheKey key, final AtomicBoolean isCancelled)
    {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(isCancelled);
        EncodedImage encodedimage = mStagingArea.get(key);
        Task temp;
        if(encodedimage != null)
        {
            FLog.v(TAG, "Found image for %s in staging area", key.toString());
            mImageCacheStatsTracker.onStagingAreaHit();
            return Task.forResult(encodedimage);
        }
        try
        {
            temp = Task.call(new Callable() {

                public EncodedImage call()
                    throws Exception
                {
                    Object obj;
                    CloseableReference closeablereference;
                    try {
                        if(isCancelled.get())
                            throw new CancellationException();
                        obj = mStagingArea.get(key);
                        if(obj == null) {
                            FLog.v(BufferedDiskCache.TAG, "Did not find image for %s in staging area", key.toString());
                            mImageCacheStatsTracker.onStagingAreaMiss();
                            closeablereference = CloseableReference.of(readFromDiskCache(key));
                            obj = new EncodedImage(closeablereference);
                            CloseableReference.closeSafely(closeablereference);
                        } else {
                            FLog.v(BufferedDiskCache.TAG, "Found image for %s in staging area", key.toString());
                            mImageCacheStatsTracker.onStagingAreaHit();
                        }

                        if(Thread.interrupted())
                        {
                            FLog.v(BufferedDiskCache.TAG, "Host thread was interrupted, decreasing reference count");
                            if(obj != null)
                                ((EncodedImage) (obj)).close();
                            throw new InterruptedException();
                        } else
                        {
                            return ((EncodedImage) (obj));
                        }
                    }
                    catch(Exception ex) {
                    }
                    return null;
                }


            }, mReadExecutor);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            FLog.w(TAG, ex, "Failed to schedule disk-cache read for %s", new Object[] {
                key.toString()
            });
            return Task.forError(ex);
        }
        return temp;
    }

    public void put(final CacheKey key, EncodedImage encodedimage)
    {
        Preconditions.checkNotNull(key);
        Preconditions.checkArgument(EncodedImage.isValid(encodedimage));
        mStagingArea.put(key, encodedimage);
        final EncodedImage finalEncodedImage = EncodedImage.cloneOrNull(encodedimage);
        try
        {
            mWriteExecutor.execute(new Runnable() {

                public void run()
                {
                    writeToDiskCache(key, finalEncodedImage);
                    mStagingArea.remove(key, finalEncodedImage);
                    EncodedImage.closeSafely(finalEncodedImage);
                    return;
                }



            });
            return;
        }
        catch(Exception exception)
        {
            FLog.w(TAG, exception, "Failed to schedule disk-cache write for %s", new Object[] {
                key.toString()
            });
        }
        mStagingArea.remove(key, encodedimage);
        EncodedImage.closeSafely(finalEncodedImage);
    }

    public Task remove(final CacheKey key)
    {
        Preconditions.checkNotNull(key);
        mStagingArea.remove(key);
        Task task;
        try
        {
            task = Task.call(new Callable() {


                public Void call()
                    throws Exception
                {
                    mStagingArea.remove(key);
                    mFileCache.remove(key);
                    return null;
                }
            }
, mWriteExecutor);
        }
        catch(Exception exception)
        {
            FLog.w(TAG, exception, "Failed to schedule disk-cache remove for %s", new Object[] {
                key.toString()
            });
            return Task.forError(exception);
        }
        return task;
    }

    private static final Class TAG = BufferedDiskCache.class;
    private final FileCache mFileCache;
    private final ImageCacheStatsTracker mImageCacheStatsTracker;
    private final PooledByteBufferFactory mPooledByteBufferFactory;
    private final PooledByteStreams mPooledByteStreams;
    private final Executor mReadExecutor;
    private final StagingArea mStagingArea = StagingArea.getInstance();
    private final Executor mWriteExecutor;








}
