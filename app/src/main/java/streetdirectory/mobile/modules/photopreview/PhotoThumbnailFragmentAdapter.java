// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.photopreview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.photopreview.service.ImageListServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.photopreview:
//            PhotoThumbnailFragment

public class PhotoThumbnailFragmentAdapter extends FragmentStatePagerAdapter
{
    public static interface OnImageClickedListener
    {

        public abstract void onAddImageClicked(int i);

        public abstract void onImageClicked(ImageListServiceOutput imagelistserviceoutput, int i);
    }


    public PhotoThumbnailFragmentAdapter(FragmentManager fragmentmanager, Context context)
    {
        super(fragmentmanager);
        mData = new ArrayList();
        fragmentWidthPercentage = 1.0F;
        scale = 1.0F;
        selectedIndex = -1;
        mFragmentManager = fragmentmanager;
        int i = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        if(context.getResources().getBoolean(R.bool.isTablet))
            FRAGMENT_WIDTH_IN_DP = 174;
        else
            FRAGMENT_WIDTH_IN_DP = 114;
        fragmentWidthPercentage = (float)Math.round(TypedValue.applyDimension(1, FRAGMENT_WIDTH_IN_DP, context.getResources().getDisplayMetrics())) / (float)i;
    }

    private static String makeFragmentName(int i, int j)
    {
        return (new StringBuilder()).append("android:switcher:").append(i).append(":").append(j).toString();
    }

    public Fragment getActiveFragment(ViewPager viewpager, int i)
    {
        String temp = makeFragmentName(viewpager.getId(), i);
        return mFragmentManager.findFragmentByTag(temp);
    }

    public int getCount()
    {
        return mData.size();
    }

    public ImageListServiceOutput getData(int i)
    {
        if(i < mData.size())
            return (ImageListServiceOutput)mData.get(i);
        else
            return null;
    }

    public Fragment getItem(final int position)
    {
        PhotoThumbnailFragment photothumbnailfragment = new PhotoThumbnailFragment() {

            public ImageListServiceOutput getData()
            {
                if(position < mData.size())
                    return (ImageListServiceOutput)mData.get(position);
                else
                    return null;
            }

            public void onPhotoClicked(ImageListServiceOutput imagelistserviceoutput)
            {
                    if(mImageClickedListener != null)
                    {
                        if(imagelistserviceoutput == null) {
                            SDLogger.info("Add Photo Clicked");
                            mImageClickedListener.onAddImageClicked(position);
                            return;
                        }
                        SDLogger.info("Photo Clicked");
                        mImageClickedListener.onImageClicked(imagelistserviceoutput, position);
                    }
                    return;
            }

        };
        photothumbnailfragment.setRetainInstance(true);
        photothumbnailfragment.scale = scale;
        return photothumbnailfragment;
    }

    public float getPageWidth(int i)
    {
        return fragmentWidthPercentage * scale;
    }

    public void setData(ArrayList arraylist)
    {
        mData = arraylist;
    }

    public void setOnImageClickedListener(OnImageClickedListener onimageclickedlistener)
    {
        mImageClickedListener = onimageclickedlistener;
    }

    private final int FRAGMENT_WIDTH_IN_DP;
    private float fragmentWidthPercentage;
    private ArrayList mData;
    private FragmentManager mFragmentManager;
    private OnImageClickedListener mImageClickedListener;
    public float scale;
    public int selectedIndex;


}
