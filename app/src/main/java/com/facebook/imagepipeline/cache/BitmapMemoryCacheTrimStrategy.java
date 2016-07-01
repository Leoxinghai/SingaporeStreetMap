

package com.facebook.imagepipeline.cache;

import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;

public class BitmapMemoryCacheTrimStrategy
    implements CountingMemoryCache.CacheTrimStrategy
{

    public BitmapMemoryCacheTrimStrategy()
    {
    }

    public double getTrimRatio(MemoryTrimType memorytrimtype)
    {

        switch(memorytrimtype.ordinal()) {
            default:
                FLog.wtf("BitmapMemoryCacheTrimStrategy", "unknown trim type: %s", new Object[]{
                        memorytrimtype
                });
                return 0.0D;
            case 1:
                if (android.os.Build.VERSION.SDK_INT < 21)
                    return 0.0D;
                return MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio();
            case 2:
            case 3:
            case 4:
                return 1.0D;
        }
    }

    private static final String TAG = "BitmapMemoryCacheTrimStrategy";
}
