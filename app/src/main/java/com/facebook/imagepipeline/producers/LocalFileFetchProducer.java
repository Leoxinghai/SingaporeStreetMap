

package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.*;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            LocalFetchProducer

public class LocalFileFetchProducer extends LocalFetchProducer
{

    public LocalFileFetchProducer(Executor executor, PooledByteBufferFactory pooledbytebufferfactory, boolean flag)
    {
        super(executor, pooledbytebufferfactory, flag);
    }

    protected EncodedImage getEncodedImage(ImageRequest imagerequest)
        throws IOException
    {
        return getEncodedImage(((java.io.InputStream) (new FileInputStream(imagerequest.getSourceFile().toString()))), (int)imagerequest.getSourceFile().length());
    }

    protected String getProducerName()
    {
        return "LocalFileFetchProducer";
    }

    static final String PRODUCER_NAME = "LocalFileFetchProducer";
}
