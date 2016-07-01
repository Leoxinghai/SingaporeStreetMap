

package com.facebook.imagepipeline.decoder;

import com.facebook.imagepipeline.image.QualityInfo;

public interface ProgressiveJpegConfig
{

    public abstract int getNextScanNumberToDecode(int i);

    public abstract QualityInfo getQualityInfo(int i);
}
