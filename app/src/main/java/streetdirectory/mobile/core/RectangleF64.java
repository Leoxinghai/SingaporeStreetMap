

package streetdirectory.mobile.core;


// Referenced classes of package streetdirectory.mobile.core:
//            PointF64

public class RectangleF64
{

    public RectangleF64()
    {
    }

    public RectangleF64(double d, double d1, double d2, double d3)
    {
        top = d1;
        left = d;
        bottom = d3;
        right = d2;
    }

    public void MakePositif()
    {
        if(right < left)
        {
            double d = left;
            left = right;
            right = d;
        }
        if(bottom < top)
        {
            double d1 = top;
            top = bottom;
            bottom = d1;
        }
    }

    public void Offset(double d, double d1)
    {
        bottom = bottom + d1;
        top = top + d1;
        left = left + d;
        right = right + d;
    }

    public boolean contain(PointF64 pointf64)
    {
        return left <= pointf64.x && pointf64.x <= right && top <= pointf64.y && pointf64.y <= bottom;
    }

    public double getHeight()
    {
        return bottom - top;
    }

    public double getWidth()
    {
        return right - left;
    }

    public double getX()
    {
        return left;
    }

    public double getY()
    {
        return top;
    }

    public boolean intersects(RectangleF64 rectanglef64)
    {
        return left <= rectanglef64.right && top <= rectanglef64.bottom && right >= rectanglef64.left && bottom >= rectanglef64.top;
    }

    public boolean intersects2(RectangleF64 rectanglef64)
    {
        return rectanglef64.left <= right && rectanglef64.right >= left && rectanglef64.top <= bottom && rectanglef64.bottom >= top;
    }

    public boolean intersectsPositif(RectangleF64 rectanglef64)
    {
        rectanglef64.MakePositif();
        MakePositif();
        return intersects(rectanglef64);
    }

    public void setHeight(double d)
    {
        bottom = top + d;
    }

    public void setWidth(double d)
    {
        right = left + d;
    }

    public void setX(double d)
    {
        left = d;
    }

    public void setY(double d)
    {
        top = d;
    }

    public double bottom;
    public double left;
    public double right;
    public double top;
}
