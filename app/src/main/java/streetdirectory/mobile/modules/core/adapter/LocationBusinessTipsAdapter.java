// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.core.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.BitmapMemoryCaching;
import streetdirectory.mobile.modules.core.*;

// Referenced classes of package streetdirectory.mobile.modules.core.adapter:
//            LocationBusinessAdapter

public class LocationBusinessTipsAdapter extends LocationBusinessAdapter
{
    public static interface OnTipsPhotoNotFoundListener
    {

        public abstract void onTipsPhotoNotFound(int i, int j, int k);
    }


    public LocationBusinessTipsAdapter(Context context)
    {
        super(context);
        if(!_context.getResources().getBoolean(R.bool.isTablet));
        _tipsPhotoSize = Math.round(TypedValue.applyDimension(1, 35, _context.getResources().getDisplayMetrics()));
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (LocationBusinessTipsServiceOutput)locationbusinessserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessTipsServiceOutput locationbusinesstipsserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, locationbusinesstipsserviceoutput, i);
        if(locationbusinesstipsserviceoutput.tipsUserID != null && locationbusinesstipsserviceoutput.tipsText != null && locationbusinesstipsserviceoutput.offer == null)
        {
            locationbusinesscellviewholder.commentLabel.setText(locationbusinesstipsserviceoutput.tipsText);
            locationbusinesscellviewholder.tipsLayout.setVisibility(View.VISIBLE);
            Bitmap bitmap = (Bitmap)_images.get(locationbusinesstipsserviceoutput.tipsUserID);
            if(bitmap == null)
            {
                locationbusinesscellviewholder.tipsPhoto.setImageDrawable(_context.getResources().getDrawable(R.drawable.facebook_thumb));
                if(_tipsPhotoNotFoundListener != null)
                    _tipsPhotoNotFoundListener.onTipsPhotoNotFound(i, _tipsPhotoSize, _tipsPhotoSize);
                return;
            } else
            {
                locationbusinesscellviewholder.tipsPhoto.setImageBitmap(bitmap);
                return;
            }
        } else
        {
            locationbusinesscellviewholder.tipsLayout.setVisibility(View.INVISIBLE);
            return;
        }
    }

    public void setOnTipsPhotoNotFoundListener(OnTipsPhotoNotFoundListener ontipsphotonotfoundlistener)
    {
        _tipsPhotoNotFoundListener = ontipsphotonotfoundlistener;
    }

    public static final int TIPS_PHOTO_PHONE_DP = 35;
    public static final int TIPS_PHOTO_TABLET_DP = 50;
    protected OnTipsPhotoNotFoundListener _tipsPhotoNotFoundListener;
    public int _tipsPhotoSize;
}
