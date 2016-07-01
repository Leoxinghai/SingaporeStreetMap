

package com.facebook.common.util;


public class Hex
{

    public Hex()
    {
    }

    public static String byte2Hex(int i)
    {
        if(i > 255 || i < 0)
            throw new IllegalArgumentException("The int converting to hex should be in range 0~255");
        else
            return (new StringBuilder()).append(String.valueOf(FIRST_CHAR[i])).append(String.valueOf(SECOND_CHAR[i])).toString();
    }

    public static byte[] decodeHex(String s) {
        byte abyte0[];
        int i;
        int j;
        boolean flag;
        int k;
        k = s.length();
        if ((k & 1) != 0)
            throw new IllegalArgumentException("Odd number of characters.");
        flag = false;
        abyte0 = new byte[k >> 1];
        i = 0;
        j = 0;

        int l;
        byte byte0;
        for(;; i++ ) {
            if (j >= k) {
                i = ((flag) ? 1 : 0);
            } else {

                l = j + 1;
                j = s.charAt(j);
                if (j > 'f') {
                    i = 1;
                } else {
                    byte0 = DIGITS[j];
                    if (byte0 == -1) {
                        i = 1;
                    } else {
                        j = l + 1;
                        l = s.charAt(l);
                        if (l > 'f') {
                            i = 1;
                        } else {
                            label0:
                            {
                                l = DIGITS[l];
                                if (l != -1)
                                    break label0;
                                i = 1;
                            }
                        }
                    }
                }
            }

//            abyte0[i] = (byte) (byte0 << 4 | l);

            if (i != 0)
                throw new IllegalArgumentException((new StringBuilder()).append("Invalid hexadecimal digit: ").append(s).toString());
            else
                return abyte0;
        }

    }

    public static String encodeHex(byte abyte0[], boolean flag)
    {
        char ac[] = new char[abyte0.length * 2];
        int j = 0;
        int i = 0;
        int k;
        for(;i < abyte0.length;)
        {
            k = abyte0[i] & 0xff;
            if(k == 0 && flag)
                break;
            int l = j + 1;
            ac[j] = FIRST_CHAR[k];
            j = l + 1;
            ac[l] = SECOND_CHAR[k];
            i++;

        }
        return new String(ac, 0, j);
    }

    public static byte[] hexStringToByteArray(String s)
    {
        return decodeHex(s.replaceAll(" ", ""));
    }

    private static final byte DIGITS[];
    private static final char FIRST_CHAR[];
    private static final char HEX_DIGITS[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static final char SECOND_CHAR[];

    static
    {
        FIRST_CHAR = new char[256];
        SECOND_CHAR = new char[256];
        for(int i = 0; i < 256; i++)
        {
            FIRST_CHAR[i] = HEX_DIGITS[i >> 4 & 0xf];
            SECOND_CHAR[i] = HEX_DIGITS[i & 0xf];
        }

        DIGITS = new byte[103];
        for(int j = 0; j <= 70; j++)
            DIGITS[j] = -1;

        for(byte byte0 = 0; byte0 < 10; byte0++)
            DIGITS[byte0 + 48] = byte0;

        for(byte byte1 = 0; byte1 < 6; byte1++)
        {
            DIGITS[byte1 + 65] = (byte)(byte1 + 10);
            DIGITS[byte1 + 97] = (byte)(byte1 + 10);
        }

    }
}
