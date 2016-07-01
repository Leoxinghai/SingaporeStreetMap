// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.core;

import android.net.Uri;
import bolts.Continuation;
import bolts.Task;
import com.android.internal.util.Predicate;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.*;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.*;
import com.facebook.imagepipeline.cache.*;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.datasource.CloseableProducerToDataSourceAdapter;
import com.facebook.imagepipeline.datasource.ProducerToDataSourceAdapter;
import com.facebook.imagepipeline.listener.ForwardingRequestListener;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.producers.Producer;
import com.facebook.imagepipeline.producers.SettableProducerContext;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicLong;

// Referenced classes of package com.facebook.imagepipeline.core:
//            ProducerSequenceFactory

public class ImagePipeline
{

    public ImagePipeline(ProducerSequenceFactory producersequencefactory, Set set, Supplier supplier, MemoryCache memorycache, MemoryCache memorycache1, BufferedDiskCache buffereddiskcache, BufferedDiskCache buffereddiskcache1,
            CacheKeyFactory cachekeyfactory)
    {
        mIdCounter = new AtomicLong();
        mProducerSequenceFactory = producersequencefactory;
        mRequestListener = new ForwardingRequestListener(set);
        mIsPrefetchEnabledSupplier = supplier;
        mBitmapMemoryCache = memorycache;
        mEncodedMemoryCache = memorycache1;
        mMainBufferedDiskCache = buffereddiskcache;
        mSmallImageBufferedDiskCache = buffereddiskcache1;
        mCacheKeyFactory = cachekeyfactory;
    }

    private String generateUniqueFutureId()
    {
        return String.valueOf(mIdCounter.getAndIncrement());
    }

    private Predicate predicateForUri(Uri uri)
    {
        final String cacheKeySourceString = uri.toString();
        return new Predicate() {

            public boolean apply(CacheKey cachekey)
            {
                if(cachekey instanceof BitmapMemoryCacheKey)
                    return ((BitmapMemoryCacheKey)cachekey).getSourceUriString().equals(cacheKeySourceString);
                else
                    return false;
            }

            public boolean apply(Object obj)
            {
                return apply((CacheKey)obj);
            }

        };
    }

    private DataSource submitFetchRequest(Producer producer, ImageRequest imagerequest, com.facebook.imagepipeline.request.ImageRequest.RequestLevel requestlevel, Object obj)
    {
        boolean flag = false;
        String s;
        RequestListener requestlistener;
        requestlevel = com.facebook.imagepipeline.request.ImageRequest.RequestLevel.getMax(imagerequest.getLowestPermittedRequestLevel(), requestlevel);
        s = generateUniqueFutureId();
        requestlistener = mRequestListener;
        if(imagerequest.getProgressiveRenderingEnabled() || !UriUtil.isNetworkUri(imagerequest.getSourceUri()))
            flag = true;
        try
        {
            DataSource temp = CloseableProducerToDataSourceAdapter.create(producer, new SettableProducerContext(imagerequest, s, requestlistener, obj, requestlevel, false, flag, imagerequest.getPriority()), mRequestListener);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception exception)
        {
            return DataSources.immediateFailedDataSource(exception);
        }
    }

    private DataSource submitPrefetchRequest(Producer producer, ImageRequest imagerequest, com.facebook.imagepipeline.request.ImageRequest.RequestLevel requestlevel, Object obj)
    {
        try
        {
            requestlevel = com.facebook.imagepipeline.request.ImageRequest.RequestLevel.getMax(imagerequest.getLowestPermittedRequestLevel(), requestlevel);
            DataSource temp = ProducerToDataSourceAdapter.create(producer, new SettableProducerContext(imagerequest, generateUniqueFutureId(), mRequestListener, obj, requestlevel, true, false, Priority.LOW), mRequestListener);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return DataSources.immediateFailedDataSource(ex);
        }
    }

    public void clearCaches()
    {
        clearMemoryCaches();
        clearDiskCaches();
    }

