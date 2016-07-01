

package streetdirectory.mobile.modules.map;

import android.graphics.Rect;

public class SdMapLayer
{

    public SdMapLayer()
    {
        scale = 1.0D;
        tileWidth = 1;
        tileHeight = 1;
    }

    public SdMapLayer(int i, int j, double d)
    {
        scale = 1.0D;
        tileWidth = 1;
        tileHeight = 1;
        tileWidth = i;
        tileHeight = j;
        scale = d;
    }

    public void getTilesRect(int i, int j, int k, int l, Rect rect)
    {
        float f1 = k;
        float f = l;
        f1 = (float)i - f1 * 0.5F;
        f = (float)j - f * 0.5F;
        rect.set((int)(f1 / (float)tileWidth), (int)(f / (float)tileHeight), (int)(((float)k + f1) / (float)tileWidth), (int)(((float)l + f) / (float)tileHeight));
    }

    public double scale;
    public int tileHeight;
    public int tileWidth;
}
