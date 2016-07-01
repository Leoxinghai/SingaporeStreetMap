

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;
import streetdirectory.mobile.modules.direction.*;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRoutesServiceOutput;

public class BusRouteMapLayer extends MapViewLayer
{

    public BusRouteMapLayer(Context context)
    {
        this(context, null);
    }

    public BusRouteMapLayer(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public BusRouteMapLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mMethod = 0;
        currentPixel = new PointF64();
        lastPixel = new PointF64();
        selectedRouteIndex = 0;
        nodeBitmap = null;
        carparkBitmap = null;
        startIm = null;
        endIm = null;
        pathBitmaps = new ArrayList();
        tempCanvas = new Canvas();
        mLastJourneyLine = null;
        mLastJourneyLinePoint = null;
        setWillNotDraw(false);
        nodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bus);
        nodeBitmapSource = new Rect(0, 0, nodeBitmap.getWidth(), nodeBitmap.getHeight());
        nodeBitmapDest = new RectF(-16F, -16F, 16F, 16F);
        tempRect = new RectF();
        tempRect.set(nodeBitmapDest);
        initializePaints();
    }

    private void doDrawJump(double d, double d1, double d2, double d3)
    {
        currentPixel.x = d;
        currentPixel.y = d1;
        currentPixel = mapView.geoToPixelX(currentPixel);
        currentPixel.x = d2;
        currentPixel.y = d3;
        currentPixel = mapView.geoToPixelX(currentPixel);
    }

    public void draw(Canvas canvas)
    {
        if(mData != null && mData.arrayOfRoutes != null && selectedRouteIndex < mData.arrayOfRoutes.size() && mData.arrayOfRoutes.get(selectedRouteIndex) != null)
        {
            JourneyRoute journeyroute = (JourneyRoute)mData.arrayOfRoutes.get(selectedRouteIndex);
            if(pathBitmap == null || pathBitmap.isRecycled() || pathBitmap.getWidth() != canvas.getWidth() || pathBitmap.getHeight() != canvas.getHeight())
            {
                if(pathBitmap != null)
                    pathBitmap.recycle();
                pathBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
                tempCanvas.setBitmap(pathBitmap);
            }
            tempCanvas.drawPaint(clearPaint);
            int i = 0;
            Iterator iterator1 = journeyroute.arrayOfLines.iterator();
            do
            {
                if(!iterator1.hasNext())
                    break;
                JourneyLine journeyline = (JourneyLine)iterator1.next();
                path.reset();
                paintMainRoad.setColor(0x8c000000 | journeyline.color & 0xffffff);
                int j = 0;
                Iterator iterator2 = journeyline.arrayOfPoints.iterator();
                while(iterator2.hasNext())
                {
                    GeoPoint geopoint = (GeoPoint)iterator2.next();
                    currentPixel.x = geopoint.longitude;
                    currentPixel.y = geopoint.latitude;
                    currentPixel = mapView.geoToPixelX(currentPixel);
                    if(j == 0)
                    {
                        if(i == 0)
                            doDrawJump(mData.start.longitude, mData.start.latitude, geopoint.longitude, geopoint.latitude);
                        path.moveTo((float)currentPixel.x, (float)currentPixel.y);
                        lastPixel.x = currentPixel.x;
                        lastPixel.y = currentPixel.y;
                    } else
                    if(MathTools.computeDistance(lastPixel, currentPixel) > 3D)
                    {
                        path.lineTo((float)currentPixel.x, (float)currentPixel.y);
                        lastPixel.x = currentPixel.x;
                        lastPixel.y = currentPixel.y;
                    }
                    if(i == journeyroute.arrayOfLines.size() - 1 && j == journeyline.arrayOfPoints.size() - 1)
                        doDrawJump(mData.end.longitude, mData.end.latitude, geopoint.longitude, geopoint.latitude);
                    if(i > 0 && j == 0)
                        doDrawJump(mLastJourneyLinePoint.longitude, mLastJourneyLinePoint.latitude, geopoint.longitude, geopoint.latitude);
                    mLastJourneyLinePoint = geopoint;
                    j++;
                }
                tempCanvas.drawPath(path, paintMainRoad);
                mLastJourneyLine = journeyline;
                i++;
            } while(true);
            canvas.drawBitmap(pathBitmap, 0.0F, 0.0F, null);
            for(Iterator iterator = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(selectedRouteIndex)).iterator(); iterator.hasNext(); canvas.drawBitmap(nodeBitmap, nodeBitmapSource, tempRect, null))
            {
                JourneyPointDetail journeypointdetail = (JourneyPointDetail)iterator.next();
                currentPixel.x = journeypointdetail.longitude;
                currentPixel.y = journeypointdetail.latitude;
                currentPixel = mapView.geoToPixelX(currentPixel);
                tempRect.set(nodeBitmapDest);
                tempRect.offset((float)currentPixel.x, (float)currentPixel.y);
            }

            currentPixel.x = focusLongitude;
            currentPixel.y = focusLatitude;
            currentPixel = mapView.geoToPixelX(currentPixel);
            canvas.drawCircle((float)currentPixel.x, (float)currentPixel.y, 64F, paintFocusFill);
            canvas.drawCircle((float)currentPixel.x, (float)currentPixel.y, 64F, paintFocusStroke);
        }
        super.draw(canvas);
    }

    protected void initializePaints()
    {
        path = new Path();
        pathWalk = new Path();
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
        paintMainRoad = new Paint();
        paintMainRoad.setFilterBitmap(false);
        paintMainRoad.setDither(false);
        paintMainRoad.setAntiAlias(false);
        paintMainRoad.setStrokeWidth(12F);
        paintMainRoad.setColor(Color.argb(140, 10, 30, 249));
        paintMainRoad.setStyle(android.graphics.Paint.Style.STROKE);
        paintMainRoad.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        paintMainRoad.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        paintWalk = new Paint(paintMainRoad);
        paintWalk.setColor(Color.argb(140, 50, 50, 50));
        paintWalk.setPathEffect(new DashPathEffect(new float[] {
            10F, 20F
        }, 5F));
        paintTrain = new Paint(paintMainRoad);
        paintTrain.setColor(Color.argb(80, 10, 100, 249));
        paintFocusFill = new Paint(paintMainRoad);
        paintFocusFill.setColor(Color.argb(60, 0, 0, 0));
        paintFocusFill.setStyle(android.graphics.Paint.Style.FILL);
        paintFocusStroke = new Paint(paintMainRoad);
        paintFocusStroke.setColor(Color.argb(200, 250, 10, 10));
        paintFocusStroke.setStrokeWidth(4F);
        paintFocusStroke.setStyle(android.graphics.Paint.Style.STROKE);
        paintFocusStroke.setDither(true);
        paintFocusStroke.setAntiAlias(true);
        paintCarparkText = new Paint(paintFocusFill);
        paintCarparkText.setTextSize(24F);
        paintCarparkText.setColor(Color.argb(255, 250, 250, 250));
    }

    protected void onUpdate()
    {
        JourneyRoute journeyroute;
        if(mData != null && mData.arrayOfRoutes != null && selectedRouteIndex < mData.arrayOfRoutes.size() && mData.arrayOfRoutes.get(selectedRouteIndex) != null)
            journeyroute = (JourneyRoute)mData.arrayOfRoutes.get(selectedRouteIndex);
        invalidate();
    }

    Bitmap carparkBitmap;
    RectF carparkBitmapDest;
    Rect carparkBitmapSource;
    Paint clearPaint;
    PointF64 currentPixel;
    ImageView endIm;
    public double focusLatitude;
    public double focusLongitude;
    PointF64 lastPixel;
    public BusRoutesServiceOutput mData;
    JourneyLine mLastJourneyLine;
    GeoPoint mLastJourneyLinePoint;
    public int mMethod;
    Bitmap nodeBitmap;
    RectF nodeBitmapDest;
    Rect nodeBitmapSource;
    Paint paintCarparkText;
    Paint paintFocusFill;
    Paint paintFocusStroke;
    Paint paintMainRoad;
    Paint paintTrain;
    Paint paintWalk;
    Path path;
    Bitmap pathBitmap;
    ArrayList pathBitmaps;
    Path pathWalk;
    int selectedRouteIndex;
    ImageView startIm;
    Canvas tempCanvas;
    RectF tempRect;
}
