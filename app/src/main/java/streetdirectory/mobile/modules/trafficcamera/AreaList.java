

package streetdirectory.mobile.modules.trafficcamera;

import java.util.ArrayList;

// Referenced classes of package streetdirectory.mobile.modules.trafficcamera:
//            TrafficArea

public class AreaList
{

    public AreaList()
    {
    }

    public static ArrayList getAreaList(String s)
    {
        ArrayList temp;
        if("sg".equals(s))
        {
            temp = new ArrayList();
            temp.add(new TrafficArea("CW", "Causeway"));
            temp.add(new TrafficArea("ECP", "ECP AYE"));
            temp.add(new TrafficArea("BKE", "BKE"));
            temp.add(new TrafficArea("CTE", "CTE"));
            temp.add(new TrafficArea("PIE", "PIE"));
            temp.add(new TrafficArea("KJE", "KJE SLE TPE"));
            temp.add(new TrafficArea("KPE", "KPE"));
            return temp;
        }
        if("my".equals(s))
        {
            temp = new ArrayList();
            temp.add(new TrafficArea("AKLEH", "AKLEH"));
            temp.add(new TrafficArea("SMART", "SMART"));
            temp.add(new TrafficArea("SPRINT", "SPRINT"));
            temp.add(new TrafficArea("PNB", "PNB"));
            temp.add(new TrafficArea("KESAS", "KESAS"));
            temp.add(new TrafficArea("LDP", "LDP"));
            temp.add(new TrafficArea("NKVE", "NKVE"));
            temp.add(new TrafficArea("PLUS", "PLUS"));
            temp.add(new TrafficArea("SILK", "SILK"));
            return temp;
        }
        if("id".equals(s))
        {
            temp = new ArrayList();
            temp.add(new TrafficArea("Jakarta Barat", "1"));
            temp.add(new TrafficArea("Jakarta Pusat", "2"));
            temp.add(new TrafficArea("Jakarta Selatan", "3"));
            temp.add(new TrafficArea("Jakarta Utara", "4"));
            temp.add(new TrafficArea("Jakarta Timur", "5"));
            temp.add(new TrafficArea("Jakarta Tangerang", "6"));
            temp.add(new TrafficArea("Depok", "7"));
            temp.add(new TrafficArea("Bali", "8"));
            return temp;
        } else
        {
            return new ArrayList();
        }
    }
}
