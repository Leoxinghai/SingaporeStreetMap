

package streetdirectory.mobile.modules.offlinemap.service;

import android.content.SharedPreferences;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.SDPreferences;

public class OfflineMapPreference
{

    public OfflineMapPreference()
    {
    }

    public static void commitData(SDPreferences sdpreferences)
    {
        progressTimeInterval = (new Date()).getTime();
        SharedPreferences.Editor temp = sdpreferences.createEditor();
        int i1 = mStatus.size();
        for(int i = 0; i < i1; i++)
        {
            int j1 = mStatus.keyAt(i);
            int j2 = mStatus.get(j1);
            temp.putInt((new StringBuilder()).append("offlinemap_status_").append(j1).toString(), j2);
        }

        i1 = mProgress.size();
        for(int j = 0; j < i1; j++)
        {
            int k1 = mProgress.keyAt(j);
            float f = ((Float)mProgress.get(k1)).floatValue();
            temp.putFloat((new StringBuilder()).append("offlinemap_percentage_").append(k1).toString(), f);
        }

        i1 = mLastLevel.size();
        for(int k = 0; k < i1; k++)
        {
            int l1 = mLastLevel.keyAt(k);
            int k2 = mLastLevel.get(l1);
            temp.putInt((new StringBuilder()).append("offlinemap_last_level_").append(l1).toString(), k2);
        }

        i1 = mLastFolder.size();
        for(int l = 0; l < i1; l++)
        {
            int i2 = mLastFolder.keyAt(l);
            int l2 = mLastFolder.get(i2);
            temp.putInt((new StringBuilder()).append("offlinemap_last_folder_").append(i2).toString(), l2);
        }

        if(temp.commit())
        {
            SDLogger.info("OfflineMap COMMIT SUCCESS");
            return;
        } else
        {
            SDLogger.error("OfflineMap COMMIT FAILED");
            return;
        }
    }

    private static void commitDataPeriodically(SDPreferences sdpreferences)
    {
        if((new Date()).getTime() - progressTimeInterval > 5000L)
            commitData(sdpreferences);
    }

    public static Date getCompletionDate(int i)
    {
        return getCompletionDate(SDPreferences.getInstance(), i);
    }

