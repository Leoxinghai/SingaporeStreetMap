

package streetdirectory.mobile.modules.offlinemap;

import android.os.Environment;
import java.io.File;
import streetdirectory.mobile.core.storage.ExternalStorage;
import streetdirectory.mobile.core.storage.InternalStorage;

public class OfflineMapStorage
{

    public OfflineMapStorage()
    {
    }

    public static File getStorageFile(String s)
    {
        if(isExternalStorageReadable() && isExternalStorageWritable())
            return ExternalStorage.getStorageFile(s);
        else
            return InternalStorage.getStorageFile(s);
    }

    public static boolean isExternalStorageReadable()
    {
        String s = Environment.getExternalStorageState();
        return "mounted".equals(s) || "mounted_ro".equals(s);
    }

    public static boolean isExternalStorageWritable()
    {
        return "mounted".equals(Environment.getExternalStorageState());
    }
}
