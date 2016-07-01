

package streetdirectory.mobile.gis.maps.configs;

import java.util.*;
import org.xml.sax.Attributes;
import streetdirectory.mobile.gis.*;

// Referenced classes of package streetdirectory.mobile.gis.maps.configs:
//            MapPresetLevelMap, MapfileScale, MapPresetSource, MapfileConfig

public class MapPresetLevel
{

    public MapPresetLevel()
    {
        maps = new ArrayList();
    }

    public static MapPresetLevel create(Attributes attributes)
    {
        MapPresetLevel mappresetlevel = new MapPresetLevel();
        if(attributes.getIndex("name") > -1)
            mappresetlevel.name = attributes.getValue("name");
        return mappresetlevel;
    }

    public MapPresetLevelMap GetLevelMap(GeoPoint geopoint)
    {
        SdArea sdarea = GeoSense.getArea(geopoint.longitude, geopoint.latitude);
        for(Iterator iterator = maps.iterator(); iterator.hasNext();)
        {
            MapPresetLevelMap mappresetlevelmap = (MapPresetLevelMap)iterator.next();
            if(mappresetlevelmap.scale.minLong < geopoint.longitude && mappresetlevelmap.scale.minLat < geopoint.latitude && mappresetlevelmap.scale.maxLong > geopoint.longitude && mappresetlevelmap.scale.maxLat > geopoint.latitude)
            {
                while(mappresetlevelmap.mapPresetSource.config.mapCode.compareToIgnoreCase("wrd") == 0 || sdarea != null && sdarea.apiAreaId.compareToIgnoreCase("sg") == 0 && mappresetlevelmap.mapPresetSource.config.mapCode.compareToIgnoreCase("sg") == 0 || sdarea != null && sdarea.apiAreaId.compareToIgnoreCase("my") == 0 && mappresetlevelmap.mapPresetSource.config.mapCode.compareToIgnoreCase("kv") == 0)
                    return mappresetlevelmap;
                if(sdarea != null && sdarea.apiAreaId.compareToIgnoreCase("id") == 0 && mappresetlevelmap.mapPresetSource.config.mapCode.compareToIgnoreCase("jkt") == 0)
                    return mappresetlevelmap;
            }
        }

        if(maps.size() > 0)
            return (MapPresetLevelMap)maps.get(0);
        else
            return null;
    }

    public double getMaxScale()
    {
        double d = 1.0D;
        Iterator iterator = maps.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            MapPresetLevelMap mappresetlevelmap = (MapPresetLevelMap)iterator.next();
            if(mappresetlevelmap.scale != null && mappresetlevelmap.finalScale > d)
                d = mappresetlevelmap.finalScale;
        } while(true);
        return d;
    }

    public double getMinScale()
    {
        double d = 1.0D;
        Iterator iterator = maps.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            MapPresetLevelMap mappresetlevelmap = (MapPresetLevelMap)iterator.next();
            if(mappresetlevelmap.scale != null && mappresetlevelmap.finalScale < d)
                d = mappresetlevelmap.finalScale;
        } while(true);
        return d;
    }

    public List maps;
    public String name;
    public int ordinal;
}
