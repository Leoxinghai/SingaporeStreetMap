

package streetdirectory.mobile.core.storage;

import android.content.Context;
import android.os.Environment;
import java.io.BufferedInputStream;
import java.io.File;
import streetdirectory.mobile.core.MainApplication;
import streetdirectory.mobile.core.SDLogger;

// Referenced classes of package streetdirectory.mobile.core.storage:
//            StorageUtil

public class ExternalStorage
{

    public ExternalStorage()
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
        File file;
        if(android.os.Build.VERSION.SDK_INT >= 8)
            return context.getExternalFilesDir(null);
        String packageName = context.getPackageName();
        file = Environment.getExternalStorageDirectory();
        if(file == null) {
//            SDLogger.error(packageName.getMessage());
            return null;

        }
        file = new File(file, (new StringBuilder()).append("Android/data/").append(context).append("/files").toString());
        return file;
    }

    public static File getStorageFile(Context context, String s)
    {
        return new File(getStorageDirectory(context), s);
    }

    public static File getStorageFile(String s)
    {
        return getStorageFile(MainApplication.getAppContext(), s);
    }

    public static boolean isStorageWriteable()
    {
        return "mounted".equals(Environment.getExternalStorageState());
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
