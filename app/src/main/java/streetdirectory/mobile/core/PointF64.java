

package streetdirectory.mobile.core;

import android.graphics.Point;

public class PointF64
{

    public PointF64()
    {
    }

    public PointF64(double d, double d1)
    {
        x = d;
        y = d1;
    }

    public int getIntX()
    {
        return (int)x;
    }

    public int getIntY()
    {
        return (int)y;
    }

    public void set(double d, double d1)
    {
        x = d;
        y = d1;
    }

    public Point toPoint()
    {
        return new Point((int)x, (int)y);
    }

    public double x;
    public double y;
}
