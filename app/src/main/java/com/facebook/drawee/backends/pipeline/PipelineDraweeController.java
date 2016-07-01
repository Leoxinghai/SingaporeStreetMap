// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.drawee.backends.pipeline;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.*;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawable.base.DrawableWithCaches;
import com.facebook.drawee.components.DeferredReleaser;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.drawable.OrientedDrawable;
import com.facebook.imagepipeline.animated.factory.AnimatedDrawableFactory;
import com.facebook.imagepipeline.image.*;
import java.util.concurrent.Executor;

public class PipelineDraweeController extends AbstractDraweeController
{

    public PipelineDraweeController(Resources resources, DeferredReleaser deferredreleaser, AnimatedDrawableFactory animateddrawablefactory, Executor executor, Supplier supplier, String s, Object obj)
    {
        super(deferredreleaser, executor, s, obj);
        mResources = resources;
        mAnimatedDrawableFactory = animateddrawablefactory;
        init(supplier);
    }

    private void init(Supplier supplier)
    {
        mDataSourceSupplier = supplier;
    }

    protected Drawable createDrawable(CloseableReference closeablereference)
    {
        Preconditions.checkState(CloseableReference.isValid(closeablereference));
        CloseableImage temp = (CloseableImage)closeablereference.get();
        if(temp instanceof CloseableStaticBitmap)
        {
            //temp = (CloseableStaticBitmap)temp;
            BitmapDrawable bitmapdrawable = new BitmapDrawable(mResources, ((CloseableStaticBitmap)temp).getUnderlyingBitmap());
            if(((CloseableStaticBitmap)temp).getRotationAngle() == 0 || ((CloseableStaticBitmap)temp).getRotationAngle() == -1)
                return bitmapdrawable;
            else
                return new OrientedDrawable(bitmapdrawable, ((CloseableStaticBitmap)temp).getRotationAngle());
        }
        if(temp instanceof CloseableAnimatedImage)
            return mAnimatedDrawableFactory.create(((CloseableAnimatedImage)temp).getImageResult());
        else
            throw new UnsupportedOperationException((new StringBuilder()).append("Unrecognized image class: ").append(closeablereference).toString());
    }

    protected Drawable createDrawable(Object obj)
    {
        return createDrawable((CloseableReference)obj);
    }

    protected DataSource getDataSource()
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x: getDataSource", Integer.valueOf(System.identityHashCode(this)));
        return (DataSource)mDataSourceSupplier.get();
    }

    protected int getImageHash(CloseableReference closeablereference)
    {
        if(closeablereference != null)
            return closeablereference.getValueHash();
        else
            return 0;
    }

    protected int getImageHash(Object obj)
    {
        return getImageHash((CloseableReference)obj);
    }

    protected ImageInfo getImageInfo(CloseableReference closeablereference)
    {
        Preconditions.checkState(CloseableReference.isValid(closeablereference));
        return (ImageInfo)closeablereference.get();
    }

    protected Object getImageInfo(Object obj)
    {
        return getImageInfo((CloseableReference)obj);
    }

    protected Resources getResources()
    {
        return mResources;
    }

    public void initialize(Supplier supplier, String s, Object obj)
    {
        super.initialize(s, obj);
        init(supplier);
    }

    protected void releaseDrawable(Drawable drawable)
    {
        if(drawable instanceof DrawableWithCaches)
            ((DrawableWithCaches)drawable).dropCaches();
    }

    protected void releaseImage(CloseableReference closeablereference)
    {
        CloseableReference.closeSafely(closeablereference);
    }

    protected void releaseImage(Object obj)
    {
        releaseImage((CloseableReference)obj);
    }

    public String toString()
    {
        return Objects.toStringHelper(this).add("super", super.toString()).add("dataSourceSupplier", mDataSourceSupplier).toString();
    }

    private static final Class TAG = PipelineDraweeController.class;
    private final AnimatedDrawableFactory mAnimatedDrawableFactory;
    private Supplier mDataSourceSupplier;
    private final Resources mResources;

}
