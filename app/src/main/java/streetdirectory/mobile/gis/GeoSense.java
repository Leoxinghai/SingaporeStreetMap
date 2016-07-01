// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.gis;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.storage.InternalStorage;

// Referenced classes of package streetdirectory.mobile.gis:
//            SdArea, MapZone, GeoPoint

public class GeoSense
{

    public GeoSense()
    {
    }

    public static void addArea(SdArea sdarea)
    {
        areaList.add(sdarea);
    }

    public static SdArea getArea(double d, double d1)
    {
        Iterator iterator;
        SdArea sdarea;
        if(areaList.size() == 0)
            readData();
        iterator = areaList.iterator();
        for(;iterator.hasNext();) {
			Iterator iterator1;
			sdarea = (SdArea)iterator.next();
			iterator1 = sdarea.mapZones.iterator();
			for(;iterator1.hasNext();) {
				boolean flag;
				MapZone mapzone = (MapZone)iterator1.next();
				flag = MathTools.ptInPolygon(new GeoPoint(d, d1), mapzone.vertices);
				if(flag)
					return sdarea;
			}
		}

        sdarea = world;
        return sdarea;
    }

    public static SdArea getArea(String s)
    {
        for(Iterator iterator = areaList.iterator(); iterator.hasNext();)
        {
            SdArea sdarea = (SdArea)iterator.next();
            if(sdarea.apiAreaId.equals(s))
                return sdarea;
        }

        return world;
    }

    public static ArrayList getAreas()
    {
        return areaList;
    }

    public static void readData()
    {
        readFromData("configs/countries.txt");
    }

    public static void readFromData(String s)
    {
        File file = InternalStorage.getStorageFile(s);
        if(!file.exists())
        	return;

        Object obj1;
        Object obj2;
        Object obj3;
        Object obj4;
        Object obj5;
        Object obj6;
        Object obj7;
        Object obj8;
        obj3 = null;
        obj8 = null;
        obj5 = null;
        obj7 = null;
        obj1 = null;
        obj2 = null;
        obj6 = null;
        obj4 = null;
        Object obj;
        int i = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(((java.io.Reader) (inputStreamReader)));
            String temp[] = null;
            for (; ; ) {
                obj2 = ((BufferedReader) (bufferedReader)).readLine();
                if (obj2 == null)
                    break;

                temp = ((String) (obj2)).split("\t");
                SdArea sdArea = new SdArea();
                sdArea.apiAreaId = temp[1].toLowerCase();
                sdArea.areaName = temp[0];
                sdArea.e164Code = temp[2];
                ((SdArea) (sdArea)).addFeatures(temp[3].replace("\"", ""));
                MapZone mapZone = new MapZone();
                mapZone.vertices = new ArrayList();
                i = 4;
            }
            String temp2[];
            for(;i < temp.length;) {
                temp2 = temp[i].replace("\"", "").split(",");
                double d;
                double d1;
                Exception exception;
                Exception exception1;
                try
                {
                    d = Double.parseDouble(temp2[0].trim());
                }
                catch(Exception exception3)
                {
                    d = 0.0D;
                }
                try
                {
                    d1 = Double.parseDouble(temp2[1].trim());
                }
                catch(Exception exception2)
                {
                    d1 = 0.0D;
                }
                if(Math.floor(d) != 0.0D && Math.floor(d1) != 0.0D)
                    ((MapZone) (obj4)).vertices.add(new GeoPoint(d, d1));

                i++;
            }

            ((SdArea) (obj3)).mapZones.add(obj4);
            addArea(((SdArea) (obj3)));
            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void removeArea(int i)
    {
        if(areaList.size() > i)
            areaList.remove(i);
    }

    public static void removeArea(SdArea sdarea)
    {
        if(areaList.contains(sdarea))
            areaList.remove(sdarea);
    }

    private static ArrayList areaList = new ArrayList();
    public static SdArea world = new SdArea("wr", "World");

}
