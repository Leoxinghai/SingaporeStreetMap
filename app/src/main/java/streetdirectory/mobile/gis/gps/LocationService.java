

package streetdirectory.mobile.gis.gps;

import android.content.Context;
import android.location.*;
import android.os.Bundle;
import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.gis.GeoPoint;

public class LocationService
{
    public static abstract class PositionChangedListener
    {

        public abstract void onPositionChanged(LocationService locationservice, GeoPoint geopoint);

        public PositionChangedListener()
        {
        }
    }


    public LocationService()
    {
        lastValidLocation = null;
        locationManager = null;
        isEnable = false;
        isInitialize = false;
        locationListeners = new ArrayList();
    }

    public static LocationService getInstance()
    {
        if(staticInstance == null)
            staticInstance = new LocationService();
        return staticInstance;
    }

    private boolean isSameProvider(String s, String s1)
    {
        if(s == null)
            return s1 == null;
        else
            return s.equals(s1);
    }

    public void disable()
    {
        LocationListener locationlistener;
        try {
            for (Iterator iterator = locationListeners.iterator(); iterator.hasNext(); ) {
                locationlistener = (LocationListener) iterator.next();
                locationManager.removeUpdates(locationlistener);
            }

            locationListeners.clear();
            isEnable = false;
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public void enable()
    {
        disable();
        String string;
        LocationListener locationlistener;
        try {
            for (Iterator iterator = locationManager.getProviders(true).iterator(); iterator.hasNext(); locationManager.requestLocationUpdates(string, 0L, 0.0F, locationlistener)) {
                string = (String) iterator.next();
                locationlistener = new LocationListener() {

                    public void onLocationChanged(Location location) {
                        if (isBetterLocation(location, lastValidLocation) && isEnable) {
                            lastValidLocation = location;
                            SDStoryStats.longitude = location.getLongitude();
                            SDStoryStats.latitude = location.getLatitude();
                            SDStoryStats.altitude = location.getAltitude();
                            SDStoryStats.speed = location.getSpeed();
                            SDStoryStats.accuracy = location.getAccuracy();
                            SDStoryStats.bearing = location.getBearing();
                            SDStoryStats.locationProvider = location.getProvider();
                            long l = System.currentTimeMillis();
                            if (l - lastSendTime > 10000L) {
                                SDLogger.debug((new StringBuilder()).append("TRACK provider:").append(provider).append(" Pos:").append(location.getLongitude()).append(",").append(location.getLatitude()).toString());
                                SDStory.post("/android/story", SDStory.createDefaultParams());
                                lastSendTime = l;
                            }
                            onPositionChanged(LocationService.this, new GeoPoint(location.getLongitude(), location.getLatitude()));
                        }
                    }

                    public void onProviderDisabled(String s) {
                    }

                    public void onProviderEnabled(String s) {
                    }

                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }

                    long lastSendTime;
                    String provider;

                    {
                        lastSendTime = 0L;
                    }
                };
                locationListeners.add(locationlistener);
            }

        } catch(SecurityException ex) {

        }

        isEnable = true;
    }

    public LocationManager getLocationManager()
    {
        return locationManager;
    }

    public void initialize(Context context)
    {
        try
        {
            if(locationManager == null)
                locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "LocationService initialize");
        }
        isInitialize = true;
    }

    protected boolean isBetterLocation(Location location, Location location1)
    {
        if(location1 == null)
            return true;
        long l = location.getTime() - location1.getTime();
        boolean flag;
        boolean flag1;
        boolean flag2;
        if(l > 0x1d4c0L)
            flag1 = true;
        else
            flag1 = false;
        if(l < 0xfffffffffffe2b40L)
            flag2 = true;
        else
            flag2 = false;
        if(l > 0L)
            flag = true;
        else
            flag = false;
        if(flag1)
            return true;
        if(flag2)
            return false;
        int i = (int)(location.getAccuracy() - location1.getAccuracy());
        boolean flag3;
        boolean flag4;
        if(i > 0)
            flag1 = true;
        else
            flag1 = false;
        if(i < 0)
            flag2 = true;
        else
            flag2 = false;
        if(i > 200)
            flag3 = true;
        else
            flag3 = false;
        flag4 = isSameProvider(location.getProvider(), location1.getProvider());
        if(flag2)
            return true;
        if(flag && !flag1)
            return true;
        return flag && !flag3 && flag4;
    }

    public void onPositionChanged(LocationService locationservice, GeoPoint geopoint)
    {
        for(Iterator iterator = listeners.iterator(); iterator.hasNext(); ((PositionChangedListener)iterator.next()).onPositionChanged(locationservice, geopoint));
    }

    private static final int TWO_MINUTES = 0x1d4c0;
    public static ArrayList listeners = new ArrayList();
    public static LocationService staticInstance;
    public boolean isEnable;
    boolean isInitialize;
    public Location lastValidLocation;
    ArrayList locationListeners;
    LocationManager locationManager;

}
