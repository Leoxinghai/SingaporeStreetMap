// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap;

import android.os.AsyncTask;
import android.widget.Toast;
import java.io.*;
import streetdirectory.mobile.core.MainApplication;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.storage.ExternalStorage;
import streetdirectory.mobile.core.storage.InternalStorage;

public class OfflineMapManager
{

    public OfflineMapManager()
    {
    }

    private static void deleteFileOrDirectory(File file)
    {
        if(file.isDirectory())
        {
            File afile[] = file.listFiles();
            int j = afile.length;
            for(int i = 0; i < j; i++)
                deleteFileOrDirectory(afile[i]);

        }
        file.delete();
    }

    public static void invalidate()
    {
    }

    private static void moveFile(String s, String s1, String s2)
    {
        try
        {
            File file = new File(s2);
            if(!((File) (file)).exists())
                ((File) (file)).mkdirs();
            FileInputStream fis = new FileInputStream((new StringBuilder()).append(s).append(File.separator).append(s1).toString());
            byte abyte0[];
            int i;
            FileOutputStream fos = new FileOutputStream((new StringBuilder()).append(s2).append(File.separator).append(s1).toString());
            abyte0 = new byte[1024];
            while(true) {
                i = ((InputStream) (fis)).read(abyte0);
                if (i == -1)
                    break;
                fos.write(abyte0, 0, i);
            }
            fis.close();
            fos.flush();
            fos.close();
            (new File((new StringBuilder()).append(s).append(File.separator).append(s1).toString())).delete();
            return;

        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            ex.printStackTrace();
            SDLogger.printStackTrace(ex, "Move Map Data Failed");
        }

        return;
    }

    private static void moveFileOrDirectory(File file)
    {
        File files[];
        if(file.isDirectory())
        {
            files = file.listFiles();
            int j = files.length;
            for(int i = 0; i < j; i++)
                moveFileOrDirectory(files[i]);

        } else
        if(file.isFile())
        {
            String s = file.getParent();
            String s1 = s.replaceAll("offline_map/data", "offline_map");
            moveFile(s, file.getName(), s1);
        }
    }

    public static void moveMapData()
    {
        File file = InternalStorage.getStorageFile("offline_map/data");
        if(file.exists())
        {
            startMoveData(file);
            return;
        }
        try
        {
            File file1 = ExternalStorage.getStorageFile("offline_map/data");
            if(file1.exists())
            {
                startMoveData(file1);
                return;
            }
        }
        catch(Exception exception)
        {
            SDLogger.printStackTrace(exception, "Move Map Data Failed");
        }
        return;
    }

    private static void startMoveData(File file)
    {
        final File fileSrc = file;
        Toast.makeText(MainApplication.getAppContext(), "Repairing Offline Map data...", 1).show();
        (new AsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((File[])aobj);
            }

            protected Void doInBackground(File afile[])
            {
                OfflineMapManager.moveFileOrDirectory(fileSrc);
                return null;
            }

            protected void onPostExecute(Object obj)
            {
                onPostExecute((Void)obj);
            }

            protected void onPostExecute(Void void1)
            {
                super.onPostExecute(void1);
                OfflineMapManager.deleteFileOrDirectory(fileSrc);
                Toast.makeText(MainApplication.getAppContext(), "Offline Map data repaired", 1).show();
            }



        }).execute(new File[] {
            file
        });
    }


}
