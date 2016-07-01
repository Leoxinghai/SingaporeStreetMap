

package streetdirectory.mobile.modules.map;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.List;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.ui.SdGestureDetector;
import streetdirectory.mobile.gis.ProjectionCoordinateSystem;
import streetdirectory.mobile.gis.maps.configs.*;

// Referenced classes of package streetdirectory.mobile.modules.map:
//            SdMapView, SdMapLayer

public class SdMapFragment extends Fragment
    implements streetdirectory.mobile.core.ui.SdGestureDetector.SdGestureDetectorListener
{
    private class FlingRunnable
        implements Runnable
    {

        public void forceFinished()
        {
            if(!scroller.isFinished())
                scroller.forceFinished(true);
        }

        public boolean isFlinging()
        {
            return !scroller.isFinished();
        }

        public void run()
        {
            if(!scroller.isFinished())
            {
                Log.d("SdMapFragment", (new StringBuilder()).append("run() called with: scroller currentX = [").append(scroller.getCurrX()).append(", currentY = [").append(scroller.getCurrY()).append("]").toString());
                boolean flag = scroller.computeScrollOffset();
                int i = scroller.getCurrX();
                int j = scroller.getCurrY();
                int k = lastX - i;
                int l = lastY;
                if(k != 0)
                {
                    float af[] = rotatePoint(k, l - j, mapView.yaw);
                    SdMapView sdmapview = mapView;
                    sdmapview.centerX = (int)((float)sdmapview.centerX + af[0]);
                    sdmapview = mapView;
                    sdmapview.centerY = (int)((float)sdmapview.centerY - af[1]);
                    mapView.requestRender();
                    lastX = i;
                    lastY = j;
                }
                if(flag)
                {
                    mapView.post(this);
                    return;
                }
            }
        }

        public void start(int i, int j, int k, int l)
        {
            lastX = i;
            lastY = j;
            scroller.fling(i, j, k, l, 0x80000000, 0x7fffffff, 0x80000000, 0x7fffffff);
            mapView.post(this);
        }

        private int lastX;
        private int lastY;
        private final Scroller scroller;

        public FlingRunnable()
        {
            super();
            lastX = 0;
            lastY = 0;
            scroller = new Scroller(getActivity());
        }
    }


    public SdMapFragment()
    {
    }

    public static SdMapFragment newInstance(String s, String s1)
    {
        SdMapFragment sdmapfragment = new SdMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", s);
        bundle.putString("param2", s1);
        sdmapfragment.setArguments(bundle);
        return sdmapfragment;
    }

    private float[] rotatePoint(float f, float f1, float f2)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(f2);
        float af[] = new float[2];
        af[0] = f;
        af[1] = f1;
        matrix.mapPoints(af);
        return af;
    }

    public void loadPreset(MapPreset mappreset)
    {
        preset = mappreset;
        MapPresetLevelMap temp = (MapPresetLevelMap)((MapPresetLevel)mappreset.levels.get(11)).maps.get(0);
        mapView.layers.add(new SdMapLayer(((MapPresetLevelMap) (temp)).scale.width, ((MapPresetLevelMap) (temp)).scale.height, 1.0D));
        PointF64 temp2 = ((MapPresetLevelMap) (temp)).mapPresetSource.config.projection.geoToPixel(103.838436D, 1.3011280000000001D, ((MapPresetLevelMap) (temp)).finalScale);
        mapView.centerX = temp2.getIntX();
        mapView.centerY = -temp2.getIntY();
        mapView.requestRender();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        if(getArguments() != null)
        {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        View lView = layoutinflater.inflate(R.layout.fragment_sd_map, viewgroup, false);
        Button button = (Button)lView.findViewById(R.id.ButtonReset);
        mapView = (SdMapView)lView.findViewById(R.id.SdMapView);
        seekBarScale = (SeekBar)lView.findViewById(R.id.seekBarScale);
        seekBarRotate = (SeekBar)lView.findViewById(R.id.seekBarRotate);
        seekBarScale.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
            {
                if(i <= 0)
                    mapView.scale = 1.0D;
                else
                    mapView.scale = i;
                mapView.requestRender();
            }

            public void onStartTrackingTouch(SeekBar seekbar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekbar)
            {
            }

        });
        seekBarRotate.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
            {
                mapView.yaw = i;
                mapView.requestRender();
            }

            public void onStartTrackingTouch(SeekBar seekbar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekbar)
            {
            }

        });
        mGestureDetector = new SdGestureDetector(getActivity(), this);
        mapView.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                return mGestureDetector.onTouchEvent(motionevent);
            }

        });
        mapView.layers.add(new SdMapLayer(256, 256, 1.0D));
        viewgroup.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mapView.centerX = 0;
                mapView.centerY = 0;
                mapView.requestRender();
            }

        });
        new FlingRunnable();
        mapView.setOnTileRequestListener(new SdMapView.OnTileRequestListener() {

            public void onTileRequest(SdMapView.Tile tile)
            {
            }

        });
        return lView;
    }

    public void onDetach()
    {
        super.onDetach();
    }

    public void onMove(SdGestureDetector sdgesturedetector)
    {
        Log.d("SdMapFragment", (new StringBuilder()).append("onMove() called with: detector = [").append(sdgesturedetector).append("]").toString());
        mapView.centerX = (int)sdgesturedetector.getCurrentX();
        mapView.centerY = (int)sdgesturedetector.getCurrentY();
        mapView.requestRender();
    }

    public void onRotate(SdGestureDetector sdgesturedetector)
    {
        Log.d("SdMapFragment", (new StringBuilder()).append("onRotate() called with: detector = [").append(sdgesturedetector).append("]").toString());
        mapView.yaw = sdgesturedetector.getAngle();
        mapView.requestRender();
    }

    public void onScale(SdGestureDetector sdgesturedetector)
    {
        Log.d("SdMapFragment", (new StringBuilder()).append("onScale() called with: detector = [").append(sdgesturedetector).append("]").toString());
        mapView.scale = sdgesturedetector.getScale();
        mapView.requestRender();
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SdMapFragment";
    private SdGestureDetector mGestureDetector;
    private String mParam1;
    private String mParam2;
    private SdMapView mapView;
    private MapPreset preset;
    private SeekBar seekBarRotate;
    private SeekBar seekBarScale;



}
