// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap.service;

import android.app.*;
import android.content.*;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.xinghai.mycurve.R;

import java.io.*;
import java.net.URI;
import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.storage.StorageUtil;
import streetdirectory.mobile.core.storage.ZipUtil;
import streetdirectory.mobile.gis.maps.configs.*;
import streetdirectory.mobile.modules.offlinemap.OfflineMapStorage;
import streetdirectory.mobile.sd.SplashActivity;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.offlinemap.service:
//            OfflineMapPackageDetailService, OfflineMapPackageDetailServiceInput, OfflineMapPreference, OfflineMapPackageDetailServiceOutput,
//            OfflineMapFileDownloadService

public class OfflineMapBackgroundService extends Service
{
    public class DownloadServiceBinder extends Binder
    {

        OfflineMapBackgroundService getService()
        {
            return OfflineMapBackgroundService.this;
        }


        public DownloadServiceBinder()
        {
            super();
        }
    }


    public OfflineMapBackgroundService()
    {
        mNotificationManager = null;
        zipSavePath = this.getExternalCacheDir();
        packageName = this.getPackageName();
        parentID = 80001;

    }

    private void broadcastDownloadFailed(int i, String s, int j)
    {
        Intent intent = new Intent();
        intent.putExtra("packageID", i);
        intent.putExtra("packageName", s);
        intent.putExtra("parentID", j);
        intent.setAction("streetdirectory.mobile.modules.offlinemap.failed");
        sendBroadcast(intent);
    }

    private void broadcastDownloadFinish(int i, String s, int j)
    {
        Intent intent = new Intent();
        intent.putExtra("packageID", i);
        intent.putExtra("packageName", s);
        intent.putExtra("parentID", j);
        intent.setAction("streetdirectory.mobile.modules.offlinemap.finish");
        sendBroadcast(intent);
    }

    private void broadcastDownloadPause(int i, String s, int j)
    {
        Intent intent = new Intent();
        intent.putExtra("packageID", i);
        intent.putExtra("packageName", s);
        intent.putExtra("parentID", j);
        intent.setAction("streetdirectory.mobile.modules.offlinemap.pause");
        sendBroadcast(intent);
    }

    private void broadcastDownloadStart(int i, String s, int j)
    {
        Intent intent = new Intent();
        intent.putExtra("packageID", i);
        intent.putExtra("packageName", s);
        intent.putExtra("parentID", j);
        intent.setAction("streetdirectory.mobile.modules.offlinemap.start");
        sendBroadcast(intent);
    }

