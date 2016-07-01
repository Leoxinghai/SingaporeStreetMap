// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.facebook.FacebookException;
import com.facebook.LoggingBehavior;
import com.facebook.internal.*;
import java.net.URISyntaxException;

public class ProfilePictureView extends FrameLayout
{
    public static interface OnErrorListener
    {

        public abstract void onError(FacebookException facebookexception);
    }


    public ProfilePictureView(Context context)
    {
        super(context);
        queryHeight = 0;
        queryWidth = 0;
        isCropped = true;
        presetSizeType = -1;
        customizedDefaultProfilePicture = null;
        initialize(context);
    }

    public ProfilePictureView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        queryHeight = 0;
        queryWidth = 0;
        isCropped = true;
        presetSizeType = -1;
        customizedDefaultProfilePicture = null;
        initialize(context);
        parseAttributes(attributeset);
    }

    public ProfilePictureView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        queryHeight = 0;
        queryWidth = 0;
        isCropped = true;
        presetSizeType = -1;
        customizedDefaultProfilePicture = null;
        initialize(context);
        parseAttributes(attributeset);
    }

    private int getPresetSizeInPixels(boolean flag)
    {
        int i;
        switch(presetSizeType) {
		default:
				return 0;
		case -2:
				i = com.facebook.android.R.dimen.com_facebook_profilepictureview_preset_size_small;
				break;
		case -3:
				i = com.facebook.android.R.dimen.com_facebook_profilepictureview_preset_size_normal;
				break;
		case -4:
				i = com.facebook.android.R.dimen.com_facebook_profilepictureview_preset_size_large;
				break;
		case -1:
				if(!flag)
					return 0;
				i = com.facebook.android.R.dimen.com_facebook_profilepictureview_preset_size_normal;
				break;
			}
			return getResources().getDimensionPixelSize(i);
    }

    private void initialize(Context context)
    {
        removeAllViews();
        image = new ImageView(context);
        FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(-1, -1);
        image.setLayoutParams(layoutParams);
        image.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
        addView(image);
    }

    private void parseAttributes(AttributeSet attributeset)
    {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeset, com.facebook.android.R.styleable.com_facebook_profile_picture_view);
        setPresetSize(typedArray.getInt(com.facebook.android.R.styleable.com_facebook_profile_picture_view_preset_size, -1));
        isCropped = typedArray.getBoolean(com.facebook.android.R.styleable.com_facebook_profile_picture_view_is_cropped, true);
        typedArray.recycle();
    }

    private void processResponse(ImageResponse imageresponse)
    {
        if(imageresponse.getRequest() != lastRequest)
			return;

        Exception exception;
        Bitmap bitmap;
        lastRequest = null;
        bitmap = imageresponse.getBitmap();
        exception = imageresponse.getError();
        if(exception == null) {
			if(bitmap != null)
			{
				setImageBitmap(bitmap);
				if(imageresponse.isCachedRedirect())
				{
					sendImageRequest(false);
					return;
				}
			}
		} else {
			//imageresponse = onErrorListener;
			if(onErrorListener == null) {
				Logger.log(LoggingBehavior.REQUESTS, 6, TAG, exception.toString());
				return;
			}
            onErrorListener.onError(new FacebookException((new StringBuilder()).append("Error in downloading profile picture for profileId: ").append(getProfileId()).toString(), exception));
		}

        return;
    }

    private void refreshImage(boolean flag)
    {
        boolean flag1 = updateImageQueryParameters();
        if(profileId == null || profileId.length() == 0 || queryWidth == 0 && queryHeight == 0)
            setBlankProfilePicture();
        else
        if(flag1 || flag)
        {
            sendImageRequest(true);
            return;
        }
    }

    private void sendImageRequest(boolean flag)
    {
        try
        {
            ImageRequest imagerequest = (new com.facebook.internal.ImageRequest.Builder(getContext(), ImageRequest.getProfilePictureUrl(profileId, queryWidth, queryHeight))).setAllowCachedRedirects(flag).setCallerTag(this).setCallback(new com.facebook.internal.ImageRequest.Callback() {

                public void onCompleted(ImageResponse imageresponse)
                {
                    processResponse(imageresponse);
                }

            }
).build();
            if(lastRequest != null)
                ImageDownloader.cancelRequest(lastRequest);
            lastRequest = imagerequest;
            ImageDownloader.downloadAsync(imagerequest);
            return;
        }
        catch(URISyntaxException urisyntaxexception)
        {
            Logger.log(LoggingBehavior.REQUESTS, 6, TAG, urisyntaxexception.toString());
        }
    }

    private void setBlankProfilePicture()
    {
        if(customizedDefaultProfilePicture == null)
        {
            int i;
            if(isCropped())
                i = com.facebook.android.R.drawable.com_facebook_profile_picture_blank_square;
            else
                i = com.facebook.android.R.drawable.com_facebook_profile_picture_blank_portrait;
            setImageBitmap(BitmapFactory.decodeResource(getResources(), i));
            return;
        } else
        {
            updateImageQueryParameters();
            setImageBitmap(Bitmap.createScaledBitmap(customizedDefaultProfilePicture, queryWidth, queryHeight, false));
            return;
        }
    }

    private void setImageBitmap(Bitmap bitmap)
    {
        if(image != null && bitmap != null)
        {
            imageContents = bitmap;
            image.setImageBitmap(bitmap);
        }
    }

    private boolean updateImageQueryParameters()
    {
        boolean flag1 = true;
        int i = getHeight();
        int j = getWidth();
        if(j < 1 || i < 1)
            return false;
        int k = getPresetSizeInPixels(false);
        if(k != 0)
        {
            j = k;
            i = k;
        }
        boolean flag;
        if(j <= i)
        {
            if(isCropped())
                i = j;
            else
                i = 0;
        } else
        if(isCropped())
            j = i;
        else
            j = 0;
        flag = flag1;
        if(j == queryWidth)
            if(i != queryHeight)
                flag = flag1;
            else
                flag = false;
        queryWidth = j;
        queryHeight = i;
        return flag;
    }

    public final OnErrorListener getOnErrorListener()
    {
        return onErrorListener;
    }

    public final int getPresetSize()
    {
        return presetSizeType;
    }

    public final String getProfileId()
    {
        return profileId;
    }

    public final boolean isCropped()
    {
        return isCropped;
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        lastRequest = null;
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        super.onLayout(flag, i, j, k, l);
        refreshImage(false);
    }

    protected void onMeasure(int i, int j)
    {
        android.view.ViewGroup.LayoutParams layoutparams = getLayoutParams();
        int i1 = 0;
        int j1 = android.view.View.MeasureSpec.getSize(j);
        int k1 = android.view.View.MeasureSpec.getSize(i);
        boolean flag = false;
        int l = j1;
        int k = j;
        if(android.view.View.MeasureSpec.getMode(j) != 0x40000000)
        {
            flag = false;
            l = j1;
            k = j;
            if(layoutparams.height == -2)
            {
                l = getPresetSizeInPixels(true);
                k = android.view.View.MeasureSpec.makeMeasureSpec(l, 0x40000000);
                flag = true;
            }
        }
        boolean flag1 = flag;
        i1 = k1;
        j = i;
        if(android.view.View.MeasureSpec.getMode(i) != 0x40000000)
        {
            flag1 = flag;
            i1 = k1;
            j = i;
            if(layoutparams.width == -2)
            {
                i1 = getPresetSizeInPixels(true);
                j = android.view.View.MeasureSpec.makeMeasureSpec(i1, 0x40000000);
                flag1 = true;
            }
        }
        if(flag1)
        {
            setMeasuredDimension(i1, l);
            measureChildren(j, k);
            return;
        } else
        {
            super.onMeasure(j, k);
            return;
        }
    }

    protected void onRestoreInstanceState(Parcelable parcelable)
    {
        if(parcelable.getClass() != Bundle.class)
        {
            super.onRestoreInstanceState(parcelable);
        } else
        {
            Bundle bundle = (Bundle)parcelable;
            super.onRestoreInstanceState(bundle.getParcelable("ProfilePictureView_superState"));
            profileId = bundle.getString("ProfilePictureView_profileId");
            presetSizeType = bundle.getInt("ProfilePictureView_presetSize");
            isCropped = bundle.getBoolean("ProfilePictureView_isCropped");
            queryWidth = bundle.getInt("ProfilePictureView_width");
            queryHeight = bundle.getInt("ProfilePictureView_height");
            setImageBitmap((Bitmap)bundle.getParcelable("ProfilePictureView_bitmap"));
            if(bundle.getBoolean("ProfilePictureView_refresh"))
            {
                refreshImage(true);
                return;
            }
        }
    }

    protected Parcelable onSaveInstanceState()
    {
        Parcelable parcelable = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ProfilePictureView_superState", parcelable);
        bundle.putString("ProfilePictureView_profileId", profileId);
        bundle.putInt("ProfilePictureView_presetSize", presetSizeType);
        bundle.putBoolean("ProfilePictureView_isCropped", isCropped);
        bundle.putParcelable("ProfilePictureView_bitmap", imageContents);
        bundle.putInt("ProfilePictureView_width", queryWidth);
        bundle.putInt("ProfilePictureView_height", queryHeight);
        boolean flag;
        if(lastRequest != null)
            flag = true;
        else
            flag = false;
        bundle.putBoolean("ProfilePictureView_refresh", flag);
        return bundle;
    }

    public final void setCropped(boolean flag)
    {
        isCropped = flag;
        refreshImage(false);
    }

    public final void setDefaultProfilePicture(Bitmap bitmap)
    {
        customizedDefaultProfilePicture = bitmap;
    }

    public final void setOnErrorListener(OnErrorListener onerrorlistener)
    {
        onErrorListener = onerrorlistener;
    }

    public final void setPresetSize(int i)
    {
        switch(i)
        {
        default:
            throw new IllegalArgumentException("Must use a predefined preset size");

        case -4:
        case -3:
        case -2:
        case -1:
            presetSizeType = i;
            break;
        }
        requestLayout();
    }

    public final void setProfileId(String s)
    {
        boolean flag = false;
        if(Utility.isNullOrEmpty(profileId) || !profileId.equalsIgnoreCase(s))
        {
            setBlankProfilePicture();
            flag = true;
        }
        profileId = s;
        refreshImage(flag);
    }

    private static final String BITMAP_HEIGHT_KEY = "ProfilePictureView_height";
    private static final String BITMAP_KEY = "ProfilePictureView_bitmap";
    private static final String BITMAP_WIDTH_KEY = "ProfilePictureView_width";
    public static final int CUSTOM = -1;
    private static final boolean IS_CROPPED_DEFAULT_VALUE = true;
    private static final String IS_CROPPED_KEY = "ProfilePictureView_isCropped";
    public static final int LARGE = -4;
    private static final int MIN_SIZE = 1;
    public static final int NORMAL = -3;
    private static final String PENDING_REFRESH_KEY = "ProfilePictureView_refresh";
    private static final String PRESET_SIZE_KEY = "ProfilePictureView_presetSize";
    private static final String PROFILE_ID_KEY = "ProfilePictureView_profileId";
    public static final int SMALL = -2;
    private static final String SUPER_STATE_KEY = "ProfilePictureView_superState";
    public static final String TAG = ProfilePictureView.class.getSimpleName();
    private Bitmap customizedDefaultProfilePicture;
    private ImageView image;
    private Bitmap imageContents;
    private boolean isCropped;
    private ImageRequest lastRequest;
    private OnErrorListener onErrorListener;
    private int presetSizeType;
    private String profileId;
    private int queryHeight;
    private int queryWidth;


}
