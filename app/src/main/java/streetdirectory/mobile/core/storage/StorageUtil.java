

package streetdirectory.mobile.core.storage;

import java.io.*;
import streetdirectory.mobile.core.SDLogger;

// Referenced classes of package streetdirectory.mobile.core.storage:
//            StreamUtil

public class StorageUtil
{

    public StorageUtil()
    {
    }

    public static void copy(File file, File file1)
    {
        try
        {
            InputStream temp= load(file);
            copy(((InputStream) (temp)), file1);
            temp.close();
            return;
        }
        catch(Exception ex)
        {
            return;
        }
    }

    public static void copy(InputStream inputstream, File file)
    {
        Object obj;
        Object obj1;
        Object obj2;
        obj2 = null;
        obj1 = null;
        obj = obj2;
        try
        {

        if(!file.exists()) {
            obj = obj2;
            File file1 = file.getParentFile();
            obj = obj2;
            if (!file1.exists()) {
                obj = obj2;
                file1.mkdirs();
                obj = obj2;
            }
            file.createNewFile();
        }
            obj = obj2;
            OutputStream os  = new BufferedOutputStream(new FileOutputStream(file), 4096);
            StreamUtil.copy(inputstream, os);
            inputstream.close();
            os.flush();
            os.close();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return;
        }
    }

    public static void createNoMediaFile(String s)
    {
        File file = new File((new StringBuilder()).append(s).append("/").append(".nomedia").toString());
        try {
            if (!file.exists()) {
                file.createNewFile();
                return;
            }
        } catch(IOException iex) {
            iex.printStackTrace();
        }
    }

    public static boolean delete(File file)
    {
        if(file.isDirectory())
        {
            String as[] = file.list();
            int j = as.length;
            for(int i = 0; i < j; i++)
            {
                String s = as[i];
                SDLogger.debug((new StringBuilder()).append("Delete: ").append(s).toString());
                if(!delete(new File(file, s)))
                    return false;
            }

        }
        return file.delete();
    }

    public static boolean delete(String s)
    {
        return delete(new File(s));
    }

    public static boolean deleteDirectory(File file)
    {
        if(file == null)
            return false;

        if(!file.exists())
            return true;
        if(!file.isDirectory())
            return false;

        String as[];
        int i;
        as = file.list();
        if(as != null) {
            i = 0;


            File file1;
            for (; i < as.length; ) {
                file1 = new File(file, as[i]);
                if (file1.isDirectory()) {
                    if (!deleteDirectory(file1))
                        return false;
                } else {
                    if (!file1.delete())
                        return false;

                }
                i++;
            }
        }
        return file.delete();
    }

    public static BufferedInputStream load(File file)
    {
        try {
            if (!file.exists())
                return null;
            BufferedInputStream temp = new BufferedInputStream(new FileInputStream(file), 8192);
            return temp;
        } catch(IOException iex) {
            iex.printStackTrace();
        }
        return null;
    }

    public static BufferedInputStream load(String s)
    {
        return load(new File(s));
    }

    public static boolean save(File file, byte abyte0[])
    {
        try
        {
            if(!file.exists())
            {
                File file1 = file.getParentFile();
                if(!file1.exists())
                    file1.mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(abyte0);
            outputStream.close();
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean save(String s, byte abyte0[])
    {
        return save(new File(s), abyte0);
    }
}