    private void delete(int i, int j)
    {
        SDHttpServiceOutput sdhttpserviceoutput;
        SDLogger.info((new StringBuilder()).append("OfflineMap Delete: ").append(i).toString());
        sdhttpserviceoutput = (SDHttpServiceOutput)(new OfflineMapPackageDetailService(new OfflineMapPackageDetailServiceInput(i))).http_execute();
        if(sdhttpserviceoutput == null)
            return;

        int k;
        int j1;
        j1 = sdhttpserviceoutput.childs.size();
        k = 4;
        Object obj;
        OfflineMapPackageDetailServiceOutput offlinemappackagedetailserviceoutput;
        Object obj2;
        Object obj3;
        Object obj4;

        for(;k < j1;) {
            if (OfflineMapPreference.getStatus(i) != 0)
                return;

            obj = new ArrayList();
            offlinemappackagedetailserviceoutput = (OfflineMapPackageDetailServiceOutput) sdhttpserviceoutput.childs.get(k);
            obj2 = new Rect(offlinemappackagedetailserviceoutput.minCol, offlinemappackagedetailserviceoutput.minRow, offlinemappackagedetailserviceoutput.maxCol, offlinemappackagedetailserviceoutput.maxRow);
            obj3 = OfflineMapPreference.getPackageList(j);
            if (obj3 != null) {
                Iterator iterator = ((ArrayList) (obj3)).iterator();
                for (; ((Iterator) (iterator)).hasNext(); ) {

                    obj4 = (Integer) ((Iterator) (iterator)).next();
                    if (OfflineMapPreference.getStatus(i) != 0)
                        return;

                    if (i != ((Integer) (obj4)).intValue()) {
                        obj4 = new OfflineMapPackageDetailServiceInput(((Integer) (obj4)).intValue());
                        if (((OfflineMapPackageDetailServiceInput) (obj4)).getSaveFile().exists()) {
                            obj4 = (SDHttpServiceOutput) (new OfflineMapPackageDetailService(((OfflineMapPackageDetailServiceInput) (obj4)))).http_execute();
                            if (((SDHttpServiceOutput) (obj4)).childs.size() < k) {
                                obj4 = (OfflineMapPackageDetailServiceOutput) ((SDHttpServiceOutput) (obj4)).childs.get(k);
                                obj4 = new Rect(((OfflineMapPackageDetailServiceOutput) (obj4)).minCol, ((OfflineMapPackageDetailServiceOutput) (obj4)).minRow, ((OfflineMapPackageDetailServiceOutput) (obj4)).maxCol, ((OfflineMapPackageDetailServiceOutput) (obj4)).maxRow);
                                Rect rect = new Rect();
                                if (rect.setIntersect(((Rect) (obj2)), ((Rect) (obj4))))
                                    ((ArrayList) (obj)).add(rect);
                            }
                        }
                    }
                }
            }


            obj2 = OfflineMapPackageDetailServiceOutput.getMapPresetLevelMap(offlinemappackagedetailserviceoutput.connectionString, k);
            obj3 = ((MapPresetLevelMap) (obj2)).mapPresetSource;
            obj4 = ((MapPresetLevelMap) (obj2)).scale;
            obj2 = generateMapDirectory(((MapPresetSource) (obj3)), ((MapfileScale) (obj4)));
            if (obj2 != null && ((File) (obj2)).exists()) {
                int l = offlinemappackagedetailserviceoutput.minRow;

                int i1;
                for (; l <= offlinemappackagedetailserviceoutput.maxRow; ) {
                    i1 = offlinemappackagedetailserviceoutput.minCol;

                    for (; i1 <= offlinemappackagedetailserviceoutput.maxCol; ) {
                        if (OfflineMapPreference.getStatus(i) != 0)
                            return;

                        for (Iterator iterator = ((ArrayList) (obj)).iterator(); iterator.hasNext(); )
                            if (!((Rect) iterator.next()).contains(i1, l)) ;

                        File file = generateMapTileFile(((MapPresetSource) (obj3)), ((MapfileScale) (obj4)), l, i1);
                        if (file != null && file.delete())
                            SDLogger.info((new StringBuilder()).append("OfflineMap Delete: ").append(file.getAbsolutePath()).toString());
                        i1++;
                    }
                    l++;
                }

                String as[];
                if (!offlinemappackagedetailserviceoutput.filemode.equalsIgnoreCase("folder")) {
                    as = offlinemappackagedetailserviceoutput.filemode.split(".zip")[0].split("_");
                    if (as.length >= 2) {
                        obj = as[0];
                        i1 = Integer.parseInt(as[1]);
                        l = 1;

                        for (; l <= i1; ) {
                            if (OfflineMapPreference.getStatus(i) != 0)
                                return;

                            Object obj1 = (new StringBuilder()).append(((String) (obj))).append("_").append(l).append(".zip").toString();
                            obj1 = new File(((File) (obj2)), (new StringBuilder()).append(i).append(((String) (obj1))).toString());
                            if (((File) (obj1)).delete())
                                SDLogger.info((new StringBuilder()).append("OfflineMap Zip Delete: ").append(((File) (obj1)).getAbsolutePath()).toString());
                            l++;
                        }

                        if (((File) (obj2)).list().length <= 0)
                            ((File) (obj2)).delete();
                    } else
                        SDLogger.error("OfflineMap Zip name not valid");
                }
            }
            k++;
        }

        SDLogger.info("OfflineMap Delete Finish");
        return;
    }

