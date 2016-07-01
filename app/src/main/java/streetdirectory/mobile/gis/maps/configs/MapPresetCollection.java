
package streetdirectory.mobile.gis.maps.configs;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.SDAsyncTask;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.service.HttpConnection;
import streetdirectory.mobile.core.service.HttpConnectionResult;
import streetdirectory.mobile.core.storage.*;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;


public class MapPresetCollection
{
    private class MapPresetXmlHandler extends DefaultHandler
    {

        public void characters(char ac[], int i, int j)
            throws SAXException
        {
            buffer.append(ac, i, j);
        }

        public void endElement(String s, String s1, String s2)
            throws SAXException
        {
            if(s1.equals("preset"))
            {
                currentPreset = null;
            } else
            {
                if(s1.equals("level"))
                {
                    currentLevel = null;
                    return;
                }
                if(s1.equals("map"))
                {
                    currentLevelMap = null;
                    return;
                }
            }
        }

        public void startElement(String s, String s1, String s2, Attributes attributes)
            throws SAXException
        {
            buffer.setLength(0);
            if(s1.equals("map_sources"))
	            return;

            if(s1.equals("map_source")) {
                MapPresetSource mapPresetSource = MapPresetSource.create(attributes, isOnline);
				if(((MapPresetSource) (mapPresetSource)).isNew)
					isNew = true;
				if(mapPresetSource != null)
				{
					mapPresetSources.add(mapPresetSource);
					return;
				}
			} else if(s1.equals("preset")) {
				currentPreset = MapPreset.create(attributes);
				if(currentPreset != null)
				{
					presets.add(currentPreset);
					return;
				}
            } else if(s1.equals("level")) {
				currentLevel = MapPresetLevel.create(attributes);
				currentLevel.ordinal = currentPreset.levels.size();
				if(currentPreset != null)
				{
					currentPreset.levels.add(currentLevel);
					return;
				}
			} else if(s1.equals("map")) {
				currentLevelMap = MapPresetLevelMap.create(attributes, mapPresetSources);
				if(currentLevelMap != null && currentLevel != null)
				{
					currentLevel.maps.add(currentLevelMap);
					return;
				}
			}
        }

        private StringBuffer buffer;
        private MapPresetLevel currentLevel;
        private MapPresetLevelMap currentLevelMap;
        private MapPreset currentPreset;
        private boolean isOnline;

        public MapPresetXmlHandler(boolean flag)
        {
            super();
            buffer = new StringBuffer();
            currentPreset = null;
            currentLevel = null;
            currentLevelMap = null;
            isOnline = flag;
        }
    }

    public static interface OnLoadPresetCompleteListener
    {

        public abstract void onLoadPresetComplete(MapPresetCollection mappresetcollection);

        public abstract void onLoadPresetFailed();
    }


    public MapPresetCollection()
    {
        isNew = false;
        mapPresetSources = new ArrayList();
        presets = new ArrayList();
    }

