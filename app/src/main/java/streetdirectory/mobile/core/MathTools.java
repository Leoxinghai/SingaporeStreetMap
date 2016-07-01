

package streetdirectory.mobile.core;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import java.util.*;
import streetdirectory.mobile.gis.GeoPoint;

// Referenced classes of package streetdirectory.mobile.core:
//            RectangleF64, PointF64

public class MathTools
{

    public MathTools()
    {
    }

    public static RectangleF64 GetBoundsGeoPoint(ArrayList arraylist)
    {
        RectangleF64 rectanglef64 = new RectangleF64();
        if(arraylist.size() == 0)
            return rectanglef64;
        double d5 = ((GeoPoint)arraylist.get(0)).longitude;
        double d1 = ((GeoPoint)arraylist.get(0)).longitude;
        double d2 = ((GeoPoint)arraylist.get(0)).latitude;
        double d = ((GeoPoint)arraylist.get(0)).latitude;
        Iterator iterator = arraylist.iterator();
        for(;iterator.hasNext();) {
            GeoPoint geopoint = (GeoPoint)iterator.next();
            double d3 = d5;
            if(d5 > geopoint.longitude)
                d3 = geopoint.longitude;
            double d4 = d2;
            if(d2 > geopoint.latitude)
                d4 = geopoint.latitude;
            double d6 = d1;
            if(d1 < geopoint.longitude)
                d6 = geopoint.longitude;
            d5 = d3;
            d2 = d4;
            d1 = d6;
            if(d < geopoint.latitude)
            {
                d = geopoint.latitude;
                d5 = d3;
                d2 = d4;
                d1 = d6;
            }
        }
        return new RectangleF64(d5, d2, d1, d);
    }

    public static RectangleF64 GetBoundsPointF64(ArrayList arraylist)
    {
        RectangleF64 rectanglef64 = new RectangleF64();
        if(arraylist.size() == 0)
            return rectanglef64;
        double d5 = ((PointF64)arraylist.get(0)).x;
        double d1 = ((PointF64)arraylist.get(0)).x;
        double d2 = ((PointF64)arraylist.get(0)).y;
        double d = ((PointF64)arraylist.get(0)).y;
        Iterator iterator = arraylist.iterator();
        for(;iterator.hasNext();) {
            PointF64 pointf64 = (PointF64)iterator.next();
            double d3 = d5;
            if(d5 > pointf64.x)
                d3 = pointf64.x;
            double d4 = d2;
            if(d2 > pointf64.y)
                d4 = pointf64.y;
            double d6 = d1;
            if(d1 < pointf64.x)
                d6 = pointf64.x;
            d5 = d3;
            d2 = d4;
            d1 = d6;
            if(d < pointf64.y)
            {
                d = pointf64.y;
                d5 = d3;
                d2 = d4;
                d1 = d6;
            }
        }
        return new RectangleF64(d5, d2, d1, d);
    }

    public static double computeDistance(double d, double d1, double d2, double d3)
    {
        return Math.sqrt(Math.pow(d2 - d, 2D) + Math.pow(d3 - d1, 2D));
    }

    public static double computeDistance(PointF64 pointf64, PointF64 pointf64_1)
    {
        return Math.sqrt(Math.pow(pointf64_1.x - pointf64.x, 2D) + Math.pow(pointf64_1.y - pointf64.y, 2D));
    }

    public static int dpToPixel(float f, Activity activity)
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        if(activity != null)
        {
            WindowManager temp = activity.getWindowManager();
            if(temp != null)
            {
                Display temp2 = temp.getDefaultDisplay();
                if(temp2 != null)
                {
                    temp2.getMetrics(displaymetrics);
                    return (int)(displaymetrics.density * f + 0.5F);
                }
            }
        }
        return (int)f;
    }

    public static boolean ptInPolygon(PointF64 pointf64, List list)
    {
        boolean flag1 = false;
        int j = list.size() - 1;
        int i = 0;
        while(i < list.size())
        {
            boolean flag;
                if(((PointF64)list.get(i)).y > pointf64.y || pointf64.y >= ((PointF64)list.get(j)).y)
                {
                    flag = flag1;
                    if(((PointF64)list.get(j)).y > pointf64.y || pointf64.y >= ((PointF64)list.get(i)).y) {
                        j = i;
                        i++;
                        flag1 = flag;
                        continue;
                    }
                }
                flag = flag1;
                if(pointf64.x < ((((PointF64)list.get(j)).x - ((PointF64)list.get(i)).x) * (pointf64.y - ((PointF64)list.get(i)).y)) / (((PointF64)list.get(j)).y - ((PointF64)list.get(i)).y) + ((PointF64)list.get(i)).x)
                    if(!flag1)
                        flag = true;
                    else
                        flag = false;

                j = i;
                i++;
                flag1 = flag;
        }
        return flag1;
    }

    public static boolean ptInPolygon(GeoPoint geopoint, List list)
    {
        boolean flag1 = false;
        int j = list.size() - 1;
        int i = 0;
        while(i < list.size())
        {
            boolean flag;
            if(((GeoPoint)list.get(i)).latitude < geopoint.latitude || geopoint.latitude <= ((GeoPoint)list.get(j)).latitude)
            {
                flag = flag1;
                if(((GeoPoint)list.get(j)).latitude < geopoint.latitude || geopoint.latitude <= ((GeoPoint)list.get(i)).latitude) {
                    j = i;
                    i++;
                    flag1 = flag;
                    continue;
                }
            }
            flag = flag1;
            if(geopoint.longitude < ((((GeoPoint)list.get(j)).longitude - ((GeoPoint)list.get(i)).longitude) * (geopoint.latitude - ((GeoPoint)list.get(i)).latitude)) / (((GeoPoint)list.get(j)).latitude - ((GeoPoint)list.get(i)).latitude) + ((GeoPoint)list.get(i)).longitude)
                if(!flag1)
                    flag = true;
                else
                    flag = false;

            j = i;
            i++;
            flag1 = flag;
        }
        return flag1;
    }

    public static String sizeToString(double d)
    {
        if(d > 1048576D)
        {
            d /= 1048576D;
            return String.format(Locale.ENGLISH, "%.2f GB", new Object[] {
                Double.valueOf(d)
            });
        }
        if(d > 1024D)
        {
            d /= 1024D;
            return String.format(Locale.ENGLISH, "%.2f MB", new Object[] {
                Double.valueOf(d)
            });
        } else
        {
            return String.format(Locale.ENGLISH, "%.2f KB", new Object[] {
                Double.valueOf(d)
            });
        }
    }

    public static String timeToString(double d)
    {
        if(d < 1.0D)
            return String.format(Locale.ENGLISH, "%.0f sec", new Object[] {
                Double.valueOf(60D * d)
            });
        if(d < 60D)
            return String.format(Locale.ENGLISH, "%.0f min", new Object[] {
                Double.valueOf(d)
            });
        if(d < 1440D)
            return String.format(Locale.ENGLISH, "%.1f hr", new Object[] {
                Double.valueOf(d / 60D)
            });
        else
            return String.format(Locale.ENGLISH, "%.1f day", new Object[] {
                Double.valueOf(d / 1440D)
            });
    }

    public static Random randomEngine = new Random();

}
