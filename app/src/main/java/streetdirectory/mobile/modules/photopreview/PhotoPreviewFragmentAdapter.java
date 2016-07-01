// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.photopreview;

import android.support.v4.app.*;
import java.util.ArrayList;
import streetdirectory.mobile.modules.photopreview.service.ImageListServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.photopreview:
//            PhotoPreviewFragment

public class PhotoPreviewFragmentAdapter extends FragmentPagerAdapter
{

    public PhotoPreviewFragmentAdapter(FragmentManager fragmentmanager)
    {
        super(fragmentmanager);
        mData = new ArrayList();
    }

    public int getCount()
    {
        return mData.size();
    }

    public Fragment getItem(final int position)
    {
        return new PhotoPreviewFragment() {

            public ImageListServiceOutput getData()
            {
                if(position < mData.size())
                    return (ImageListServiceOutput)mData.get(position);
                else
                    return null;
            }

        };
    }

    public void setData(ArrayList arraylist)
    {
        mData = arraylist;
    }

    private ArrayList mData;

}
