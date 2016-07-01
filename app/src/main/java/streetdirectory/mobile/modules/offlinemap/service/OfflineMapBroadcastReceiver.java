

package streetdirectory.mobile.modules.offlinemap.service;

import android.content.*;

public class OfflineMapBroadcastReceiver extends BroadcastReceiver
{

    public OfflineMapBroadcastReceiver()
    {
    }

    public void onFailedDownload(int i, String s, int j)
    {
    }

    public void onFinishDownload(int i, String s, int j)
    {
    }

    public void onPauseDownload(int i, String s, int j)
    {
    }

    public void onReceive(Context context, Intent intent)
    {
        int i = intent.getIntExtra("packageID", -1);
        String temp = intent.getStringExtra("packageName");
        int j = intent.getIntExtra("parentID", -1);
        String temp2 = intent.getAction();
        if("streetdirectory.mobile.modules.offlinemap.start".equals(temp))
        {
            onStartDownload(i, temp2, j);
        } else
        {
            if("streetdirectory.mobile.modules.offlinemap.pause".equals(temp))
            {
                onPauseDownload(i, temp2, j);
                return;
            }
            if("streetdirectory.mobile.modules.offlinemap.failed".equals(temp))
            {
                onFailedDownload(i, temp2, j);
                return;
            }
            if("streetdirectory.mobile.modules.offlinemap.finish".equals(temp))
            {
                onFinishDownload(i, temp2, j);
                return;
            }
        }
    }

    public void onStartDownload(int i, String s, int j)
    {
    }

    public static final String DOWNLOAD_FAILED = "streetdirectory.mobile.modules.offlinemap.failed";
    public static final String DOWNLOAD_FINISH = "streetdirectory.mobile.modules.offlinemap.finish";
    public static final String DOWNLOAD_PAUSE = "streetdirectory.mobile.modules.offlinemap.pause";
    public static final String DOWNLOAD_START = "streetdirectory.mobile.modules.offlinemap.start";
}
