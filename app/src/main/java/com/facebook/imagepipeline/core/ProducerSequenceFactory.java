// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.core;

import android.net.Uri;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.media.MediaUtils;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.producers.*;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.core:
//            ProducerFactory

public class ProducerSequenceFactory
{

    public ProducerSequenceFactory(ProducerFactory producerfactory, NetworkFetcher networkfetcher, boolean flag, boolean flag1)
    {
        mProducerFactory = producerfactory;
        mNetworkFetcher = networkfetcher;
        mResizeAndRotateEnabledForNetwork = flag;
        mDownsampleEnabled = flag1;
        mPostprocessorSequences = new HashMap();
        mCloseableImagePrefetchSequences = new HashMap();
    }

    private Producer getBackgroundNetworkFetchToEncodedMemorySequence()
    {
        Producer producer;
        if(mBackgroundNetworkFetchToEncodedMemorySequence == null)
            mBackgroundNetworkFetchToEncodedMemorySequence = mProducerFactory.newBackgroundThreadHandoffProducer(getCommonNetworkFetchToEncodedMemorySequence());
        producer = mBackgroundNetworkFetchToEncodedMemorySequence;
        return producer;
    }

    private Producer getBasicDecodedImageSequence(ImageRequest imagerequest)
    {
        Preconditions.checkNotNull(imagerequest);
        Uri temp = imagerequest.getSourceUri();
        Preconditions.checkNotNull(imagerequest, "Uri is null.");
        if(UriUtil.isNetworkUri(temp))
            return getNetworkFetchSequence();
        if(UriUtil.isLocalFileUri(temp))
            if(MediaUtils.isVideo(MediaUtils.extractMime(temp.getPath())))
                return getLocalVideoFileFetchSequence();
            else
                return getLocalImageFileFetchSequence();
        if(UriUtil.isLocalContentUri(temp))
            return getLocalContentUriFetchSequence();
        if(UriUtil.isLocalAssetUri(temp))
            return getLocalAssetFetchSequence();
        if(UriUtil.isLocalResourceUri(temp))
            return getLocalResourceFetchSequence();
        if(UriUtil.isDataUri(temp))
            return getDataFetchSequence();
        String s = imagerequest.toString();

        String temp2;
        if(s.length() > 30) {
            temp2 = (new StringBuilder()).append(s.substring(0, 30)).append("...").toString();
            throw new RuntimeException((new StringBuilder()).append("Unsupported uri scheme! Uri is: ").append(temp2).toString());
        }
        return null;
    }

    private Producer getCommonNetworkFetchToEncodedMemorySequence()
    {
        Producer producer;
        if(mCommonNetworkFetchToEncodedMemorySequence == null)
        {
            mCommonNetworkFetchToEncodedMemorySequence = ProducerFactory.newAddImageTransformMetaDataProducer(newEncodedCacheMultiplexToTranscodeSequence(mProducerFactory.newNetworkFetchProducer(mNetworkFetcher)));
            if(mResizeAndRotateEnabledForNetwork && !mDownsampleEnabled)
                mCommonNetworkFetchToEncodedMemorySequence = mProducerFactory.newResizeAndRotateProducer(mCommonNetworkFetchToEncodedMemorySequence);
        }
        producer = mCommonNetworkFetchToEncodedMemorySequence;
        return producer;
    }

    private Producer getDataFetchSequence()
    {
        Object obj1;
        if(mDataFetchSequence == null)
            return null;
        obj1 = mProducerFactory.newDataFetchProducer();
        Object obj = obj1;
        if(android.os.Build.VERSION.SDK_INT < 18)
            obj = mProducerFactory.newWebpTranscodeProducer(((Producer) (obj1)));
        obj1 = mProducerFactory;
        obj1 = ProducerFactory.newAddImageTransformMetaDataProducer(((Producer) (obj)));
        obj = obj1;
        if(!mDownsampleEnabled)
            obj = mProducerFactory.newResizeAndRotateProducer(((Producer) (obj1)));
        mDataFetchSequence = newBitmapCacheGetToDecodeSequence(((Producer) (obj)));
        obj = mDataFetchSequence;
        return ((Producer) (obj));
    }

