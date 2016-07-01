

package com.facebook.imagepipeline.producers;

import android.net.Uri;
import android.util.Base64;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Preconditions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            LocalFetchProducer

public class DataFetchProducer extends LocalFetchProducer
{

    public DataFetchProducer(PooledByteBufferFactory pooledbytebufferfactory, boolean flag)
    {
        super(CallerThreadExecutor.getInstance(), pooledbytebufferfactory, flag);
    }

    static byte[] getData(String s)
    {
        Preconditions.checkArgument(s.substring(0, 5).equals("data:"));
        int i = s.indexOf(',');
        String s1 = s.substring(i + 1, s.length());
        if(isBase64(s.substring(0, i)))
            return Base64.decode(s1, 0);
        else
            return Uri.decode(s1).getBytes();
    }

    static boolean isBase64(String s)
    {
        if(!s.contains(";"))
        {
            return false;
        } else
        {
            String ss[] = s.split(";");
            return ss[ss.length - 1].equals("base64");
        }
    }

    protected EncodedImage getEncodedImage(ImageRequest imagerequest)
        throws IOException
    {
        byte temp[] = getData(imagerequest.getSourceUri().toString());
        return getByteBufferBackedEncodedImage(new ByteArrayInputStream(temp), temp.length);
    }

    protected String getProducerName()
    {
        return "DataFetchProducer";
    }

    private static final String PRODUCER_NAME = "DataFetchProducer";
}
