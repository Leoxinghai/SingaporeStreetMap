

package com.facebook.common.util;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecureHashUtil
{

    public SecureHashUtil()
    {
    }

    private static String convertToHex(byte abyte0[])
        throws UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder(abyte0.length);
        int j = abyte0.length;
        for(int i = 0; i < j; i++)
        {
            int k = abyte0[i] & 0xff;
            stringbuilder.append((char)HEX_CHAR_TABLE[k >>> 4]);
            stringbuilder.append((char)HEX_CHAR_TABLE[k & 0xf]);
        }

        return stringbuilder.toString();
    }

    public static String makeMD5Hash(String s)
    {
        try
        {
            s = makeMD5Hash(s.getBytes("utf-8"));
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new RuntimeException(s);
        }
        return s;
    }

    public static String makeMD5Hash(byte abyte0[])
    {
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(abyte0, 0, abyte0.length);
            String temp = convertToHex(messagedigest.digest());
            return temp;
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static String makeSHA1Hash(String s)
    {
        try
        {
            s = makeSHA1Hash(s.getBytes("utf-8"));
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new RuntimeException(s);
        }
        return s;
    }

    public static String makeSHA1Hash(byte abyte0[])
    {
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
            messagedigest.update(abyte0, 0, abyte0.length);
            String temp = convertToHex(messagedigest.digest());
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static String makeSHA1HashBase64(byte abyte0[])
    {
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
            messagedigest.update(abyte0, 0, abyte0.length);
            String temp = Base64.encodeToString(messagedigest.digest(), 11);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }

    }

    static final byte HEX_CHAR_TABLE[] = {
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        97, 98, 99, 100, 101, 102
    };

}
