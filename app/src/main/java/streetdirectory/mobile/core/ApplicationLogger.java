

package streetdirectory.mobile.core;

import android.util.Log;

public class ApplicationLogger
{

    public ApplicationLogger()
    {
    }

    public static void debug(String s, String s1)
    {
        Log.d(s, s1);
    }

    public static void error(String s, String s1)
    {
        Log.e(s, s1);
    }

    public static void info(String s, String s1)
    {
        Log.i(s, s1);
    }

    public static void verbose(String s, String s1)
    {
        Log.v(s, s1);
    }
}
