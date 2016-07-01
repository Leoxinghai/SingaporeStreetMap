

package com.facebook.imagepipeline.nativecode;

import com.facebook.common.soloader.SoLoaderShim;
import java.util.*;

public class ImagePipelineNativeLoader
{

    public ImagePipelineNativeLoader()
    {
    }

    public static void load()
    {
        for(int i = 0; i < DEPENDENCIES.size(); i++)
            SoLoaderShim.loadLibrary((String)DEPENDENCIES.get(i));

        SoLoaderShim.loadLibrary("imagepipeline");
    }

    public static final List DEPENDENCIES;
    public static final String DSO_NAME = "imagepipeline";

    static
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add("webp");
        DEPENDENCIES = Collections.unmodifiableList(arraylist);
    }
}
