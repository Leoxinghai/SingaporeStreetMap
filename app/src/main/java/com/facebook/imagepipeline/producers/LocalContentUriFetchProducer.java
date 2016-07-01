// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.ContactsContract;
import com.facebook.common.logging.FLog;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imageutils.JfifUtil;
import java.io.*;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            LocalFetchProducer

public class LocalContentUriFetchProducer extends LocalFetchProducer
{

    public LocalContentUriFetchProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, ContentResolver contentresolver, boolean flag)
    {
        super(executor, pooledbytebufferfactory, flag);
        mContentResolver = contentresolver;
    }

    private EncodedImage getCameraImage(Uri uri, ResizeOptions resizeoptions)
        throws IOException
    {
        Cursor cursor = mContentResolver.query(uri, PROJECTION, null, null, null);
        if(uri == null)
            return null;
        int i = cursor.getCount();
        if(i == 0)
        {
            cursor.close();
            return null;
        }
        String s;
        cursor.moveToFirst();
        s = cursor.getString(cursor.getColumnIndex("_data"));
        if(resizeoptions == null) {
            cursor.close();
            return null;

        }
        EncodedImage encodedImage = getThumbnail(resizeoptions, cursor.getInt(cursor.getColumnIndex("_id")));
        if(encodedImage == null) {
            encodedImage = getEncodedImage(new FileInputStream(s), getLength(s));
            cursor.close();
            return encodedImage;
        }
        encodedImage.setRotationAngle(getRotationAngle(s));
        cursor.close();
        return encodedImage;
    }

    private static int getLength(String s)
    {
        if(s == null)
            return -1;
        else
            return (int)(new File(s)).length();
    }

    private static int getRotationAngle(String s)
    {
        int i = 0;
        if(s != null)
            try
            {
                i = JfifUtil.getAutoRotateAngleFromOrientation((new ExifInterface(s)).getAttributeInt("Orientation", 1));
            }
            catch(IOException ioexception)
            {
                FLog.e(TAG, ioexception, "Unable to retrieve thumbnail rotation for %s", new Object[] {
                    s
                });
                return 0;
            }
        return i;
    }

    private EncodedImage getThumbnail(ResizeOptions resizeoptions, int i)
        throws IOException
    {
        EncodedImage encodedimage;
        int j;
        encodedimage = null;
        j = getThumbnailKind(resizeoptions);
        Object obj = encodedimage;

        if(j != 0) {
			obj = null;
			Cursor cursor = android.provider.MediaStore.Images.Thumbnails.queryMiniThumbnail(mContentResolver, i, j, THUMBNAIL_PROJECTION);
			if(resizeoptions != null) {
				obj = resizeoptions;
                cursor.moveToFirst();
				obj = resizeoptions;
				if(cursor.getCount() <= 0) {
					obj = encodedimage;
					if(resizeoptions != null) {
                        cursor.close();
						return null;
					}

				} else {
					obj = resizeoptions;
					String s = cursor.getString(cursor.getColumnIndex("_data"));
					obj = resizeoptions;
					if((new File(s)).exists()) {
                        obj = resizeoptions;
                        encodedimage = getEncodedImage(new FileInputStream(s), getLength(s));
                        obj = encodedimage;
                        if (resizeoptions != null) {
                            cursor.close();
                            return encodedimage;
                        }
                    }
				}
			} else {
				obj = encodedimage;
				if(cursor != null)
				{
                    cursor.close();
					return null;
				}
			}

		}



        return ((EncodedImage) (obj));

        /*
        resizeoptions;
        if(obj != null)
            ((Cursor) (obj)).close();
        throw resizeoptions;
        */
    }

    private static int getThumbnailKind(ResizeOptions resizeoptions)
    {
        if(isThumbnailBigEnough(resizeoptions, MICRO_THUMBNAIL_DIMENSIONS))
            return 3;
        return !isThumbnailBigEnough(resizeoptions, MINI_THUMBNAIL_DIMENSIONS) ? 0 : 1;
    }

    private static boolean isCameraUri(Uri uri)
    {
        String temp = uri.toString();
        return temp.startsWith(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()) || temp.startsWith(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString());
    }

    private static boolean isContactUri(Uri uri)
    {
        return "com.android.contacts".equals(uri.getAuthority()) && !uri.getPath().startsWith(DISPLAY_PHOTO_PATH);
    }

    static boolean isThumbnailBigEnough(ResizeOptions resizeoptions, Rect rect)
    {
        return (float)resizeoptions.width <= (float)rect.width() * 1.333333F && (float)resizeoptions.height <= (float)rect.height() * 1.333333F;
    }

    protected EncodedImage getEncodedImage(ImageRequest imagerequest)
        throws IOException
    {
        Uri uri = imagerequest.getSourceUri();
        EncodedImage encodedimage = null;
        InputStream inputStream = null;
        if(!isContactUri(uri)) {
			if(isCameraUri(uri)) {
				encodedimage = getCameraImage(uri, imagerequest.getResizeOptions());

			}
			if(encodedimage == null)
				return getEncodedImage(mContentResolver.openInputStream(uri), -1);
		} else {
			if(uri.toString().endsWith("/photo"))
                inputStream  = mContentResolver.openInputStream(uri);
			else
                inputStream = android.provider.ContactsContract.Contacts.openContactPhotoInputStream(mContentResolver, uri);
            encodedimage = getEncodedImage(((java.io.InputStream) (inputStream)), -1);
		}

        return encodedimage;

    }

    protected String getProducerName()
    {
        return "LocalContentUriFetchProducer";
    }

    private static final float ACCEPTABLE_REQUESTED_TO_ACTUAL_SIZE_RATIO = 1.333333F;
    private static final String DISPLAY_PHOTO_PATH;
    private static final Rect MICRO_THUMBNAIL_DIMENSIONS = new Rect(0, 0, 96, 96);
    private static final Rect MINI_THUMBNAIL_DIMENSIONS = new Rect(0, 0, 512, 384);
    private static final int NO_THUMBNAIL = 0;
    static final String PRODUCER_NAME = "LocalContentUriFetchProducer";
    private static final String PROJECTION[] = {
        "_id", "_data"
    };
    private static final Class TAG = LocalContentUriFetchProducer.class;
    private static final String THUMBNAIL_PROJECTION[] = {
        "_data"
    };
    private final ContentResolver mContentResolver;

    static
    {
        DISPLAY_PHOTO_PATH = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "display_photo").getPath();
    }
}
