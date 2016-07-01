
package streetdirectory.mobile.gis.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.*;
import android.hardware.GeomagneticField;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import java.io.File;
import java.util.*;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.storage.AssetsUtil;
import streetdirectory.mobile.core.storage.InternalStorage;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.ProjectionCoordinateSystem;
import streetdirectory.mobile.gis.gps.GpsViewLayer;
import streetdirectory.mobile.gis.gps.LocationService;
import streetdirectory.mobile.gis.maps.configs.MapPreset;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;
import streetdirectory.mobile.gis.maps.configs.MapPresetLevel;
import streetdirectory.mobile.gis.maps.configs.MapPresetLevelMap;
import streetdirectory.mobile.gis.maps.configs.MapfileScale;
//import streetdirectory.mobile.R;
import com.xinghai.mycurve.R;


public class MapView extends RelativeLayout
{
    private class GoToAnimation extends Animation
    {

        protected void applyTransformation(float f, Transformation transformation)
        {
            double d1 = (toScale - fromScale) * (double)f;
            double d = d1;
            double d2;
            if(tensionScale > 0.0D)
            {
                ts = tensionScale - d1;
                if(half >= (double)f)
                    ts = ts * ((double)f / half);
                else
                    ts = ts * (2D - (double)f / half);
                d = d1 + ts;
            }
            SDLogger.debug((new StringBuilder()).append("goto #2 ts=").append(ts).append(", scale=").append(d).append(", time=").append(f).toString());
            d1 = (toX - fromX) * (double)f;
            d2 = (toY - fromY) * (double)f;
            if((double)f < 1.0D)
                if(ignoreScale)
                {
                    goTo(fromX + d1, fromY + d2, mapScale, false);
                    return;
                } else
                {
                    goTo(fromX + d1, fromY + d2, fromScale + d, false);
                    return;
                }
            if(ignoreScale)
            {
                goTo(fromX + d1, fromY + d2, mapScale);
                return;
            } else
            {
                goTo(fromX + d1, fromY + d2, toScale);
                return;
            }
        }

        public void calculateTension()
        {
            Object obj = getProjection();
            if(obj != null)
            {
                PointF64 pointf64 = ((ProjectionCoordinateSystem) (obj)).geoToPixel(fromX, fromY, fromScale);
                obj = ((ProjectionCoordinateSystem) (obj)).geoToPixel(toX, toY, fromScale);
                double d = Math.abs(((PointF64) (obj)).x - pointf64.x);
                double d1 = Math.abs(((PointF64) (obj)).y - pointf64.y);
                if(d > d1)
                    d /= getMeasuredWidth();
                else
                    d = d1 / (double)getMeasuredHeight();
                tensionScale = fromScale * d;
            }
        }

        public double fromScale;
        public double fromX;
        public double fromY;
        private double half;
        public boolean ignoreScale;
        public double tensionScale;
        public double toScale;
        public double toX;
        public double toY;
        double ts;

        private GoToAnimation()
        {
            super();
            ignoreScale = false;
            tensionScale = 50D;
            half = 0.5D;
            ts = 1.0D;
        }

    }

    public static interface OnCompassChangedListener
    {
        public abstract void onCompassChanged(boolean flag);
    }

    public static interface OnDragListener
    {

        public abstract void onEndDrag(MapView mapview);

        public abstract void onStartDrag(MapView mapview);
    }

    public static interface OnFinishUpdateMapListener
    {

        public abstract void onFinishUpdateMap(MapView mapview);
    }

    public static interface OnMapClickedListener
    {

        public abstract void onMapClicked(MapView mapview, GeoPoint geopoint, Point point);
    }

    private class SwipeAnimation extends Animation
    {

        protected void applyTransformation(float f, Transformation transformation)
        {
            float f1 = (toX - fromX) * f;
            f = (toY - fromY) * f;
            scrollMapBy(f1 - lastX, f - lastY, false);
            lastX = f1;
            lastY = f;
        }

        public float fromX;
        public float fromY;
        public float lastX;
        public float lastY;
        public float toX;
        public float toY;

        private SwipeAnimation()
        {
            super();
            lastX = 0.0F;
            lastY = 0.0F;
        }

    }

    private class ZoomAnimation extends Animation
    {

        protected void applyTransformation(float f, Transformation transformation)
        {
            double d = toScale;
            double d1 = fromScale;
            double d2 = f;
            if((double)f < 1.0D)
            {
                zoomMapTo(fromScale + (d - d1) * d2, false);
                return;
            } else
            {
                zoomMapTo(toScale);
                return;
            }
        }

        public double fromScale;
        public double toScale;

        private ZoomAnimation()
        {
            super();
        }

    }

    private class ZoomToPositionAnimation extends Animation
    {

        protected void applyTransformation(float f, Transformation transformation)
        {
            double d = toX;
            double d1 = fromX;
            double d2 = f;
            double d3 = toY;
            double d4 = fromY;
            double d5 = f;
            center.longitude = fromX + (d - d1) * d2;
            center.latitude = fromY + (d3 - d4) * d5;
            d = toScale;
            d1 = fromScale;
            d2 = f;
            if((double)f < 1.0D)
            {
                zoomMapTo(fromScale + (d - d1) * d2, false);
                return;
            } else
            {
                zoomMapTo(toScale);
                return;
            }
        }

        public double fromScale;
        public double fromX;
        public double fromY;
        public double toScale;
        public double toX;
        public double toY;

        private ZoomToPositionAnimation()
        {
            super();
        }

    }


    public MapView(Context context)
    {
        this(context, null);
    }

