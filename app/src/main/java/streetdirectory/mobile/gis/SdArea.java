

package streetdirectory.mobile.gis;

import java.util.ArrayList;
import java.util.Iterator;

public class SdArea
{

    public SdArea()
    {
        mapZones = new ArrayList();
        features = new ArrayList();
    }

    public SdArea(String s, String s1)
    {
        mapZones = new ArrayList();
        features = new ArrayList();
        apiAreaId = s;
        areaName = s1;
    }

    public void addFeatures(String s)
    {
        String ss[] = s.split(";");
        if(ss != null)
        {
            int j = ss.length;
            for(int i = 0; i < j; i++)
            {
                Object obj = ss[i];
                if(!features.contains(obj))
                    features.add(obj);
            }

        }
    }

    public boolean isFeatureAvailable(String s)
    {
        for(Iterator iterator = features.iterator(); iterator.hasNext();)
            if(((String)iterator.next()).equalsIgnoreCase(s))
                return true;

        return false;
    }

    public boolean adv;
    public String apiAreaId;
    public String areaName;
    public String e164Code;
    public ArrayList features;
    public ArrayList mapZones;
}
