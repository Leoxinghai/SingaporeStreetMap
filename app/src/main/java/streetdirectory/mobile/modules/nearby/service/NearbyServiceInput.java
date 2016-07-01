

package streetdirectory.mobile.modules.nearby.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class NearbyServiceInput extends SDHttpServiceInput
{

    public NearbyServiceInput()
    {
        type = 1;
        categoryType = 1;
        offersCategoryId = -1;
        profile = "iphone";
    }

    public NearbyServiceInput(String s, int i, double d, double d1, float f,
            int j, int k)
    {
        type = 1;
        categoryType = 1;
        offersCategoryId = -1;
        profile = "iphone";
        initialize(s, i, d, d1, f, j, k, 0, 1, -1, false);
    }

    public NearbyServiceInput(String s, int i, double d, double d1, float f,
            int j, int k, int l, int i1)
    {
        type = 1;
        categoryType = 1;
        offersCategoryId = -1;
        profile = "iphone";
        initialize(s, i, d, d1, f, j, k, l, i1, -1, false);
    }

    public NearbyServiceInput(String s, int i, double d, double d1, float f,
            int j, int k, int l, int i1, int j1)
    {
        type = 1;
        categoryType = 1;
        offersCategoryId = -1;
        profile = "iphone";
        initialize(s, i, d, d1, f, j, k, l, i1, j1, false);
    }

    public NearbyServiceInput(String s, int i, double d, double d1, float f,
            int j, int k, int l, int i1, int j1, boolean flag)
    {
        type = 1;
        categoryType = 1;
        offersCategoryId = -1;
        profile = "iphone";
        initialize(s, i, d, d1, f, j, k, l, i1, j1, flag);
    }

    public NearbyServiceInput(String s, int i, double d, double d1, float f,
            int j, int k, int l, int i1, boolean flag)
    {
        type = 1;
        categoryType = 1;
        offersCategoryId = -1;
        profile = "iphone";
        initialize(s, i, d, d1, f, j, k, l, i1, -1, flag);
    }

    public String getURL()
    {
        if(offersCategoryId == -1)
            return URLFactory.createURLNearbyList(countryCode, longitude, latitude, type, kmRange, start, limit, categoryID, categoryType, apiVersion, profile);
        else
            return URLFactory.createURLNearbyList(countryCode, longitude, latitude, type, kmRange, start, limit, categoryID, categoryType, apiVersion, offersCategoryId, profile);
    }

    public void initialize(String s, int i, double d, double d1, float f,
            int j, int k, int l, int i1, int j1, boolean flag)
    {
        countryCode = s;
        type = i;
        longitude = d;
        latitude = d1;
        kmRange = f;
        start = j;
        limit = k;
        categoryID = l;
        categoryType = i1;
        offersCategoryId = j1;
        if(flag)
            profile = "iphone_compact";
    }

    public int categoryID;
    public int categoryType;
    public String countryCode;
    public float kmRange;
    public double latitude;
    public int limit;
    public double longitude;
    public int offersCategoryId;
    public String profile;
    public int start;
    public int type;
}
