// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.gis.maps.configs;

import android.content.res.AssetManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.service.HttpConnection;
import streetdirectory.mobile.core.service.HttpConnectionResult;
import streetdirectory.mobile.core.storage.StorageUtil;
import streetdirectory.mobile.core.storage.StreamUtil;
import streetdirectory.mobile.gis.ProjectionCoordinateSystem;
import streetdirectory.mobile.service.SDHttpServiceInput;

// Referenced classes of package streetdirectory.mobile.gis.maps.configs:
//            MapfileScale

public class MapfileConfig
{
    private class MapfileConfigXmlHandler extends DefaultHandler
    {

        public void characters(char ac[], int i, int j)
            throws SAXException
        {
            try
            {
                buffer.append(ac, i, j);
                return;
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                return;
            }
        }

        public void endElement(String s, String s1, String s2)
            throws SAXException {
            boolean flag;
            try {
                if (s1.equals("TITLE")) {
                    title = buffer.toString();
                    return;
                } else if (s1.equals("PROJECTION")) {
                    projectionString = buffer.toString();
                    projection = ProjectionCoordinateSystem.CreateInstance(projectionString);
                    return;
                } else if (s1.equals("YEAR")) {
                    year = Integer.parseInt(buffer.toString());
                    return;
                } else if (s1.equals("MAPCODE")) {
                    //mapCode = buffer.toString();
                    mapCode = "sg";
                    System.out.println("MapFileConfig.MapCode."+s1+":"+mapCode);
                    return;
                }
                year = 1990;
                return;
            }
            catch (Exception ex) {
                return;
            }

        }

        public void startElement(String s, String s1, String s2, Attributes attributes)
            throws SAXException
        {
            try
            {
                buffer.setLength(0);
                MapfileScale mapfileScale = null;

                if(s1.equals("SCALE"))
                    mapfileScale = MapfileScale.create(attributes);

                System.out.println("MapfileConfig.SCALE." + s +":"+mapfileScale.levelCode+":"+mapfileScale.levelName);
                if(s != null) {
                    mapfileScale.build(mapFileConfig);
                    System.out.println("MapfileConfig.SCALE2." + s +":"+mapfileScale.levelCode+":"+mapfileScale.levelName);
                    scales.add(mapfileScale);
                }
            }
            catch(Exception ex)
            {
                return;
            }
        }

        private StringBuffer buffer;
        private MapfileConfig mapFileConfig;

        public MapfileConfigXmlHandler(MapfileConfig mapfileconfig1)
        {
            super();
            mapFileConfig = null;
            buffer = new StringBuffer();
            mapFileConfig = mapfileconfig1;
        }
    }


    public MapfileConfig()
    {
        scales = new ArrayList();
    }

    public static MapfileConfig createOffline(File file)
    {
        MapfileConfig mapfileconfig = new MapfileConfig();
        mapfileconfig.loadOffline(file);
        return mapfileconfig;
    }

    public static MapfileConfig createOnline(String s, File file)
    {
        MapfileConfig mapfileconfig = new MapfileConfig();
        mapfileconfig.loadOnline(s, file);
        return mapfileconfig;
    }

    public static MapfileConfig createFromAssets() {
        MapfileConfig mapfileconfig = new MapfileConfig();
        AssetManager assetManager = SDApplication.getAppContext().getAssets();
        MapPresetCollection mappresetcollection;
        try {
            InputStream inputStream = assetManager.open("internal/configs/config.xml");
            mapfileconfig.parseConfigXml(inputStream);
            return mapfileconfig;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private void parseConfigXml(InputStream inputstream)
    {
        try
        {
            XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlreader.setContentHandler(new MapfileConfigXmlHandler(this));
            xmlreader.parse(new InputSource(inputstream));
            build();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "MapfileConfig parseConfigXml");
        }
    }

    public void build()
    {
    }

    public void loadOffline(File file)
    {
        BufferedInputStream bufferedInputStream = StorageUtil.load(file);
        if(bufferedInputStream == null)
            return;
        try {
            parseConfigXml(bufferedInputStream);
            bufferedInputStream.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return;
    }

    public void loadOnline(final String url, File file)
    {
        HttpConnection httpConnection = new HttpConnection(new SDHttpServiceInput() {

            public String getURL()
            {
                return url;
            }

        });
        HttpConnectionResult httpconnectionresult = httpConnection.execute();
        if(httpconnectionresult.inputStream == null)
            return;
        ByteArrayOutputStream bytearrayoutputstream = StreamUtil.inputStreamToByteArrayOutputStream(httpconnectionresult.inputStream);
        httpconnectionresult.close();
        httpConnection.close();
        byte result[] = bytearrayoutputstream.toByteArray();
        StorageUtil.save(file, result);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(result);
        parseConfigXml(byteArrayInputStream);
        httpConnection.close();
        return;
    }

    public String mapCode;
    public ProjectionCoordinateSystem projection;
    public String projectionString;
    public List scales;
    public String title;
    public int year;
}
