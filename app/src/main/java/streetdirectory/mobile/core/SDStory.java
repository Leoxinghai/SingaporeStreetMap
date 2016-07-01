

package streetdirectory.mobile.core;

import java.net.URLEncoder;
import java.util.*;

// Referenced classes of package streetdirectory.mobile.core:
//            SDStoryStats, ISDStoryProvider, SDStoryParam

public class SDStory
{

    public SDStory()
    {
    }

    public static HashMap createDefaultParams()
    {
        HashMap hashmap = new HashMap();
        hashmap.put("os", (new StringBuilder()).append("android ").append(android.os.Build.VERSION.RELEASE).toString());
        hashmap.put("d_id", SDStoryStats.deviceID);
        hashmap.put("sd_uid", SDStoryStats.SDuId);
        hashmap.put("loc_longitude", Double.toString(SDStoryStats.longitude));
        hashmap.put("loc_latitude", Double.toString(SDStoryStats.latitude));
        hashmap.put("loc_altitude", Double.toString(SDStoryStats.altitude));
        hashmap.put("loc_accuracy", Float.toString(SDStoryStats.accuracy));
        hashmap.put("loc_speed", Float.toString(SDStoryStats.speed));
        hashmap.put("loc_bearing", Float.toString(SDStoryStats.bearing));
        hashmap.put("loc_provider", SDStoryStats.locationProvider);
        hashmap.put("d_batt_lev", Integer.toString(SDStoryStats.batteryLevel));
        hashmap.put("d_batt_stat", SDStoryStats.batteryStatus);
        hashmap.put("d_batt_charg", SDStoryStats.batteryCharger);
        hashmap.put("net_prov", SDStoryStats.networkProvider);
        hashmap.put("net_prov_name", SDStoryStats.networkProviderName);
        return hashmap;
    }

    public static String createParameterString(HashMap hashmap)
    {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = hashmap.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            String s1 = (String)hashmap.get(s);
            if(s1 != null)
            {
                stringbuilder.append("&");
                stringbuilder.append((new StringBuilder()).append(s).append("=").append(tryEncode(s1)).toString());
            }
        } while(true);
        return stringbuilder.toString();
    }

    public static void post(String s, HashMap hashmap)
    {
        for(Iterator iterator = providers.iterator(); iterator.hasNext(); ((ISDStoryProvider)iterator.next()).post(s, hashmap));
    }

    public static void test()
    {
        post("direction_page_start", SDStoryParam.create("lon", "1.34535").add("lat", "103.45435").add("id", "2390482348230").build());
    }

    public static String tryEncode(String s)
    {
        try
        {
            s = URLEncoder.encode(s, "UTF8");
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return "na";
        }
        return s;
    }

    static ArrayList providers = new ArrayList();

}
