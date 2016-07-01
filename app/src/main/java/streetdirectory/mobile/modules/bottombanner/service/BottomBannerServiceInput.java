

package streetdirectory.mobile.modules.bottombanner.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BottomBannerServiceInput extends SDHttpServiceInput
{

    public BottomBannerServiceInput()
    {
    }

    public BottomBannerServiceInput(String s, double d, double d1, double d2,
            double d3, int i, int j, int k, int l, String s1,
            int i1, int j1)
    {
        super(s);
        topLeftLongitude = d;
        topLeftLatitude = d1;
        bottomRightLongitude = d2;
        bottomRightLatitude = d3;
        topLeftCol = i;
        topLeftRow = j;
        bottomRightCol = k;
        bottomRightRow = l;
        categoryID = s1;
        level = i1;
        limit = j1;
    }

    public String getURL()
    {
        return URLFactory.createURLBottomBanner(countryCode, topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, topLeftCol, topLeftRow, bottomRightCol, bottomRightRow, categoryID, level, limit, apiVersion);
    }

    public int bottomRightCol;
    public double bottomRightLatitude;
    public double bottomRightLongitude;
    public int bottomRightRow;
    public String categoryID;
    public int level;
    public int limit;
    public int topLeftCol;
    public double topLeftLatitude;
    public double topLeftLongitude;
    public int topLeftRow;
}
