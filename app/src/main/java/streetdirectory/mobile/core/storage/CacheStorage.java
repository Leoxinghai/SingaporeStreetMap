

package streetdirectory.mobile.core.storage;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.File;
import streetdirectory.mobile.core.MainApplication;

// Referenced classes of package streetdirectory.mobile.core.storage:
//            StorageUtil

public class CacheStorage
{

    public CacheStorage()
    {
    }

    public static boolean clear()
    {
        return StorageUtil.delete(getStorageDirectory());
    }

    public static boolean fileExists(Context context, String s)
    {
        return getStorageFile(context, s).exists();
    }

    public static boolean fileExists(String s)
    {
        return fileExists(MainApplication.getAppContext(), s);
    }

    public static File getStorageDirectory()
    {
        return getStorageDirectory(MainApplication.getAppContext());
    }

    public static File getStorageDirectory(Context context)
    {
        return context.getCacheDir();
    }

    public static File getStorageFile(Context context, String s)
    {
        return new File(getStorageDirectory(context), s);
    }

    public static File getStorageFile(String s)
    {
        return getStorageFile(MainApplication.getAppContext(), s);
    }

    public static BufferedInputStream load(String s)
    {
        return StorageUtil.load(getStorageFile(s));
    }

    public static boolean save(String s, byte abyte0[])
    {
        return StorageUtil.save(getStorageFile(s), abyte0);
    }
}
