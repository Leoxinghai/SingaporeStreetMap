

package streetdirectory.mobile.modules.nearby.service;

import android.content.Context;
import android.content.res.Resources;

import com.xinghai.mycurve.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import streetdirectory.mobile.modules.core.LocationBusinessTipsServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class NearbyServiceOutput extends LocationBusinessTipsServiceOutput
{

    public NearbyServiceOutput()
    {
    }

    public String generateImageURL(Context context, int i, int j)
    {
        Object obj = null;
        String temp;
        if(categoryID != 93 && categoryID != 1009 && categoryID != 1100) {
			if(offer != null && offerThumbnailImage != null)
				return URLFactory.createURLResizeImage(offerThumbnailImage, 384, 384, 1, 40);
			if(siteBanner != null && siteBanner.length() > 2)
			{
                temp = URLFactory.createURLSiteBanner(countryCode, siteBanner, i, j);
			} else
			{
                temp = null;
				if(imageURL != null)
                    temp = URLFactory.createURLResizeImage(imageURL, i, j);
			}
		} else {
			byte byte0 = 11;
			if(context.getResources().getBoolean(R.bool.isTablet))
				byte0 = 13;
            temp = URLFactory.createURLMapImage(longitude, latitude, i, j, byte0);
		}
        return temp;
    }

    public void populateData()
    {
        super.populateData();
        distanceInMeter = Double.parseDouble((String)hashData.get("d"));
        if(distanceInMeter >= 1000D)
        {
            DecimalFormat decimalformat = new DecimalFormat("###.##");
            distance = (new StringBuilder()).append(decimalformat.format(distanceInMeter / 1000D)).append("km").toString();
            return;
        }
        try
        {
            distance = String.format("%.0fm", new Object[] {
                Double.valueOf(distanceInMeter)
            });
            return;
        }
        catch(Exception exception)
        {
            distanceInMeter = 0.0D;
        }
        distance = "0m";
        return;
    }

    private static final long serialVersionUID = 0x1be264dffbaffa97L;
    public String distance;
    public double distanceInMeter;
}
