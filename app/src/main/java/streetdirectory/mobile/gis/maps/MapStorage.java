

package streetdirectory.mobile.gis.maps;

import java.io.File;

public class MapStorage
{

    public MapStorage()
    {
    }

    public static final String CacheFolderName = "cachedmaps";
    public static final String ConfigFolderName = "configs";
    public static final String DateTimeFormat = "yyyyMMddHHmmss";
    public static final String DefaultOnlinePresetUrl = "http://www.streetdirectory.com/map/preset.xml";
    public static final String PresetDateKey = "preset_date";
    public static final String PresetName = "preset.xml";
    public static final String RootUrl = "";
    public static File cacheDir;
}