    public static MapPresetCollection createFromAsset(Context context)
    {
        AssetManager assetManager = context.getAssets();
        MapPresetCollection mappresetcollection;
        try {
            InputStream inputStream = assetManager.open("internal/configs/preset.xml");
            mappresetcollection = new MapPresetCollection();
            mappresetcollection.parsePresetXml(inputStream, false);
            return mappresetcollection;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static MapPresetCollection createOffline()
    {
        java.io.BufferedInputStream bufferedinputstream = StorageUtil.load(new File(InternalStorage.getStorageDirectory(), "configs/preset.xml"));
        if(bufferedinputstream != null)
        {
            MapPresetCollection mappresetcollection = new MapPresetCollection();
            if(!mappresetcollection.parsePresetXml(bufferedinputstream, false))
                return null;
            try
            {
                bufferedinputstream.close();
            }
            catch(Exception exception)
            {
                return mappresetcollection;
            }
            return mappresetcollection;
        } else
        {
            return null;
        }
    }

    public static MapPresetCollection createOnline(final String url)
    {
        File file = new File(InternalStorage.getStorageDirectory(), "configs/preset.xml");
        HttpConnection httpConnection = new HttpConnection(new SDHttpServiceInput(url) {

            public String getURL()
            {
                return url;
            }

        });

        HttpConnectionResult httpconnectionresult = httpConnection.execute();
        if(httpconnectionresult.inputStream != null)
        {
            Object obj = StreamUtil.inputStreamToByteArrayOutputStream(httpconnectionresult.inputStream);
            httpconnectionresult.close();
            httpConnection.close();
            byte abyte0[] = ((ByteArrayOutputStream) (obj)).toByteArray();
            MapPresetCollection mapPresetCollection = new MapPresetCollection();
            mapPresetCollection.isNew = true;
            obj = new ByteArrayInputStream(abyte0);
            if(!mapPresetCollection.parsePresetXml(((InputStream) (obj)), true))
                return null;
            StorageUtil.save(file, abyte0);
            try
            {
                ((InputStream) (obj)).close();
            }
            catch(Exception exception)
            {
                return mapPresetCollection;
            }
            return mapPresetCollection;
        } else
        {
            return null;
        }
    }

    public static void loadOfflineInBackground(final OnLoadPresetCompleteListener onloadpresetcompletelistener)
    {
        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected MapPresetCollection doInBackground(Void avoid[])
            {
                try
                {
                    MapPresetCollection mapPresetCollection = MapPresetCollection.createOffline();
                    return mapPresetCollection;
                }
                catch(Exception ex)
                {
                    SDLogger.printStackTrace(ex, "MapPresetCollection loadOfflineInBackground");
                    return null;
                }
            }

            protected void onPostExecute(Object obj)
            {
                onPostExecute((MapPresetCollection)obj);
            }

            protected void onPostExecute(MapPresetCollection mappresetcollection)
            {
                if(mappresetcollection != null)
                {
                    onloadpresetcompletelistener.onLoadPresetComplete(mappresetcollection);
                    return;
                } else
                {
                    onloadpresetcompletelistener.onLoadPresetFailed();
                    return;
                }
            }

        }).executeTask(new Void[0]);
    }

    public static void loadOnlineInBackground(final OnLoadPresetCompleteListener onloadpresetcompletelistener)
    {
        (new SDAsyncTask() {
//        (new AsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected MapPresetCollection doInBackground(Void avoid[])
            {
                try
                {
                    //MapPresetCollection mapPresetCollection = MapPresetCollection.createOnline(URLFactory.createURLPreset());
                    MapPresetCollection mapPresetCollection = MapPresetCollection.createFromAsset(SDApplication.getAppContext());

                    return mapPresetCollection;
                }
                catch(Exception ex)
                {
                    SDLogger.printStackTrace(ex, "MapPresetCollection loadOfflineInBackground");
                    return null;
                }
            }

            protected void onPostExecute(Object obj)
            {

                onPostExecute((MapPresetCollection)obj);
            }

            protected void onPostExecute(MapPresetCollection mappresetcollection)
            {
                if(mappresetcollection != null)
                {
                    onloadpresetcompletelistener.onLoadPresetComplete(mappresetcollection);
                    return;
                } else
                {
                    onloadpresetcompletelistener.onLoadPresetFailed();
                    return;
                }
            }
        }).executeTask(new Void[0]);

        //}).executeTask(new Void[0]);
    }

    private boolean parsePresetXml(InputStream inputstream, boolean flag)
    {
        try
        {
            presets.clear();
            mapPresetSources.clear();
            XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlreader.setContentHandler(new MapPresetXmlHandler(flag));
            xmlreader.parse(new InputSource(inputstream));
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "MapPresetCollection parsePresetXml");
            return false;
        }
        return true;
    }

    public MapPreset get(int i)
    {
        if(presets != null && presets.size() > i)
            return (MapPreset)presets.get(i);
        else
            return null;
    }

    public List getMapSources()
    {
        return mapPresetSources;
    }

    public List getPresets()
    {
        return presets;
    }

    public static final String REG_KEY = "SdMobile_Config_preset";
    public boolean isNew;
    List mapPresetSources;
    List presets;
}
