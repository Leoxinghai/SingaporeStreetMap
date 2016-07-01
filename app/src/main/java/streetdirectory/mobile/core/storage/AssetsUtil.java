

package streetdirectory.mobile.core.storage;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.InputStream;
import streetdirectory.mobile.core.MainApplication;
import streetdirectory.mobile.core.SDLogger;

// Referenced classes of package streetdirectory.mobile.core.storage:
//            StorageUtil

public class AssetsUtil
{

    public AssetsUtil()
    {
    }

    public static void copy(AssetManager assetmanager, String s, File file, boolean flag)
    {
        String as2[];
        int j;
        try {
            as2 = assetmanager.list(s);
            j = as2.length;
            int i = 0;

            for (; i < j; ) {
                String s1;
                boolean flag2;
                s1 = as2[i];
                flag2 = false;
                File file1;
                String s2;
                s2 = (new StringBuilder()).append(s).append("/").append(s1).toString();
                file1 = new File(file, s1);
                boolean flag1 = flag2;
                if (!s1.endsWith(".gif")) {
                    flag1 = flag2;
                    if (!s1.endsWith(".png")) {
                        boolean flag3 = s1.endsWith(".xml");
                        flag1 = flag2;
                        if (!flag3) {
                            String as[] = null;
                            String as1[] = assetmanager.list(s2);
                            as = as1;

                            flag1 = flag2;
                            if (as != null) {
                                flag1 = flag2;
                                if (as.length > 0) {
                                    flag1 = true;
                                    SDLogger.debug((new StringBuilder()).append("Directory: ").append(s1).toString());
                                    copy(assetmanager, as, s2, file1, flag);
                                }
                            }
                        }
                    }
                }

                /*
                if (flag1)
                    break MISSING_BLOCK_LABEL_247;
                if (flag)
                    break MISSING_BLOCK_LABEL_204;
                if (file1.exists())
                    break MISSING_BLOCK_LABEL_247;
                */

                //if(s1.endsWith(".gif") || s1.endsWith(".png") ) {
                    InputStream inputstream;
                    SDLogger.debug((new StringBuilder()).append("Copy: ").append(s1).toString());
                    inputstream = assetmanager.open(s2);
                    StorageUtil.copy(inputstream, file1);
                    inputstream.close();
                //}
                i++;
            }


        } catch(Exception exception) {
            SDLogger.error(exception.getMessage());
        }
        return;
    }

    public static void copy(AssetManager assetmanager, String as[], String s, File file, boolean flag) {
        int j = as.length;
        int i = 0;
        String as1[] = null;

        try {
        for (; i < j; ) {
            String s1;
            boolean flag2;
            s1 = as[i];
            flag2 = false;
            File file1;
            String s2;
            s2 = (new StringBuilder()).append(s).append("/").append(s1).toString();
            file1 = new File(file, s1);
            boolean flag1 = flag2;
            if (s1.endsWith(".gif") || s1.endsWith(".png")) {

            } else {
                flag1 = flag2;
                boolean flag3 = s1.endsWith(".xml");
                flag1 = flag2;
                if (!flag3) {
                    String as2[] = assetmanager.list(s2);
                    as1 = as2;
                    flag1 = flag2;
                }
                if (as1 != null) {
                    flag1 = flag2;
                    if (as1.length > 0) {
                        flag1 = true;
                        SDLogger.debug((new StringBuilder()).append("Directory: ").append(s1).toString());
                        copy(assetmanager, as1, s2, file1, flag);
                    }
                }

            }
            InputStream inputstream;
            SDLogger.debug((new StringBuilder()).append("Copy: ").append(s1).toString());
            inputstream = assetmanager.open(s2);
            StorageUtil.copy(inputstream, file1);
            inputstream.close();

            i++;
        }

        } catch(Exception exception)

        {
            SDLogger.error(exception.getMessage());
        }
        return;
    }

    public static void copy(String s, File file)
    {
        copy(s, file, false);
    }

    public static void copy(String s, File file, boolean flag)
    {
        copy(getAssManager(), s, file, flag);
    }

    public static AssetManager getAssManager()
    {
        if(assManager == null)
            assManager = MainApplication.getAppContext().getAssets();
        return assManager;
    }

    public static boolean isDirectory(AssetManager assetmanager, String s)
    {
        int i;
        try
        {
            i = assetmanager.list(s).length;
            if(i > 0)
                return true;
            return false;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return false;
        }
    }

    public static boolean isDirectory(String s)
    {
        return isDirectory(getAssManager(), s);
    }

    static AssetManager assManager = null;

}
