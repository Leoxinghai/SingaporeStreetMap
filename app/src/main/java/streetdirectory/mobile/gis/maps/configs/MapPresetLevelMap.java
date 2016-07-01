// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.gis.maps.configs;

import java.util.*;
import org.xml.sax.Attributes;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.SDLogger;

// Referenced classes of package streetdirectory.mobile.gis.maps.configs:
//            MapPresetSource, MapfileConfig, MapfileScale

public class MapPresetLevelMap
{
    public class MapTileInfo
    {

        public String id;
        public String mapDir;

        public MapTileInfo()
        {
            super();
            mapDir = "";
            id = "";
        }
    }


    public MapPresetLevelMap()
    {
        scaleOverride = 1.0D;
        finalScale = 1.0D;
    }

    public static MapPresetLevelMap create(Attributes attributes, List list)
    {
        MapPresetLevelMap mappresetlevelmap = new MapPresetLevelMap();
        boolean flag;

        if(attributes.getIndex("name") > -1)
            mappresetlevelmap.name = attributes.getValue("name");
        if(attributes.getIndex("connection_string") > -1)
            mappresetlevelmap.connectionString = attributes.getValue("connection_string");
        if(attributes.getIndex("connection_type") > -1)
            mappresetlevelmap.connectionTypeString = attributes.getValue("connection_type");
        if(attributes.getIndex("scale_override") > -1)
            mappresetlevelmap.scaleOverride = Double.parseDouble(attributes.getValue("scale_override"));
        flag = mappresetlevelmap.build(list);
        System.out.println("MapPresetLevelMap.create." + flag+":"+mappresetlevelmap.connectionString);
        if(!flag)
            return null;
        else
            return mappresetlevelmap;

    }

    private MapPresetSource getSource(String s, List list)
    {
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            MapPresetSource mappresetsource = (MapPresetSource)iterator.next();
            if(mappresetsource.name.equalsIgnoreCase(s))
                return mappresetsource;
        }

        return null;
    }

    public boolean build(List list)
    {
        String s = "";
        int j = 0;
        HashMap hashmap;
        String as[];
        Iterator iterator;
        String as1[];
        String s1;
        Exception exception;
        int i;
        int k;
        boolean flag;
        try
        {
            hashmap = new HashMap();
            System.out.println("MapPresetLevelMap.connectionString." + connectionString);
            as = connectionString.split(";");
        }
        catch(Exception ex)
        {
            return false;
        }
        if(as != null) {
			k = as.length;
			i = 0;

			for(;i < k;) {
				as1 = as[i].split("=");
				if(as1 != null) {
					if(as1.length > 1)
						hashmap.put(as1[0], as1[1]);
				}

				i++;
			}
	    }

        iterator = hashmap.keySet().iterator();
        i = j;
		for(;iterator.hasNext();) {
			s1 = (String)iterator.next();
            System.out.println("MapPresetLevelMap.build.s1."+s1);
			if(s1.equals("name")) {
                s = (String) hashmap.get(s1);
            } else if(s1.equals("level")) {
                j = Integer.parseInt((String)hashmap.get(s1));
                i = j;
			}
        }

        mapPresetSource = getSource(s, list);

        //if(i>=4)
        //  i = 3;

        System.out.println("MapPresetLevelMap.build." + mapPresetSource +":"+ mapPresetSource.config.scales.size() +":"+(i-1));

        if((mapPresetSource != null) && mapPresetSource.config.scales.size() > i - 1) {
			scale = (MapfileScale)mapPresetSource.config.scales.get(i - 1);
			finalScale = scale.scalePixelPerMeter / scaleOverride;
            System.out.println("MapPresetLevelMap.build.finalScale." + finalScale +":"+i);
			return true;
		} else {
            System.out.println("Exception.1");
        }
        return false;
    }

    public String getTileUri(int i, int j, MapTileInfo maptileinfo)
    {
        String s1 = (new StringBuilder()).append(mapPresetSource.connectionString).append("/").append(scale.folder).append("/").toString();
        String s;

        mapPresetSource.config.mapCode = "sg";
        if(scale.isUseGrid)
        {
            int k = (int)Math.ceil(((double)i * (double)scale.gridRow) / (double)scale.gridTotalHeight);
            int l = (int)Math.ceil(((double)j * (double)scale.gridCol) / (double)scale.gridTotalWidth);
            s = (new StringBuilder()).append(k).append("_").append(l).toString();
            s1 = (new StringBuilder()).append(s1).append(s).append("/").toString();
            //s = (new StringBuilder()).append(mapPresetSource.config.mapCode).append("_").append(scale.levelCode).append("_").append(s).append("_").append(String.valueOf(i)).append("_").append(String.valueOf(j)).append(scale.fileExtension).toString();
            s = (new StringBuilder()).append("sg").append("_").append(scale.levelCode).append("_").append(s).append("_").append(String.valueOf(i)).append("_").append(String.valueOf(j)).append(scale.fileExtension).toString();
        } else
        {
            //s = (new StringBuilder()).append(mapPresetSource.config.mapCode).append(String.valueOf(i)).append("_").append(String.valueOf(j)).append("_").append(scale.levelCode).append(scale.fileExtension).toString();
            s = (new StringBuilder()).append("sg").append(String.valueOf(i)).append("_").append(String.valueOf(j)).append("_").append(scale.levelCode).append(scale.fileExtension).toString();
        }
        if(maptileinfo != null)
        {
            maptileinfo.mapDir = s1;
            maptileinfo.id = s;
        }
        String rURL = (new StringBuilder()).append(s1).append(s).toString();
        System.out.println("MapPresetLevelMap.getTileUrl."+rURL);
        return rURL;
    }

    public String getTileUriGlobal(int i, int j, MapTileInfo maptileinfo)
    {
        int k = (int)(scale.topLeftPixel.x / (double)scale.width);
        return getTileUri((int)(scale.topLeftPixel.y / (double)scale.height) + i, j - k, maptileinfo);
    }

    public String connectionString;
    public String connectionTypeString;
    public double finalScale;
    public MapPresetSource mapPresetSource;
    public String name;
    public MapfileScale scale;
    public double scaleOverride;
}