    private void deleteAsync(final int packageID, final int parentID)
    {
        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected Void doInBackground(Void avoid[])
            {
                delete(packageID, parentID);
                return null;
            }

        }).executeTask(new Void[0]);
    }

    private void displayNotification(int i, String s, String s1, boolean flag)
    {
        try
        {
            if(mNotificationManager == null)
                mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Context context = getApplicationContext();
            PendingIntent pendingintent = PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class), 0);
            Notification temp = (new android.support.v4.app.NotificationCompat.Builder(context)).setContentTitle(s).setContentText(s1).setSmallIcon(R.drawable.ic_launcher).setContentIntent(pendingintent).build();
            if(flag) {
                temp.flags = ((Notification) (temp)).flags | 0x10;
            }
            mNotificationManager.notify(i, temp);
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return;
        }
    }

    private void displayOptionNotification(int i, String s, int j, String s1, String s2, boolean flag)
    {
        if(mNotificationManager == null)
            mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Context context = getApplicationContext();
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, new Intent(), 0);
        Object obj = new Intent(this, OfflineMapBackgroundService.class);
        ((Intent) (obj)).putExtra("mode", 1);
        ((Intent) (obj)).putExtra("packageID", i);
        ((Intent) (obj)).putExtra("parentID", j);
        ((Intent) (obj)).putExtra("packageName", s);
        obj = PendingIntent.getActivity(context, 1, ((Intent) (obj)), 0);
        Intent intent = new Intent(this, OfflineMapBackgroundService.class);
        intent.putExtra("mode", 2);
        intent.putExtra("packageID", i);
        intent.putExtra("parentID", j);
        intent.putExtra("packageName", s);
        PendingIntent temp = PendingIntent.getActivity(context, 1, intent, 0);
        Notification temp2 = (new android.support.v4.app.NotificationCompat.Builder(context)).setContentTitle(s1).setContentText(s2).setSmallIcon(R.drawable.ic_launcher).setContentIntent(pendingintent).addAction(R.drawable.ic_launcher, "PAUSE", ((PendingIntent) (obj))).addAction(R.drawable.ic_launcher, "DELETE", temp).build();
        if(flag)
            temp2.flags = ((Notification) (temp2)).flags | 0x10;
        mNotificationManager.notify(i, temp2);
    }

    private void download(final int packageID, String s, int i)
    {
        int level;
        int j;
        int k;
        final int total;
        final SDPreferences pref;
        final int zipFolder1;
        pref = SDPreferences.getInstance();
        zipFolder1 = OfflineMapPreference.getStatus(packageID);
        SDLogger.debug((new StringBuilder()).append("STATUS : ").append(Integer.toString(zipFolder1)).append(", PACKAGE ID : ").append(Integer.toString(packageID)).toString());
        if(zipFolder1 == 1)
            return;

        final SDHttpServiceOutput output;
        SDLogger.info((new StringBuilder()).append("OfflineMap Download: ").append(packageID).toString());
        OfflineMapPreference.setStatus(pref, packageID, 1);
        if(zipFolder1 == 0)
            OfflineMapPreference.setProgress(pref, packageID, 0.0F);
        broadcastDownloadStart(packageID, s, i);
        displayNotification(packageID, (new StringBuilder()).append(s).append(" Map").toString(), "Download in progress...", false);
        output = (SDHttpServiceOutput)(new OfflineMapPackageDetailService(new OfflineMapPackageDetailServiceInput(packageID))).http_execute();
        if(output == null) {
            SDLogger.error("OfflineMap Failed");
            OfflineMapPreference.setStatus(pref, packageID, 2);
            displayNotification(packageID, (new StringBuilder()).append(s).append(" Map").toString(), "Failed to download.", true);
            broadcastDownloadFailed(packageID, s, i);
            stopSelf();
            return;
        }

        j = OfflineMapPreference.getLastLevelIndex(packageID);
        k = OfflineMapPreference.getLastZipFolder(packageID);
        total = ((SDHttpServiceOutput) (output)).childs.size();
        level = j;

        try {

            for (; level < total; ) {
                MapPresetLevelMap mappresetlevelmap;
                Object obj1;
                obj1 = (OfflineMapPackageDetailServiceOutput) ((SDHttpServiceOutput) (output)).childs.get(level);
                OfflineMapPreference.setLastLevelIndex(pref, packageID, level);
                mappresetlevelmap = OfflineMapPackageDetailServiceOutput.getMapPresetLevelMap(((OfflineMapPackageDetailServiceOutput) (obj1)).connectionString, level);
                URI temp_uri = new URI(mappresetlevelmap.mapPresetSource.offlineConnectionString);
                String temp_path = ((URI) (temp_uri)).getPath();
                temp_path = (new StringBuilder()).append(((String) (temp_path))).append("/").append(mappresetlevelmap.scale.folder).toString();
                if (!((OfflineMapPackageDetailServiceOutput) (obj1)).filemode.equalsIgnoreCase("folder")) {
                    String as[] = ((OfflineMapPackageDetailServiceOutput) (obj1)).filemode.split(".zip")[0].split("_");
                    if (as.length >= 2) {
                        final int zipCount;
                        obj1 = as[0];
                        try {
                            zipCount = Integer.parseInt(as[1]);
                        }
                        // Misplaced declaration of an exception variable
                        catch (Exception ex) {
                            SDLogger.printStackTrace(((Exception) (ex)), "OfflineMap Zip failed");
                            OfflineMapPreference.setStatus(pref, packageID, 2);
                            displayNotification(packageID, (new StringBuilder()).append(s).append(" Map").toString(), "Failed to download.", true);
                            OfflineMapPreference.commitData(pref);
                            broadcastDownloadFailed(packageID, s, i);
                            stopSelf();
                            return;
                        }

                        int zipFolder0;
                        if (level == j)
                            zipFolder0 = k;
                        else
                            zipFolder0 = 1;

                            for (; zipFolder0 <= zipCount; ) {

                                final int zipFolder = zipFolder0;
                            if (OfflineMapPreference.getStatus(packageID) != 1) {
                                removeNotification(packageID);
                                return;
                            }
                            OfflineMapPreference.setLastZipFolder(pref, packageID, zipFolder);
                            Log.d("Offline Map", (new StringBuilder()).append("Last zip folder in preference : ").append(((String) (obj1))).append("_").append(zipFolder).append(".zip").toString());
                            String strTemp = (new StringBuilder()).append(((String) (obj1))).append("_").append(zipFolder).append(".zip").toString();
                            final String final_s = (new StringBuilder()).append(mappresetlevelmap.mapPresetSource.offlineConnectionString).append("/").append(strTemp).toString();
                            SDLogger.info((new StringBuilder()).append("OfflineMap Download URL: ").append(final_s).toString());
                            final File final_file = OfflineMapStorage.getStorageFile((new StringBuilder()).append("offline_map/").append(((String) (strTemp))).append("/").append(packageID).append(strTemp).toString());

                            final int f_level = level;
                            (new OfflineMapFileDownloadService(s, final_file) {

                                public void onFailed(Exception exception) {
                                    SDLogger.printStackTrace(exception, "OfflineMap Failed");
                                    OfflineMapPreference.setStatus(pref, packageID, 2);
                                    displayNotification(packageID, (new StringBuilder()).append(packageID).append(" Map").toString(), "Failed to download.", true);
                                    OfflineMapPreference.commitData(pref);
                                    broadcastDownloadFailed(packageID, "",1);
                                    stopSelf();
                                }

                                public void onProgress(long l, long l1) {
                                    int i1 = 0;
                                    int k1 = 0;
                                    for (int j1 = 0; j1 < total; ) {
                                        OfflineMapPackageDetailServiceOutput offlinemappackagedetailserviceoutput = (OfflineMapPackageDetailServiceOutput) output.childs.get(j1);
                                        int i2 = k1;
                                        if (j1 < f_level)
                                            i2 = (int) ((double) k1 + offlinemappackagedetailserviceoutput.size);
                                        i1 = (int) ((double) i1 + offlinemappackagedetailserviceoutput.size);
                                        j1++;
                                        k1 = i2;
                                    }

                                    float f = ((float) l * 1.0F) / (float) l1;
                                    SDLogger.info((new StringBuilder()).append("File Progress: ").append(f).toString());
                                    OfflineMapPackageDetailServiceOutput offlinemappackagedetailserviceoutput1 = (OfflineMapPackageDetailServiceOutput) output.childs.get(f_level);
                                    double d = 1.0F / (float) zipCount;
                                    double d1 = offlinemappackagedetailserviceoutput1.size;
                                    f = (float) (int) ((double) k1 + (double) (((float) zipFolder + f) - 1.0F) * (d * d1)) / (float) i1;
                                    SDLogger.info((new StringBuilder()).append("Total Progress: ").append(f).toString());
                                    OfflineMapPreference.setProgress(pref, packageID, f);
                                    if (OfflineMapPreference.getStatus(packageID) != 1) {
                                        abort();
                                        removeNotification(packageID);
                                    }
                                }

                                public void onSuccess() {
                                    Exception exception = ZipUtil.extract(zipSavePath, zipSavePath.getParentFile(), new streetdirectory.mobile.core.storage.ZipUtil.OnProgressExtract() {

                                                public boolean isAborted() {
                                                    return OfflineMapPreference.getStatus(packageID) != 1;
                                                }

                                                public void onFileFinished(String s, long l) {
                                                }

                                                public void onFileStarted(String s) {
                                                }

                                                public void onFinished(int i) {
                                                }

                                                public void onProgress(String s, long l) {
                                                }

                                                public void onStarted() {
                                                }

                                            }
                                    );
                                    if (exception == null) {
                                        if (!StorageUtil.delete(zipSavePath)) {
                                            SDLogger.error("Delete OfflineMap FAILED");
                                            return;
                                        }
                                    } else {
                                        if (exception != ZipUtil.ABORT_EXCEPTION) {
                                            if (exception instanceof FileNotFoundException) {
                                                OfflineMapPreference.setStatus(pref, packageID, 2);
                                                displayNotification(packageID, (new StringBuilder()).append(packageName).append(" Map").toString(), "No space left to download.", true);
                                                OfflineMapPreference.commitData(pref);
                                                broadcastDownloadFailed(packageID, packageName, parentID);
                                                stopSelf();
                                                return;
                                            }
                                        } else {
                                            OfflineMapPreference.setStatus(pref, packageID, 2);
                                            removeNotification(packageID);
                                        }
                                    }

                                    return;
                                }

                            }
                            ).execute();
                            zipFolder0++;
                        }
                    } else
                        SDLogger.error("OfflineMap Zip name not valid");
                }

                level++;
            }

            OfflineMapPreference.setCompletionDate(pref, packageID, new Date());
            OfflineMapPreference.setProgress(pref, packageID, 1.0F);
            OfflineMapPreference.setStatus(pref, packageID, 3);
            SDLogger.debug((new StringBuilder()).append("STATUS : ").append(Integer.toString(OfflineMapPreference.getStatus(packageID))).append(", PACKAGE ID : ").append(Integer.toString(packageID)).toString());
            OfflineMapPreference.commitData(pref);
            displayNotification(packageID, (new StringBuilder()).append(s).append(" Map").toString(), "Download complete.", true);
            broadcastDownloadFinish(packageID, s, i);
            stopSelf();
            return;

        } catch(Exception ex) {
            SDLogger.printStackTrace(((Exception) (ex)), "OfflineMap parse URL failed (check if preset or off_connetion_string is not null)");
            OfflineMapPreference.setStatus(pref, packageID, 2);
            displayNotification(packageID, (new StringBuilder()).append(s).append(" Map").toString(), "Failed to download.", true);
            broadcastDownloadFailed(packageID, s, i);
            return;
        }
    }

    private void downloadAsync(final int packageID, final String packageName, final int parentID)
    {
        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected Void doInBackground(Void avoid[])
            {
                download(packageID, packageName, parentID);
                return null;
            }

        }).executeTask(new Void[0]);
    }

    private File generateMapDirectory(MapPresetSource mappresetsource, MapfileScale mapfilescale)
    {
        Object obj = null;
        try {
            URI temp = new URI((new StringBuilder()).append(mappresetsource.connectionString).append("/").append(mapfilescale.folder).toString());
            String temp2 = temp.getPath();

            if (temp2 != null)
                return OfflineMapStorage.getStorageFile((new StringBuilder()).append("offline_map/").append(mappresetsource).toString());
            else
                return null;
        } catch(Exception ex) {
            SDLogger.printStackTrace(ex, "OfflineMap parse delete directory URL failed");
            return null;
        }
    }

    private File generateMapTileFile(MapPresetSource mappresetsource, MapfileScale mapfilescale, int i, int j)
    {
        String s;
        s = (new StringBuilder()).append(mappresetsource.connectionString).append("/").append(mapfilescale.folder).append("/").toString();
        String temp;
        if(mapfilescale.isUseGrid)
        {
            int k = (int)Math.ceil(((double)i * (double)mapfilescale.gridRow) / (double)mapfilescale.gridTotalHeight);
            int l = (int)Math.ceil(((double)j * (double)mapfilescale.gridCol) / (double)mapfilescale.gridTotalWidth);
            String s1 = (new StringBuilder()).append(k).append("_").append(l).toString();
            s = (new StringBuilder()).append(s).append(s1).append("/").toString();
            temp = (new StringBuilder()).append(mappresetsource.config.mapCode).append("_").append(mapfilescale.levelCode).append("_").append(s1).append("_").append(String.valueOf(i)).append("_").append(String.valueOf(j)).append(mapfilescale.fileExtension).toString();

        } else
        {
            temp = (new StringBuilder()).append(mappresetsource.config.mapCode).append(String.valueOf(i)).append("_").append(String.valueOf(j)).append("_").append(mapfilescale.levelCode).append(mapfilescale.fileExtension).toString();
        }
        s = null;
        try {
            URI temp2 = new URI((new StringBuilder()).append(s).append(temp).toString());
            String temp3 = temp2.getPath();
            if (temp3 != null)
                return OfflineMapStorage.getStorageFile((new StringBuilder()).append("offline_map/").append(temp3).toString());
            else
                return null;
        } catch(Exception ex) {
            SDLogger.printStackTrace(ex, "OfflineMap parse delete URL failed");
        }
        return null;
    }

    public static boolean isRunning(Context context)
    {
        for(Iterator iterator = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(0x7fffffff).iterator(); iterator.hasNext();)
        {
            android.app.ActivityManager.RunningServiceInfo runningserviceinfo = (android.app.ActivityManager.RunningServiceInfo)iterator.next();
            if(OfflineMapBackgroundService.class.getName().equals(runningserviceinfo.service.getClassName()) && runningserviceinfo.started)
                return true;
        }

        return false;
    }

    private void logToFile(String s, String s1, String s2)
    {
        File file;
        Object obj;
        file = new File(s);
        if(!file.exists())
            file.mkdir();
        File temp = new File((new StringBuilder()).append(s).append(s1).toString());
        obj = null;
        s = null;
        file = null;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp, true));
            bufferedWriter.write(s2);
            bufferedWriter.write(13);
            bufferedWriter.write(10);
            bufferedWriter.close();
            return;
        } catch(IOException iex) {
            iex.printStackTrace();
            return;

        }
    }

    private void removeNotification(int i)
    {
        if(mNotificationManager != null)
            mNotificationManager.cancel(i);
    }

    public IBinder onBind(Intent intent)
    {
        return _binder;
    }

    public int onStartCommand(Intent intent, int i, int j)
    {
        SDLogger.info("onStartCommand");
        if(intent == null)
            return 3;

        SDPreferences sdpreferences;
        int k;
        int l;
        i = intent.getIntExtra("mode", -1);
        k = intent.getIntExtra("parentID", -1);
        l = intent.getIntExtra("packageID", -1);
        String temp = intent.getStringExtra("packageName");
        sdpreferences = SDPreferences.getInstance();
        if(l == -1)
            return 3;

        switch(i) {
            default:
                return 3;
            case 0:
                SDLogger.info((new StringBuilder()).append("Download: ").append(l).toString());
                downloadAsync(l, temp, k);
                break;
            case 1:
                SDLogger.info((new StringBuilder()).append("Pause: ").append(l).toString());
                OfflineMapPreference.setStatus(sdpreferences, l, 2);
                broadcastDownloadPause(l, temp, k);
                removeNotification(l);
                OfflineMapPreference.commitData(sdpreferences);
                stopSelf(j);
                break;
            case 2:
                SDLogger.info((new StringBuilder()).append("Delete: ").append(l).toString());
                deleteAsync(l, k);
                OfflineMapPreference.setStatus(sdpreferences, l, 0);
                OfflineMapPreference.setProgress(sdpreferences, l, 0.0F);
                OfflineMapPreference.setLastLevelIndex(sdpreferences, l, 0);
                OfflineMapPreference.setLastZipFolder(sdpreferences, l, 1);
                OfflineMapPreference.commitData(sdpreferences);
                stopSelf(j);
                break;
        }
        return 3;
    }

    public static final int MODE_DELETE = 2;
    public static final int MODE_PAUSE = 1;
    public static final int MODE_START = 0;
    private final IBinder _binder = new DownloadServiceBinder();
    private NotificationManager mNotificationManager;

    public static File zipSavePath;
    public static String packageName;
    public static int parentID;

}
