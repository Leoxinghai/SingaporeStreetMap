

package streetdirectory.mobile.gis.maps;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import streetdirectory.mobile.gis.GeoPoint;

// Referenced classes of package streetdirectory.mobile.gis.maps:
//            MapView

public abstract class MapViewLayer extends RelativeLayout
{
    public static interface OnMapLayerClickedListener
    {

        public abstract void onMapLayerClicked(MapViewLayer mapviewlayer, GeoPoint geopoint, Point point);
    }


    public MapViewLayer(Context context)
    {
        this(context, null);
    }

    public MapViewLayer(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public MapViewLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    public void mapLayerClicked(GeoPoint geopoint, Point point)
    {
        onMapLayerClicked(geopoint, point);
    }

    protected void onMapLayerClicked(GeoPoint geopoint, Point point)
    {
        if(mMapLayerClickedListener != null)
            mMapLayerClickedListener.onMapLayerClicked(this, geopoint, point);
    }

    protected abstract void onUpdate();

    public void setMapLayerClickedListener(OnMapLayerClickedListener onmaplayerclickedlistener)
    {
        mMapLayerClickedListener = onmaplayerclickedlistener;
    }

    public void update()
    {
        onUpdate();
    }

    private OnMapLayerClickedListener mMapLayerClickedListener;
    public MapView mapView;
}
