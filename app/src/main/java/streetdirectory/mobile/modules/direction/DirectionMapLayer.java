

package streetdirectory.mobile.modules.direction;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;
import streetdirectory.mobile.modules.direction.service.DirectionDetailServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyRoute, JourneyLine, JourneyPointDetail, Carpark

public class DirectionMapLayer extends MapViewLayer
{

    public DirectionMapLayer(Context context)
    {
        this(context, null);
    }

    public DirectionMapLayer(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public DirectionMapLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mMethod = 0;
        currentPixel = new PointF64();
        lastPixel = new PointF64();
        selectedRouteIndex = 0;
        busIcon = null;
        busIconRect = null;
        dest = null;
        nodeBitmap = null;
        carparkBitmap = null;
        busBitmap = null;
        startIm = null;
        endIm = null;
        pathBitmaps = new ArrayList();
        tempCanvas = new Canvas();
        mLastJourneyLine = null;
        mLastJourneyLinePoint = null;
        ((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_direction_map_layer, this, true);
        setWillNotDraw(false);
        nodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_circle_white);
        nodeBitmapSource = new Rect(0, 0, nodeBitmap.getWidth(), nodeBitmap.getHeight());
        nodeBitmapDest = new RectF(-16F, -16F, 16F, 16F);
        carparkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_carpark);
        busBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bus);
        carparkBitmapSource = new Rect(0, 0, nodeBitmap.getWidth(), nodeBitmap.getHeight());
        carparkBitmapDest = new RectF(-24F, -24F, 24F, 24F);
        tempRect = new RectF();
        tempRect.set(carparkBitmapDest);
        paintBus = new Paint();
        paintBus.setColor(Color.rgb(200, 0, 0));
        paintBus.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
        busIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bus);
        busIconRect = new RectF(0.0F, 0.0F, UIHelper.toPixel(15F), UIHelper.toPixel(15F));
        dest = new RectF(busIconRect);
        startIm = (ImageView)findViewById(R.id.pinStart);
        endIm = (ImageView)findViewById(R.id.pinEnd);
        initializePaints();
    }

    private void doDrawJump(double d, double d1, double d2, double d3)
    {
        pathWalk.reset();
        currentPixel.x = d;
        currentPixel.y = d1;
        currentPixel = mapView.geoToPixelX(currentPixel);
        pathWalk.moveTo((float)currentPixel.x, (float)currentPixel.y);
        currentPixel.x = d2;
        currentPixel.y = d3;
        currentPixel = mapView.geoToPixelX(currentPixel);
        pathWalk.lineTo((float)currentPixel.x, (float)currentPixel.y);
        tempCanvas.drawPath(pathWalk, paintWalk);
        pathWalk.reset();
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
            Iterator iterator3 = journeyroute.arrayOfLines.iterator();
            do
            {
                if(!iterator3.hasNext())
                    break;
                JourneyLine journeyline = (JourneyLine)iterator3.next();
                path.reset();
                paintMainRoad.setColor(0x8c000000 | journeyline.color & 0xffffff);
                int j = 0;
                Iterator iterator4 = journeyline.arrayOfPoints.iterator();
                while(iterator4.hasNext())
                {
                    GeoPoint geopoint = (GeoPoint)iterator4.next();
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

            if(mData.arrayOfCarpark != null)
            {
                char c = 'A';
                for(Iterator iterator1 = mData.arrayOfCarpark.iterator(); iterator1.hasNext();)
                {
                    Carpark carpark = (Carpark)iterator1.next();
                    currentPixel.x = carpark.longitude;
                    currentPixel.y = carpark.latitude;
                    currentPixel = mapView.geoToPixelX(currentPixel);
                    tempRect.set(carparkBitmapDest);
                    tempRect.offset((float)currentPixel.x, (float)currentPixel.y);
                    canvas.drawBitmap(carparkBitmap, carparkBitmapSource, tempRect, null);
                    canvas.drawText((new StringBuilder()).append("").append(c).toString(), (float)currentPixel.x - 6F, (float)currentPixel.y + 8F, paintCarparkText);
                    c++;
                }

            }
            if(mData.arrayOfArrayOfPointDetail != null)
            {
                Iterator iterator2 = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(selectedRouteIndex)).iterator();
                do
                {
                    if(!iterator2.hasNext())
                        break;
                    JourneyPointDetail journeypointdetail1 = (JourneyPointDetail)iterator2.next();
                    if(journeypointdetail1.title.startsWith("Take"))
                    {
                        currentPixel.x = journeypointdetail1.longitude;
                        currentPixel.y = journeypointdetail1.latitude;
                        currentPixel = mapView.geoToPixelX(currentPixel);
                        dest.set(busIconRect);
                        dest.offset((float)currentPixel.x - busIconRect.width() / 2.0F, (float)currentPixel.y - busIconRect.height() / 2.0F);
                        canvas.drawBitmap(busIcon, null, dest, paintBus);
                    }
                } while(true);
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

    public void layoutView(View view, double d, double d1)
    {
        android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)view.getLayoutParams();
        currentPixel.x = d;
        currentPixel.y = d1;
        currentPixel = mapView.geoToPixelX(currentPixel);
        int i = (int)currentPixel.x - layoutparams.width / 2;
        int j = (int)currentPixel.y - layoutparams.height;
        int k = layoutparams.width;
        int l = layoutparams.height;
        layoutparams.leftMargin = (int)currentPixel.x - layoutparams.width / 2;
        layoutparams.topMargin = (int)currentPixel.y - layoutparams.height;
        layoutparams.rightMargin = layoutparams.leftMargin + layoutparams.width;
        layoutparams.bottomMargin = layoutparams.topMargin + layoutparams.height;
        view.layout(i, j, i + k, j + l);
    }

    protected void onUpdate()
    {
        if(mData != null && mData.arrayOfRoutes != null && selectedRouteIndex < mData.arrayOfRoutes.size() && mData.arrayOfRoutes.get(selectedRouteIndex) != null)
        {
            JourneyRoute journeyroute = (JourneyRoute)mData.arrayOfRoutes.get(selectedRouteIndex);
            layoutView(startIm, mData.start.longitude, mData.start.latitude);
            layoutView(endIm, mData.end.longitude, mData.end.latitude);
        }
        invalidate();
    }

    Bitmap busBitmap;
    public Bitmap busIcon;
    RectF busIconRect;
    Bitmap carparkBitmap;
    RectF carparkBitmapDest;
    Rect carparkBitmapSource;
    Paint clearPaint;
    PointF64 currentPixel;
    RectF dest;
    ImageView endIm;
    public double focusLatitude;
    public double focusLongitude;
    PointF64 lastPixel;
    public DirectionDetailServiceOutput mData;
    JourneyLine mLastJourneyLine;
    GeoPoint mLastJourneyLinePoint;
    public int mMethod;
    Bitmap nodeBitmap;
    RectF nodeBitmapDest;
    Rect nodeBitmapSource;
    Paint paintBus;
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
