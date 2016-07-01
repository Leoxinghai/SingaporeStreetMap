

package streetdirectory.mobile.core.storage;

import java.io.*;

public class StreamUtil
{

    public StreamUtil()
    {
    }

    public static void copy(InputStream inputstream, OutputStream outputstream)
    {
        byte abyte0[] = new byte[4096];

        try {
            for (; ; ) {
                int i = inputstream.read(abyte0, 0, abyte0.length);
                if (i < 0)
                    break;
                outputstream.write(abyte0, 0, i);
            }
        } catch(IOException ioex) {
            ioex.printStackTrace();
        }
        //inputstream;
        //inputstream.printStackTrace();
    }

    public static ByteArrayOutputStream inputStreamToByteArrayOutputStream(InputStream inputstream) {
        ByteArrayOutputStream bytearrayoutputstream;
        byte abyte0[];
        bytearrayoutputstream = new ByteArrayOutputStream();
        abyte0 = new byte[4096];


        try {
            for (;;) {
                int i = inputstream.read(abyte0);
                if (i < 0)
                    break;
                 bytearrayoutputstream.write(abyte0, 0, i);
            }

            inputstream.close();
            return bytearrayoutputstream;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }

    }

    public static String inputStreamToString(InputStream inputstream)
    {
        StringBuilder stringbuilder;
        byte abyte0[];
        stringbuilder = new StringBuilder();
        abyte0 = new byte[4096];

        try
        {
            for(;;) {
                int i = inputstream.read(abyte0);
                if (i < 0)
                    break;
                stringbuilder.append(new String(abyte0, 0, i));
            }
            inputstream.close();
            String temp = stringbuilder.toString();
            return temp;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

    public static final int DEFAULT_BUFFER_SIZE = 4096;
}