    public void clearDiskCaches()
    {
        mMainBufferedDiskCache.clearAll();
        mSmallImageBufferedDiskCache.clearAll();
    }

    public void clearMemoryCaches()
    {
        Predicate predicate = new Predicate() {

            public boolean apply(CacheKey cachekey)
            {
                return true;
            }

            public boolean apply(Object obj)
            {
                return apply((CacheKey)obj);
            }

        }
;
        mBitmapMemoryCache.removeAll(predicate);
        mEncodedMemoryCache.removeAll(predicate);
    }

    public void evictFromCache(Uri uri)
    {
        evictFromMemoryCache(uri);
        evictFromDiskCache(uri);
    }

    public void evictFromDiskCache(Uri uri)
    {
        evictFromDiskCache(ImageRequest.fromUri(uri));
    }

    public void evictFromDiskCache(ImageRequest imagerequest)
    {
        CacheKey temp = mCacheKeyFactory.getEncodedCacheKey(imagerequest);
        mMainBufferedDiskCache.remove(temp);
        mSmallImageBufferedDiskCache.remove(temp);
    }

    public void evictFromMemoryCache(Uri uri)
    {
        Predicate predicate = predicateForUri(uri);
        mBitmapMemoryCache.removeAll(predicate);
        final String cacheKeySourceString = uri.toString();
        Predicate temp = new Predicate() {

            public boolean apply(CacheKey cachekey)
            {
                return cachekey.toString().equals(cacheKeySourceString);
            }

            public boolean apply(Object obj)
            {
                return apply((CacheKey)obj);
            }



        };
        mEncodedMemoryCache.removeAll(temp);
    }

    public DataSource fetchDecodedImage(ImageRequest imagerequest, Object obj)
    {
        try
        {
            DataSource temp = submitFetchRequest(mProducerSequenceFactory.getDecodedImageProducerSequence(imagerequest), imagerequest, com.facebook.imagepipeline.request.ImageRequest.RequestLevel.FULL_FETCH, obj);
            return temp;
        }
        catch(Exception ex)
        {
            return DataSources.immediateFailedDataSource(ex);
        }
    }

    public DataSource fetchEncodedImage(ImageRequest imagerequest, Object obj)
    {
        Preconditions.checkNotNull(imagerequest.getSourceUri());
        ImageRequest imagerequest1;
        Producer producer;
        try
        {
            producer = mProducerSequenceFactory.getEncodedImageProducerSequence(imagerequest);
        }
        catch(Exception ex)
        {
            return DataSources.immediateFailedDataSource(ex);
        }
        imagerequest1 = imagerequest;
        if(imagerequest.getResizeOptions() != null)
            imagerequest1 = ImageRequestBuilder.fromRequest(imagerequest).setResizeOptions(null).build();
        DataSource temp = submitFetchRequest(producer, imagerequest1, com.facebook.imagepipeline.request.ImageRequest.RequestLevel.FULL_FETCH, obj);
        return temp;
    }

    public DataSource fetchImageFromBitmapCache(ImageRequest imagerequest, Object obj)
    {
        try
        {
            DataSource temp  = submitFetchRequest(mProducerSequenceFactory.getDecodedImageProducerSequence(imagerequest), imagerequest, com.facebook.imagepipeline.request.ImageRequest.RequestLevel.BITMAP_MEMORY_CACHE, obj);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return DataSources.immediateFailedDataSource(ex);
        }
    }

    public Supplier getDataSourceSupplier(final ImageRequest imageRequest, final Object callerContext, final boolean bitmapCacheOnly)
    {
        return new Supplier() {

            public DataSource get()
            {
                if(bitmapCacheOnly)
                    return fetchImageFromBitmapCache(imageRequest, callerContext);
                else
                    return fetchDecodedImage(imageRequest, callerContext);
            }

            public String toString()
            {
                return Objects.toStringHelper(this).add("uri", imageRequest.getSourceUri()).toString();
            }

        };
    }

