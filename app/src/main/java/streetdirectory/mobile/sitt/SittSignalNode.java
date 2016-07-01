

package streetdirectory.mobile.sitt;

import android.net.wifi.ScanResult;
import java.util.*;

public class SittSignalNode
{

    public SittSignalNode()
    {
        createdTime = Calendar.getInstance(TimeZone.getTimeZone("US"), Locale.US).getTimeInMillis();
    }

    public SittSignalNode(ScanResult scanresult)
    {
        ssid = scanresult.SSID;
        bssid = scanresult.BSSID;
        level = scanresult.level;
        frequency = scanresult.frequency;
        createdTime = Calendar.getInstance(TimeZone.getTimeZone("US"), Locale.US).getTimeInMillis();
    }

    public static SittSignalNode newNode(SittSignalNode sittsignalnode)
    {
        SittSignalNode sittsignalnode1 = new SittSignalNode();
        sittsignalnode1.ssid = sittsignalnode.ssid;
        sittsignalnode1.bssid = sittsignalnode.bssid;
        sittsignalnode1.level = sittsignalnode.level;
        sittsignalnode1.frequency = sittsignalnode.frequency;
        sittsignalnode1.createdTime = sittsignalnode.createdTime;
        return sittsignalnode1;
    }

    public String bssid;
    public long createdTime;
    public int frequency;
    public double level;
    public String ssid;
}
