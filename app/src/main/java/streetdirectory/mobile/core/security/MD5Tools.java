

package streetdirectory.mobile.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tools
{

    public MD5Tools()
    {
    }

    public static String md5(String ss)
    {
        StringBuffer stringbuffer;
        int i;
        try
        {
            byte[] s = MessageDigest.getInstance("MD5").digest(ss.getBytes());
            stringbuffer = new StringBuffer();
            i = 0;
            for(;i < s.length;) {
                stringbuffer.append(Integer.toHexString(s[i] & 0xff | 0x100).substring(1, 3));
                i++;
            }
            ss = stringbuffer.toString();
            return ss;
        }
        // Misplaced declaration of an exception variable
        catch(java.security.NoSuchAlgorithmException ex)
        {
            return "";
        }
    }
}
