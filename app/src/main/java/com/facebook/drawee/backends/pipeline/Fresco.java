

package com.facebook.drawee.backends.pipeline;

import android.content.Context;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.*;

// Referenced classes of package com.facebook.drawee.backends.pipeline:
//            PipelineDraweeControllerBuilderSupplier, PipelineDraweeControllerBuilder

public class Fresco
{

    private Fresco()
    {
    }

    public static PipelineDraweeControllerBuilderSupplier getDraweeControllerBuilderSupplier()
    {
        return sDraweeControllerBuilderSupplier;
    }

    public static ImagePipeline getImagePipeline()
    {
        return getImagePipelineFactory().getImagePipeline();
    }

    public static ImagePipelineFactory getImagePipelineFactory()
    {
        return ImagePipelineFactory.getInstance();
    }

    public static void initialize(Context context)
    {
        ImagePipelineFactory.initialize(context);
        initializeDrawee(context);
    }

    public static void initialize(Context context, ImagePipelineConfig imagepipelineconfig)
    {
        ImagePipelineFactory.initialize(imagepipelineconfig);
        initializeDrawee(context);
    }

    private static void initializeDrawee(Context context)
    {
        sDraweeControllerBuilderSupplier = new PipelineDraweeControllerBuilderSupplier(context);
        SimpleDraweeView.initialize(sDraweeControllerBuilderSupplier);
    }

    public static PipelineDraweeControllerBuilder newDraweeControllerBuilder()
    {
        return sDraweeControllerBuilderSupplier.get();
    }

    public static void shutDown()
    {
        sDraweeControllerBuilderSupplier = null;
        SimpleDraweeView.shutDown();
        ImagePipelineFactory.shutDown();
    }

    private static PipelineDraweeControllerBuilderSupplier sDraweeControllerBuilderSupplier;
}
