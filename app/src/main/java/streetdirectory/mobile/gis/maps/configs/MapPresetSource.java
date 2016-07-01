

package streetdirectory.mobile.gis.maps.configs;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.InputStream;
import java.util.Random;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.core.storage.InternalStorage;

// Referenced classes of package streetdirectory.mobile.gis.maps.configs:
//            MapfileConfig

public class MapPresetSource
{

    public MapPresetSource()
    {
        isNew = false;
    }



    public static MapPresetSource create(Attributes attributes, boolean flag)
    {
        MapPresetSource mappresetsource = new MapPresetSource();
        mappresetsource.name = attributes.getValue("name");
        System.out.println("MapPresetSource.create."+mappresetsource.connectionString);
        mappresetsource.connectionString = attributes.getValue("connection_string");

        if(mappresetsource.connectionString == null) {
            System.out.println("MapPresetSource." + mappresetsource.connectionString +":"+attributes.getLength());
        }

        mappresetsource.offlineConnectionString = attributes.getValue("off_connection_string");
        mappresetsource.fullConnectionString = (new StringBuilder()).append(mappresetsource.connectionString).append("/config.xml").toString();
        mappresetsource.connectionTypeString = attributes.getValue("connection_type");
        File file = new File(InternalStorage.getStorageDirectory(), (new StringBuilder()).append("configs/").append(mappresetsource.name).append("/config.xml").toString());
        MapfileConfig mapfileConfig;
        flag = false;
        if(!flag)
        {
            //mapfileConfig = MapfileConfig.createOffline(file);
            mapfileConfig = MapfileConfig.createFromAssets();
            if(attributes != null)
                mappresetsource.config = mapfileConfig;
        } else
        {
            mappresetsource.isNew = true;
            mapfileConfig = MapfileConfig.createOnline((new StringBuilder()).append(mappresetsource.fullConnectionString).append("?").append(MathTools.randomEngine.nextInt()).toString(), file);
            if(attributes != null)
            {
                mappresetsource.config = mapfileConfig;
                return mappresetsource;
            }
        }
        return mappresetsource;
    }

    public static MapPresetSource create(XMLReader xmlreader)
    {
        return null;
    }

    public static final String REG_KEY_PREFIX = "SdMobile_Config_";
    public MapfileConfig config;
    public String connectionString;
    public String connectionTypeString;
    public String fullConnectionString;
    public boolean isNew;
    public String name;
    public String offlineConnectionString;
}
