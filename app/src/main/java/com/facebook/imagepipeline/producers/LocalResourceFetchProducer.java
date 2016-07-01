

package com.facebook.imagepipeline.producers;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.IOException;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            LocalFetchProducer

public class LocalResourceFetchProducer extends LocalFetchProducer
{

    public LocalResourceFetchProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, Resources resources, boolean flag)
    {
        super(executor, pooledbytebufferfactory, flag);
        mResources = resources;
    }

    private int getLength(ImageRequest imagerequest)
    {
        ImageRequest imagerequest1;
        ImageRequest imagerequest2;
        imagerequest2 = null;
        imagerequest1 = null;
        int i;
        long l;
        try
        {
            AssetFileDescriptor assetFileDescriptor = mResources.openRawResourceFd(getResourceId(imagerequest));
            imagerequest1 = imagerequest;
            imagerequest2 = imagerequest;
            l = assetFileDescriptor.getLength();
            int j = (int)l;
            i = j;
            if(assetFileDescriptor != null)
            {
                try
                {
                    assetFileDescriptor.close();
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    return j;
                }
                i = j;
            }
            return i;
        }
        catch(Exception ex)
        {
            i = -1;
            return i;
        }
    }

    private static int getResourceId(ImageRequest imagerequest)
    {
        return Integer.parseInt(imagerequest.getSourceUri().getPath().substring(1));
    }

    protected EncodedImage getEncodedImage(ImageRequest imagerequest)
        throws IOException
    {
        return getEncodedImage(mResources.openRawResource(getResourceId(imagerequest)), getLength(imagerequest));
    }

    protected String getProducerName()
    {
        return "LocalResourceFetchProducer";
    }

    static final String PRODUCER_NAME = "LocalResourceFetchProducer";
    private final Resources mResources;
}