    public static Date getCompletionDate(SDPreferences sdpreferences, int i)
    {
        Object obj;
        String s;
        Date date = (Date)mCompletionDate.get(i);
        obj = date;
        try {
            if (date == null) {
                s = sdpreferences.getStringForKey((new StringBuilder()).append("offlinemap_complete_date_").append(i).toString(), null);
                obj = date;
                if (s != null) {
                    Date date1 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH)).parse(s);
                    mCompletionDate.put(i, date1);
                    return date1;
                }
            }
            return date;

        } catch(Exception exception) {
            SDLogger.printStackTrace(exception, "OfflineMapBackgroundService getCompletionDate failed");
            return date;
        }
    }

    public static int getLastLevelIndex(int i)
    {
        Integer integer1 = Integer.valueOf(mLastLevel.get(i, -1));
        Integer integer = integer1;
        if(integer1.intValue() == -1)
        {
            integer = Integer.valueOf(SDPreferences.getInstance().getIntForKey((new StringBuilder()).append("offlinemap_last_level_").append(i).toString(), 0));
            mLastLevel.put(i, integer.intValue());
        }
        return integer.intValue();
    }

    public static int getLastZipFolder(int i)
    {
        Integer integer1 = Integer.valueOf(mLastFolder.get(i, -1));
        Integer integer = integer1;
        if(integer1.intValue() == -1)
        {
            integer = Integer.valueOf(SDPreferences.getInstance().getIntForKey((new StringBuilder()).append("offlinemap_last_folder_").append(i).toString(), 1));
            mLastFolder.put(i, integer.intValue());
        }
        return integer.intValue();
    }

    public static ArrayList getPackageList(int i)
    {
        return (ArrayList)mPackageList.get(i);
    }

    public static float getProgress(int i)
    {
        Float float2 = (Float)mProgress.get(i, Float.valueOf(-1F));
        Float float1 = float2;
        if(float2.floatValue() == -1F)
        {
            float1 = Float.valueOf(SDPreferences.getInstance().getFloatForKey((new StringBuilder()).append("offlinemap_percentage_").append(i).toString(), 0.0F));
            mProgress.put(i, float1);
        }
        return float1.floatValue();
    }

    public static int getStatus(int i)
    {
        Integer integer1 = Integer.valueOf(mStatus.get(i, -1));
        Integer integer = integer1;
        if(integer1.intValue() == -1)
        {
            integer = Integer.valueOf(SDPreferences.getInstance().getIntForKey((new StringBuilder()).append("offlinemap_status_").append(i).toString(), 0));
            mStatus.put(i, integer.intValue());
        }
        return integer.intValue();
    }

    public static boolean isComplete(int i)
    {
        return isComplete(SDPreferences.getInstance(), i);
    }

    public static boolean isComplete(SDPreferences sdpreferences, int i)
    {
        return sdpreferences.getStringForKey((new StringBuilder()).append("offlinemap_complete_date_").append(i).toString(), null) != null;
    }

    public static void setCompletionDate(int i, Date date)
    {
        setCompletionDate(SDPreferences.getInstance(), i, date);
    }

    public static void setCompletionDate(SDPreferences sdpreferences, int i, Date date)
    {
        mCompletionDate.put(i, date);
        String temp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH)).format(date);
        sdpreferences.setValueForKey((new StringBuilder()).append("offlinemap_complete_date_").append(i).toString(), temp);
    }

    public static void setLastLevelIndex(int i, int j)
    {
        setLastLevelIndex(SDPreferences.getInstance(), i, j);
    }

    public static void setLastLevelIndex(SDPreferences sdpreferences, int i, int j)
    {
        mLastLevel.put(i, j);
        commitDataPeriodically(sdpreferences);
    }

    public static void setLastZipFolder(int i, int j)
    {
        setLastZipFolder(SDPreferences.getInstance(), i, j);
    }

    public static void setLastZipFolder(SDPreferences sdpreferences, int i, int j)
    {
        mLastFolder.put(i, j);
        commitDataPeriodically(sdpreferences);
    }

    public static void setPackageList(int i, ArrayList arraylist)
    {
        mPackageList.put(i, arraylist);
    }

    public static void setProgress(int i, float f)
    {
        setProgress(SDPreferences.getInstance(), i, f);
    }

    public static void setProgress(SDPreferences sdpreferences, int i, float f)
    {
        mProgress.put(i, Float.valueOf(f));
    }

    public static void setStatus(int i, int j)
    {
        setStatus(SDPreferences.getInstance(), i, j);
    }

    public static void setStatus(SDPreferences sdpreferences, int i, int j)
    {
        mStatus.put(i, j);
        commitDataPeriodically(sdpreferences);
    }

    public static final int COMPLETE = 3;
    public static final int DOWNLOAD = 1;
    public static final int HAVENT_DOWNLOAD = 0;
    public static final String LAST_FOLDER_KEY = "offlinemap_last_folder_";
    public static final String LAST_LEVEL_KEY = "offlinemap_last_level_";
    public static final int PAUSE = 2;
    public static final String PROGRESS_KEY = "offlinemap_percentage_";
    public static final String STATUS_KEY = "offlinemap_status_";
    private static final SparseArray mCompletionDate = new SparseArray();
    private static final SparseIntArray mLastFolder = new SparseIntArray();
    private static final SparseIntArray mLastLevel = new SparseIntArray();
    private static final SparseArray mPackageList = new SparseArray();
    private static final SparseArray mProgress = new SparseArray();
    private static final SparseIntArray mStatus = new SparseIntArray();
    private static long progressTimeInterval = 0L;

}
