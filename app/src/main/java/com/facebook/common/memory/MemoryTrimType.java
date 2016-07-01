

package com.facebook.common.memory;


public enum MemoryTrimType
{
    OnCloseToDalvikHeapLimit("OnCloseToDalvikHeapLimit", 0, 0.5D),
    OnSystemLowMemoryWhileAppInForeground("OnSystemLowMemoryWhileAppInForeground", 1, 0.5D),
    OnSystemLowMemoryWhileAppInBackground("OnSystemLowMemoryWhileAppInBackground", 2, 1.0D),
    OnAppBackgrounded("OnAppBackgrounded", 3, 1.0D);

    private MemoryTrimType(String s, int i, double d)
    {
        sType = s;
        iType = i;
        mSuggestedTrimRatio = d;
    }

    public double getSuggestedTrimRatio()
    {
        return mSuggestedTrimRatio;
    }
    private double mSuggestedTrimRatio;
    private String sType;
    private int iType;
}
