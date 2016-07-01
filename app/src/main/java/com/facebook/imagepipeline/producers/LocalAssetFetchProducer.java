

package com.facebook.imagepipeline.producers;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.IOException;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            LocalFetchProducer

public class LocalAssetFetchProducer extends LocalFetchProducer
{

    public LocalAssetFetchProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, AssetManager assetmanager, boolean flag)
    {
        super(executor, pooledbytebufferfactory, flag);
        mAssetManager = assetmanager;
    }

    private static String getAssetName(ImageRequest imagerequest)
    {
        return imagerequest.getSourceUri().getPath().substring(1);
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
            AssetFileDescriptor assetFileDescriptor = mAssetManager.openFd(getAssetName(imagerequest));
            imagerequest1 = imagerequest;
            imagerequest2 = imagerequest;
            l = assetFileDescriptor.getLength();
            int j = (int)l;
            i = j;
            if(imagerequest != null)
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
            ex.printStackTrace();
        }
        return i;
    }

    protected EncodedImage getEncodedImage(ImageRequest imagerequest)
        throws IOException
    {
        return getEncodedImage(mAssetManager.open(getAssetName(imagerequest), 2), getLength(imagerequest));
    }

    protected String getProducerName()
    {
        return "LocalAssetFetchProducer";
    }

    static final String PRODUCER_NAME = "LocalAssetFetchProducer";
    private final AssetManager mAssetManager;
}
