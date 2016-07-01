

package streetdirectory.mobile.sd;

import java.util.ArrayList;
import streetdirectory.mobile.gis.maps.configs.MapPreset;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;

public class SDBlackboard
{
    public static abstract class LoadMapPresetCompleteListener
    {

        protected abstract void onComplete(MapPresetCollection mappresetcollection);

        public LoadMapPresetCompleteListener()
        {
        }
    }


    public SDBlackboard()
    {
    }

    static class SUBCLASS1 implements streetdirectory.mobile.gis.maps.configs.MapPresetCollection.OnLoadPresetCompleteListener {

    public void onLoadPresetComplete(MapPresetCollection mappresetcollection)
    {
        SDBlackboard.preset = mappresetcollection.get(0);
        MapPresetCollection.loadOnlineInBackground(new streetdirectory.mobile.gis.maps.configs.MapPresetCollection.OnLoadPresetCompleteListener() {

            public void onLoadPresetComplete(MapPresetCollection mappresetcollection)
            {
                SDBlackboard.preset = mappresetcollection.get(0);
            }

            public void onLoadPresetFailed()
            {
            }

        });
        listener.onComplete(mappresetcollection);
    }

    public void onLoadPresetFailed()
    {
    }

    SUBCLASS1(LoadMapPresetCompleteListener _listener) {
        listener = _listener;
    }
    LoadMapPresetCompleteListener listener;

}

    public static void reloadMapPreset(LoadMapPresetCompleteListener loadmappresetcompletelistener)
    {
        MapPresetCollection.loadOfflineInBackground(new SUBCLASS1(loadmappresetcompletelistener));
    }

    public static ArrayList countryList = new ArrayList();
    public static String currentCountryCode = "sg";
    public static double currentLatitude = 1.2894823898199825D;
    public static double currentLongitude = 103.80729837373376D;
    public static MapPreset preset;

}
