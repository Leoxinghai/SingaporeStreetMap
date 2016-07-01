

package streetdirectory.mobile.core;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SDUncaughtExceptionHandler
    implements Thread.UncaughtExceptionHandler
{

    public SDUncaughtExceptionHandler(String s, String s1)
    {
        localPath = s;
        url = s1;
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread thread, Throwable throwable)
    {
        Object obj = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        obj = (new StringBuilder()).append(((String) (obj))).append("_sdmobile.stacktrace.txt").toString();
        if(localPath != null)
        {
            obj = new File((new StringBuilder()).append(localPath).append("/").append(((String) (obj))).toString());
            if(!((File) (obj)).exists())
                ((File) (obj)).getParentFile().mkdirs();
            try
            {
                obj = new PrintStream(((File) (obj)));
                throwable.printStackTrace(((PrintStream) (obj)));
                ((PrintStream) (obj)).close();
            }
            catch(Exception exception) { }
        }
        if(url == null);
        defaultUEH.uncaughtException(thread, throwable);
    }

    private Thread.UncaughtExceptionHandler defaultUEH;
    private String localPath;
    private String url;
}
