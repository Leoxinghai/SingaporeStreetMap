

package streetdirectory.mobile.gis.maps.configs;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;

public class MapPreset
{

    public MapPreset()
    {
        levels = new ArrayList();
    }

    public static MapPreset create(Attributes attributes)
    {
        MapPreset mappreset = new MapPreset();
        if(attributes.getIndex("name") > -1)
            mappreset.name = attributes.getValue("name");
        return mappreset;
    }

    public List levels;
    public String name;
}
