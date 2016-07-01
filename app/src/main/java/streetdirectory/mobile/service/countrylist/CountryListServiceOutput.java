

package streetdirectory.mobile.service.countrylist;

import java.util.ArrayList;
import java.util.HashMap;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.service.SDDataOutput;

public class CountryListServiceOutput extends SDDataOutput
{

    public CountryListServiceOutput()
    {
        boundaryLongitude = new ArrayList();
        boundaryLatitude = new ArrayList();
        boundaries = new ArrayList();
    }

    public CountryListServiceOutput(String s, String s1, boolean flag)
    {
        boundaryLongitude = new ArrayList();
        boundaryLatitude = new ArrayList();
        boundaries = new ArrayList();
        hashData.put("country", s);
        hashData.put("country_nm", s1);
        if(flag)
            s = "1";
        else
            s = "0";
        hashData.put("adv", s);
        populateData();
    }

    public void populateData()
    {
        super.populateData();
        countryCode = (String)hashData.get("country");
        countryName = (String)hashData.get("country_nm");
        hasSDAdv = "1".equals(hashData.get("adv"));
        String s;
        String as[];
        String as1[];
        GeoPoint geopoint;
        int i;
        int j;
        try
        {
            s = (String)hashData.get("br");
            if(s == null)
                return;

            as = s.split(";");
            j = as.length;
            i = 0;

            for(;i < j;) {
                as1 = as[i].split(",");
                if (as1.length == 2) {
                    geopoint = new GeoPoint(Double.parseDouble(as1[0]), Double.parseDouble(as1[1]));
                    boundaries.add(geopoint);
                    boundaryLongitude.add(Double.valueOf(Double.parseDouble(as1[0])));
                    boundaryLatitude.add(Double.valueOf(Double.parseDouble(as1[1])));
                }
                i++;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }

    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(CountryListServiceOutput.class);
    private static final long serialVersionUID = 0x603ccc7e1fd2950dL;
    public ArrayList boundaries;
    public ArrayList boundaryLatitude;
    public ArrayList boundaryLongitude;
    public String countryCode;
    public String countryName;
    public boolean hasSDAdv;

}
