// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.image;

import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.SharedReference;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imageutils.BitmapUtil;
import com.facebook.imageutils.JfifUtil;
import java.io.Closeable;
import java.io.InputStream;

public class EncodedImage
    implements Closeable
{

    public EncodedImage(Supplier supplier)
    {
        mImageFormat = ImageFormat.UNKNOWN;
        mRotationAngle = -1;
        mWidth = -1;
        mHeight = -1;
        mSampleSize = 1;
        mStreamSize = -1;
        Preconditions.checkNotNull(supplier);
        mPooledByteBufferRef = null;
        mInputStreamSupplier = supplier;
    }

    public EncodedImage(Supplier supplier, int i)
    {
        this(supplier);
        mStreamSize = i;
    }

    public EncodedImage(CloseableReference closeablereference)
    {
        mImageFormat = ImageFormat.UNKNOWN;
        mRotationAngle = -1;
        mWidth = -1;
        mHeight = -1;
        mSampleSize = 1;
        mStreamSize = -1;
        Preconditions.checkArgument(CloseableReference.isValid(closeablereference));
        mPooledByteBufferRef = closeablereference.clone();
        mInputStreamSupplier = null;
    }

    public static EncodedImage cloneOrNull(EncodedImage encodedimage)
    {
        if(encodedimage != null)
            return encodedimage.cloneOrNull();
        else
            return null;
    }

    public static void closeSafely(EncodedImage encodedimage)
    {
        if(encodedimage != null)
            encodedimage.close();
    }

    public static boolean isMetaDataAvailable(EncodedImage encodedimage)
    {
        return encodedimage.mRotationAngle >= 0 && encodedimage.mWidth >= 0 && encodedimage.mHeight >= 0;
    }

    public static boolean isValid(EncodedImage encodedimage)
    {
        return encodedimage != null && encodedimage.isValid();
    }

    public EncodedImage cloneOrNull()
    {
        EncodedImage encodedimage;
        if(mInputStreamSupplier == null) {
			CloseableReference closeablereference;
			closeablereference = CloseableReference.cloneOrNull(mPooledByteBufferRef);
			if(closeablereference != null)
				encodedimage = new EncodedImage(closeablereference);
			else
				encodedimage = null;
			CloseableReference.closeSafely(closeablereference);
		} else {
			encodedimage = new EncodedImage(mInputStreamSupplier, mStreamSize);
		}
        if(encodedimage != null)
            encodedimage.copyMetaDataFrom(this);
        return encodedimage;
    }

    public void close()
    {
        CloseableReference.closeSafely(mPooledByteBufferRef);
    }

    public void copyMetaDataFrom(EncodedImage encodedimage)
    {
        mImageFormat = encodedimage.getImageFormat();
        mWidth = encodedimage.getWidth();
        mHeight = encodedimage.getHeight();
        mRotationAngle = encodedimage.getRotationAngle();
        mSampleSize = encodedimage.getSampleSize();
        mStreamSize = encodedimage.getSize();
    }

    public CloseableReference getByteBufferRef()
    {
        return CloseableReference.cloneOrNull(mPooledByteBufferRef);
    }

    public int getHeight()
    {
        return mHeight;
    }

    public ImageFormat getImageFormat()
    {
        return mImageFormat;
    }

    public InputStream getInputStream()
    {
        CloseableReference closeablereference;
        if(mInputStreamSupplier != null)
            return (InputStream)mInputStreamSupplier.get();
        closeablereference = CloseableReference.cloneOrNull(mPooledByteBufferRef);
        if(closeablereference == null)
            return null;
        PooledByteBufferInputStream pooledbytebufferinputstream = new PooledByteBufferInputStream((PooledByteBuffer)closeablereference.get());
        CloseableReference.closeSafely(closeablereference);
        return pooledbytebufferinputstream;
    }

    public int getRotationAngle()
    {
        return mRotationAngle;
    }

    public int getSampleSize()
    {
        return mSampleSize;
    }

    public int getSize()
    {
        if(mPooledByteBufferRef != null && mPooledByteBufferRef.get() != null)
            return ((PooledByteBuffer)mPooledByteBufferRef.get()).size();
        else
            return mStreamSize;
    }

    public SharedReference getUnderlyingReferenceTestOnly()
    {
        SharedReference sharedreference;
        if(mPooledByteBufferRef == null)
	        sharedreference = null;
	    else
        	sharedreference = mPooledByteBufferRef.getUnderlyingReferenceTestOnly();
        return sharedreference;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public boolean isCompleteAt(int i)
    {
        if(mImageFormat == ImageFormat.JPEG && mInputStreamSupplier == null)
        {
            Preconditions.checkNotNull(mPooledByteBufferRef);
            PooledByteBuffer pooledbytebuffer = (PooledByteBuffer)mPooledByteBufferRef.get();
            if(pooledbytebuffer.read(i - 2) != -1 || pooledbytebuffer.read(i - 1) != -39)
                return false;
        }
        return true;
    }

    public boolean isValid()
    {
        boolean flag = true;
        if(!CloseableReference.isValid(mPooledByteBufferRef)) {
			Supplier supplier = mInputStreamSupplier;
			if(supplier == null)
					flag = false;
		}
        return flag;
    }

    public void parseMetaData()
    {
		ImageFormat imageformat = ImageFormatChecker.getImageFormat_WrapIOException(getInputStream());
		mImageFormat = imageformat;
		if(!ImageFormat.isWebpFormat(imageformat))
		{
			Pair pair = BitmapUtil.decodeDimensions(getInputStream());
			if(pair != null)
			{
				mWidth = ((Integer)pair.first).intValue();
				mHeight = ((Integer)pair.second).intValue();
				if(imageformat != ImageFormat.JPEG) {
			        mRotationAngle = 0;
			        return;
				}
				if(mRotationAngle == -1)
					mRotationAngle = JfifUtil.getAutoRotateAngleFromOrientation(JfifUtil.getOrientation(getInputStream()));
			}
		}
		return;
    }

    public void setHeight(int i)
    {
        mHeight = i;
    }

    public void setImageFormat(ImageFormat imageformat)
    {
        mImageFormat = imageformat;
    }

    public void setRotationAngle(int i)
    {
        mRotationAngle = i;
    }

    public void setSampleSize(int i)
    {
        mSampleSize = i;
    }

    public void setStreamSize(int i)
    {
        mStreamSize = i;
    }

    public void setWidth(int i)
    {
        mWidth = i;
    }

    public static final int DEFAULT_SAMPLE_SIZE = 1;
    public static final int UNKNOWN_HEIGHT = -1;
    public static final int UNKNOWN_ROTATION_ANGLE = -1;
    public static final int UNKNOWN_STREAM_SIZE = -1;
    public static final int UNKNOWN_WIDTH = -1;
    private int mHeight;
    private ImageFormat mImageFormat;
    private final Supplier mInputStreamSupplier;
    private final CloseableReference mPooledByteBufferRef;
    private int mRotationAngle;
    private int mSampleSize;
    private int mStreamSize;
    private int mWidth;
}