    public Supplier getEncodedImageDataSourceSupplier(final ImageRequest imageRequest, final Object callerContext)
    {
        return new Supplier() {

            public DataSource get()
            {
                return fetchEncodedImage(imageRequest, callerContext);
            }


            public String toString()
            {
                return Objects.toStringHelper(this).add("uri", imageRequest.getSourceUri()).toString();
            }

        };
    }

    public boolean isInBitmapMemoryCache(Uri uri)
    {
        Predicate temp = predicateForUri(uri);
        return mBitmapMemoryCache.contains(temp);
    }

    public boolean isInBitmapMemoryCache(ImageRequest imagerequest)
    {
        CacheKey temp = mCacheKeyFactory.getBitmapCacheKey(imagerequest);
        CloseableReference temp2 = mBitmapMemoryCache.get(temp);
        boolean flag = CloseableReference.isValid(temp2);
        CloseableReference.closeSafely(temp2);
        return flag;
    }

    public DataSource isInDiskCache(Uri uri)
    {
        return isInDiskCache(ImageRequest.fromUri(uri));
    }

    public DataSource isInDiskCache(final ImageRequest cacheKey)
    {
        final CacheKey temp = mCacheKeyFactory.getEncodedCacheKey(cacheKey);
        final SettableDataSource dataSource = SettableDataSource.create();
        mMainBufferedDiskCache.contains(temp).continueWithTask(new Continuation() {

            public Task then(Task task)
                throws Exception
            {
                if(!task.isCancelled() && !task.isFaulted() && ((Boolean)task.getResult()).booleanValue())
                    return Task.forResult(Boolean.valueOf(true));
                else
                    return mSmallImageBufferedDiskCache.contains(temp);
            }
        }).continueWith(new Continuation() {

            public Void then(Task task)
                throws Exception
            {
                SettableDataSource settabledatasource = dataSource;
                boolean flag;
                if(!task.isCancelled() && !task.isFaulted() && ((Boolean)task.getResult()).booleanValue())
                    flag = true;
                else
                    flag = false;
                settabledatasource.setResult(Boolean.valueOf(flag));
                return null;
            }

        });
        return dataSource;
    }

    public DataSource prefetchToBitmapCache(ImageRequest imagerequest, Object obj)
    {
        if(!((Boolean)mIsPrefetchEnabledSupplier.get()).booleanValue())
            return DataSources.immediateFailedDataSource(PREFETCH_EXCEPTION);
        try
        {
            DataSource temp = submitPrefetchRequest(mProducerSequenceFactory.getDecodedImagePrefetchProducerSequence(imagerequest), imagerequest, com.facebook.imagepipeline.request.ImageRequest.RequestLevel.FULL_FETCH, obj);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return DataSources.immediateFailedDataSource(ex);
        }
    }

    public DataSource prefetchToDiskCache(ImageRequest imagerequest, Object obj)
    {
        if(!((Boolean)mIsPrefetchEnabledSupplier.get()).booleanValue())
            return DataSources.immediateFailedDataSource(PREFETCH_EXCEPTION);
        try
        {
            DataSource temp = submitPrefetchRequest(mProducerSequenceFactory.getEncodedImagePrefetchProducerSequence(imagerequest), imagerequest, com.facebook.imagepipeline.request.ImageRequest.RequestLevel.FULL_FETCH, obj);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return DataSources.immediateFailedDataSource(ex);
        }
    }

    private static final CancellationException PREFETCH_EXCEPTION = new CancellationException("Prefetching is not enabled");
    private final MemoryCache mBitmapMemoryCache;
    private final CacheKeyFactory mCacheKeyFactory;
    private final MemoryCache mEncodedMemoryCache;
    private AtomicLong mIdCounter;
    private final Supplier mIsPrefetchEnabledSupplier;
    private final BufferedDiskCache mMainBufferedDiskCache;
    private final ProducerSequenceFactory mProducerSequenceFactory;
    private final RequestListener mRequestListener;
    private final BufferedDiskCache mSmallImageBufferedDiskCache;


}
