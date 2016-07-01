

package com.facebook.imagepipeline.decoder;

import com.facebook.common.internal.Preconditions;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import java.util.Collections;
import java.util.List;

// Referenced classes of package com.facebook.imagepipeline.decoder:
//            ProgressiveJpegConfig

public class SimpleProgressiveJpegConfig
    implements ProgressiveJpegConfig
{
    private static class DefaultDynamicValueConfig
        implements DynamicValueConfig
    {

        public int getGoodEnoughScanNumber()
        {
            return 0;
        }

        public List getScansToDecode()
        {
            return Collections.EMPTY_LIST;
        }

        private DefaultDynamicValueConfig()
        {
        }

    }

    public static interface DynamicValueConfig
    {

        public abstract int getGoodEnoughScanNumber();

        public abstract List getScansToDecode();
    }


    public SimpleProgressiveJpegConfig()
    {
        this(((DynamicValueConfig) (new DefaultDynamicValueConfig())));
    }

    public SimpleProgressiveJpegConfig(DynamicValueConfig dynamicvalueconfig)
    {
        mDynamicValueConfig = (DynamicValueConfig)Preconditions.checkNotNull(dynamicvalueconfig);
    }

    public int getNextScanNumberToDecode(int i)
    {
        List list = mDynamicValueConfig.getScansToDecode();
        if(list == null || list.isEmpty())
            return i + 1;
        for(int j = 0; j < list.size(); j++)
            if(((Integer)list.get(j)).intValue() > i)
                return ((Integer)list.get(j)).intValue();

        return 0x7fffffff;
    }

    public QualityInfo getQualityInfo(int i)
    {
        boolean flag;
        if(i >= mDynamicValueConfig.getGoodEnoughScanNumber())
            flag = true;
        else
            flag = false;
        return ImmutableQualityInfo.of(i, flag, false);
    }

    private final DynamicValueConfig mDynamicValueConfig;
}
