

package streetdirectory.mobile.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringTools
{

    public StringTools()
    {
    }

    public static boolean isStringEmpty(String s)
    {
        return s == null || s.length() == 0;
    }

    public static byte[] tryParseByte(String s)
    {
        return s.getBytes();
    }

    public static Date tryParseDate(String s, String s1, Date date)
    {
        try
        {
            Date temp = (new SimpleDateFormat(s1)).parse(s);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return date;
        }
    }

    public static double tryParseDouble(String s, double d)
    {
        double d1;
        try
        {
            d1 = Double.parseDouble(s);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return d;
        }
        return d1;
    }

    public static float tryParseFloat(String s, float f)
    {
        float f1;
        try
        {
            f1 = Float.parseFloat(s);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return f;
        }
        return f1;
    }

    public static int tryParseInt(String s, int i)
    {
        int j;
        try
        {
            j = Integer.parseInt(s);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return i;
        }
        return j;
    }

    public static long tryParseLong(String s, long l)
    {
        long l1;
        try
        {
            l1 = Long.parseLong(s);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return l;
        }
        return l1;
    }
}