    private Producer getDecodedImagePrefetchSequence(Producer producer)
    {
        if(!mCloseableImagePrefetchSequences.containsKey(producer))
        {
            Object obj = mProducerFactory;
            obj = ProducerFactory.newSwallowResultProducer(producer);
            mCloseableImagePrefetchSequences.put(producer, obj);
        }
        producer = (Producer)mCloseableImagePrefetchSequences.get(producer);
        return producer;
    }

    private Producer getLocalAssetFetchSequence()
    {
        Producer producer;
        if(mLocalAssetFetchSequence == null)
            mLocalAssetFetchSequence = newBitmapCacheGetToLocalTransformSequence(mProducerFactory.newLocalAssetFetchProducer());
        producer = mLocalAssetFetchSequence;
        return producer;
    }

    private Producer getLocalContentUriFetchSequence()
    {
        Producer producer;
        if(mLocalContentUriFetchSequence == null)
            mLocalContentUriFetchSequence = newBitmapCacheGetToLocalTransformSequence(mProducerFactory.newContentUriFetchProducer());
        producer = mLocalContentUriFetchSequence;
        return producer;
    }

    private Producer getLocalImageFileFetchSequence()
    {
        Producer producer;
        if(mLocalImageFileFetchSequence == null)
            mLocalImageFileFetchSequence = newBitmapCacheGetToLocalTransformSequence(mProducerFactory.newLocalFileFetchProducer());
        producer = mLocalImageFileFetchSequence;
        return producer;
    }

    private Producer getLocalResourceFetchSequence()
    {
        Producer producer;
        if(mLocalResourceFetchSequence == null)
            mLocalResourceFetchSequence = newBitmapCacheGetToLocalTransformSequence(mProducerFactory.newLocalResourceFetchProducer());
        producer = mLocalResourceFetchSequence;
        return producer;
    }

    private Producer getLocalVideoFileFetchSequence()
    {
        Producer producer;
        if(mLocalVideoFileFetchSequence == null)
            mLocalVideoFileFetchSequence = newBitmapCacheGetToBitmapCacheSequence(mProducerFactory.newLocalVideoThumbnailProducer());
        producer = mLocalVideoFileFetchSequence;
        return producer;
    }

    private Producer getNetworkFetchSequence()
    {
        Producer producer;
        if(mNetworkFetchSequence == null)
            mNetworkFetchSequence = newBitmapCacheGetToDecodeSequence(getCommonNetworkFetchToEncodedMemorySequence());
        producer = mNetworkFetchSequence;
        return producer;
    }

    private Producer getNetworkFetchToEncodedMemoryPrefetchSequence()
    {
        Producer producer;
        if(mNetworkFetchToEncodedMemoryPrefetchSequence == null)
        {
            ProducerFactory producerfactory = mProducerFactory;
            mNetworkFetchToEncodedMemoryPrefetchSequence = ProducerFactory.newSwallowResultProducer(getBackgroundNetworkFetchToEncodedMemorySequence());
        }
        producer = mNetworkFetchToEncodedMemoryPrefetchSequence;
        return producer;
    }

    private Producer getPostprocessorSequence(Producer producer)
    {
        if(!mPostprocessorSequences.containsKey(producer))
        {
            Object obj = mProducerFactory.newPostprocessorProducer(producer);
            obj = mProducerFactory.newPostprocessorBitmapMemoryCacheProducer(((Producer) (obj)));
            mPostprocessorSequences.put(producer, obj);
        }
        producer = (Producer)mPostprocessorSequences.get(producer);
        return producer;
    }

    private Producer newBitmapCacheGetToBitmapCacheSequence(Producer producer)
    {
        producer = mProducerFactory.newBitmapMemoryCacheProducer(producer);
        producer = mProducerFactory.newBitmapMemoryCacheKeyMultiplexProducer(producer);
        producer = mProducerFactory.newBackgroundThreadHandoffProducer(producer);
        return mProducerFactory.newBitmapMemoryCacheGetProducer(producer);
    }

    private Producer newBitmapCacheGetToDecodeSequence(Producer producer)
    {
        return newBitmapCacheGetToBitmapCacheSequence(mProducerFactory.newDecodeProducer(producer));
    }

