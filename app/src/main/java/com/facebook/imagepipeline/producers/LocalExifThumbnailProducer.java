// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Pair;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.*;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imageutils.BitmapUtil;
import com.facebook.imageutils.JfifUtil;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, StatefulProducerRunnable,
//            ProducerListener, BaseProducerContextCallbacks

public class LocalExifThumbnailProducer
    implements Producer
{

    public LocalExifThumbnailProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, ContentResolver contentresolver)
    {
        mExecutor = executor;
        mPooledByteBufferFactory = pooledbytebufferfactory;
        mContentResolver = contentresolver;
    }

    private EncodedImage buildEncodedImage(PooledByteBuffer pooledbytebuffer, ExifInterface exifinterface)
    {
        int j = -1;
        Pair pair = BitmapUtil.decodeDimensions(new PooledByteBufferInputStream(pooledbytebuffer));
        int k = getRotationAngle(exifinterface);
        int i;
        if(pair != null)
            i = ((Integer)pair.first).intValue();
        else
            i = -1;
        if(pair != null)
            j = ((Integer)pair.second).intValue();
        EncodedImage encodedImage = new EncodedImage(CloseableReference.of(pooledbytebuffer));
        encodedImage.setImageFormat(ImageFormat.JPEG);
        encodedImage.setRotationAngle(k);
        encodedImage.setWidth(i);
        encodedImage.setHeight(j);
        return encodedImage;
    }

    private String getRealPathFromUri(Uri uri)
    {
        String s = null;
        if(UriUtil.isLocalContentUri(uri))
        {
            Cursor cursor = mContentResolver.query(uri, null, null, null, null);
            if(cursor != null)
            {
                cursor.moveToFirst();
                s = cursor.getString(cursor.getColumnIndex("_data"));
                cursor.close();
            }
        } else
        if(UriUtil.isLocalFileUri(uri))
            return uri.getPath();
        return s;
    }

    private int getRotationAngle(ExifInterface exifinterface)
    {
        return JfifUtil.getAutoRotateAngleFromOrientation(Integer.parseInt(exifinterface.getAttribute("Orientation")));
    }

    boolean canReadAsFile(String s)
        throws IOException
    {
        File file;
        if(s != null)
            if((file = new File(s)).exists() && file.canRead())
                return true;
        return false;
    }

    ExifInterface getExifInterface(Uri uri)
        throws IOException
    {
        String temp = getRealPathFromUri(uri);
        if(canReadAsFile(temp))
            return new ExifInterface(temp);
        else
            return null;
    }

    public void produceResults(final Consumer final_consumer, ProducerContext producercontext)
    {
        final ImageRequest imageRequest = producercontext.getImageRequest();
        final StatefulProducerRunnable statefulProducerRunnable = new StatefulProducerRunnable(final_consumer, producercontext.getListener(), "LocalExifThumbnailProducer", producercontext.getId()) {

            protected void disposeResult(EncodedImage encodedimage)
            {
                EncodedImage.closeSafely(encodedimage);
            }

            protected void disposeResult(Object obj)
            {
                disposeResult((EncodedImage)obj);
            }

            protected Map getExtraMapOnSuccess(EncodedImage encodedimage)
            {
                boolean flag;
                if(encodedimage != null)
                    flag = true;
                else
                    flag = false;
                return ImmutableMap.of("createdThumbnail", Boolean.toString(flag));
            }

            protected Map getExtraMapOnSuccess(Object obj)
            {
                return getExtraMapOnSuccess((EncodedImage)obj);
            }

            protected EncodedImage getResult()
                throws Exception
            {
                Object obj = imageRequest.getSourceUri();
                obj = getExifInterface(((Uri) (obj)));
                if(obj == null || !((ExifInterface) (obj)).hasThumbnail())
                {
                    return null;
                } else
                {
                    byte abyte0[] = ((ExifInterface) (obj)).getThumbnail();
                    PooledByteBuffer pooledbytebuffer = mPooledByteBufferFactory.newByteBuffer(abyte0);
                    return buildEncodedImage(pooledbytebuffer, ((ExifInterface) (obj)));
                }
            }

        };
        producercontext.addCallbacks(new BaseProducerContextCallbacks() {

            public void onCancellationRequested()
            {
                statefulProducerRunnable.cancel();
            }

        });
        mExecutor.execute(statefulProducerRunnable);
    }

    static final String CREATED_THUMBNAIL = "createdThumbnail";
    static final String PRODUCER_NAME = "LocalExifThumbnailProducer";
    private final ContentResolver mContentResolver;
    private final Executor mExecutor;
    private final PooledByteBufferFactory mPooledByteBufferFactory;


}
