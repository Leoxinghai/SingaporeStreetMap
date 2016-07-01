

package com.facebook.imagepipeline.cache;

import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;

public class NativeMemoryCacheTrimStrategy
    implements CountingMemoryCache.CacheTrimStrategy
{

    public NativeMemoryCacheTrimStrategy()
    {
    }

    public double getTrimRatio(MemoryTrimType memorytrimtype)
    {

        switch(memorytrimtype.ordinal())
        {
        default:
            FLog.wtf("NativeMemoryCacheTrimStrategy", "unknown trim type: %s", new Object[] {
                memorytrimtype
            });
            // fall through

        case 1: // '\001'
            return 0.0D;

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return 1.0D;
        }
    }

    private static final String TAG = "NativeMemoryCacheTrimStrategy";
}
