

package streetdirectory.mobile.core;

import java.io.*;
import streetdirectory.mobile.core.storage.StreamUtil;

// Referenced classes of package streetdirectory.mobile.core:
//            ApplicationLogger

public class SDLogger extends ApplicationLogger
{

    public SDLogger()
    {
    }

    public static void debug(String s)
    {
        if(s != null)
            debug("Streetdirectory", s);
    }

    public static void debugSan(String s)
    {
        if(s != null)
            debug("sangga", s);
    }

    public static void error(String s)
    {
        if(s != null)
            error("Streetdirectory", s);
    }

    public static void info(String s)
    {
        if(s != null)
            debug("Streetdirectory", s);
    }

    public static InputStream printInputStream(InputStream inputstream)
    {
        try
        {
            ByteArrayOutputStream bytearrayoutputstream = StreamUtil.inputStreamToByteArrayOutputStream(inputstream);
            inputstream.close();
            debug(StreamUtil.inputStreamToString(new ByteArrayInputStream(bytearrayoutputstream.toByteArray())));
            inputstream = new ByteArrayInputStream(bytearrayoutputstream.toByteArray());
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
        return inputstream;
    }

    public static void printStackTrace(Error error1, String s)
    {
        error1.printStackTrace();
        debug((new StringBuilder()).append(s).append(": ").append(error1.toString()).toString());
    }

    public static void printStackTrace(Exception exception)
    {
        printStackTrace(exception, "");
    }

    public static void printStackTrace(Exception exception, String s)
    {
        if(exception != null) {
            exception.printStackTrace();
            debug((new StringBuilder()).append(s).append(": ").append(exception.toString()).toString());
        }
    }

    public static void verbose(String s)
    {
        if(s != null)
            verbose("Streetdirectory", s);
    }
}
