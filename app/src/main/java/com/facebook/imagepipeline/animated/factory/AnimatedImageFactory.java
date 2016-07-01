// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.factory;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.animated.base.*;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableBackendProvider;
import com.facebook.imagepipeline.animated.impl.AnimatedImageCompositor;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.gif.GifImage;
import com.facebook.imagepipeline.image.*;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.webp.WebPImage;
import java.util.ArrayList;
import java.util.List;

public class AnimatedImageFactory
{

    public AnimatedImageFactory(AnimatedDrawableBackendProvider animateddrawablebackendprovider, PlatformBitmapFactory platformbitmapfactory)
    {
        mAnimatedDrawableBackendProvider = animateddrawablebackendprovider;
        mBitmapFactory = platformbitmapfactory;
    }

    private CloseableReference createBitmap(int i, int j, android.graphics.Bitmap.Config config)
    {
        CloseableReference temp = mBitmapFactory.createBitmap(i, j, config);
        ((Bitmap)temp.get()).eraseColor(0);
        if(android.os.Build.VERSION.SDK_INT >= 12)
            ((Bitmap)temp.get()).setHasAlpha(true);
        return temp;
    }

    private CloseableReference createPreviewBitmap(AnimatedImage animatedimage, android.graphics.Bitmap.Config config, int i)
    {
        CloseableReference temp = createBitmap(animatedimage.getWidth(), animatedimage.getHeight(), config);
        AnimatedImageResult temp2 = AnimatedImageResult.forAnimatedImage(animatedimage);
        (new AnimatedImageCompositor(mAnimatedDrawableBackendProvider.get(temp2, null), new com.facebook.imagepipeline.animated.impl.AnimatedImageCompositor.Callback() {

            public CloseableReference getCachedBitmap(int j)
            {
                return null;
            }

            public void onIntermediateResult(int j, Bitmap bitmap)
            {
            }

        })).renderFrame(i, (Bitmap)temp.get());
        return temp;
    }

    private List decodeAllFrames(AnimatedImage animatedimage, android.graphics.Bitmap.Config config)
    {
        final ArrayList bitmaps = new ArrayList();
        AnimatedImageResult temp = AnimatedImageResult.forAnimatedImage(animatedimage);
        AnimatedDrawableBackend temp2 = mAnimatedDrawableBackendProvider.get(temp, null);
        AnimatedImageCompositor animatedimagecompositor = new AnimatedImageCompositor(temp2, new com.facebook.imagepipeline.animated.impl.AnimatedImageCompositor.Callback() {

            public CloseableReference getCachedBitmap(int j)
            {
                return CloseableReference.cloneOrNull((CloseableReference)bitmaps.get(j));
            }

            public void onIntermediateResult(int j, Bitmap bitmap)
            {
            }

        });

        for(int i = 0; i < animatedimage.getFrameCount(); i++)
        {
            CloseableReference closeablereference = createBitmap(animatedimage.getWidth(), animatedimage.getHeight(), config);
            animatedimagecompositor.renderFrame(i, (Bitmap)closeablereference.get());
            bitmaps.add(closeablereference);
        }

        return bitmaps;
    }

    private CloseableAnimatedImage getCloseableImage(ImageDecodeOptions imagedecodeoptions, AnimatedImage animatedimage, android.graphics.Bitmap.Config config)
    {
        CloseableReference closeablereference;
        List list;
        CloseableReference closeablereference1;
        List list1;
        CloseableReference closeablereference2;
        Object obj;
        obj = null;
        list1 = null;
        closeablereference2 = null;
        closeablereference = null;
        list = null;
        closeablereference1 = closeablereference2;
        int i;
        if(!imagedecodeoptions.useLastFrameForPreview) {
            i = 0;
        } else {
            closeablereference1 = closeablereference2;
            i = animatedimage.getFrameCount() - 1;
        }

        closeablereference1 = closeablereference2;
        if(imagedecodeoptions.decodeAllFrames) {
            closeablereference1 = closeablereference2;
            list1 = decodeAllFrames(animatedimage, config);
            list = list1;
            closeablereference1 = closeablereference2;
            closeablereference = CloseableReference.cloneOrNull((CloseableReference) list1.get(i));
            closeablereference2 = closeablereference;
        }
        list = list1;
        closeablereference1 = closeablereference;
        if(imagedecodeoptions.decodePreviewFrame) {
            closeablereference2 = closeablereference;
            if (closeablereference == null) {
                list = list1;
                closeablereference1 = closeablereference;
                closeablereference2 = createPreviewBitmap(animatedimage, config, i);
            }
        }
        list = list1;
        closeablereference1 = closeablereference2;
        CloseableAnimatedImage temp = new CloseableAnimatedImage(AnimatedImageResult.newBuilder(animatedimage).setPreviewBitmap(closeablereference2).setFrameForPreview(i).setDecodedFrames(list1).build());
        CloseableReference.closeSafely(closeablereference2);
        CloseableReference.closeSafely(list1);
        return temp;
    }

    public CloseableImage decodeGif(EncodedImage encodedimage, ImageDecodeOptions imagedecodeoptions, android.graphics.Bitmap.Config config)
    {
        CloseableReference temp = encodedimage.getByteBufferRef();
        Preconditions.checkNotNull(encodedimage);
        PooledByteBuffer pooledbytebuffer;
        boolean flag;
        if(!imagedecodeoptions.forceOldAnimationCode)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        pooledbytebuffer = (PooledByteBuffer)temp.get();
        CloseableAnimatedImage temp2 = getCloseableImage(imagedecodeoptions, GifImage.create(pooledbytebuffer.getNativePtr(), pooledbytebuffer.size()), config);
        CloseableReference.closeSafely(temp);
        return temp2;
    }

    public CloseableImage decodeWebP(EncodedImage encodedimage, ImageDecodeOptions imagedecodeoptions, android.graphics.Bitmap.Config config)
    {
        CloseableReference temp = encodedimage.getByteBufferRef();
        Preconditions.checkNotNull(encodedimage);
        PooledByteBuffer pooledbytebuffer;
        boolean flag;
        if(!imagedecodeoptions.forceOldAnimationCode)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        pooledbytebuffer = (PooledByteBuffer)temp.get();
        CloseableAnimatedImage temp2 = getCloseableImage(imagedecodeoptions, WebPImage.create(pooledbytebuffer.getNativePtr(), pooledbytebuffer.size()), config);
        CloseableReference.closeSafely(temp);
        return temp2;
    }

    private final AnimatedDrawableBackendProvider mAnimatedDrawableBackendProvider;
    private final PlatformBitmapFactory mBitmapFactory;
}