    private Producer newBitmapCacheGetToLocalTransformSequence(Producer producer)
    {
        return newBitmapCacheGetToDecodeSequence(newLocalTransformationsSequence(newEncodedCacheMultiplexToTranscodeSequence(producer)));
    }

    private Producer newEncodedCacheMultiplexToTranscodeSequence(Producer producer)
    {
        Object obj = producer;
        if(android.os.Build.VERSION.SDK_INT < 18)
            obj = mProducerFactory.newWebpTranscodeProducer(producer);
        producer = mProducerFactory.newDiskCacheProducer(((Producer) (obj)));
        producer = mProducerFactory.newEncodedMemoryCacheProducer(producer);
        return mProducerFactory.newEncodedCacheKeyMultiplexProducer(producer);
    }

    private Producer newLocalTransformationsSequence(Producer producer)
    {
        Object obj = mProducerFactory;
        obj = ProducerFactory.newAddImageTransformMetaDataProducer(producer);
        producer = ((Producer) (obj));
        if(!mDownsampleEnabled)
            producer = mProducerFactory.newResizeAndRotateProducer(((Producer) (obj)));
        com.facebook.imagepipeline.producers.ThrottlingProducer throttlingproducer = mProducerFactory.newThrottlingProducer(5, producer);
        obj = mProducerFactory.newLocalExifThumbnailProducer();
        producer = ((Producer) (obj));
        if(!mDownsampleEnabled)
            producer = mProducerFactory.newResizeAndRotateProducer(((Producer) (obj)));
        obj = mProducerFactory;
        return ProducerFactory.newBranchOnSeparateImagesProducer(producer, throttlingproducer);
    }

    private static void validateEncodedImageRequest(ImageRequest imagerequest)
    {
        Preconditions.checkNotNull(imagerequest);
        Preconditions.checkArgument(UriUtil.isNetworkUri(imagerequest.getSourceUri()));
        boolean flag;
        if(imagerequest.getLowestPermittedRequestLevel().getValue() <= com.facebook.imagepipeline.request.ImageRequest.RequestLevel.ENCODED_MEMORY_CACHE.getValue())
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
    }

    public Producer getDecodedImagePrefetchProducerSequence(ImageRequest imagerequest)
    {
        return getDecodedImagePrefetchSequence(getBasicDecodedImageSequence(imagerequest));
    }

    public Producer getDecodedImageProducerSequence(ImageRequest imagerequest)
    {
        Producer producer1 = getBasicDecodedImageSequence(imagerequest);
        Producer producer = producer1;
        if(imagerequest.getPostprocessor() != null)
            producer = getPostprocessorSequence(producer1);
        return producer;
    }

    public Producer getEncodedImagePrefetchProducerSequence(ImageRequest imagerequest)
    {
        validateEncodedImageRequest(imagerequest);
        return getNetworkFetchToEncodedMemoryPrefetchSequence();
    }

    public Producer getEncodedImageProducerSequence(ImageRequest imagerequest)
    {
        validateEncodedImageRequest(imagerequest);
        if(mEncodedImageProducerSequence == null)
            mEncodedImageProducerSequence = new RemoveImageTransformMetaDataProducer(getBackgroundNetworkFetchToEncodedMemorySequence());
        return mEncodedImageProducerSequence;
    }

    private static final int MAX_SIMULTANEOUS_FILE_FETCH_AND_RESIZE = 5;
    Producer mBackgroundNetworkFetchToEncodedMemorySequence;
    Map mCloseableImagePrefetchSequences;
    private Producer mCommonNetworkFetchToEncodedMemorySequence;
    Producer mDataFetchSequence;
    private final boolean mDownsampleEnabled;
    Producer mEncodedImageProducerSequence;
    Producer mLocalAssetFetchSequence;
    Producer mLocalContentUriFetchSequence;
    Producer mLocalImageFileFetchSequence;
    Producer mLocalResourceFetchSequence;
    Producer mLocalVideoFileFetchSequence;
    Producer mNetworkFetchSequence;
    Producer mNetworkFetchToEncodedMemoryPrefetchSequence;
    private final NetworkFetcher mNetworkFetcher;
    Map mPostprocessorSequences;
    private final ProducerFactory mProducerFactory;
    private final boolean mResizeAndRotateEnabledForNetwork;
}