    public MapView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public MapView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        firstTimeGps = true;
        mapState = 0;
        bestZoomLevel = 0;
        lastLevelOrdinal = -1;
        maxScale = 1.0D;
        minScale = 100D;
        lastMapScale = -1D;
        lastTouchTime = -1L;
        gtAnim = null;
        swipeAnimation = new SwipeAnimation();
        zoomAnimation = new ZoomAnimation();
        goToAnimation = new GoToAnimation();
        zoomToPositionAnimation = new ZoomToPositionAnimation();
        isAnimating = false;
        isSwiping = false;
        isPanEnabled = true;
        mapScale = 72.153454545454537D;
        center = new GeoPoint(103.80729837373376D, 1.2894823898199825D);
        gpsLayer = null;
        lastDegree = 0.0F;
        isRotating = false;
        compassTimer = new Timer();
        mDeclanation = 0.0F;
        compassInterpolator = new LinearInterpolator();
        compassTime = System.currentTimeMillis();
        initLayout(context);
        initData(context);
        initEvent();
    }

    public static View createLocationWarningDialog(final Context context, boolean flag, boolean flag1)
    {
        boolean flag2 = false;
        final View view = View.inflate(context, R.layout.layout_location_warning, null);
        TextView textview2 = (TextView)view.findViewById(R.id.textViewNotifTitle);
        TextView textview = (TextView)view.findViewById(R.id.textViewNotifDesc);
        ImageButton imagebutton = (ImageButton)view.findViewById(R.id.buttonClose);
        final Button buttonGPSSwitch = (Button)view.findViewById(R.id.buttonGPSSwitch);
        CheckBox checkbox = (CheckBox)view.findViewById(R.id.checkBoxDontShow);
        TextView textview1 = (TextView)view.findViewById(R.id.textViewDontShow);
        String s;
        int i;
        if(flag1)
            s = "GPS is off.";
        else
            s = "Location Service is off.";
        textview2.setText(s);
        if(flag1)
            s = "Turn on for more accurate position?";
        else
            s = "Singapore Maps need access to your location. Please turn on Location Service.";
        textview.setText(s);
        if(flag1)
            i = 0;
        else
            i = 8;
        textview1.setVisibility(i);
        if(flag1)
            i = ((flag2) ? 1 : 0);
        else
            i = 8;
        checkbox.setVisibility(i);
        imagebutton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1)
            {
                ((ViewGroup)view.getParent()).removeView(view);
            }

        });
        buttonGPSSwitch.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1)
            {
                if(buttonGPSSwitch.getText().toString().equals("On"))
                    context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }

        });
        checkbox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundbutton, boolean flag3)
            {
                if(flag3)
                {
                    SharedPreferences.Editor editor = SDPreferences.getInstance().createEditor();
                    editor.putBoolean("gps_dont_show_message", true);
                    editor.commit();
                }
            }

        });

        view.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, MathTools.dpToPixel(64F, (Activity)context)));
        return view;
    }

    private void initData(Context context)
    {
        if(!isInEditMode())
        {
            locationService = LocationService.getInstance();
            LocationService.listeners.remove(locationServiceListener);
            locationServiceListener = new streetdirectory.mobile.gis.gps.LocationService.PositionChangedListener() {

                public void onPositionChanged(LocationService locationservice, GeoPoint geopoint)
                {
                    GeomagneticField geomagneticfield = new GeomagneticField(Double.valueOf(locationservice.lastValidLocation.getLatitude()).floatValue(), Double.valueOf(locationservice.lastValidLocation.getLongitude()).floatValue(), Double.valueOf(locationservice.lastValidLocation.getAltitude()).floatValue(), System.currentTimeMillis());

                    final GeoPoint geoPoint = new GeoPoint(Double.valueOf(locationservice.lastValidLocation.getLatitude()).floatValue(), Double.valueOf(locationservice.lastValidLocation.getLongitude()).floatValue());

                    mDeclanation = geomagneticfield.getDeclination();
                    post( new Runnable() {

                        public void run()
                        {
                            updateGpsLayer(geoPoint);
                        }

                    });
                    if(gpsLayer != null)
                    {
                        gpsLayer.accuracy = locationservice.lastValidLocation.getAccuracy();
                        gpsLayer.startAnimation();
                    }
                    post( new Runnable() {

                        public void run()
                        {
                            if(!isAnimating)
                            {
                                if(firstTimeGps)
                                {
                                    firstTimeGps = false;
                                    goToAnimate(geoPoint.longitude, geoPoint.latitude, getGoToScale());
                                } else
                                {
                                    goToAnimate(geoPoint.longitude, geoPoint.latitude, mapScale, true);
                                }
                                if(mapCanvas != null)
                                    mapCanvas.invalidate();
                            }
                        }

                    });
                }

            };
            LocationService.listeners.add(locationServiceListener);
            locationService.initialize(context);
            compass = new Compass(context) {

                public void onSensorChanged(SensorEvent sensorevent)
                {
                    super.onSensorChanged(sensorevent);
                    System.out.println("MapView.Compass.onSensorChanged." + sensorevent.values.length+":"+sensorevent.values[0]);
                    mYawOld = mYaw;
                    mYaw = compass.getYaw();
                }

            };
            addGpsLayer(context);
            setGpsLayerVisibility(false);
            if(gpsLayer != null)
                gpsLayer.stopAnimation();
        }
    }

    private void initEvent()
    {
        if(!isInEditMode())
        {
            if(findMeButton != null)
                findMeButton.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view)
                    {

                        if(findMeButton.isChecked())
                        {
                            startGps();
                            return;
                        }

                        if(locationService.isEnable && android.os.Build.VERSION.SDK_INT >= 12)
                        {
                            findMeButton.setVisibility(View.INVISIBLE);
                            compassButton.setVisibility(View.VISIBLE);
                            coneImage.setVisibility(View.VISIBLE);
                            compass.enable();
                            if(onCompassChangedListener != null)
                                onCompassChangedListener.onCompassChanged(compass.isEnabled());
                            compassTimer.cancel();
                            compassTimer = new Timer();
                            compassTimer.scheduleAtFixedRate(new TimerTask() {

                                public void run()
                                {
                                    float f1 = -360F;

                                    if(android.os.Build.VERSION.SDK_INT >= 12)
                                    {
                                        float f = mYaw;
                                        float f2 = mapCanvas.getRotation();
                                        float f3 = f * -1F - f2;
                                        if(f3 > 180F)
                                            f = -360F;
                                        else
                                        if(f3 < -180F)
                                            f = 360F;
                                        else
                                            f = f3;

                                        System.out.println("compassTimer.1_" + f3+":"+mYaw);

                                        f = (f3 + f) / 7.575758F;
                                        f2 = mDeclanation + f2 + f;

                                        System.out.println("compassTimer.2_" + f2 +":"+mDeclanation);

                                        if(f2 > 180F)
                                            f = f1;
                                        else
                                        if(f2 < -180F)
                                            f = 360F;
                                        else
                                            f = f2;

                                        final float afnew = f;
                                        mapCanvas.post( new Runnable() {

                                            public void run()
                                            {
                                                if(android.os.Build.VERSION.SDK_INT >= 12)
                                                {
                                                    System.out.println("compassTimer.setRotaion." + afnew);
                                                    mapCanvas.setRotation(afnew);
                                                    layersLayout.setRotation(afnew);
                                                }
                                            }

                                        });
                                    }
                                }
                            }, 0L, 33L);
                            return;
                        } else
                        {
                            stopGps();
                            return;
                        }
                    }

                });
            if(zoomInButton != null)
                zoomInButton.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view)
                    {
                        zoomIn();
                    }

                });
            if(zoomOutButton != null)
                zoomOutButton.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view)
                    {
                        zoomOut();
                    }

                });
            if(compassButton != null)
                compassButton.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view)
                    {
                        System.out.println("compassButton.clicked."+compass);
                        if(compass != null)
                        {
                            compassButton.setVisibility(View.INVISIBLE);
                            coneImage.setVisibility(View.INVISIBLE);
                            if(compass.isEnabled() && android.os.Build.VERSION.SDK_INT >= 12)
                            {
                                compass.disable();
                                if(onCompassChangedListener != null)
                                    onCompassChangedListener.onCompassChanged(compass.isEnabled());
                                compassTimer.cancel();
                                mapCanvas.animate().rotation(0.0F);
                                layersLayout.animate().rotation(0.0F);
                            }
                            findMeButton.setVisibility(View.VISIBLE);
                            if(locationService.isEnable)
                                locationService.disable();
                            gpsLayer.stopAnimation();
                        }
                    }

                });
        }
    }

    private void initLayout(Context context)
    {
        ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_map_view, this, true);
        if(!isInEditMode())
        {
            setClipChildren(false);
            setClipToPadding(false);
            mapCanvas = (MapViewCanvas)findViewById(R.id.MapViewCanvas);
            if(mapCanvas != null)
                mapCanvas.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
            layersLayout = (FrameLayout)findViewById(R.id.MapLayer);
            coneImage = (ImageView)findViewById(R.id.ConeImage);
            zoomInButton = (ImageButton)findViewById(R.id.ZoomInButton);
            zoomOutButton = (ImageButton)findViewById(R.id.ZoomOutButton);
            findMeButton = (ToggleButton)findViewById(R.id.FindMeButton);
            compassButton = (Button)findViewById(R.id.CompassButton);
        }
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
    }

    private boolean isGPSEnabled()
    {
        getContext();
        return ((LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps");
    }

    private boolean isNetworkProviderEnabled()
    {
        return ((LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("network");
    }

    private void rotateMap(float f, float f1)
    {
        if(mapCanvas != null)
        {
            isRotating = true;
            lastDegree = f1;
            RotateAnimation rotateanimation = new RotateAnimation(f, f1, mapCanvas.getWidth() / 2, mapCanvas.getHeight() / 2);
            rotateanimation.setFillAfter(true);
            rotateanimation.setAnimationListener(animationListener);
            rotateanimation.setDuration(250L);
            mapCanvas.startAnimation(rotateanimation);
        }
    }

    private void scrollMapBy(float f, float f1, boolean flag)
    {
        System.out.println("scrollMapBy." + f +":"+f1+":"+flag);
        if(mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null)
        {
            System.out.println("scrollMapBy2.");
            PointF64 pointf64 = mapCanvas.lastVisible.map.scale.projection.metricToPixelF64(mapCanvas.lastVisible.lastMetric.x, mapCanvas.lastVisible.lastMetric.y, mapScale);
            pointf64 = new PointF64(pointf64.x - (double)f, pointf64.y - (double)f1);
            center = mapCanvas.lastVisible.map.scale.projection.pixelToGeo(pointf64.x, pointf64.y, mapScale);
            if(!flag) {
                update(true);
                return;
            }
            update(false);
            arrange();
        }
        return;
    }

    private float spacing(MotionEvent motionevent)
    {
        float f = motionevent.getX(0) - motionevent.getX(1);
        float f1 = motionevent.getY(0) - motionevent.getY(1);
        return (float)Math.sqrt(f * f + f1 * f1);
    }

    private void startSwipeAnimation(float f, float f1, float f2, float f3)
    {
        if(swipeAnimationListener == null)
        {
            swipeAnimationListener = new android.view.animation.Animation.AnimationListener() {

                public void onAnimationEnd(Animation animation)
                {
                    isSwiping = false;
                    onAfterDrag();
                }

                public void onAnimationRepeat(Animation animation)
                {
                }

                public void onAnimationStart(Animation animation)
                {
                }

            };
            swipeAnimation.setAnimationListener(swipeAnimationListener);
        }
        isSwiping = true;
        swipeAnimation.fromX = f;
        swipeAnimation.fromY = f1;
        swipeAnimation.toX = f2;
        swipeAnimation.toY = f3;
        swipeAnimation.lastX = 0.0F;
        swipeAnimation.lastY = 0.0F;
        swipeAnimation.setInterpolator(new DecelerateInterpolator());
        swipeAnimation.setDuration(500L);
        startAnimation(swipeAnimation);
    }

    private void startZoomAnimation(double d, double d1)
    {
        zoomAnimation.fromScale = d;
        zoomAnimation.toScale = d1;
        System.out.println("startZoomAnimation " + d + ":" + d1);
        zoomAnimation.setInterpolator(new DecelerateInterpolator());
        zoomAnimation.setDuration(300L);
        startAnimation(zoomAnimation);
    }

    private void startZoomToPositionAnimation(double d, double d1, double d2, double d3)
    {
        zoomToPositionAnimation.fromScale = d;
        zoomToPositionAnimation.fromX = center.longitude;
        zoomToPositionAnimation.fromY = center.latitude;
        zoomToPositionAnimation.toScale = d1;
        zoomToPositionAnimation.toX = d2;
        zoomToPositionAnimation.toY = d3;
        zoomToPositionAnimation.setInterpolator(new DecelerateInterpolator());
        zoomToPositionAnimation.setDuration(300L);
        startAnimation(zoomToPositionAnimation);
    }

    private void stopSwipeAnimation()
    {
        clearAnimation();
        update(true);
    }

    private void zoomInInternal()
    {
        if(mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null)
        {
            int i = mapCanvas.lastVisible.level.ordinal;
            MapPresetLevelMap mappresetlevelmap = mapCanvas.lastVisible.map;
            if(mappresetlevelmap != null)
                if(mapScale > mappresetlevelmap.finalScale)
                    startZoomAnimation(mapScale, mappresetlevelmap.finalScale);
                else
                if(i < mapCanvas.preset.levels.size() - 1)
                {
                    Object obj = (MapPresetLevel)mapCanvas.preset.levels.get(i + 1);
                    if(obj != null)
                    {
                        obj = ((MapPresetLevel) (obj)).GetLevelMap(center);
                        if(obj != null)
                        {
                            startZoomAnimation(mapScale, ((MapPresetLevelMap) (obj)).finalScale);
                            return;
                        }
                    }
                }
        }
    }

    private void zoomInToPosition(double d, double d1)
    {
        if(mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null)
        {
            int i = mapCanvas.lastVisible.level.ordinal;
            MapPresetLevelMap mappresetlevelmap = mapCanvas.lastVisible.map;
            if(mappresetlevelmap != null)
                if(mapScale > mappresetlevelmap.finalScale)
                    startZoomToPositionAnimation(mapScale, mappresetlevelmap.finalScale, d, d1);
                else
                if(i < mapCanvas.preset.levels.size() - 1)
                {
                    MapPresetLevel mapPresetLevel = (MapPresetLevel)mapCanvas.preset.levels.get(i + 1);
                    if(mapPresetLevel != null)
                    {
                        MapPresetLevelMap mapPresetLevelMap = ((MapPresetLevel) (mapPresetLevel)).GetLevelMap(center);
                        if(mapPresetLevelMap != null)
                        {
                            startZoomToPositionAnimation(mapScale, ((MapPresetLevelMap) (mapPresetLevelMap)).finalScale, d, d1);
                            return;
                        }
                    }
                }
        }
    }

    public void addGpsLayer(Context context)
    {
        if(gpsLayer == null)
        {
            gpsLayer = new GpsViewLayer(context);
            addLayer(gpsLayer);
        }
    }

    public void addLayer(MapViewLayer mapviewlayer)
    {
        mapviewlayer.mapView = this;
        if(layersLayout != null)
            layersLayout.addView(mapviewlayer);
    }

    public void addLayer(MapViewLayer mapviewlayer, int i)
    {
        mapviewlayer.mapView = this;
        if(layersLayout.getChildCount() > i)
        {
            layersLayout.addView(mapviewlayer, i);
            return;
        } else
        {
            layersLayout.addView(mapviewlayer);
            return;
        }
    }

    protected void arrange()
    {
        System.out.println("MapView.arrange");
        if(mapCanvas != null && mapCanvas.lastVisible != null && mapCanvas.lastVisible.level != null)
            lastLevelOrdinal = mapCanvas.lastVisible.level.ordinal;
        if(lastLevelOrdinal < 5)
            mapCanvas.setBackgroundColor(SEA_BACKGROUND_COLOR);
        else
            mapCanvas.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        mapCanvas.arrange();

        mapCanvas.invalidate();
    }

    public PointF64 geoToMetric(double d, double d1)
    {
        if(mapCanvas != null && mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null && mapCanvas.lastVisible.map.scale != null && mapCanvas.lastVisible.map.scale.projection != null)
            return mapCanvas.lastVisible.map.scale.projection.geoToMetric(d, d1);
        else
            return new PointF64();
    }

    public PointF64 geoToPixel(double d, double d1)
    {
        if(mapCanvas != null && mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null && mapCanvas.lastVisible.map.scale != null && mapCanvas.lastVisible.map.scale.projection != null)
        {
            PointF64 pointf64 = mapCanvas.lastVisible.map.scale.projection.geoToPixel(d, d1, mapCanvas.lastVisible.map.finalScale);
            pointf64.x = (pointf64.x - mapCanvas.lastVisible.topLeftWorld.x) * mapCanvas.lastVisible.scaledDelta;
            pointf64.y = (pointf64.y - mapCanvas.lastVisible.topLeftWorld.y) * mapCanvas.lastVisible.scaledDelta;
            return pointf64;
        } else
        {
            return new PointF64();
        }
    }

    public PointF64 geoToPixelX(PointF64 pointf64)
    {
        if(mapCanvas != null && mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null && mapCanvas.lastVisible.map.scale != null && mapCanvas.lastVisible.map.scale.projection != null)
        {
            pointf64 = mapCanvas.lastVisible.map.scale.projection.geoToPixelX(pointf64, mapCanvas.lastVisible.map.finalScale);
            pointf64.x = (pointf64.x - mapCanvas.lastVisible.topLeftWorld.x) * mapCanvas.lastVisible.scaledDelta;
            pointf64.y = (pointf64.y - mapCanvas.lastVisible.topLeftWorld.y) * mapCanvas.lastVisible.scaledDelta;
            return pointf64;
        } else
        {
            return pointf64;
        }
    }

    public double getBestScale()
    {
        double d;
        try
        {
            d = ((MapPresetLevel)mapCanvas.preset.levels.get(bestZoomLevel - 1)).GetLevelMap(center).finalScale;
        }
        catch(Exception exception)
        {
            SDLogger.printStackTrace(exception, "MapView getBestScale");
            return 0.0D;
        }
        return d;
    }

    public int getCurrentLevelOrdinal()
    {
        int i;
        try
        {
            i = mapCanvas.lastVisible.level.ordinal;
        }
        catch(Exception exception)
        {
            return 0;
        }
        return i;
    }

    public double getGoToScale()
    {
        double d1 = mapScale;
        double d = d1;
        try
        {
            if(d1 > ((MapViewLevel)mapCanvas.levels.get(11)).map.scale.scalePixelPerMeter)
                d = ((MapViewLevel)mapCanvas.levels.get(11)).map.scale.scalePixelPerMeter;
        }
        catch(Exception exception)
        {
            return d1;
        }
        return d;
    }

    public ToggleButton getGpsButton()
    {
        return findMeButton;
    }

    public Location getLastGpsLocation()
    {
        if(locationService != null)
            return locationService.lastValidLocation;
        else
            return null;
    }

    public MapViewLevel getLastVisibleLevel()
    {
        MapViewLevel mapviewlevel;
        try
        {
            mapviewlevel = mapCanvas.lastVisible;
        }
        catch(Exception exception)
        {
            return null;
        }
        return mapviewlevel;
    }

    public MapPreset getPreset()
    {
        return mapCanvas.preset;
    }

    public ProjectionCoordinateSystem getProjection()
    {
        if(mapCanvas != null && mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null && mapCanvas.lastVisible.map.scale != null)
            return mapCanvas.lastVisible.map.scale.projection;
        else
            return null;
    }

    public Rect getTilesBoundary()
    {
        Rect rect = null;
        if(mapCanvas.lastVisible != null)
        {
            rect = new Rect();
            PointF64 pointf64 = mapCanvas.lastVisible.topLeftMap;
            MapfileScale mapfilescale = mapCanvas.lastVisible.map.scale;
            double d1 = mapCanvas.lastVisible.scaledHalfWidth * 2D;
            double d2 = mapCanvas.lastVisible.scaledHalfHeight * 2D;
            double d = d1;
            if(d1 > mapCanvas.lastVisible.width)
                d = mapCanvas.lastVisible.width;
            d1 = d2;
            if(d2 > mapCanvas.lastVisible.height)
                d1 = mapCanvas.lastVisible.height;
            int i = (int)Math.ceil((float)pointf64.x / (float)mapfilescale.width);
            int j = (int)Math.ceil(((float)pointf64.x + (float)d) / (float)mapfilescale.width);
            int k = (int)Math.ceil((float)pointf64.y / (float)mapfilescale.height);
            int l = (int)Math.ceil(((float)pointf64.y + (float)d1) / (float)mapfilescale.height);
            rect.left = i;
            rect.top = k;
            rect.right = j;
            rect.bottom = l;
        }
        return rect;
    }

    public ImageButton getZoomInButton()
    {
        return zoomInButton;
    }

    public ImageButton getZoomOutButton()
    {
        return zoomOutButton;
    }

    public void goTo(double d, double d1, double d2)
    {
        goTo(d, d1, d2, true);
    }

    public void goTo(double d, double d1, double d2, boolean flag)
    {
        center.longitude = d;
        center.latitude = d1;
        mapScale = d2;
        boolean flag1;
        if(!flag)
            flag1 = true;
        else
            flag1 = false;
        update(flag1);
        if(flag)
            arrange();
    }

    public void goTo(double d, double d1, int i)
    {
        goTo(d, d1, ((MapPresetLevel)mapCanvas.preset.levels.get(i)).GetLevelMap(new GeoPoint(d, d1)).scale.scalePixelPerMeter);
    }

    public void goToAnimate(double d, double d1, double d2)
    {
        goToAnimate(d, d1, d2, false);
    }

    public void goToAnimate(double d, double d1, double d2, boolean flag)
    {
        PointF64 pointf64 = geoToPixel(center.longitude, center.latitude);
        PointF64 pointf64_1 = geoToPixel(d, d1);
        goToAnimation.ignoreScale = flag;
        goToAnimation.fromScale = mapScale;
        goToAnimation.toScale = d2;
        goToAnimation.fromX = center.longitude;
        goToAnimation.toX = d;
        goToAnimation.fromY = center.latitude;
        goToAnimation.toY = d1;
        goToAnimation.calculateTension();
        goToAnimation.setInterpolator(new DecelerateInterpolator());
        if(Math.abs(pointf64.x - pointf64_1.x) < 500D && Math.abs(pointf64.y - pointf64_1.y) < 500D)
        {
            goToAnimation.tensionScale = -1D;
            goToAnimation.setDuration(500L);
        } else
        {
            goToAnimation.setDuration(1200L);
        }
        startAnimation(goToAnimation);
    }

    public void goToAnimate(double d, double d1, int i)
    {
        goToAnimate(d, d1, ((MapPresetLevel)mapCanvas.preset.levels.get(i)).GetLevelMap(new GeoPoint(d, d1)).scale.scalePixelPerMeter);
    }

    public void invalidate()
    {
        super.invalidate();
        if(!isInEditMode() && layersLayout != null)
        {
            for(int i = 0; i < layersLayout.getChildCount(); i++)
                ((MapViewLayer)layersLayout.getChildAt(i)).invalidate();

        }
    }

    public boolean isCompassEnabled()
    {
        return compass.isEnabled();
    }

    public boolean isLocationServiceEnabled()
    {
        return locationService.isEnable;
    }

    public boolean isSwiping()
    {
        return isSwiping;
    }

    protected void onAfterDrag()
    {
        if(onDragListener != null)
            onDragListener.onEndDrag(this);
    }

    protected void onAnimationEnd()
    {
        super.onAnimationEnd();
        isAnimating = false;
        if(!isInEditMode() && gtAnim != null)
        {
            if(!gtAnim.ignoreScale)
                mapScale = gtAnim.toScale;
            gtAnim = null;
        }
        if(!isInEditMode())
        {
            update(false);
            arrange();
        }
    }

    protected void onAnimationStart()
    {
        super.onAnimationStart();
        isAnimating = true;
        if(!isInEditMode())
        {
            if(!(getAnimation() instanceof GoToAnimation))
                gtAnim = null;
             else
                gtAnim = (GoToAnimation)getAnimation();
        }
        return;
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        LocationService.listeners.remove(locationServiceListener);
    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(i, j);
    }

    protected void onSizeChanged(int i, int j, int k, int l)
    {
        super.onSizeChanged(i, j, k, l);
        System.out.println("MapView.onSizeChanged"+isInEditMode());
        if(!isInEditMode())
            redraw();
    }

    protected void onStartDrag()
    {
        if(compass != null && compass.isEnabled() && android.os.Build.VERSION.SDK_INT >= 12)
        {
            compass.disable();
            if(onCompassChangedListener != null)
                onCompassChangedListener.onCompassChanged(compass.isEnabled());
            compassTimer.cancel();
            mapCanvas.animate().rotation(0.0F);
            layersLayout.animate().rotation(0.0F);
        }
        coneImage.setVisibility(View.INVISIBLE);
        if(locationService != null)
            locationService.disable();
        gpsLayer.stopAnimation();
        compassButton.setVisibility(View.INVISIBLE);
        findMeButton.setVisibility(View.VISIBLE);
        findMeButton.setChecked(false);
        stopGps(true);
        if(onDragListener != null)
            onDragListener.onStartDrag(this);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(isInEditMode())
        	return true;

        float f;
        float f1;
        int i;
        i = motionevent.getAction();
        f = motionevent.getX();
        f1 = motionevent.getY();

        switch(i & 0xff) {
		default:
		case 3:
		case 4:
				return true;
		case 0:
				long l = System.currentTimeMillis();
				stopSwipeAnimation();
				mX = f;
				mY = f1;
				sX = f;
				sY = f1;
				oX = f;
				oY = f1;
                GeoPoint geoPoint;
				if(l - lastTouchTime < 250L)
				{
                    geoPoint = pixelToGeo(f, f1);
					if(!isPanEnabled)
						zoomInInternal();
					else
						zoomInToPosition(((GeoPoint) (geoPoint)).longitude, ((GeoPoint) (geoPoint)).latitude);
					SDLogger.debug("input double tap");
				} else
				{
					lastTouchTime = l;
					mapState = 1;
				}
				break;
		case 1:
		case 6:
				if(mapState == 1 && isPanEnabled)
				{
					swipeX = mX - sX;
					swipeY = mY - sY;
					float f2 = Math.abs(oX - f);
					float f5 = Math.abs(oY - f1);

					if(f2 < 10F && f5 < 10F)
					{
                        geoPoint = pixelToGeo(f, f1);
						Point point = new Point((int)f, (int)f1);
						for(int j = 0; j < layersLayout.getChildCount(); j++)
							((MapViewLayer)layersLayout.getChildAt(j)).mapLayerClicked(geoPoint, point);

						if(onMapClickedListener != null)
							onMapClickedListener.onMapClicked(this, geoPoint, point);
						arrange();
					} else
					if(Math.abs(swipeX) > 2.0F && Math.abs(swipeY) > 2.0F)
						startSwipeAnimation(0.0F, 0.0F, swipeX * 3F, swipeY * 3F);
					else
						arrange();
					onAfterDrag();
				} else
				{
					arrange();
				}
				mapState = 0;
				break;
		case 2:
				float f3;
				float f6;
				f3 = mX;
				f6 = mY;
				sX = mX;
				sY = mY;
				if(mapState != 1 || !isPanEnabled) {
					if(mapState == 2)
					{
						float f4 = spacing(motionevent);
						if(f4 > 10F)
						{
							f4 /= oldDist;
							mapScale = tScale / (double)f4;
							update(true);
						}
					}
				} else {
					onStartDrag();
					scrollMapBy(f - f3, f1 - f6, false);
				}

				mX = f;
				mY = f1;
				break;

		case 5:
				oldDist = spacing(motionevent);
				if(oldDist > 10F)
				{
					mapState = 2;
					tScale = mapScale;
				}
				break;
			}
			return true;
    }

    public GeoPoint pixelToGeo(double d, double d1)
    {
        if(mapCanvas != null && mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null && mapCanvas.lastVisible.map.scale != null && mapCanvas.lastVisible.map.scale.projection != null)
        {
            d /= mapCanvas.lastVisible.scaledDelta;
            double d2 = mapCanvas.lastVisible.topLeftWorld.x;
            d1 /= mapCanvas.lastVisible.scaledDelta;
            double d3 = mapCanvas.lastVisible.topLeftWorld.y;
            return mapCanvas.lastVisible.map.scale.projection.pixelToGeo(d + d2, d1 + d3, mapCanvas.lastVisible.map.finalScale);
        } else
        {
            return new GeoPoint();
        }
    }

    public void redraw()
    {
        System.out.println("MapView.redraw");
        update(false);
        arrange();
        mapCanvas.invalidate();
        System.out.println("MapView.redraw.invalidate" + mapCanvas);

    }

    public void removeGpsLayer()
    {
        if(gpsLayer != null)
        {
            removeLayer(gpsLayer);
            gpsLayer = null;
        }
    }

    public void removeLayer(MapViewLayer mapviewlayer)
    {
        layersLayout.removeView(mapviewlayer);
    }

    public void scrollMapTo(GeoPoint geopoint)
    {
        scrollMapTo(geopoint, true);
    }

    public void scrollMapTo(GeoPoint geopoint, boolean flag)
    {
        center = geopoint;
        boolean flag1;
        if(!flag)
            flag1 = true;
        else
            flag1 = false;
        update(flag1);
        if(flag)
            arrange();
    }

    public void setFastMode(boolean flag)
    {
        mapCanvas.setFastMode(flag);
        redraw();
    }

    public void setGpsLayerVisibility(boolean flag)
    {
		if(gpsLayer != null)
		{
			if(!flag) {
				gpsLayer.setVisibility(View.INVISIBLE);
			} else {
				gpsLayer.setVisibility(View.VISIBLE);
				gpsLayer.update();
			}
		}
		return;
    }

    public void setLocationServiceEnabled(boolean flag)
    {
        if(flag)
        {
            findMeButton.setVisibility(View.VISIBLE);
            return;
        } else
        {
            findMeButton.setVisibility(View.INVISIBLE);
            return;
        }
    }

    public void setOnAfterUpdateListener(OnFinishUpdateMapListener onfinishupdatemaplistener)
    {
        onFinishUpdateMapListener = onfinishupdatemaplistener;
    }

    public void setOnCompassChangedListener(OnCompassChangedListener oncompasschangedlistener)
    {
        onCompassChangedListener = oncompasschangedlistener;
    }

    public void setOnDragListener(OnDragListener ondraglistener)
    {
        onDragListener = ondraglistener;
    }

    public void setOnMapClicked(OnMapClickedListener onmapclickedlistener)
    {
        onMapClickedListener = onmapclickedlistener;
    }

    public void setPanEnabled(boolean flag)
    {
        isPanEnabled = flag;
    }

    public void setPreset(MapPreset mappreset)
    {
        MapPreset mappreset1 = mappreset;
        AssetManager assetManager;
        if(mappreset == null)
        {
            mappreset1 = MapPresetCollection.createFromAsset(SDApplication.getAppContext()).get(0);
            assetManager = AssetsUtil.getAssManager();
            File file = new File(InternalStorage.getStorageDirectory(), "configs");
            AssetsUtil.copy(assetManager, new String[] {
                "preset.xml"
            }, "internal/configs", file, true);
        }
        mapCanvas.setPreset(mappreset1, center, mapScale);
        if(mapCanvas.preset != null && mapCanvas.preset.levels.size() > 0)
        {
            minScale = ((MapPresetLevel)mapCanvas.preset.levels.get(mapCanvas.preset.levels.size() - 1)).getMinScale();
            maxScale = ((MapPresetLevel)mapCanvas.preset.levels.get(0)).getMaxScale();
            bestZoomLevel = mapCanvas.preset.levels.size();
            minScale = minScale / 2D;
        }
        update(true);
        redraw();
    }

    public void startGps()
    {
        startGps(false);
    }

    public void startGps(boolean flag)
    {
        System.out.println("startGps.1" + flag);
        if(!isNetworkProviderEnabled())
        {
            findMeButton.setChecked(false);
            UIHelper.showSdDialog(getContext(), createLocationWarningDialog(getContext(), true, false));
        } else
        {
            System.out.println("startGps.2");
            firstTimeGps = true;
            if(gpsLayer != null)
                gpsLayer.setAccuracyVisibility(true);
            if(!locationService.isEnable)
                locationService.enable();
            setGpsLayerVisibility(true);
            if(locationService != null && locationService.lastValidLocation != null)
            {
                System.out.println("startGps.3");
                double d = locationService.lastValidLocation.getLongitude();
                double d1 = locationService.lastValidLocation.getLatitude();
                double d2 = getGoToScale();
                firstTimeGps = false;
                System.out.println("startGps.4-----" + d + ":"+d1+":"+d2);
                goToAnimate(d, d1, d2);
            }
            if(flag)
            {
                findMeButton.setChecked(true);
                return;
            }
        }
    }

    public void stopGps()
    {
        stopGps(false);
    }

    public void stopGps(boolean flag)
    {
        if(gpsLayer != null)
            gpsLayer.setAccuracyVisibility(false);
        if(locationService.isEnable)
            locationService.disable();
        gpsLayer.stopAnimation();
        if(flag)
            findMeButton.setChecked(false);
    }

    protected void update(boolean flag)
    {

        if(mapScale < maxScale) {
			if(!zoomOutButton.isEnabled())
				zoomOutButton.setEnabled(true);
		} else {
            mapScale = maxScale;
            if (zoomOutButton.isEnabled())
                zoomOutButton.setEnabled(false);
        }

        if(mapCanvas != null && mapCanvas.lastVisible != null && mapCanvas.lastVisible.level != null) {
            System.out.println("MapView.update." + mapCanvas.lastVisible.level.ordinal+":"+lastLevelOrdinal);

			if(mapScale <= minScale)
				mapScale = minScale;
			lastLevelOrdinal = mapCanvas.lastVisible.level.ordinal;

            lastLevelOrdinal = 13;

			if(lastLevelOrdinal < mapCanvas.preset.levels.size() + 0) {
				if(!zoomInButton.isEnabled()) {
					zoomInButton.setEnabled(true);
				}
			} else {
				if(zoomInButton.isEnabled())
					zoomInButton.setEnabled(false);
			}
			if(mapScale <= minScale && zoomInButton.isEnabled())
				zoomInButton.setEnabled(false);
		}

        if(center.longitude > 180D)
            center.longitude = 180D;
        if(center.longitude < -180D)
            center.longitude = -180D;
        if(center.latitude > 90D)
            center.latitude = 90D;
        if(center.latitude < -90D)
            center.latitude = -90D;
        mapCanvas.update(flag, center, mapScale);
        for(int i = 0; i < layersLayout.getChildCount(); i++)
            ((MapViewLayer)layersLayout.getChildAt(i)).update();

        if(mapScale != lastMapScale)
        {
            lastMapScale = mapScale;
            if(onFinishUpdateMapListener != null)
                onFinishUpdateMapListener.onFinishUpdateMap(this);
        }
        return;

        //SDLogger.printStackTrace(exception, "MapView update");
    }

    public void updateGpsLayer(GeoPoint geopoint)
    {
        if(gpsLayer != null)
        {
            gpsLayer.longitude = geopoint.longitude;
            gpsLayer.latitude = geopoint.latitude;
            gpsLayer.update();
        }
    }

    public void zoomIn()
    {
        System.out.println("zoomIn.1");
        if(mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null)
        {
            int i = mapCanvas.lastVisible.level.ordinal;
            System.out.println("zoomIn.2." + i);
            if(mapCanvas.lastVisible.map != null) {
                System.out.println("zoomIn.3");
                if (i < mapCanvas.preset.levels.size() - 1) {
                    MapPresetLevel mapPresetLevel = (MapPresetLevel) mapCanvas.preset.levels.get(i + 1);
                    if (mapPresetLevel != null) {
                        MapPresetLevelMap mapPresetLevelMap = mapPresetLevel.GetLevelMap(center);
                        if (mapPresetLevelMap != null) {
                            clearAnimation();
                            startZoomAnimation(mapScale, mapPresetLevelMap.finalScale);
                        }
                    }
                } else if (i < mapCanvas.preset.levels.size()) {
                    MapPresetLevel mappresetlevel = (MapPresetLevel) mapCanvas.preset.levels.get(i);
                    if (mappresetlevel != null && mappresetlevel.GetLevelMap(center) != null) {
                        clearAnimation();
                        startZoomAnimation(mapScale, minScale);
                        return;
                    }
                }
            }
        }
    }

    public void zoomMapBy(double d)
    {
        mapScale = mapScale + d;
        update(false);
        arrange();
    }

    public void zoomMapTo(double d)
    {
        zoomMapTo(d, true);
    }

    public void zoomMapTo(double d, boolean flag)
    {
        mapScale = d;
        boolean flag1;
        if(!flag)
            flag1 = true;
        else
            flag1 = false;
        update(flag1);
        if(flag)
            arrange();
    }

    public void zoomOut()
    {
        System.out.println("zoomOut.1");
        if(mapCanvas.lastVisible != null && mapCanvas.lastVisible.map != null)
        {
            int i = mapCanvas.lastVisible.level.ordinal;
            System.out.println("zoomOut.2." + i);
            if(mapCanvas.lastVisible.map != null && i > 0)
            {
                MapPresetLevel mappresetlevel = (MapPresetLevel)mapCanvas.preset.levels.get(i - 1);
                if(i == mapCanvas.preset.levels.size() - 1)
                {
                    if(mapScale < ((MapViewLevel)mapCanvas.levels.get(i)).map.scale.scalePixelPerMeter)
                        mappresetlevel = (MapPresetLevel)mapCanvas.preset.levels.get(i);
                }
                if(mappresetlevel != null)
                {
                    MapPresetLevelMap mapPresetLevelMap = mappresetlevel.GetLevelMap(center);
                    System.out.println("zoomOut.3." + center +":"+mapPresetLevelMap);
                    if(mapPresetLevelMap != null)
                    {
                        clearAnimation();
                        startZoomAnimation(mapScale, mapPresetLevelMap.finalScale);
                    }
                }
            }
        }
    }

    public static final int DEFAULT_BACKGROUND_COLOR = Color.rgb(204, 255, 204);
    public static final int SEA_BACKGROUND_COLOR = Color.rgb(102, 204, 255);
    public static final int STATE_DRAG = 1;
    public static final int STATE_NONE = 0;
    public static final int STATE_ZOOM = 2;
    android.view.animation.Animation.AnimationListener animationListener = new android.view.animation.Animation.AnimationListener() {

        public void onAnimationEnd(Animation animation)
        {
            isRotating = false;
            if(!compass.isEnabled() && android.os.Build.VERSION.SDK_INT >= 12)
                mapCanvas.setRotation(0.0F);
        }

        public void onAnimationRepeat(Animation animation)
        {
        }

        public void onAnimationStart(Animation animation)
        {
        }

    };
    private int bestZoomLevel;
    public GeoPoint center;
    private Compass compass;
    private Button compassButton;
    LinearInterpolator compassInterpolator;
    long compassTime;
    Timer compassTimer;
    TimerTask compassTimerTask;
    private ImageView coneImage;
    private ToggleButton findMeButton;
    private boolean firstTimeGps;
    private GoToAnimation goToAnimation;
    private GpsViewLayer gpsLayer;
    private GoToAnimation gtAnim;
    private boolean isAnimating;
    private boolean isPanEnabled;
    boolean isRotating;
    private boolean isSwiping;
    private GeoPoint lastCompassPosition;
    private float lastDegree;
    private int lastLevelOrdinal;
    private double lastMapScale;
    private long lastTouchTime;
    private FrameLayout layersLayout;
    private LocationService locationService;
    streetdirectory.mobile.gis.gps.LocationService.PositionChangedListener locationServiceListener;
    private float mDeclanation;
    private float mX;
    private float mY;
    private float mYaw;
    private float mYawOld;
    private MapViewCanvas mapCanvas;
    public double mapScale;
    private int mapState;
    private double maxScale;
    private double minScale;
    private float oX;
    private float oY;
    private float oldDist;
    private OnCompassChangedListener onCompassChangedListener;
    private OnDragListener onDragListener;
    private OnFinishUpdateMapListener onFinishUpdateMapListener;
    private OnMapClickedListener onMapClickedListener;
    private float sX;
    private float sY;
    private SwipeAnimation swipeAnimation;
    private android.view.animation.Animation.AnimationListener swipeAnimationListener;
    private float swipeX;
    private float swipeY;
    private double tScale;
    private ZoomAnimation zoomAnimation;
    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;
    private ZoomToPositionAnimation zoomToPositionAnimation;




/*
    static float access$1002(MapView mapview, float f)
    {
        mapview.mYaw = f;
        return f;
    }

*/









/*
    static boolean access$1802(MapView mapview, boolean flag)
    {
        mapview.isSwiping = flag;
        return flag;
    }

*/




/*
    static float access$402(MapView mapview, float f)
    {
        mapview.mDeclanation = f;
        return f;
    }

*/





/*
    static boolean access$702(MapView mapview, boolean flag)
    {
        mapview.firstTimeGps = flag;
        return flag;
    }

*/



/*
    static float access$902(MapView mapview, float f)
    {
        mapview.mYawOld = f;
        return f;
    }

*/
}
