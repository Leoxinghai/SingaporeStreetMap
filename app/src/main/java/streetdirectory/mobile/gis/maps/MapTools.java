

package streetdirectory.mobile.gis.maps;

import android.graphics.Bitmap;
import java.io.*;
import streetdirectory.mobile.core.storage.*;

public class MapTools
{

    public MapTools()
    {
    }

    public static String getMapCacheStoragePath(String s)
    {
        return CacheStorage.getStorageFile(s).getAbsolutePath();
    }

    public static String getOfflineMapExternalStoragePath()
    {
        File file = ExternalStorage.getStorageFile("offline_map/");
        if(!file.exists() && !file.mkdir())
            return null;
        else
            return file.getAbsolutePath();
    }

    public static String getOfflineMapInternalStoragePath()
    {
        File file = InternalStorage.getStorageFile("offline_map/");
        if(!file.exists() && !file.mkdir())
            return null;
        else
            return file.getAbsolutePath();
    }

    public static String getOfflineMapStoragePath()
    {
        if(!ExternalStorage.getStorageFile("offline_map/").exists())
            return getOfflineMapInternalStoragePath();
        else
            return getOfflineMapExternalStoragePath();
    }

    public static void saveBitmapToCache(Bitmap bitmap, String s)
    {
        saveBitmapToPath(bitmap, getMapCacheStoragePath(s));
    }

    public static void saveBitmapToPath(Bitmap bitmap, String s)
    {
        try {
            File file = new File(s);
            if (!file.exists()) {
                File pfile = file.getParentFile();
                if (!pfile.exists() && !pfile.mkdirs())
                    return;
                if (!pfile.createNewFile())
                    return;
            }
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, bytearrayoutputstream);
            byte temp[] = bytearrayoutputstream.toByteArray();
            FileOutputStream fos = new FileOutputStream(s);
            fos.write(temp);
            fos.flush();
            fos.close();
            return;
        } catch(IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public static void saveOfflineMapTile(Bitmap bitmap, String s)
    {
        saveBitmapToPath(bitmap, (new StringBuilder()).append(getOfflineMapStoragePath()).append(s).toString());
    }
}
